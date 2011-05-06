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

import static fixengine.messages.MsgTypeValue.BUSINESS_MESSAGE_REJECT;
import static fixengine.messages.MsgTypeValue.HEARTBEAT;
import static fixengine.messages.MsgTypeValue.LOGON;
import static fixengine.messages.MsgTypeValue.LOGOUT;
import static fixengine.messages.MsgTypeValue.REJECT;
import static fixengine.messages.MsgTypeValue.RESEND_REQUEST;
import static fixengine.messages.MsgTypeValue.SEQUENCE_RESET;
import static fixengine.messages.MsgTypeValue.TEST_REQUEST;

import java.util.logging.Logger;

import fixengine.messages.Value;
import lang.DefaultTimeSource;
import lang.TimeSource;

import org.joda.time.DateTime;

import fixengine.Config;
import fixengine.messages.DefaultMessageVisitor;
import fixengine.messages.FixMessage;
import fixengine.messages.Heartbeat;
import fixengine.messages.LogonMessage;
import fixengine.messages.Logout;
import fixengine.messages.Message;
import fixengine.messages.MessageComparator;
import fixengine.messages.MessageFactory;
import fixengine.messages.MessageValidator;
import fixengine.messages.MessageVisitor;
import fixengine.messages.ParseException;
import fixengine.messages.Parser;
import fixengine.messages.Reject;
import fixengine.messages.ResendRequest;
import fixengine.messages.SequenceReset;
import fixengine.messages.TestRequest;
import fixengine.messages.Validator.ErrorHandler;
import fixengine.messages.Validator.ErrorLevel;
import fixengine.messages.fix42.BusinessMessageReject;
import fixengine.session.store.SessionStore;
import fixengine.tags.fix42.BeginSeqNo;
import fixengine.tags.fix42.BusinessRejectReason;
import fixengine.tags.fix42.EncryptMethod;
import fixengine.tags.fix42.EndSeqNo;
import fixengine.tags.fix42.GapFillFlag;
import fixengine.tags.fix42.HeartBtInt;
import fixengine.tags.fix42.NewSeqNo;
import fixengine.tags.fix42.RefMsgType;
import fixengine.tags.fix42.RefSeqNo;
import fixengine.tags.fix42.TestReqID;
import fixengine.tags.fix42.Text;
import fixengine.tags.fix43.SessionRejectReason;
import silvertip.Connection;

/**
 * @author Karim Osman
 */
public class Session {
    private static final long DEFAULT_LOGOUT_RESPONSE_TIMEOUT_MSEC = 10000;
    private static final Logger LOG = Logger.getLogger("Session");
    private static final int MAX_CONSECUTIVE_RESEND_REQUESTS = 2;

    protected MessageQueue<FixMessage> incomingQueue = new MessageQueue<FixMessage>();
    protected MessageQueue<Message> outgoingQueue = new MessageQueue<Message>();

    protected Sequence outgoingSeq = new Sequence();

    protected final HeartBtIntValue heartBtInt;
    protected final Config config;
    protected final SessionStore store;
    protected final MessageFactory messageFactory;
    protected final MessageComparator messageComparator;

    private TimeSource timeSource = new DefaultTimeSource();
    private long testReqId;
    private boolean initiatedLogout;
    private boolean authenticated;
    private boolean available = true;

    private DateTime prevTxTime = currentTime();
    private DateTime prevRxTime = currentTime();

    private boolean waitingForResponseToInitiatedLogout;
    private DateTime logoutInitiatedAt;

    public Session(HeartBtIntValue heartBtInt, Config config, SessionStore store, MessageFactory messageFactory, MessageComparator messageComparator) {
        this.heartBtInt = heartBtInt;
        this.config = config;
        this.store = store;
        this.messageFactory = messageFactory;
        this.messageComparator = messageComparator;
        store.load(this);
    }

