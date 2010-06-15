/*
 * Copyright 2008 the original author or authors.
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
import fixengine.Config;
import fixengine.Version;
import fixengine.io.ObjectOutputStream;
import fixengine.messages.AbstractFieldsValidator;
import fixengine.messages.AbstractMessageValidator;
import fixengine.messages.BusinessMessageRejectMessage;
import fixengine.messages.BusinessRejectReason;
import fixengine.messages.DefaultMessageVisitor;
import fixengine.messages.Field;
import fixengine.messages.GarbledMessage;
import fixengine.messages.HeartbeatMessage;
import fixengine.messages.LogoutMessage;
import fixengine.messages.Message;
import fixengine.messages.MessageVisitor;
import fixengine.messages.NullMessage;
import fixengine.messages.RejectMessage;
import fixengine.messages.ResendRequestMessage;
import fixengine.messages.SequenceResetMessage;
import fixengine.messages.Session;
import fixengine.messages.SessionRejectReason;
import fixengine.messages.TestRequestMessage;
import fixengine.messages.UnknownMessage;
import fixengine.messages.Validator;
import fixengine.session.store.SessionStore;

/**
 * @author Pekka Enberg 
 */
public abstract class AbstractSession implements Session {
    protected TimeSource timeSource = new DefaultTimeSource();
    protected MessageQueue queue = new MessageQueue();
    protected Sequence outgoingSeq = new Sequence();
    protected final ObjectOutputStream<Message> stream;
    protected final Config config;
    protected boolean authenticated;
    protected SessionStore store;

    public AbstractSession(ObjectOutputStream<Message> stream, Config config, SessionStore store) {
        this.stream = stream;
        this.config = config;
        this.store = store;
    }

    public void setTimeSource(TimeSource timeSource) {
        this.timeSource = timeSource;
    }

    public Config getConfig() {
        return config;
    }

    public Sequence getOutgoingSeq() {
        return outgoingSeq;
    }
    
    public void setOutgoingSeq(Sequence outgoingSeq) {
        this.outgoingSeq = outgoingSeq;
    }

    public boolean isDisconnected() {
        return stream.isClosed();
    }

    public void disconnect() {
        authenticated = false;
        stream.close();
        synchronized (queue) {
            queue.notify();
        }
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void logout() {
        send(new LogoutMessage());

        Message message = waitForResponse();
        /*
         * The other end can send an optional Logout message before terminating
         * the connection or it can just terminate the connection immediately.
         */
        message.apply(new DefaultMessageVisitor() {
            @Override
            public void visit(LogoutMessage message) {
            }

            @Override
            public void visit(NullMessage message) {
            }

            @Override
            public void defaultAction(Message message) {
                sessionReject(message, SessionRejectReason.INVALID_MSG_TYPE, "Invalid MsgType(35): " + message.getMsgType());
            }
        });
        authenticated = false;
        disconnect();
    }

    public void send(Message message) {
        message.setHeaderConfig(config);
        message.setMsgSeqNum(outgoingSeq.next());
        message.setSendingTime(timeSource.currentTime());
        stream.writeObject(message);
        store.save(this);
    }

    public Message processMessage(final MessageVisitor processor) {
        Message message = waitForResponse();
        message.apply(new DefaultMessageVisitor() {
            @Override
            public void defaultAction(Message message) {
                message.apply(processor);
            }
            
            @Override
            public void visit(LogoutMessage message) {
                send(new LogoutMessage());
                disconnect();
            }
        });
        return message;
    }

    private Message waitForResponse() {
        if (isDisconnected())
            return new NullMessage();

        synchronized (queue) {
            if (queue.isEmpty()) {
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                }
            }
            if (queue.isEmpty()) {
                return new NullMessage();
            }
            return queue.dequeue();
        }
    }

