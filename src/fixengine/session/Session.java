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
import fixengine.messages.BusinessRejectReason;
import fixengine.messages.DefaultMessageVisitor;
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
import fixengine.messages.SessionRejectReason;
import fixengine.messages.TestRequestMessage;
import fixengine.messages.Validator;
import fixengine.session.store.SessionStore;

/**
 * @author Karim Osman
 */
public class Session {
  protected MessageQueue queue = new MessageQueue();
  protected Sequence outgoingSeq = new Sequence();
  protected TimeSource timeSource = new DefaultTimeSource();

  protected final HeartBtInt heartBtInt;
  protected final Config config;
  protected final SessionStore store;

  private long testReqId;
  private boolean initiatedLogout;

  private long prevTxTimeMsec = System.currentTimeMillis();
  private long prevRxTimeMsec = System.currentTimeMillis();

  public Session(HeartBtInt heartBtInt, Config config, SessionStore store) {
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
           * We're out-of-sync if there's a gap in the sequence numbers.
           * However, if the other side is in the middle of resending the
           * missing messages, don't attempt to sync after each received
           * message.
           */
          if (!conn.isClosed() && isOutOfSync() && message.getMsgSeqNum() != expected)
            syncMessages(conn);
        }

        @Override public void invalidMessage(int msgSeqNum, SessionRejectReason reason, String text) {
          sessionReject(conn, msgSeqNum, reason, text);
        }

        @Override public void unknownMsgType(String msgType, int msgSeqNum) {
          businessReject(conn, msgType, msgSeqNum, BusinessRejectReason.UNKNOWN_MESSAGE_TYPE, "MsgType(35): Unknown message type: " + msgType);
        }

        @Override public void invalidMessageType(String msgType, int msgSeqNum) {
          sessionReject(conn, msgSeqNum, SessionRejectReason.INVALID_MSG_TYPE, "MsgType(35): Invalid message type: " + msgType);
        }

        @Override public void garbledMessage(String text) {
          /* Ignore the message. */
        }