    public void receive(final Connection conn, final FixMessage message, final MessageVisitor visitor) {
        prevRxTime = currentTime();
        try {
            if (!parseMsgSeqNum(conn, message)) {
                return;
            }

            if (message.getMsgType().equals(SEQUENCE_RESET)) {
                Parser.parse(messageFactory, message, new Parser.Callback() {
                    @Override public void message(Message sequenceReset) {
                        if (!processSequenceReset(conn, (SequenceReset) sequenceReset))
                            processMessage(conn, message, visitor);
                    }

                    @Override public void invalidMessage(int msgSeqNum, Value<Integer> reason, String text) {
                    }

                    @Override public void unsupportedMsgType(String msgType, int msgSeqNum) {
                    }

                    @Override public void invalidMsgType(String msgType, int msgSeqNum) {
                    }
                });
            } else {
                processMessage(conn, message, visitor);
            }

        } finally {
            store.save(this);
        }
    }

    private void processMessage(final Connection conn, FixMessage message, MessageVisitor visitor) {
        message.setReceiveTime(currentTime());

        int expectedMsgSeqNum = incomingQueue.nextSeqNum();
        incomingQueue.enqueue(message);

        if (message.getMsgSeqNum() != expectedMsgSeqNum) {
            if (!authenticated) {
                processInSyncMessageQueue(conn, visitor);
            }
            processOutOfSyncMessageQueue(conn, message);
        } else if (!conn.isClosed()) {
            processInSyncMessageQueue(conn, visitor);
        }
    }

    private boolean processSequenceReset(Connection conn, SequenceReset message) {
        if (message.getBoolean(GapFillFlag.Tag())) {
            return processSequenceResetGapFill(conn, message);
        }
        return processSequenceResetReset(conn, message);
    }

    private boolean processSequenceResetGapFill(Connection conn, SequenceReset message) {
        if (message.getMsgSeqNum() < incomingQueue.nextSeqNum()) {
            if (!message.getPossDupFlag()) {
                String text = "MsgSeqNum too low, expecting " + incomingQueue.nextSeqNum() + " but received " + message.getMsgSeqNum();
                getLogger().severe(text);
                terminate(conn, text);
            }
            return true;
        }
        return false;
    }

    private boolean processSequenceResetReset(Connection conn, SequenceReset message) {
        if (message.getNewSeqNo() == message.getMsgSeqNum()) {
            getLogger().warning("NewSeqNo(36)=" + message.getNewSeqNo() + " is equal to expected MsgSeqNum(34)=" + message.getMsgSeqNum());
        } else if (message.getNewSeqNo() < message.getMsgSeqNum()) {
            String text = "Value is incorrect (out of range) for this tag, NewSeqNum(36)=" + message.getNewSeqNo();
            getLogger().warning(text);
            sessionReject(conn, message.getMsgSeqNum(), SessionRejectReason.InvalidValue(), text);
        } else {
            incomingQueue.reset(message.getNewSeqNo());
        }
        return true;
    }

    private void processOutOfSyncMessageQueue(final Connection conn, final FixMessage message) {
        if (message.getMsgSeqNum() > incomingQueue.nextSeqNum()) {
            if (incomingQueue.getOutOfOrderCount() > MAX_CONSECUTIVE_RESEND_REQUESTS) {
                terminate(conn, "Maximum resend requests (" + MAX_CONSECUTIVE_RESEND_REQUESTS + ") exceeded");
                return;
            }
            sendResendRequest(conn, incomingQueue.nextSeqNum(), 0);
        } else {
            Parser.parse(messageFactory, message, new Parser.Callback() {
                @Override public void message(Message msg) {
                    if (!msg.getPossDupFlag()) {
                        terminateOnMsgSeqNumTooLow(conn, message);
                    }
                }

                @Override public void invalidMessage(int msgSeqNum, Value<Integer> reason, String text) {
                    terminateOnMsgSeqNumTooLow(conn, message);
                }

                @Override public void unsupportedMsgType(String msgType, int msgSeqNum) {
                    terminateOnMsgSeqNumTooLow(conn, message);
                }

                @Override public void invalidMsgType(String msgType, int msgSeqNum) {
                    terminateOnMsgSeqNumTooLow(conn, message);
                }
            });
        }
    }