    public void receive(Message message) {
        message.apply(new DefaultMessageVisitor() {
            @Override
            public void visit(GarbledMessage message) {
                /* Ignore the message.  */
            }
            
            @Override
            public void visit(NullMessage message) {
                /* The other end terminated the connection.  */
                stream.close();
            }
         
            @Override
            public void visit(UnknownMessage message) {
                if (config.supports(Version.FIX_4_2)) {
                    if (message.hasValidMsgType()) {
                        businessReject(message, BusinessRejectReason.UNKNOWN_MESSAGE_TYPE, message.getMsgType());
                    } else {
                        sessionReject(message, SessionRejectReason.INVALID_MSG_TYPE, message.getMsgType());
                    }
                } else {
                    sessionReject(message, null, message.getMsgType());
                }
            }

            @Override
            public void defaultAction(Message message) {
                doReceive(message);
            }
        });
    }

    private void sessionReject(Message message, SessionRejectReason reason, String text) {
        queue.skip(message);
        RejectMessage reject = new RejectMessage();
        reject.setRefSeqNo(message.getMsgSeqNum());
        reject.setSessionRejectReason(reason);
        reject.setText(text);
        send(reject);
    }

    private void businessReject(Message message, BusinessRejectReason reason, String text) {
        queue.skip(message);
        BusinessMessageRejectMessage reject = new BusinessMessageRejectMessage();
        reject.setRefSeqNo(message.getMsgSeqNum());
        reject.setRefMsgType(message.getMsgType());
        reject.setBusinessRejectReason(reason);
        reject.setText(text);
        send(reject);
    }

    private void doReceive(Message message) {
        int expected;

        synchronized (queue) {
            expected = queue.nextSeqNum();

            if (validate(message)) {
                process(message);
            }
        }

        /*
         * We're out-of-sync if there's a gap in the sequence numbers. However,
         * if the other side is in the middle of resending the missing messages,
         * don't attempt to sync after each received message.
         */
        if (!isDisconnected() && isOutOfSync()
                && message.getMsgSeqNum() != expected) {
            syncMessages();
        }
    }

    private boolean validate(Message message) {
        for (Validator<Message> validator : validators) {
            if (!validator.validate(message))
                return false;
        }
        return true;
    }

    private final List<Validator<Message>> validators = new ArrayList<Validator<Message>>() {
        {
            add(new AbstractMessageValidator() {
                @Override
                protected boolean isValid(Message message) {
                    return !message.isTooLowSeqNum(queue.nextSeqNum());
                }

                @Override
                protected void error(Message message) {
                    terminate(message, "MsgSeqNum too low, expecting " + queue.nextSeqNum() + " but received " + message.getMsgSeqNum());
                }
            });
            add(new AbstractMessageValidator() {
                @Override
                protected boolean isValid(Message message) {
                    return message.hasValidBeginString(config);
                }

                @Override
                protected void error(Message message) {
                    terminate(message, "BeginString is invalid, expecting " + config.getVersion().value() + " but received " + message.getBeginString());
                }
            });
            add(new AbstractMessageValidator() {
                @Override
                protected boolean isValid(Message message) {
                    return !authenticated || message.hasValidSenderCompId(config);
                }

                @Override
                protected void error(Message message) {
                    sessionReject(message, SessionRejectReason.COMP_ID_PROBLEM, "Invalid SenderCompId(49): " + message.getSenderCompId());
                    terminate(message, message.getSenderCompId());
                }
            });
            add(new AbstractMessageValidator() {
                @Override
                protected boolean isValid(Message message) {
                    return message.hasOrigSendTimeAfterSendingTime();
                }

                @Override
                protected void error(Message message) {
                    String text = "OrigSendTime " + message.getOrigSendingTime() + " after " + message.getSendingTime();
                    sessionReject(message, SessionRejectReason.SENDING_TIME_ACCURACY_PROBLEM, text);
                    terminate(message, text);
                }
            });
            add(new AbstractMessageValidator() {
                @Override
                protected boolean isValid(Message message) {
                    return message.hasAccurateSendingTime(timeSource.currentTime());
                }

                @Override
                protected void error(Message message) {
                    String text = "SendingTime is invalid: " + message.getSendingTime();
                    sessionReject(message, SessionRejectReason.SENDING_TIME_ACCURACY_PROBLEM, text);
                    terminate(message, text);
                }
            });
            add(new AbstractMessageValidator() {
                @Override
                protected boolean isValid(Message message) {
                    return !message.isOrigSendingTimeMissing();
                }

                @Override
                protected void error(Message message) {
                    sessionReject(message, SessionRejectReason.TAG_MISSING, "OrigSendingTime(122) is missing");
                }
            });
            add(new AbstractMessageValidator() {
                @Override
                protected boolean isValid(Message message) {
                    return message.isPointToPoint();
                }

                @Override
                protected void error(Message message) {
                    sessionReject(message, SessionRejectReason.COMP_ID_PROBLEM, "Third-party message routing is not supported");
                }
            });
            add(new AbstractFieldsValidator() {
                @Override
                protected boolean isValid(Field field) {
                    return field.isFormatValid();
                }

                @Override
                protected void error(Message message, Field field) {
                    sessionReject(message, SessionRejectReason.INVALID_VALUE_FORMAT, toString(field) + ": Invalid value format");
                }
            });
            add(new AbstractFieldsValidator() {
                @Override
                protected boolean isValid(Field field) {
                    return field.isValueValid();
                }

                @Override
                protected void error(Message message, Field field) {
                    sessionReject(message, SessionRejectReason.INVALID_VALUE, toString(field) + ": Invalid value");
                }
            });
            add(new AbstractFieldsValidator() {
                @Override
                protected boolean isValid(Field field) {
                    return !field.isEmpty();
                }

                @Override
                protected void error(Message message, Field field) {
                    sessionReject(message, SessionRejectReason.EMPTY_TAG, toString(field) + ": Empty tag");
                }
            });
            add(new AbstractFieldsValidator() {
                @Override
                protected boolean isValid(Field field) {
                    return !field.isMissing();
                }

                @Override
                protected void error(Message message, Field field) {
                    sessionReject(message, SessionRejectReason.TAG_MISSING, toString(field) + ": Tag missing");
                }
            });
        }
        private static final long serialVersionUID = 1L;
    };

