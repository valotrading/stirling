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

import java.util.ArrayList;
import java.util.List;

import lang.DefaultTimeSource;
import lang.TimeSource;
import silvertip.Connection;
import fixengine.Config;
import fixengine.messages.AbstractFieldsValidator;
import fixengine.messages.AbstractMessageValidator;
import fixengine.messages.BusinessMessageRejectMessage;
import fixengine.messages.BusinessRejectReasonValue;
import fixengine.messages.DefaultMessageVisitor;
import fixengine.messages.EncryptMethodValue;
import fixengine.messages.Field;
import fixengine.messages.HeartbeatMessage;
import fixengine.messages.LogonMessage;
import fixengine.messages.LogoutMessage;
import fixengine.messages.Message;
import fixengine.messages.MessageVisitor;
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
import fixengine.tags.RefMsgType;
import fixengine.tags.RefSeqNo;
import fixengine.tags.SessionRejectReason;
import fixengine.tags.TestReqID;
import fixengine.tags.Text;

/**
 * @author Karim Osman
 */
public class Session {
    protected MessageQueue queue = new MessageQueue();
    protected Sequence outgoingSeq = new Sequence();
    protected TimeSource timeSource = new DefaultTimeSource();

    protected final HeartBtIntValue heartBtInt;
    protected final Config config;
    protected final SessionStore store;

    private long testReqId;
    private boolean initiatedLogout;
    private boolean authenticated;
    private boolean available = true;

    private long prevTxTimeMsec = System.currentTimeMillis();
    private long prevRxTimeMsec = System.currentTimeMillis();

    public Session(HeartBtIntValue heartBtInt, Config config, SessionStore store) {
        this.heartBtInt = heartBtInt;
        this.config = config;
        this.store = store;
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

    public void send(Connection conn, Message message) {
        message.setHeaderConfig(config);
        message.setMsgSeqNum(outgoingSeq.next());
        message.setSendingTime(timeSource.currentTime());
        conn.send(silvertip.Message.fromString(message.format()));
        prevTxTimeMsec = System.currentTimeMillis();
        store.save(this);
    }

    public void receive(final Connection conn, silvertip.Message message, final MessageVisitor visitor) {
        prevRxTimeMsec = System.currentTimeMillis();
        try {
            Parser.parse(message, new Parser.Callback() {
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
                    sessionReject(conn, msgSeqNum, reason, text);
                }

                @Override public void unsupportedMsgType(String msgType, int msgSeqNum) {
                    queue.skip(msgSeqNum);
                    businessReject(conn, msgType, msgSeqNum, BusinessRejectReasonValue.UNKNOWN_MESSAGE_TYPE,
                            "MsgType(35): Unknown message type: " + msgType);
                }

                @Override public void invalidMsgType(String msgType, int msgSeqNum) {
                    queue.skip(msgSeqNum);
                    sessionReject(conn, msgSeqNum, SessionRejectReasonValue.INVALID_MSG_TYPE, "MsgType(35): Invalid message type: " + msgType);
                }

                @Override public void garbledMessage(String text) {
                    /* Ignore the message. */
                }
            });
        } finally {
            store.save(this);
        }
    }

    public void logon(Connection conn) {
        authenticated = initiatedLogout = false;
        LogonMessage message = new LogonMessage();
        message.setInteger(HeartBtInt.TAG, 30);
        message.setEnum(EncryptMethod.TAG, EncryptMethodValue.NONE);
        send(conn, message);
    }

    public void logout(final Connection conn) {
        send(conn, new LogoutMessage());
        initiatedLogout = true;
    }