    private void terminateOnMsgSeqNumTooLow(Connection conn, FixMessage message) {
        String text = "MsgSeqNum too low, expecting " + incomingQueue.nextSeqNum() + " but received " + message.getMsgSeqNum();
        getLogger().severe(text);
        terminate(conn, text);
    }

    private void processInSyncMessageQueue(final Connection conn, final MessageVisitor visitor) {
        while (!incomingQueue.isEmpty()) {
            Parser.parse(messageFactory, incomingQueue.dequeue(), new Parser.Callback() {
                @Override public void message(Message message) {
                    if (validate(conn, message))
                        process(conn, message, visitor);
                    else
                        incomingQueue.skip(message.getMsgSeqNum());
                }

                @Override public void invalidMessage(int msgSeqNum, Value<Integer> reason, String text) {
                    incomingQueue.skip(msgSeqNum);
                    if (authenticated) {
                        getLogger().severe(text);
                        sessionReject(conn, msgSeqNum, reason, text);
                    } else {
                        getLogger().severe(text);
                        logout(conn);
                    }
                }

                @Override public void unsupportedMsgType(String msgType, int msgSeqNum) {
                    getLogger().warning("MsgType(35): Unknown message type: " + msgType);
                    incomingQueue.skip(msgSeqNum);
                    businessReject(conn, msgType, msgSeqNum, BusinessRejectReason.UnknownMessageType(), "MsgType(35): Unknown message type: " + msgType);
                }

                @Override public void invalidMsgType(String msgType, int msgSeqNum) {
                    getLogger().warning("MsgType(35): Invalid message type: " + msgType);
                    incomingQueue.skip(msgSeqNum);
                    sessionReject(conn, msgSeqNum, SessionRejectReason.InvalidMsgType(), "MsgType(35): Invalid message type: " + msgType);
                }
            });
        }
    }

    private boolean parseMsgSeqNum(Connection conn, FixMessage message) {
        try {
            int msgSeqNum = Parser.parseMsgSeqNum(message);
            message.setMsgSeqNum(msgSeqNum);
            return true;
        } catch (ParseException e) {
            getLogger().severe(e.getMessage());
            terminate(conn, e.getMessage());
            return false;
        }
    }

    private boolean validate(final Connection conn, final Message message) {
        return MessageValidator.validate(this, message, new ErrorHandler() {
            @Override public void sessionReject(Value<Integer> reason, String text, ErrorLevel level, boolean terminate) {
                logError(text, level);
                Session.this.sessionReject(conn, message, reason, text);
                if (terminate)
                    Session.this.terminate(conn, text);
            }

            @Override public void businessReject(Value<Integer> reason, String text, ErrorLevel level) {
                logError(text, level);
                Session.this.businessReject(conn, message.getMsgType(), message.getMsgSeqNum(), reason, text);
            }

            @Override public void terminate(String text) {
                logError(text, ErrorLevel.ERROR);
                Session.this.terminate(conn, text);
            }

            private void logError(String text, ErrorLevel level) {
                switch (level) {
                case WARNING:
                    getLogger().warning(text);
                    break;
                case ERROR:
                    getLogger().severe(text);
                    break;
                }
            }
        });
    }