    private void terminate(Message message, String text) {
        LogoutMessage logout = new LogoutMessage();
        logout.setText(text);
        send(logout);
        disconnect();
    }

    private void process(Message message) {
        message.apply(new DefaultMessageVisitor() {
            public void visit(TestRequestMessage message) {
                queue.skip(message);
                HeartbeatMessage heartbeat = new HeartbeatMessage();
                heartbeat.setTestReqId(message.getTestReqId());
                send(heartbeat);
            }

            public void visit(ResendRequestMessage message) {
                queue.skip(message);
                int newSeqNo = outgoingSeq.peek();
                outgoingSeq.reset(message.getBeginSeqNo());
                fillSequenceGap(newSeqNo);
            }

            public void visit(SequenceResetMessage message) {
                processSeqReset(message);
            }

            public void defaultAction(Message message) {
                queue.enqueue(message);

                if (!isOutOfSync())
                    queue.notify();
            }
        });
    }

    private void fillSequenceGap(int newSeqNo) {
        SequenceResetMessage seqReset = new SequenceResetMessage();
        seqReset.setPossDupFlag(true);
        seqReset.setGapFillFlag(true);
        seqReset.setNewSeqNo(newSeqNo);
        send(seqReset);
        outgoingSeq.reset(newSeqNo);
    }

    public boolean isOutOfSync() {
        synchronized (queue) {
            return queue.hasSeqNumGap();
        }
    }

    private void syncMessages() {
        int beginSeqNo;
        synchronized (queue) {
            beginSeqNo = queue.nextSeqNum();
        }
        ResendRequestMessage resendReq = new ResendRequestMessage();
        resendReq.setBeginSeqNo(beginSeqNo);
        resendReq.setEndSeqNo(0);
        send(resendReq);
    }

    private void processSeqReset(SequenceResetMessage message) {
        if (!message.isResetOk(queue.nextSeqNum())) {
            throw new InvalidSequenceResetException("Expected "
                    + queue.nextSeqNum() + ", but was: "
                    + message.getMsgSeqNum());
        }
        queue.reset(message.getNewSeqNo());
    }
}
