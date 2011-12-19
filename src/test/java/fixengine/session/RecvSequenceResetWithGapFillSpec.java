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

import jdave.junit4.JDaveRunner;

import org.junit.runner.RunWith;

import fixengine.messages.fix42.MsgTypeValue;
import fixengine.tags.fix42.GapFillFlag;
import fixengine.tags.fix42.NewSeqNo;

@RunWith(JDaveRunner.class) public class RecvSequenceResetWithGapFillSpec extends InitiatorSpecification {
    public class InitializedSession {
        /* Ref ID 10: a. Receive Sequence Reset (Gap Fill) message with NewSeqNo >
         * MsgSeqNum and MsgSeqNo > than expected MsgSeqNum */
        public void msgSeqNumGreaterThanExpectedSeqNum() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(MsgTypeValue.SEQUENCE_RESET)
                        .msgSeqNum(4)
                        .bool(GapFillFlag.Tag(), true)
                        .integer(NewSeqNo.Tag(), 3)
                    .build());
            server.expect(MsgTypeValue.RESEND_REQUEST);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 2);
        }

        /* Ref ID 10: b. Receive Sequence Reset (Gap Fill) message with NewSeqNo >
         * MsgSeqNum and MsgSeqNum = to expected MsgSeqNum */
        public void msgSeqNumEqualToExpectedSeqNumAndNewSeqNoGreaterThanMsgSeqNum() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(MsgTypeValue.SEQUENCE_RESET)
                        .msgSeqNum(2)
                        .bool(GapFillFlag.Tag(), true)
                        .integer(NewSeqNo.Tag(), 5)
                    .build());
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 5);
        }

        /* Ref ID 10: c. Receive Sequence Reset (Gap Fill) message with NewSeqNo >
         * MsgSeqNum and MsgSeqNum < than expected MsgSeqNum and
         * PossDupFlag = "Y" */
        public void msgSeqNumSmallerThanExpectedSeqNumWithPossDupFlag() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(MsgTypeValue.SEQUENCE_RESET)
                        .msgSeqNum(1)
                        .setPossDupFlag(true)
                        .bool(GapFillFlag.Tag(), true)
                        .integer(NewSeqNo.Tag(), 5)
                    .build());
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 2);
        }

        /* Ref ID 10: d. Receive Sequence Reset (Gap Fill) message with NewSeqNo >
         * MsgSeqNum and MsgSeqNum < than expected MsgSeqNum and without
         * PossDupFlag = "Y" */
        public void msgSeqNumSmallerThanExpectedSeqNumWithoutPossDupFlag() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(MsgTypeValue.SEQUENCE_RESET)
                        .msgSeqNum(1)
                        .bool(GapFillFlag.Tag(), true)
                        .integer(NewSeqNo.Tag(), 5)
                    .build());
            server.expect(MsgTypeValue.LOGOUT);
            checking(expectLogSevere("MsgSeqNum too low, expecting 2 but received 1"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 2);
        }

        /* Ref ID 10: e. Receive Sequence Reset (Gap Fill) message with NewSeqNo <= MsgSeqNum
         * and MsgSeqNum = to expected sequence number */
        public void msgSeqNumEqualToExpectedSeqNumAndNewSeqNoSmallerOrEqualToMsgSeqNum() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(MsgTypeValue.SEQUENCE_RESET)
                        .msgSeqNum(2)
                        .bool(GapFillFlag.Tag(), true)
                        .integer(NewSeqNo.Tag(), 2)
                    .build());
            server.expect(MsgTypeValue.REJECT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 3);
        }

        public void detectPossibleInfiniteResendLoopWithMessagesOtherThanSequenceReset() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            for (int numOfMsgsSent = 0; numOfMsgsSent <= Session.MAX_CONSECUTIVE_RESEND_REQUESTS; numOfMsgsSent++) {
              server.respond(
                      new MessageBuilder(MsgTypeValue.HEARTBEAT)
                          .msgSeqNum(numOfMsgsSent + 3)
                      .build());
              if (numOfMsgsSent < Session.MAX_CONSECUTIVE_RESEND_REQUESTS)
                server.expect(MsgTypeValue.RESEND_REQUEST);
            }
            server.expect(MsgTypeValue.LOGOUT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 2);
        }

        public void detectPossibleInfiniteResendLoopWithSequenceReset() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            for (int numOfMsgsSent = 0; numOfMsgsSent <= Session.MAX_CONSECUTIVE_RESEND_REQUESTS; numOfMsgsSent++) {
              server.respond(
                      new MessageBuilder(MsgTypeValue.SEQUENCE_RESET)
                          .msgSeqNum(numOfMsgsSent + 3)
                          .bool(GapFillFlag.Tag(), true)
                          .integer(NewSeqNo.Tag(), 2)
                      .build());
              if (numOfMsgsSent < Session.MAX_CONSECUTIVE_RESEND_REQUESTS)
                server.expect(MsgTypeValue.RESEND_REQUEST);
            }
            server.expect(MsgTypeValue.LOGOUT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 2);
        }

        public void detectPossibleInfiniteResendLoop() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            for (int numOfMsgsSent = 0; numOfMsgsSent <= Session.MAX_CONSECUTIVE_RESEND_REQUESTS; numOfMsgsSent++) {
              server.respond(
                      new MessageBuilder(MsgTypeValue.HEARTBEAT)
                          .msgSeqNum(numOfMsgsSent + 3)
                      .build());
              server.expect(MsgTypeValue.RESEND_REQUEST);
              numOfMsgsSent++;
              server.respond(
                      new MessageBuilder(MsgTypeValue.SEQUENCE_RESET)
                          .msgSeqNum(numOfMsgsSent + 4)
                          .bool(GapFillFlag.Tag(), true)
                          .integer(NewSeqNo.Tag(), 2)
                      .build());
              if (numOfMsgsSent + 1 < Session.MAX_CONSECUTIVE_RESEND_REQUESTS)
                server.expect(MsgTypeValue.RESEND_REQUEST);
            }
            server.expect(MsgTypeValue.LOGOUT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 2);
        }
    }
}