    private void process(final Connection conn, Message message, final MessageVisitor visitor) {
        if (authenticated) {
            message.apply(new DefaultMessageVisitor() {
                @Override public void visit(TestRequest message) {
                    store.saveIncomingMessage(Session.this, message);
                    incomingQueue.skip(message.getMsgSeqNum());
                    Heartbeat heartbeat = (Heartbeat) messageFactory.create(HEARTBEAT);
                    heartbeat.setString(TestReqID.Tag(), message.getString(TestReqID.Tag()));
                    send(conn, heartbeat);
                }

                @Override public void visit(ResendRequest message) {
                    store.saveIncomingMessage(Session.this, message);
                    if (outgoingQueue.isEmpty()) {
                        incomingQueue.skip(message.getMsgSeqNum());
                        int beginSeqNo = message.getInteger(BeginSeqNo.Tag());
                        int endSeqNo = message.getInteger(EndSeqNo.Tag());
                        for (Message msg : store.load(Session.this, beginSeqNo, endSeqNo))
                            send(conn, msg);
                    } else {
                        while (!outgoingQueue.isEmpty()) {
                            Message msg = outgoingQueue.dequeue();
                            msg.setPossDupFlag(true);
                            msg.setSendingTime(currentTime());
                            conn.send(FixMessage.fromString(msg.format()));
                            prevTxTime = currentTime();
                        }
                    }
                }

                @Override public void visit(SequenceReset message) {
                    store.saveIncomingMessage(Session.this, message);
                    processSeqReset(conn, message);
                }

                @Override public void visit(Logout message) {
                    store.saveIncomingMessage(Session.this, message);
                    incomingQueue.skip(message.getMsgSeqNum());
                    if (!initiatedLogout)
                        send(conn, messageFactory.create(LOGOUT));
                    else
                        waitingForResponseToInitiatedLogout = false;
                    conn.close();
                }

                @Override public void defaultAction(Message message) {
                    if (message.getPossResend() && store.isDuplicate(Session.this, message)) {
                        store.saveIncomingMessage(Session.this, message);
                    } else {
                        store.saveIncomingMessage(Session.this, message);
                        message.apply(visitor);
                    }
                }
            });
        } else {
            message.apply(new DefaultMessageVisitor() {
                @Override public void visit(LogonMessage message) {
                    authenticated = true;
                    message.apply(visitor);
                }

                @Override public void defaultAction(Message message) {
                    getLogger().severe("first message is not a logon");
                    logout(conn);
                }
            });
        }
    }

    private void sendResendRequest(Connection conn, int beginSeqNo, int endSeqNo) {
        ResendRequest resendReq = (ResendRequest) messageFactory.create(RESEND_REQUEST);
        resendReq.setInteger(BeginSeqNo.Tag(), beginSeqNo);
        resendReq.setInteger(EndSeqNo.Tag(), endSeqNo);
        send(conn, resendReq);
    }

    private void processSeqReset(Connection conn, SequenceReset message) {
        int newSeqNo = message.getInteger(NewSeqNo.Tag());
        if (newSeqNo <= message.getMsgSeqNum()) {
            sessionReject(conn, message.getMsgSeqNum(), SessionRejectReason.InvalidValue(),
                "Attempt to lower sequence number, invalid value NewSeqNum(36)=" + newSeqNo);
        } else {
            incomingQueue.reset(newSeqNo);
        }
    }

    private void sessionReject(Connection conn, Message message, Value<Integer> reason, String text) {
        sessionReject(conn, message.getMsgSeqNum(), reason, text);
    }

    private void sessionReject(Connection conn, int msgSeqNum, Value<Integer> reason, String text) {
        Reject reject = (Reject) messageFactory.create(REJECT);
        reject.setInteger(RefSeqNo.Tag(), msgSeqNum);
        reject.setEnum(SessionRejectReason.Tag(), reason);
        reject.setString(Text.Tag(), text);
        send(conn, reject);
    }

    private void businessReject(Connection conn, String msgType, int msgSeqNum, Value<Integer> reason, String text) {
        BusinessMessageReject reject = (BusinessMessageReject) messageFactory.create(BUSINESS_MESSAGE_REJECT);
        reject.setInteger(RefSeqNo.Tag(), msgSeqNum);
        reject.setString(RefMsgType.Tag(), msgType);
        reject.setEnum(BusinessRejectReason.Tag(), reason);
        reject.setString(Text.Tag(), text);
        send(conn, reject);
    }

    private void terminate(Connection conn, String text) {
        Logout logout = (Logout) messageFactory.create(LOGOUT);
        logout.setString(Text.Tag(), text);
        send(conn, logout);
        conn.close();
    }

    public void logon(Connection conn) {
        authenticated = initiatedLogout = false;
        LogonMessage message = (LogonMessage) messageFactory.create(LOGON);
        message.setInteger(HeartBtInt.Tag(), 30);
        message.setEnum(EncryptMethod.Tag(), EncryptMethod.None());
        sendOutOfQueue(conn, message);
    }

