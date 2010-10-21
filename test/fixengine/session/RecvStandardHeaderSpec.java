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

import org.joda.time.DateTime;
import org.junit.runner.RunWith;

import fixengine.messages.EncryptMethodValue;
import fixengine.messages.Message;
import fixengine.messages.MsgTypeValue;
import fixengine.tags.BeginString;
import fixengine.tags.BodyLength;
import fixengine.tags.CheckSum;
import fixengine.tags.EncryptMethod;
import fixengine.tags.HeartBtInt;
import fixengine.tags.MsgSeqNum;
import fixengine.tags.MsgType;
import fixengine.tags.SenderCompID;
import fixengine.tags.SendingTime;
import fixengine.tags.TargetCompID;
import fixengine.tags.TestReqID;

@RunWith(JDaveRunner.class) public class RecvStandardHeaderSpec extends InitiatorSpecification {
    public class InitializedSession {
        /* Ref ID 2: a. MsgSeqNum received as expected */
        public void msgSeqNumReceivedAsExpected() throws Exception {
            logonHeartbeat();
            specify(session.getIncomingSeq().peek(), 3);
        }

        /* Ref ID 2: b. MsgSeqNum higher than expected */
        public void msgSeqNumHigherThanExpected() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(new MessageBuilder(MsgTypeValue.HEARTBEAT).msgSeqNum(3).build());
            server.expect(MsgTypeValue.RESEND_REQUEST);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }

        /* Ref ID 2: c. MsgSeqNum lower than expected without PossDupFlag set to Y */
        public void msgSeqNumLowerThanExpectedWithoutPossDupFlag() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(new MessageBuilder(MsgTypeValue.HEARTBEAT).msgSeqNum(1).build());
            server.expect(MsgTypeValue.LOGOUT);
            checking(expectLogSevere("MsgSeqNum too low, expecting 2 but received 1"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }

        /* Ref ID 2: d. Garbled message received */
        public void garbledMessageReceived() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(MsgTypeValue.HEARTBEAT)
                        .setBeginString("")
                        .msgSeqNum(2)
                    .build());
            server.respond(
                    new MessageBuilder(MsgTypeValue.TEST_REQUEST)
                        .msgSeqNum(2)
                        .string(TestReqID.TAG, "12345678")
                    .build());
            server.expect(MsgTypeValue.HEARTBEAT);
            checking(expectLogWarning("BeginString(8) is empty"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }

        /*
         * Ref ID 2: e. PossDupFlag set to Y; OrigSendingTime specified is less
         * than or equal to SendingTime and MsgSeqNum lower than expected.
         */
        public void possDupFlagOrigSendingTimeLessThanSendingTime() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(MsgTypeValue.HEARTBEAT)
                        .msgSeqNum(2)
                    .build());
            server.respond(
                    new MessageBuilder(MsgTypeValue.HEARTBEAT)
                        .setPossDupFlag(true)
                        .setOrigSendingTime(new DateTime().minusMinutes(1))
                        .msgSeqNum(2)
                    .build());
            server.respondLogout(3);
            server.expect(MsgTypeValue.LOGOUT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }

        /*
         * Ref ID 2: f. PossDupFlag set to Y; OrigSendingTime specified is
         * greater than SendingTime and MsgSeqNum as expected
         */
        public void possDupFlagOrigSendingTimeGreaterThanSendingTime() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            final Message msg1 = new MessageBuilder(MsgTypeValue.HEARTBEAT)
                    .msgSeqNum(2)
                    .build();
            server.respond(msg1);
            final Message msg2 = new MessageBuilder(MsgTypeValue.HEARTBEAT)
                    .setPossDupFlag(true)
                    .setOrigSendingTime(new DateTime().plusMinutes(1))
                    .msgSeqNum(3)
                    .build();
            server.respond(msg2);
            server.expect(MsgTypeValue.REJECT);
            server.expect(MsgTypeValue.LOGOUT);
            checking(expectLogSevere("OrigSendingTime " + formatDateTime(msg2.getOrigSendingTime()) +
                        " after " + formatDateTime(msg1.getSendingTime())));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 4);
        }

        /* Ref ID 2: g. PossDupFlag set to Y and OrigSendingTime not specified */
        public void possDupFlagOrigSendingTimeMissing() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(MsgTypeValue.HEARTBEAT)
                        .msgSeqNum(2)
                    .build());
            server.respond(
                    new MessageBuilder(MsgTypeValue.HEARTBEAT)
                        .setPossDupFlag(true)
                        .msgSeqNum(3)
                    .build());
            server.expect(MsgTypeValue.REJECT);
            checking(expectLogSevere("OrigSendingTime(122): Required tag missing"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }

        /*
         * Ref ID: 2: h. BeginString value received as expected and specified in
         * testing profile and matches BeginString on outbound messages.
         */
        public void beginStringReceivedAsExpected() throws Exception {
            logonHeartbeat();
        }

        /*
         * Ref ID 2: i. BeginString value (e.g. "FIX.4.2") received did not match
         * value expected and specified in testing profile or does not match
         * BeginString on outbound messages.
         */
        public void beginStringReceivedDoesNotMatchExpectedValue() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respond(
                    new MessageBuilder(MsgTypeValue.LOGON)
                        .setBeginString("FIX.4.3")
                        .msgSeqNum(1)
                        .integer(HeartBtInt.TAG, getHeartbeatIntervalInSeconds())
                        .enumeration(EncryptMethod.TAG, EncryptMethodValue.NONE)
                    .build());
            server.expect(MsgTypeValue.LOGOUT);
            checking(expectLogSevere("BeginString is invalid, expecting FIX.4.2 but received FIX.4.3"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }

        /*
         * Ref ID 2: j. SenderCompID and TargetCompID values received as
         * expected and specified in testing profile.
         */
        public void senderAndTargetCompIDsReceivedAsExpected() throws Exception {
            logonHeartbeat();
        }

        /*
         * Ref ID 2: k. SenderCompID and TargetCompID values received did not
         * match values expected and specified in testing profile.
         */
        public void senderCompIdDoesNotMatchExpectedValues() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respond(
                    new MessageBuilder(MsgTypeValue.LOGON)
                        .msgSeqNum(1)
                        .setSenderCompID("SENDER")
                        .integer(HeartBtInt.TAG, getHeartbeatIntervalInSeconds())
                        .enumeration(EncryptMethod.TAG, EncryptMethodValue.NONE)
                    .build());
            server.expect(MsgTypeValue.REJECT);
            server.expect(MsgTypeValue.LOGOUT);
            checking(expectLogSevere("Invalid SenderCompID(49): SENDER"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 2);
        }

        /*
         * Ref ID 2: k. SenderCompID and TargetCompID values received did not
         * match values expected and specified in testing profile.
         */
        public void targetCompIdDoesNotMatchExpectedValues() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respond(
                    new MessageBuilder(MsgTypeValue.LOGON)
                        .msgSeqNum(1)
                        .setTargetCompID("TARGET")
                        .integer(HeartBtInt.TAG, getHeartbeatIntervalInSeconds())
                        .enumeration(EncryptMethod.TAG, EncryptMethodValue.NONE)
                    .build());
            server.expect(MsgTypeValue.REJECT);
            server.expect(MsgTypeValue.LOGOUT);
            checking(expectLogSevere("Invalid TargetCompID(56): TARGET"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 2);
        }

        /* Ref ID 2: l. BodyLength value received is correct. */
        public void bodyLengthReceivedIsCorrect() throws Exception {
            logonHeartbeat();
        }

        /* Ref ID 2: m. BodyLength value received is incorrect. */
        public void bodyLengthReceivedIsIncorrect() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(message("10", "0")
                    .field(MsgSeqNum.TAG, "2")
                    .field(SendingTime.TAG, "20100701-12:09:40")
                    .field(TestReqID.TAG, "1")
                    .field(CheckSum.TAG, "206")
                    .toString());
            checking(expectLogWarning("Expected tag not found: CheckSum(10)"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 2);
        }

        /*
         * Ref ID 2: n. SendingTime value received is specified in UTC
         * (Universal Time Coordinated also known as GMT) and is within a
         * reasonable time (i.e. 2 minutes) of atomic clock-based time.
         */
        public void sendingTimeReceivedIsWithinReasonableTime() throws Exception {
            logonHeartbeat();
        }

        /*
         * Ref ID 2: o. SendingTime value received is either not specified in
         * UTC (Universal Time Coordinated also known as GMT) or is not within a
         * reasonable time (i.e. 2 minutes) of atomic clock-based time.
         */
        public void sendingTimeReceivedIsNotWithinReasonableTime() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            final Message msg = new MessageBuilder(MsgTypeValue.HEARTBEAT)
                    .msgSeqNum(2)
                    .setSendingTime(new DateTime().minusMinutes(10))
                .build();
            server.respond(msg);
            server.expect(MsgTypeValue.REJECT);
            server.expect(MsgTypeValue.LOGOUT);
            checking(expectLogSevere("SendingTime is invalid: " + formatDateTime(msg.getSendingTime())));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 3);
        }

        /*
         * Ref ID 2: p. MsgType value received is valid (defined in spec or
         * classified as user-defined).
         */
        public void msgTypeValueReceivedIsValid() throws Exception {
            logonHeartbeat();
        }

        /*
         * Ref ID 2: q. MsgType value received is not valid (defined in spec or
         * classified as user-defined).
         */
        public void msgTypeValueReceivedIsNotValid() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    message("56", "ZZ")
                    .field(MsgSeqNum.TAG, "2")
                    .field(SendingTime.TAG, "20100701-12:09:40")
                    .field(CheckSum.TAG, "115")
                    .toString());
            server.expect(MsgTypeValue.REJECT);
            checking(expectLogWarning("MsgType(35): Invalid message type: ZZ"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 3);
        }

        /*
         * Ref ID 2: r. MsgType value received is valid (defined in spec or
         * classified as user-defined) but not supported or registered in
         * testing profile.
         */
        public void msgTypeValueReceivedIsValidButNotSupported() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    message("55", "P")
                    .field(MsgSeqNum.TAG, "2")
                    .field(SendingTime.TAG, "20100701-12:09:40")
                    .field(CheckSum.TAG, "014")
                    .toString());
            server.expect(MsgTypeValue.BUSINESS_MESSAGE_REJECT);
            checking(expectLogWarning("MsgType(35): Unknown message type: P"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 3);
        }

        /*
         * Ref ID 2: s. BeginString, BodyLength, and MsgType are first three
         * fields of message.
         */
        public void firstThreeFieldsOfMessageAreValid() throws Exception {
            logonHeartbeat();
        }

        /*
         * Ref ID 2: t. BeginString, BodyLength, and MsgType are not the first three
         * fields of message.
         */
        public void beginStringIsNotTheFirstField() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    message()
                    /* BeginString missing */
                    .field(BodyLength.TAG, "10")
                    .field(MsgType.TAG, "0")
                    .field(MsgSeqNum.TAG, "2")
                    .field(SendingTime.TAG, "20100701-12:09:40")
                    .field(CheckSum.TAG, "165")
                    .toString());
            checking(expectLogWarning("Expected tag not found: BeginString(8)"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 2);
        }

        /*
         * Ref ID 2: t. BeginString, BodyLength, and MsgType are not the first three
         * fields of message.
         */
        public void bodyLengthIsNotTheSecondField() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    message()
                    .field(BeginString.TAG, "FIX.4.2")
                    /* BodyLength missing */
                    .field(MsgType.TAG, "0")
                    .field(MsgSeqNum.TAG, "2")
                    .field(SendingTime.TAG, "20100701-12:09:40")
                    .field(CheckSum.TAG, "165")
                    .toString());
            checking(expectLogWarning("Expected tag not found: BodyLength(9)"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 2);
        }

        /*
         * Ref ID 2: t. BeginString, BodyLength, and MsgType are not the first three
         * fields of message.
         */
        public void msgTypeIsNotTheThirdField() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    message()
                    .field(BeginString.TAG, "FIX.4.2")
                    .field(BodyLength.TAG, "26")
                    /* MsgType missing */
                    .field(MsgSeqNum.TAG, "2")
                    .field(SendingTime.TAG, "20100701-12:09:40")
                    .field(CheckSum.TAG, "214")
                    .toString());
            checking(expectLogWarning("Expected tag not found: MsgType(35)"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 2);
        }

        public void msgSeqNumMissing() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    message()
                    .field(BeginString.TAG, "FIX.4.2")
                    .field(BodyLength.TAG, "50")
                    .field(MsgType.TAG, "0")
                    /* MsgSeqNum missing */
                    .field(SenderCompID.TAG, ACCEPTOR)
                    .field(TargetCompID.TAG, INITIATOR)
                    .field(SendingTime.TAG, "20100701-12:09:40")
                    .field(CheckSum.TAG, "018")
                    .toString());
            server.expect(MsgTypeValue.LOGOUT);
            checking(expectLogSevere("MsgSeqNum(34) is missing"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 2);
        }
    }
}
