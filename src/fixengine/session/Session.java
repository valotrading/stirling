/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fixengine.session;

import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import lang.DefaultTimeSource;
import lang.TimeSource;
import silvertip.Connection;
import fixengine.Config;
import fixengine.messages.AbstractFieldsValidator;
import fixengine.messages.AbstractMessageValidator;
import fixengine.messages.BusinessMessageRejectMessage;
import fixengine.messages.BusinessRejectReasonValue;
import fixengine.messages.DefaultMessageFactory;
import fixengine.messages.DefaultMessageVisitor;
import fixengine.messages.EncryptMethodValue;
import fixengine.messages.Field;
import fixengine.messages.HeartbeatMessage;
import fixengine.messages.LogonMessage;
import fixengine.messages.LogoutMessage;
import fixengine.messages.Message;
import fixengine.messages.MessageFactory;
import fixengine.messages.MessageVisitor;
import fixengine.messages.MessageValidator;
import fixengine.messages.Parser;
import fixengine.messages.RejectMessage;
import fixengine.messages.ResendRequestMessage;
import fixengine.messages.SequenceResetMessage;
import fixengine.messages.SessionRejectReasonValue;
import fixengine.messages.TestRequestMessage;
import fixengine.messages.Validator;
import fixengine.session.store.SessionStore;
import fixengine.tags.BeginSeqNo;
import fixengine.tags.BusinessRejectReason;
import fixengine.tags.EncryptMethod;
import fixengine.tags.EndSeqNo;
import fixengine.tags.GapFillFlag;
import fixengine.tags.HeartBtInt;
import fixengine.tags.NewSeqNo;
import fixengine.tags.OrigSendingTime;
import fixengine.tags.RefMsgType;
import fixengine.tags.RefSeqNo;
import fixengine.tags.SessionRejectReason;
import fixengine.tags.TestReqID;
import fixengine.tags.Text;

import static fixengine.messages.MsgTypeValue.HEARTBEAT;
import static fixengine.messages.MsgTypeValue.LOGON;
import static fixengine.messages.MsgTypeValue.LOGOUT;
import static fixengine.messages.MsgTypeValue.RESEND_REQUEST;
import static fixengine.messages.MsgTypeValue.SEQUENCE_RESET;
import static fixengine.messages.MsgTypeValue.TEST_REQUEST;
import static fixengine.messages.MsgTypeValue.REJECT;
import static fixengine.messages.MsgTypeValue.BUSINESS_MESSAGE_REJECT;

import fixengine.messages.Validator.ErrorLevel;
import fixengine.messages.Validator.ErrorHandler;

/**
 * @author Karim Osman
 */
public class Session {
    private static final long DEFAULT_LOGOUT_RESPONSE_TIMEOUT_MSEC = 10000;

    protected MessageQueue queue = new MessageQueue();
    protected Sequence outgoingSeq = new Sequence();
    protected TimeSource timeSource = new DefaultTimeSource();

    protected final HeartBtIntValue heartBtInt;
    protected final Config config;
    protected final SessionStore store;
    protected final Logger logger;
    protected final long logoutResponseTimeoutMsec;
    protected final MessageFactory messageFactory;

    private long testReqId;
    private boolean initiatedLogout;
    private boolean authenticated;
    private boolean available = true;

    private DateTime prevTxTime = timeSource.currentTime();
    private DateTime prevRxTime = timeSource.currentTime();

    private boolean waitingForResponseToInitiatedLogout;
    private DateTime logoutInitiatedAt;

    public Session(HeartBtIntValue heartBtInt, Config config, SessionStore store) {
        this(heartBtInt, config, store, Logger.getLogger("Session"), DEFAULT_LOGOUT_RESPONSE_TIMEOUT_MSEC, new DefaultMessageFactory());
    }

    public Session(HeartBtIntValue heartBtInt, Config config, SessionStore store, MessageFactory messageFactory) {
        this(heartBtInt, config, store, Logger.getLogger("Session"), DEFAULT_LOGOUT_RESPONSE_TIMEOUT_MSEC, messageFactory);
    }