    public void sequenceReset(Connection conn, Sequence seq) {
        SequenceResetMessage message = new SequenceResetMessage();
        message.setHeaderConfig(config);
        message.setSendingTime(timeSource.currentTime());
        message.setMsgSeqNum(seq.peek());
        message.setInteger(NewSeqNo.TAG, seq.next());
        message.setBoolean(GapFillFlag.TAG, false);
        conn.send(silvertip.Message.fromString(message.format()));
        prevTxTimeMsec = System.currentTimeMillis();
        setOutgoingSeq(seq);
        store.save(this);
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void keepAlive(Connection conn) {
        long curTimeMsec = System.currentTimeMillis();

        if (curTimeMsec - prevTxTimeMsec > heartBtInt.heartbeat().delayMsec()) {
            heartbeat(conn);
            prevTxTimeMsec = System.currentTimeMillis();
        }

        if (curTimeMsec - prevRxTimeMsec > heartBtInt.testRequest().delayMsec()) {
            testRequest(conn);
            prevRxTimeMsec = System.currentTimeMillis();
        }
    }

    public void heartbeat(Connection conn) {
        send(conn, new HeartbeatMessage());
    }

    protected boolean checkSeqResetSeqNum() {
        return true;
    }

    private void testRequest(Connection conn) {
        TestRequestMessage req = new TestRequestMessage();
        req.setString(TestReqID.TAG, Long.toString(++testReqId));
        send(conn, req);
    }

    private void process(final Connection conn, Message message, final MessageVisitor visitor) {
        if (authenticated) {
            message.apply(new DefaultMessageVisitor() {
                @Override public void visit(TestRequestMessage message) {
                    queue.skip(message);
                    HeartbeatMessage heartbeat = new HeartbeatMessage();
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
                    processSeqReset(message);
                }

                @Override public void visit(LogoutMessage message) {
                    queue.skip(message);
                    if (!initiatedLogout)
                        send(conn, new LogoutMessage());
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
                    logout(conn);
                }
            });
        }
    }

    private boolean validate(final Connection conn, Message message) {
        List<Validator<Message>> validators = new ArrayList<Validator<Message>>() {
            {
                add(new AbstractMessageValidator() {
                    @Override protected boolean isValid(Message message) {
                        return available;
                    }

                    @Override protected void error(Message message) {
                        businessReject(conn, message.getMsgType(), message.getMsgSeqNum(), BusinessRejectReasonValue.APPLICATION_NOT_AVAILABLE,
                                "Application not available");
                    }
                });
                add(new AbstractMessageValidator() {
                    @Override protected boolean isValid(Message message) {
                        return !message.isTooLowSeqNum(queue.nextSeqNum());
                    }

                    @Override protected void error(Message message) {
                        terminate(conn, message, "MsgSeqNum too low, expecting " + queue.nextSeqNum() + " but received "
                                + message.getMsgSeqNum());
                    }
                });
                add(new AbstractMessageValidator() {
                    @Override protected boolean isValid(Message message) {
                        return message.hasValidBeginString(config);
                    }

                    @Override protected void error(Message message) {
                        terminate(conn, message, "BeginString is invalid, expecting " + config.getVersion().value() + " but received "
                                + message.getBeginString());
                    }
                });
                add(new AbstractMessageValidator() {
                    @Override protected boolean isValid(Message message) {
                        return message.hasValidSenderCompId(config);
                    }

                    @Override protected void error(Message message) {
                        sessionReject(conn, message, SessionRejectReasonValue.COMP_ID_PROBLEM, "Invalid SenderCompId(49): "
                                + message.getSenderCompId());
                        terminate(conn, message, message.getSenderCompId());
                    }
                });
                add(new AbstractMessageValidator() {
                    @Override protected boolean isValid(Message message) {
                        return message.hasOrigSendTimeAfterSendingTime();
                    }

                    @Override protected void error(Message message) {
                        String text = "OrigSendTime " + message.getOrigSendingTime() + " after " + message.getSendingTime();
                        sessionReject(conn, message, SessionRejectReasonValue.SENDING_TIME_ACCURACY_PROBLEM, text);
                        terminate(conn, message, text);
                    }
                });
                add(new AbstractMessageValidator() {
                    @Override protected boolean isValid(Message message) {
                        return message.hasAccurateSendingTime(timeSource.currentTime());
                    }

                    @Override protected void error(Message message) {
                        String text = "SendingTime is invalid: " + message.getSendingTime();
                        sessionReject(conn, message, SessionRejectReasonValue.SENDING_TIME_ACCURACY_PROBLEM, text);
                        terminate(conn, message, text);
                    }
                });
                add(new AbstractMessageValidator() {
                    @Override protected boolean isValid(Message message) {
                        return message.isPointToPoint();
                    }

                    @Override protected void error(Message message) {
                        sessionReject(conn, message, SessionRejectReasonValue.COMP_ID_PROBLEM, "Third-party message routing is not supported");
                    }
                });
                add(new AbstractFieldsValidator() {
                    @Override protected boolean isValid(Field field) {
                        if (field.isConditional()) {
                            return true;
                        }
                        return !field.isMissing();
                    }

                    @Override protected void error(Message message, Field field) {
                        sessionReject(conn, message, SessionRejectReasonValue.TAG_MISSING, toString(field) + ": Tag missing");
                    }
                });
                add(new AbstractFieldsValidator() {
                    @Override protected boolean isValid(Field field) {
                        if (!field.isConditional()) {
                            return true;
                        }
                        return !field.isMissing();
                    }

                    @Override protected void error(Message message, Field field) {
                        businessReject(conn, message.getMsgType(), message.getMsgSeqNum(), BusinessRejectReasonValue.CONDITIONALLY_REQUIRED_FIELD_MISSING, toString(field) + ": Conditionally required field missing");
                    }
                });
            }
            private static final long serialVersionUID = 1L;
        };
        for (Validator<Message> validator : validators) {
            if (!validator.validate(message))
                return false;
        }
        return true;
    }

    private boolean isOutOfSync() {
        return queue.hasSeqNumGap();
    }

    private void sessionReject(Connection conn, Message message, SessionRejectReasonValue reason, String text) {
        sessionReject(conn, message.getMsgSeqNum(), reason, text);
    }

    private void sessionReject(Connection conn, int msgSeqNum, SessionRejectReasonValue reason, String text) {
        RejectMessage reject = new RejectMessage();
        reject.setInteger(RefSeqNo.TAG, msgSeqNum);
        reject.setEnum(SessionRejectReason.TAG, reason);
        reject.setString(Text.TAG, text);
        send(conn, reject);
    }

    private void businessReject(Connection conn, String msgType, int msgSeqNum, BusinessRejectReasonValue reason, String text) {
        BusinessMessageRejectMessage reject = new BusinessMessageRejectMessage();
        reject.setInteger(RefSeqNo.TAG, msgSeqNum);
        reject.setString(RefMsgType.TAG, msgType);
        reject.setEnum(BusinessRejectReason.TAG, reason);
        reject.setString(Text.TAG, text);
        send(conn, reject);
    }

    private void terminate(Connection conn, Message message, String text) {
        LogoutMessage logout = new LogoutMessage();
        logout.setString(Text.TAG, text);
        send(conn, logout);
        conn.close();
    }

    private void fillSequenceGap(Connection conn, int newSeqNo) {
        SequenceResetMessage seqReset = new SequenceResetMessage();
        seqReset.setPossDupFlag(true);
        seqReset.setBoolean(GapFillFlag.TAG, true);
        seqReset.setInteger(NewSeqNo.TAG, newSeqNo);
        send(conn, seqReset);
        outgoingSeq.reset(newSeqNo);
    }

    private void syncMessages(Connection conn) {
        int beginSeqNo = queue.nextSeqNum();
        ResendRequestMessage resendReq = new ResendRequestMessage();
        resendReq.setInteger(BeginSeqNo.TAG, beginSeqNo);
        resendReq.setInteger(EndSeqNo.TAG, 0);
        send(conn, resendReq);
    }

    private void processSeqReset(SequenceResetMessage message) {
        if (checkSeqResetSeqNum() && !message.isResetOk(queue.nextSeqNum()))
            throw new InvalidSequenceResetException("Expected: " + queue.nextSeqNum() + ", but was: " + message.getMsgSeqNum());
        queue.reset(message.getInteger(NewSeqNo.TAG));
    }
}