    public void logon(Connection conn, LogonMessage logonMessage) {
        authenticated = initiatedLogout = false;
        sendOutOfQueue(conn, logonMessage);
    }

    private void sendOutOfQueue(Connection conn, Message message) {
        message.setHeaderConfig(config);
        message.setMsgSeqNum(outgoingSeq.next());
        message.setSendingTime(currentTime());
        conn.send(FixMessage.fromString(message.format()));
        prevTxTime = currentTime();
        store.saveOutgoingMessage(this, message);
    }

    public void logout(final Connection conn) {
        send(conn, messageFactory.create(LOGOUT));
        initiatedLogout = true;
        logoutInitiatedAt = currentTime();
        waitingForResponseToInitiatedLogout = true;
    }

    public void sequenceReset(Connection conn, Sequence seq) {
        SequenceReset message = (SequenceReset) messageFactory.create(SEQUENCE_RESET);
        message.setHeaderConfig(config);
        message.setSendingTime(currentTime());
        message.setMsgSeqNum(seq.peek());
        message.setInteger(NewSeqNo.Tag(), seq.next());
        message.setBoolean(GapFillFlag.Tag(), false);
        conn.send(FixMessage.fromString(message.format()));
        prevTxTime = currentTime();
        setOutgoingSeq(seq);
        store.saveOutgoingMessage(this, message);
    }

    public void keepAlive(Connection conn) {
        if (isTimedOut(prevTxTime, heartBtInt.heartbeat().delayMsec())) {
            heartbeat(conn);
            prevTxTime = currentTime();
        }

        if (isTimedOut(prevRxTime, heartBtInt.testRequest().delayMsec())) {
            testRequest(conn);
            prevRxTime = currentTime();
        }
    }

    public void heartbeat(Connection conn) {
        send(conn, messageFactory.create(HEARTBEAT));
    }

    private void testRequest(Connection conn) {
        TestRequest req = (TestRequest) messageFactory.create(TEST_REQUEST);
        req.setString(TestReqID.Tag(), Long.toString(++testReqId));
        send(conn, req);
    }

    public void send(Connection conn, Message message) {
        message.setHeaderConfig(config);
        message.setMsgSeqNum(outgoingSeq.next());
        outgoingQueue.enqueue(message);
        if (conn != null && !conn.isClosed()) {
            while (!outgoingQueue.isEmpty()) {
                Message msg = outgoingQueue.dequeue();
                msg.setSendingTime(currentTime());
                conn.send(FixMessage.fromString(msg.format()));
                prevTxTime = currentTime();
            }
        }
        store.saveOutgoingMessage(this, message);
    }

    public void processInitiatedLogout(Connection conn) {
        if (waitingForResponseToInitiatedLogout && isTimedOut(logoutInitiatedAt, getLogoutResponseTimeoutMsec())) {
            getLogger().warning("Response to logout not received in " + getLogoutResponseTimeoutMsec() / 1000
                    + " second(s), disconnecting");
            waitingForResponseToInitiatedLogout = false;
            conn.close();
        }
    }

    private boolean isTimedOut(DateTime dateTime, long timeoutMsec) {
        DateTime now = currentTime();
        DateTime timeOutAt = dateTime.plusMillis((int)timeoutMsec);
        return now.isAfter(timeOutAt);
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
        seq.reset(incomingQueue.nextSeqNum());
        return seq;
    }

    public void setIncomingSeq(Sequence seq) {
        incomingQueue.reset(seq.peek());
    }

    public DateTime currentTime() {
        return timeSource.currentTime();
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }

    public MessageFactory getMessageFactory() {
        return messageFactory;
    }

    public MessageComparator getMessageComparator() {
        return messageComparator;
    }

    protected long getLogoutResponseTimeoutMsec() {
        return DEFAULT_LOGOUT_RESPONSE_TIMEOUT_MSEC;
    }

    protected Logger getLogger() {
        return LOG;
    }

    protected boolean checkSeqResetSeqNum() {
        return true;
    }
}