    public Session(HeartBtIntValue heartBtInt, Config config, SessionStore store, Logger logger,
            long logoutResponseTimeoutMsec, MessageFactory messageFactory) {
        this.heartBtInt = heartBtInt;
        this.config = config;
        this.store = store;
        this.logger = logger;
        this.logoutResponseTimeoutMsec = logoutResponseTimeoutMsec;
        this.messageFactory = messageFactory;
        store.load(this);
    }

    public Config getConfig() {
        return config;
    }

    public Sequence getOutgoingSeq() {
        return outgoingSeq;
    }

    public void setOutgoingSeq(Sequence seq) {
        outgoingSeq = seq;
    }

    public Sequence getIncomingSeq() {
        Sequence seq = new Sequence();
        seq.reset(queue.nextSeqNum());
        return seq;
    }

    public void setIncomingSeq(Sequence seq) {
        queue.reset(seq.peek());
    }

    public DateTime currentTime() {
        return timeSource.currentTime();
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void send(Connection conn, Message message) {
        message.setHeaderConfig(config);
        message.setMsgSeqNum(outgoingSeq.next());
        message.setSendingTime(timeSource.currentTime());
        conn.send(silvertip.Message.fromString(message.format()));
        prevTxTime = timeSource.currentTime();
        store.save(this);
    }

    public void receive(final Connection conn, silvertip.Message message, final MessageVisitor visitor) {
        prevRxTime = timeSource.currentTime();
        try {
            Parser.parse(messageFactory, message, new Parser.Callback() {
                @Override public void message(Message message) {
                    int expected = queue.nextSeqNum();

                    if (validate(conn, message))
                        process(conn, message, visitor);
                    else
                        queue.skip(message);

                    /*
                     * We're out-of-sync if there's a gap in the sequence
                     * numbers. However, if the other side is in the middle of
                     * resending the missing messages, don't attempt to sync
                     * after each received message.
                     */
                    if (!conn.isClosed() && isOutOfSync() && message.getMsgSeqNum() != expected)
                        syncMessages(conn);
                }

                @Override public void invalidMessage(int msgSeqNum, SessionRejectReasonValue reason, String text) {
                    queue.skip(msgSeqNum);
                    if (authenticated) {
                        logger.severe(text);
                        sessionReject(conn, msgSeqNum, reason, text);
                    } else {
                        logger.severe(text);
                        logout(conn);
                    }
                }

                @Override public void unsupportedMsgType(String msgType, int msgSeqNum) {
                    logger.warning("MsgType(35): Unknown message type: " + msgType);
                    queue.skip(msgSeqNum);
                    businessReject(conn, msgType, msgSeqNum, BusinessRejectReasonValue.UNKNOWN_MESSAGE_TYPE, "MsgType(35): Unknown message type: " + msgType);
                }

                @Override public void invalidMsgType(String msgType, int msgSeqNum) {
                    logger.warning("MsgType(35): Invalid message type: " + msgType);
                    queue.skip(msgSeqNum);
                    sessionReject(conn, msgSeqNum, SessionRejectReasonValue.INVALID_MSG_TYPE, "MsgType(35): Invalid message type: " + msgType);
                }

                @Override public void garbledMessage(String text) {
                    /* Ignore the message. */
                    logger.warning(text);
                }
            });
        } finally {
            store.save(this);
        }
    }

    public void logon(Connection conn) {
        authenticated = initiatedLogout = false;
        LogonMessage message = (LogonMessage) messageFactory.create(LOGON);
        message.setInteger(HeartBtInt.TAG, 30);
        message.setEnum(EncryptMethod.TAG, EncryptMethodValue.NONE);
        send(conn, message);
    }

    public void logout(final Connection conn) {
        send(conn, (LogoutMessage) messageFactory.create(LOGOUT));
        initiatedLogout = true;
        logoutInitiatedAt = timeSource.currentTime();
        waitingForResponseToInitiatedLogout = true;
    }

    public void sequenceReset(Connection conn, Sequence seq) {
        SequenceResetMessage message = (SequenceResetMessage) messageFactory.create(SEQUENCE_RESET);
        message.setHeaderConfig(config);
        message.setSendingTime(timeSource.currentTime());
        message.setMsgSeqNum(seq.peek());
        message.setInteger(NewSeqNo.TAG, seq.next());
        message.setBoolean(GapFillFlag.TAG, false);
        conn.send(silvertip.Message.fromString(message.format()));
        prevTxTime = timeSource.currentTime();
        setOutgoingSeq(seq);
        store.save(this);
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }

    public void keepAlive(Connection conn) {
        if (isTimedOut(prevTxTime, heartBtInt.heartbeat().delayMsec())) {
            heartbeat(conn);
            prevTxTime = timeSource.currentTime();
        }

        if (isTimedOut(prevRxTime, heartBtInt.testRequest().delayMsec())) {
            testRequest(conn);
            prevRxTime = timeSource.currentTime();
        }
    }

    public void processInitiatedLogout(Connection conn) {
        if (waitingForResponseToInitiatedLogout && isTimedOut(logoutInitiatedAt, logoutResponseTimeoutMsec)) {
            logger.warning("Response to logout not received in " + logoutResponseTimeoutMsec / 1000 + " second(s), disconnecting");
            waitingForResponseToInitiatedLogout = false;
            conn.close();
        }
    }

    private boolean isTimedOut(DateTime dateTime, long timeoutMsec) {
        DateTime now = timeSource.currentTime();
        DateTime timeOutAt = dateTime.plusMillis((int)timeoutMsec);
        return now.isAfter(timeOutAt);
    }

    public void heartbeat(Connection conn) {
        send(conn, (HeartbeatMessage) messageFactory.create(HEARTBEAT));
    }

    protected boolean checkSeqResetSeqNum() {
        return true;
    }

    private void testRequest(Connection conn) {
        TestRequestMessage req = (TestRequestMessage) messageFactory.create(TEST_REQUEST);
        req.setString(TestReqID.TAG, Long.toString(++testReqId));
        send(conn, req);
    }

    private void process(final Connection conn, Message message, final MessageVisitor visitor) {
        if (authenticated) {
            message.apply(new DefaultMessageVisitor() {
                @Override public void visit(TestRequestMessage message) {
                    queue.skip(message);
                    HeartbeatMessage heartbeat = (HeartbeatMessage) messageFactory.create(HEARTBEAT);
                    heartbeat.setString(TestReqID.TAG, message.getString(TestReqID.TAG));
                    send(conn, heartbeat);
                }

                @Override public void visit(ResendRequestMessage message) {
                    queue.skip(message);
                    int newSeqNo = outgoingSeq.peek();
                    outgoingSeq.reset(message.getInteger(BeginSeqNo.TAG));
                    fillSequenceGap(conn, newSeqNo);
                }

                @Override public void visit(SequenceResetMessage message) {
                    processSeqReset(conn, message);
                }

                @Override public void visit(LogoutMessage message) {
                    queue.skip(message);
                    if (!initiatedLogout)
                        send(conn, (LogoutMessage) messageFactory.create(LOGOUT));
                    else
                        waitingForResponseToInitiatedLogout = false;
                    conn.close();
                }

                @Override public void defaultAction(Message message) {
                    queue.enqueue(message);
                    if (!isOutOfSync()) {
                        while (!queue.isEmpty())
                            queue.dequeue().apply(visitor);
                    }
                }
            });
        } else {
            message.apply(new DefaultMessageVisitor() {
                @Override public void visit(LogonMessage message) {
                    authenticated = true;
                    queue.enqueue(message);
                    if (!isOutOfSync()) {
                        while (!queue.isEmpty())
                            queue.dequeue().apply(visitor);
                    }
                }

                @Override public void defaultAction(Message message) {
                    logger.severe("first message is not a logon");
                    logout(conn);
                }
            });
        }
    }

    private boolean validate(final Connection conn, final Message message) {
        return MessageValidator.validate(this, message, new ErrorHandler() {
            @Override public void sessionReject(SessionRejectReasonValue reason, String text, ErrorLevel level, boolean terminate) {
                logError(text, level);
                Session.this.sessionReject(conn, message, reason, text);
                if (terminate)
                    Session.this.terminate(conn, message, text);
            }

            @Override public void businessReject(BusinessRejectReasonValue reason, String text, ErrorLevel level) {
                logError(text, level);
                Session.this.businessReject(conn, message.getMsgType(), message.getMsgSeqNum(), reason, text);
            }

            @Override public void terminate(String text) {
                logError(text, ErrorLevel.ERROR);
                Session.this.terminate(conn, message, text);
            }

            private void logError(String text, ErrorLevel level) {
                switch (level) {
                case WARNING:
                    logger.warning(text);
                    break;
                case ERROR:
                    logger.severe(text);
                    break;
                }
            }
        });
    }

    private boolean isOutOfSync() {
        return queue.hasSeqNumGap();
    }

    private void sessionReject(Connection conn, Message message, SessionRejectReasonValue reason, String text) {
        sessionReject(conn, message.getMsgSeqNum(), reason, text);
    }

    private void sessionReject(Connection conn, int msgSeqNum, SessionRejectReasonValue reason, String text) {
        RejectMessage reject = (RejectMessage) messageFactory.create(REJECT);
        reject.setInteger(RefSeqNo.TAG, msgSeqNum);
        reject.setEnum(SessionRejectReason.TAG, reason);
        reject.setString(Text.TAG, text);
        send(conn, reject);
    }

    private void businessReject(Connection conn, String msgType, int msgSeqNum, BusinessRejectReasonValue reason, String text) {
        BusinessMessageRejectMessage reject = (BusinessMessageRejectMessage) messageFactory.create(BUSINESS_MESSAGE_REJECT);
        reject.setInteger(RefSeqNo.TAG, msgSeqNum);
        reject.setString(RefMsgType.TAG, msgType);
        reject.setEnum(BusinessRejectReason.TAG, reason);
        reject.setString(Text.TAG, text);
        send(conn, reject);
    }

    private void terminate(Connection conn, Message message, String text) {
        LogoutMessage logout = (LogoutMessage) messageFactory.create(LOGOUT);
        logout.setString(Text.TAG, text);
        send(conn, logout);
        conn.close();
    }

    private void fillSequenceGap(Connection conn, int newSeqNo) {
        SequenceResetMessage seqReset = (SequenceResetMessage) messageFactory.create(SEQUENCE_RESET);
        seqReset.setPossDupFlag(true);
        seqReset.setBoolean(GapFillFlag.TAG, true);
        seqReset.setInteger(NewSeqNo.TAG, newSeqNo);
        send(conn, seqReset);
        outgoingSeq.reset(newSeqNo);
    }

    private void syncMessages(Connection conn) {
        int beginSeqNo = queue.nextSeqNum();
        ResendRequestMessage resendReq = (ResendRequestMessage) messageFactory.create(RESEND_REQUEST);
        resendReq.setInteger(BeginSeqNo.TAG, beginSeqNo);
        resendReq.setInteger(EndSeqNo.TAG, 0);
        send(conn, resendReq);
    }

    private void processSeqReset(Connection conn, SequenceResetMessage message) {
        int newSeqNo = message.getInteger(NewSeqNo.TAG);
        if (checkSeqResetSeqNum() && !message.isResetOk(queue.nextSeqNum())) {
            int beginSeqNo = queue.nextSeqNum();
            ResendRequestMessage resendReq = (ResendRequestMessage) messageFactory.create(RESEND_REQUEST);
            resendReq.setInteger(BeginSeqNo.TAG, beginSeqNo);
            resendReq.setInteger(EndSeqNo.TAG, message.getMsgSeqNum() - 1);
            send(conn, resendReq);
        } else if (newSeqNo <= message.getMsgSeqNum() && message.getBoolean(GapFillFlag.TAG)) {
            sessionReject(conn, message.getMsgSeqNum(), SessionRejectReasonValue.INVALID_VALUE,
                "Attempt to lower sequence number, invalid value NewSeqNum(36)=" + newSeqNo);
        } else if (newSeqNo < message.getMsgSeqNum() && !message.getBoolean(GapFillFlag.TAG)) {
            logger.warning("Value is incorrect (out of range) for this tag, NewSeqNum(36)=" + newSeqNo);
            sessionReject(conn, message.getMsgSeqNum(), SessionRejectReasonValue.INVALID_VALUE,
                "Value is incorrect (out of range) for this tag, NewSeqNum(36)=" + newSeqNo);
        } else {
            if (newSeqNo == message.getMsgSeqNum() && !message.getBoolean(GapFillFlag.TAG))
                logger.warning("NewSeqNo(36)=" + newSeqNo + " is equal to expected MsgSeqNum(34)=" + message.getMsgSeqNum());
            queue.reset(newSeqNo);
        }
    }
}