        @Override public void invalidBeginString(String text) {
          terminate(conn, null, text);
        }
      });
    } finally {
      store.save(this);
    }
  }

  public void logon(Connection conn) {
    initiatedLogout = false;
    send(conn, new LogonMessage(false));
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
    message.setNewSeqNo(seq.next());
    message.setGapFillFlag(false);
    conn.send(silvertip.Message.fromString(message.format()));
    prevTxTimeMsec = System.currentTimeMillis();
    setOutgoingSeq(seq);
    store.save(this);
  }

  public void keepAlive(Connection conn) {
    long curTimeMsec = System.currentTimeMillis();

    if (curTimeMsec - prevTxTimeMsec > heartBtInt.heartbeat().delayMsec()) {
      heartbeat(conn);
      prevTxTimeMsec = System.currentTimeMillis();
    }

    if (curTimeMsec - prevRxTimeMsec > heartBtInt.testRequest().delayMsec())  {
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
    req.setTestReqId(Long.toString(++testReqId));
    send(conn, req);
  }

  private void process(final Connection conn, Message message, final MessageVisitor visitor) {
    message.apply(new DefaultMessageVisitor() {
      @Override
      public void visit(TestRequestMessage message) {
        queue.skip(message);
        HeartbeatMessage heartbeat = new HeartbeatMessage();
        heartbeat.setTestReqId(message.getTestReqId());
        send(conn, heartbeat);
      }

      @Override
      public void visit(ResendRequestMessage message) {
        queue.skip(message);
        int newSeqNo = outgoingSeq.peek();
        outgoingSeq.reset(message.getBeginSeqNo());
        fillSequenceGap(conn, newSeqNo);
      }

      @Override
      public void visit(SequenceResetMessage message) {
        processSeqReset(message);
      }

      @Override
      public void visit(LogoutMessage message) {
        queue.skip(message);
        if (!initiatedLogout) send(conn, new LogoutMessage());
        conn.close();
      }

      @Override
      public void defaultAction(Message message) {
        queue.enqueue(message);
        if (!isOutOfSync()) {
          while (!queue.isEmpty()) queue.dequeue().apply(visitor);
        }
      }
    });
  }

  private boolean validate(final Connection conn, Message message) {
    List<Validator<Message>> validators = new ArrayList<Validator<Message>>() {
      {
        add(new AbstractMessageValidator() {
          @Override
          protected boolean isValid(Message message) {
            return !message.isTooLowSeqNum(queue.nextSeqNum());
          }

          @Override
          protected void error(Message message) {
            terminate(conn, message, "MsgSeqNum too low, expecting " + queue.nextSeqNum() + " but received " + message.getMsgSeqNum());
          }
        });
        add(new AbstractMessageValidator() {
          @Override
          protected boolean isValid(Message message) {
            return message.hasValidBeginString(config);
          }

          @Override
          protected void error(Message message) {
            terminate(conn, message, "BeginString is invalid, expecting " + config.getVersion().value() + " but received " + message.getBeginString());
          }
        });
        add(new AbstractMessageValidator() {
          @Override
          protected boolean isValid(Message message) {
            return message.hasValidSenderCompId(config);
          }

          @Override
          protected void error(Message message) {
            sessionReject(conn, message, SessionRejectReason.COMP_ID_PROBLEM, "Invalid SenderCompId(49): " + message.getSenderCompId());
            terminate(conn, message, message.getSenderCompId());
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
            sessionReject(conn, message, SessionRejectReason.SENDING_TIME_ACCURACY_PROBLEM, text);
            terminate(conn, message, text);
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
            sessionReject(conn, message, SessionRejectReason.SENDING_TIME_ACCURACY_PROBLEM, text);
            terminate(conn, message, text);
          }
        });
        add(new AbstractMessageValidator() {
          @Override
          protected boolean isValid(Message message) {
            return !message.isOrigSendingTimeMissing();
          }

          @Override
          protected void error(Message message) {
            sessionReject(conn, message, SessionRejectReason.TAG_MISSING, "OrigSendingTime(122) is missing");
          }
        });
        add(new AbstractMessageValidator() {
          @Override
          protected boolean isValid(Message message) {
            return message.isPointToPoint();
          }

          @Override
          protected void error(Message message) {
            sessionReject(conn, message, SessionRejectReason.COMP_ID_PROBLEM, "Third-party message routing is not supported");
          }
        });
        add(new AbstractFieldsValidator() {
          @Override
          protected boolean isValid(Field field) {
            return !field.isUnrecognized();
          }

          @Override
          protected void error(Message message, Field field) {
            sessionReject(conn, message, SessionRejectReason.INVALID_TAG, toString(field) + ": Invalid tag");
          }
        });
        add(new AbstractFieldsValidator() {
            @Override
            protected boolean isValid(Field field) {
              return !field.isUserDefined();
            }

            @Override
            protected void error(Message message, Field field) {
              sessionReject(conn, message, SessionRejectReason.INVALID_TAG_NUMBER, toString(field) + ": Invalid tag number");
            }
          });
        add(new AbstractFieldsValidator() {
          @Override
          protected boolean isValid(Field field) {
            return !field.isMissing();
          }

          @Override
          protected void error(Message message, Field field) {
            sessionReject(conn, message, SessionRejectReason.TAG_MISSING, toString(field) + ": Tag missing");
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

  private void sessionReject(Connection conn, Message message, SessionRejectReason reason, String text) {
      sessionReject(conn, message.getMsgSeqNum(), reason, text);
  }

  private void sessionReject(Connection conn, int msgSeqNum, SessionRejectReason reason, String text) {
    RejectMessage reject = new RejectMessage();
    reject.setRefSeqNo(msgSeqNum);
    reject.setSessionRejectReason(reason);
    reject.setText(text);
    send(conn, reject);
  }

  private void businessReject(Connection conn, String msgType, int msgSeqNum, BusinessRejectReason reason, String text) {
    BusinessMessageRejectMessage reject = new BusinessMessageRejectMessage();
    reject.setRefSeqNo(msgSeqNum);
    reject.setRefMsgType(msgType);
    reject.setBusinessRejectReason(reason);
    reject.setText(text);
    send(conn, reject);
  }

  private void terminate(Connection conn, Message message, String text) {
    LogoutMessage logout = new LogoutMessage();
    logout.setText(text);
    send(conn, logout);
    conn.close();
  }

  private void fillSequenceGap(Connection conn, int newSeqNo) {
    SequenceResetMessage seqReset = new SequenceResetMessage();
    seqReset.setPossDupFlag(true);
    seqReset.setGapFillFlag(true);
    seqReset.setNewSeqNo(newSeqNo);
    send(conn, seqReset);
    outgoingSeq.reset(newSeqNo);
  }

  private void syncMessages(Connection conn) {
    int beginSeqNo = queue.nextSeqNum();
    ResendRequestMessage resendReq = new ResendRequestMessage();
    resendReq.setBeginSeqNo(beginSeqNo);
    resendReq.setEndSeqNo(0);
    send(conn, resendReq);
  }

  private void processSeqReset(SequenceResetMessage message) {
    if (checkSeqResetSeqNum() && !message.isResetOk(queue.nextSeqNum()))
      throw new InvalidSequenceResetException("Expected: " + queue.nextSeqNum() + ", but was: " + message.getMsgSeqNum());
    queue.reset(message.getNewSeqNo());
  }
}
