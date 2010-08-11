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

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import jdave.Specification;
import jdave.junit4.JDaveRunner;
import lang.DefaultTimeSource;

import org.joda.time.DateTime;
import org.junit.runner.RunWith;

import silvertip.Connection;
import silvertip.Events;
import silvertip.protocols.FixMessageParser;
import fixengine.Config;
import fixengine.Version;
import fixengine.messages.BooleanField;
import fixengine.messages.DefaultMessageVisitor;
import fixengine.messages.EncryptMethodValue;
import fixengine.messages.EnumField;
import fixengine.messages.Field;
import fixengine.messages.Formattable;
import fixengine.messages.IntegerField;
import fixengine.messages.Message;
import fixengine.messages.MessageHeader;
import fixengine.messages.MsgTypeValue;
import fixengine.messages.Parser;
import fixengine.messages.RawMessageBuilder;
import fixengine.messages.SessionRejectReasonValue;
import fixengine.messages.StringField;
import fixengine.messages.Tag;
import fixengine.session.store.SessionStore;
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
import fixengine.tags.RefSeqNo;
import fixengine.tags.BeginSeqNo;
import fixengine.tags.EndSeqNo;
import fixengine.tags.NewSeqNo;
import fixengine.tags.GapFillFlag;

@RunWith(JDaveRunner.class) public class InitiatorSpec extends Specification<Void> {
    private static final Version VERSION = Version.FIX_4_2;
    private static final String INITIATOR = "initiator";
    private static final String ACCEPTOR = "OPENFIX";
    private static final int HEARTBEAT_INTERVAL = 1;
    private static final int PORT = 7001;

    private final SimpleAcceptor server = new SimpleAcceptor(PORT);
    private Connection connection;
    private Session session;

    public class Logon {
        /* Ref ID 1B: b. Send Logon message */
        public void valid() throws Exception {
            server.expect(LOGON);
            server.respondLogon();
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }

        /* Ref ID 1B: c. Valid Logon message as response is received */
        public void validButMsgSeqNumIsTooHigh() throws Exception {
            server.expect(LOGON);
            server.respond(
                    new MessageBuilder(LOGON)
                        .msgSeqNum(2)
                        .integer(HeartBtInt.TAG, HEARTBEAT_INTERVAL)
                        .enumeration(EncryptMethod.TAG, EncryptMethodValue.NONE)
                    .build());
            server.expect(RESEND_REQUEST);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }

        /* Ref ID 1B: d. Invalid Logon message is received */
        public void invalid() throws Exception {
            // TODO: Invalid MsgType
            // TODO: Garbled message
            server.expect(LOGON);
            server.respond(
                    new MessageBuilder(LOGON)
                        .msgSeqNum(1)
                        .integer(HeartBtInt.TAG, HEARTBEAT_INTERVAL)
                        /* EncryptMethod(98) missing */
                    .build());
            server.expect(LOGOUT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }

        /* Ref ID 1B: e. Receive any message other than a Logon message. */
        public void otherMessageThanLogon() throws Exception {
            server.expect(LOGON);
            server.respond(
                    new MessageBuilder(HEARTBEAT)
                        .msgSeqNum(1)
                    .build());
            server.expect(LOGOUT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }
    }

    public class ReceiveMessageStandardHeader {
        /* Ref ID 2: a. MsgSeqNum received as expected */
        public void msgSeqNumReceivedAsExpected() throws Exception {
            logonHeartbeat();
            specify(session.getIncomingSeq().peek(), 3);
        }

        /* Ref ID 2: b. MsgSeqNum higher than expected */
        public void msgSeqNumHigherThanExpected() throws Exception {
            server.expect(LOGON);
            server.respondLogon();
            server.respond(new MessageBuilder(HEARTBEAT).msgSeqNum(3).build());
            server.expect(RESEND_REQUEST);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }

        /* Ref ID 2: c. MsgSeqNum lower than expected without PossDupFlag set to Y */
        public void msgSeqNumLowerThanExpectedWithoutPossDupFlag() throws Exception {
            server.expect(LOGON);
            server.respondLogon();
            server.respond(new MessageBuilder(HEARTBEAT).msgSeqNum(1).build());
            server.expect(LOGOUT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }

        /* Ref ID 2: d. Garbled message received */
        public void garbledMessageReceived() throws Exception {
            server.expect(LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(HEARTBEAT)
                        .setBeginString("")
                        .msgSeqNum(2)
                    .build());
            server.respond(
                    new MessageBuilder(TEST_REQUEST)
                        .msgSeqNum(2)
                        .string(TestReqID.TAG, "12345678")
                    .build());
            server.expect(HEARTBEAT);
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
            server.expect(LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(HEARTBEAT)
                        .msgSeqNum(2)
                    .build());
            server.respond(
                    new MessageBuilder(HEARTBEAT)
                        .setPossDupFlag(true)
                        .setOrigSendingTime(new DateTime().minusMinutes(1))
                        .msgSeqNum(2)
                    .build());
            server.respondLogout(3);
            server.expect(LOGOUT);
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
            server.expect(LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(HEARTBEAT)
                        .msgSeqNum(2)
                    .build());
            server.respond(
                    new MessageBuilder(HEARTBEAT)
                        .setPossDupFlag(true)
                        .setOrigSendingTime(new DateTime().plusMinutes(1))
                        .msgSeqNum(3)
                    .build());
            server.expect(REJECT);
            server.expect(LOGOUT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 4);
        }

        /* Ref ID 2: g. PossDupFlag set to Y and OrigSendingTime not specified */
        public void possDupFlagOrigSendingTimeMissing() throws Exception {
            server.expect(LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(HEARTBEAT)
                        .msgSeqNum(2)
                    .build());
            server.respond(
                    new MessageBuilder(HEARTBEAT)
                        .setPossDupFlag(true)
                        .msgSeqNum(3)
                    .build());
            server.expect(REJECT);
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
            server.expect(LOGON);
            server.respond(
                    new MessageBuilder(LOGON)
                        .setBeginString("FIX.4.3")
                        .msgSeqNum(1)
                        .integer(HeartBtInt.TAG, HEARTBEAT_INTERVAL)
                        .enumeration(EncryptMethod.TAG, EncryptMethodValue.NONE)
                    .build());
            server.expect(LOGOUT);
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
            server.expect(LOGON);
            server.respond(
                    new MessageBuilder(LOGON)
                        .msgSeqNum(1)
                        .setSenderCompID("SENDER")
                        .integer(HeartBtInt.TAG, HEARTBEAT_INTERVAL)
                        .enumeration(EncryptMethod.TAG, EncryptMethodValue.NONE)
                    .build());
            server.expect(REJECT);
            server.expect(LOGOUT);
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
            server.expect(LOGON);
            server.respond(
                    new MessageBuilder(LOGON)
                        .msgSeqNum(1)
                        .setTargetCompID("TARGET")
                        .integer(HeartBtInt.TAG, HEARTBEAT_INTERVAL)
                        .enumeration(EncryptMethod.TAG, EncryptMethodValue.NONE)
                    .build());
            server.expect(REJECT);
            server.expect(LOGOUT);
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
            server.expect(LOGON);
            server.respondLogon();
            server.respond(message("10", "0")
                    .field(MsgSeqNum.TAG, "2")
                    .field(SendingTime.TAG, "20100701-12:09:40")
                    .field(TestReqID.TAG, "1")
                    .field(CheckSum.TAG, "206")
                    .toString());
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
            server.expect(LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(HEARTBEAT)
                        .msgSeqNum(2)
                        .setSendingTime(new DateTime().minusMinutes(10))
                    .build());
            server.expect(REJECT);
            server.expect(LOGOUT);
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
         * classified as user- defined).
         */
        public void msgTypeValueReceivedIsNotValid() throws Exception {
            server.expect(LOGON);
            server.respondLogon();
            server.respond(
                    message("52", "ZZ")
                    .field(MsgSeqNum.TAG, "2")
                    .field(SendingTime.TAG, "20100701-12:09:40")
                    .field(CheckSum.TAG, "075")
                    .toString());
            server.expect(REJECT);
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
            server.expect(LOGON);
            server.respondLogon();
            server.respond(
                    message("51", "P")
                    .field(MsgSeqNum.TAG, "2")
                    .field(SendingTime.TAG, "20100701-12:09:40")
                    .field(CheckSum.TAG, "230")
                    .toString());
            server.expect(BUSINESS_MESSAGE_REJECT);
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
            server.expect(LOGON);
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
            server.expect(LOGON);
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
            server.expect(LOGON);
            server.respondLogon();
            server.respond(
                    message()
                    .field(BeginString.TAG, "FIX.4.2")
                    .field(BodyLength.TAG, "26")
                    /* MsgType missing */
                    .field(MsgSeqNum.TAG, "2")
                    .field(SendingTime.TAG, "20100701-12:09:40")
                    .field(CheckSum.TAG, "165")
                    .toString());
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 2);
        }
    }

    public class ReceiveMessageStandardTrailer {
        /* Ref ID 3: a. Valid CheckSum */
        public void valid() throws Exception {
            logonHeartbeat();
            specify(session.getIncomingSeq().peek(), 3);
        }

        /* Ref ID 3: b. Invalid CheckSum */
        public void invalid() throws Exception {
            server.expect(LOGON);
            server.respondLogon();
            server.respond(
                    message()
                    .field(BeginString.TAG, "FIX.4.2")
                    .field(BodyLength.TAG, "55")
                    .field(MsgType.TAG, "0")
                    .field(SenderCompID.TAG, "OPENFIX")
                    .field(TargetCompID.TAG, "initiator")
                    .field(MsgSeqNum.TAG, "2")
                    .field(SendingTime.TAG, "20100810-07:25:02")
                    .field(CheckSum.TAG, "100")
                    .toString());
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 2);
        }

        /* Ref ID 3: c. Garbled message */
        public void garbledMessage() throws Exception {
            missingCheckSumField();
        }

        /* Ref ID 3: d. CheckSum is last field of message, value has length of
         * 3, and is delimited by <SOH>. */
        public void checkSumIsLastFieldOfMsgEtc() throws Exception {
            logonHeartbeat();
            specify(session.getIncomingSeq().peek(), 3);
        }

        /* Ref ID 3: e. CheckSum is not the last field of message. */
        public void checkSumIsNotTheLastFieldOfMsg() throws Exception {
            missingCheckSumField();
        }

        /* Ref ID 3: e. CheckSum value does not have length of 3. */
        public void checkSumValueDoesNotHaveLengthOfThree() throws Exception {
            server.expect(LOGON);
            server.respondLogon();
            server.respond(
                    message()
                    .field(BeginString.TAG, "FIX.4.2")
                    .field(BodyLength.TAG, "56")
                    .field(MsgType.TAG, "0")
                    .field(SenderCompID.TAG, "acceptor")
                    .field(TargetCompID.TAG, "initiator")
                    .field(MsgSeqNum.TAG, "2")
                    .field(SendingTime.TAG, "20100810-07:58:22")
                    .field(CheckSum.TAG, "48")
                    .toString());
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 2);
        }

        /* Ref ID 3: e. CheckSum is not delimited by <SOH>. */
        public void checkSumIsNotDelimitedBySOH() throws Exception {
            server.expect(LOGON);
            server.respondLogon();
            String msg = message()
                .field(BeginString.TAG, "FIX.4.2")
                .field(BodyLength.TAG, "55")
                .field(MsgType.TAG, "0")
                .field(SenderCompID.TAG, "OPENFIX")
                .field(TargetCompID.TAG, "initiator")
                .field(MsgSeqNum.TAG, "2")
                .field(SendingTime.TAG, "20100810-07:25:02")
                .field(CheckSum.TAG, "239").toString();
            server.respond(msg.substring(0, msg.length() - 1));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 2);
        }

        private void missingCheckSumField() throws Exception {
            server.expect(LOGON);
            server.respondLogon();
            server.respond(
                    message()
                    .field(BeginString.TAG, "FIX.4.2")
                    .field(BodyLength.TAG, "55")
                    .field(MsgType.TAG, "0")
                    .field(SenderCompID.TAG, "OPENFIX")
                    .field(TargetCompID.TAG, "initiator")
                    .field(MsgSeqNum.TAG, "2")
                    .field(SendingTime.TAG, "20100810-07:25:02")
                    .toString());
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 2);
        }
    }

    public class SendHeartbeatMessage {
        /* Ref ID 4: a. No data sent during preset heartbeat interval
         * (HeartBeatInt field). */
        public void noDataSentDuringPresetHeartbeatInterval() throws Exception {
            logonHeartbeatTestRequest();
        }

        /* Ref ID 4: b. A TestRequest message is received. */
        public void testRequestMsgReceived() throws Exception {
            logonHeartbeatTestRequest();
        }

        private void logonHeartbeatTestRequest() throws Exception {
            server.expect(LOGON);
            server.respondLogon();
            server.expect(HEARTBEAT);
            server.expect(TEST_REQUEST);
            server.respondLogout(3);
            server.expect(LOGOUT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            }, true);
        }
    }

    public class ReceiveHeartbeatMessage {
        /* Ref ID 5: Valid Heartbeat message */
        public void valid() throws Exception {
            server.expect(LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(HEARTBEAT)
                        .msgSeqNum(2)
                    .build(), HEARTBEAT_INTERVAL * 500L);
            server.respondLogout(3);
            server.expect(LOGOUT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }
    }

    public class SendTestRequest {
        /* Ref ID 6: No data received during preset heartbeat interval
         * (HeatbeatInt field) + "some reasonable amount of time" (use 20% of
         * HeartBeatInt field) */
        public void noDataReceivedDuringPresetHeartbeatInterval() throws Exception {
            server.expect(LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(TEST_REQUEST)
                        .msgSeqNum(2)
                        .string(TestReqID.TAG, "12345678")
                    .build());
            // TODO: Verify that TestReqID of Heartbeat matches
            server.expect(HEARTBEAT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }
    }

    public class ReceiveRejectMessage {
        /* Ref ID 7: Valid reject message */
        public void valid() throws Exception {
            server.expect(LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(REJECT)
                        .msgSeqNum(2)
                        .integer(RefSeqNo.TAG, 2)
                    .build());
            server.respondLogout(3);
            server.expect(LOGOUT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 4);
        }
    }

    public class ReceiveResendRequestMessage {
        /* TODO: The current implementation does not support resending of
         * messages as specified in this test case, instead a SequenceReset is
         * issued. Once message recovery is implemented this test case shall be
         * rewritten. */
        /* Ref ID 8: Valid Resend Request */
        public void valid() throws Exception {
            server.expect(LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(HEARTBEAT)
                        .msgSeqNum(2)
                    .build());
            server.respond(
                    new MessageBuilder(RESEND_REQUEST)
                        .msgSeqNum(3)
                        .integer(BeginSeqNo.TAG, 1)
                        .integer(EndSeqNo.TAG, 0)
                    .build());
            server.expect(SEQUENCE_RESET);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }
    }

    public class SynchronizeSequenceNumbers {
        /* Ref ID 9: Application failure */
        public void applicationFailure() throws Exception {
            server.expect(LOGON);
            server.respondLogon();
            server.expect(HEARTBEAT);
            server.expect(SEQUENCE_RESET);
            server.respondLogout(2);
            server.expect(LOGOUT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                    session.heartbeat(connection);
                    session.sequenceReset(connection, new Sequence());
                }
            });
            specify(session.getIncomingSeq().peek(), 3);
            specify(session.getOutgoingSeq().peek(), 3);
        }
    }

    public class ReceiveSequenceReset {
        /* Ref ID 10: a. Receive Sequence Reset (Gap Fill) message with NewSeqNo >
         * MsgSeqNum and MsgSeqNo > than expected MsgSeqNum */
        public void msgSeqNumGreaterThanExpectedSeqNum() throws Exception {
            server.expect(LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(SEQUENCE_RESET)
                        .msgSeqNum(4)
                        .bool(GapFillFlag.TAG, true)
                        .integer(NewSeqNo.TAG, 3)
                    .build());
            server.expect(RESEND_REQUEST);
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
            server.expect(LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(SEQUENCE_RESET)
                        .msgSeqNum(2)
                        .bool(GapFillFlag.TAG, true)
                        .integer(NewSeqNo.TAG, 5)
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
            server.expect(LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(SEQUENCE_RESET)
                        .msgSeqNum(1)
                        .setPossDupFlag(true)
                        .bool(GapFillFlag.TAG, true)
                        .integer(NewSeqNo.TAG, 5)
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
            server.expect(LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(SEQUENCE_RESET)
                        .msgSeqNum(1)
                        .bool(GapFillFlag.TAG, true)
                        .integer(NewSeqNo.TAG, 5)
                    .build());
            server.expect(LOGOUT);
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
            server.expect(LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(SEQUENCE_RESET)
                        .msgSeqNum(2)
                        .bool(GapFillFlag.TAG, true)
                        .integer(NewSeqNo.TAG, 2)
                    .build());
            server.expect(REJECT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 2);
        }
    }

    private void logonHeartbeat() throws Exception {
        server.expect(LOGON);
        server.respondLogon();
        server.respond(
                new MessageBuilder(HEARTBEAT)
                    .msgSeqNum(2)
                .build());
        runInClient(new Runnable() {
            @Override public void run() {
                session.logon(connection);
            }
        });
    }

    private void runInClient(Runnable command) throws Exception {
        runInClient(command, false);
    }

    private void runInClient(Runnable command, boolean keepAlive) throws Exception {
        Thread serverThread = new Thread(server);
        serverThread.start();
        server.awaitForStart();
        Events events = Events.open(1000);
        session = newSession();
        connection = openConnection(session, keepAlive);
        events.register(connection);
        command.run();
        events.dispatch();
        server.stop();
        specify(server.passed());
    }

    private Session newSession() {
        return new Session(new HeartBtIntValue(HEARTBEAT_INTERVAL), getConfig(), new SessionStore() {
            @Override public void load(Session session) {
            }

            @Override public void resetOutgoingSeq(String senderCompId, String targetCompId, Sequence incomingSeq, Sequence outgoingSeq) {
            }

            @Override public void save(Session session) {
            }
        });
    }

    private Config getConfig() {
        Config config = new Config();
        config.setSenderCompId(INITIATOR);
        config.setTargetCompId(ACCEPTOR);
        config.setVersion(VERSION);
        return config;
    }

    private Connection openConnection(final Session session, final boolean keepAlive) throws IOException {
        return Connection.connect(new InetSocketAddress("localhost", PORT), new FixMessageParser(), new Connection.Callback() {
            public void messages(Connection conn, Iterator<silvertip.Message> messages) {
                while (messages.hasNext())
                    session.receive(conn, messages.next(), new DefaultMessageVisitor());
            }

            public void idle(Connection conn) {
                if (keepAlive)
                    session.keepAlive(conn);
                else
                    conn.close();
            }
        });
    }

    static RawMessageBuilder message() {
        return new RawMessageBuilder();
    }

    static RawMessageBuilder message(String bodyLength, String msgType) {
        return message()
                .field(BeginString.TAG, "FIX.4.2")
                .field(BodyLength.TAG, bodyLength)
                .field(MsgType.TAG, msgType)
                .field(SenderCompID.TAG, "Sender")
                .field(TargetCompID.TAG, "Target");
    }

    class MessageBuilder {
        private final Message message;

        public MessageBuilder(MsgTypeValue type) {
            MessageHeader header = new MessageHeader(type);
            header.setBeginString(VERSION.value());
            header.setString(SenderCompID.TAG, ACCEPTOR);
            header.setString(TargetCompID.TAG, INITIATOR);
            header.setDateTime(SendingTime.TAG, new DefaultTimeSource().currentTime());
            this.message = type.newMessage(header);
        }

        public MessageBuilder setBeginString(String beginString) {
            message.setBeginString(beginString);
            return this;
        }

        public MessageBuilder setSendingTime(DateTime sendingTime) {
            message.setSendingTime(sendingTime);
            return this;
        }

        public MessageBuilder setSenderCompID(String senderCompID) {
            message.setSenderCompId(senderCompID);
            return this;
        }

        public MessageBuilder setTargetCompID(String targetCompID) {
            message.setTargetCompId(targetCompID);
            return this;
        }

        public MessageBuilder msgSeqNum(int msgSeqNum) {
            message.setMsgSeqNum(msgSeqNum);
            return this;
        }

        public MessageBuilder setPossDupFlag(boolean possDupFlag) {
            message.setPossDupFlag(possDupFlag);
            return this;
        }

        public MessageBuilder setOrigSendingTime(DateTime origSendingTime) {
            message.setOrigSendingTime(origSendingTime);
            return this;
        }

        public MessageBuilder string(Tag<StringField> tag, String value) {
            message.setString(tag, value);
            return this;
        }

        public MessageBuilder integer(Tag<IntegerField> tag, Integer value) {
            message.setInteger(tag, value);
            return this;
        }

        public MessageBuilder bool(Tag<BooleanField> tag, boolean value) {
            message.setBoolean(tag, value);
            return this;
        }

        <T extends Formattable> MessageBuilder enumeration(Tag<? extends EnumField<T>> tag, T value) {
            message.setEnum(tag, value);
            return this;
        }

        public MessageBuilder integer(int value) {
            return this;
        }

        public Message build() {
            return message;
        }
    }

    class SimpleAcceptor implements Runnable {
        private final CountDownLatch serverStopped = new CountDownLatch(1);
        private final CountDownLatch serverStarted = new CountDownLatch(1);
        private final List<Runnable> commands = new ArrayList<Runnable>();
        private Socket clientSocket;
        private int successCount;
        private int failureCount;
        private final int port;

        private SimpleAcceptor(int port) {
            this.port = port;
        }

        public boolean passed() {
            return failureCount == 0 && successCount == commands.size();
        }

        public void respondLogon() {
            server.respond(
                    new MessageBuilder(LOGON)
                        .msgSeqNum(1)
                        .integer(HeartBtInt.TAG, HEARTBEAT_INTERVAL)
                        .enumeration(EncryptMethod.TAG, EncryptMethodValue.NONE)
                    .build());
        }

        public void respondLogout(int msgSeqNum) {
            server.respond(
                    new MessageBuilder(LOGOUT)
                        .msgSeqNum(msgSeqNum)
                    .build());
        }

        public void respond(final Message message) {
            respond(message.format());
        }

        public void respond(final Message message, final long delaySendingByMilliseconds) {
            respond(message.format(), delaySendingByMilliseconds);
        }

        private void respond(final String raw, final long delaySendingByMilliseconds) {
            this.commands.add(new Runnable() {
                @Override public void run() {
                    try {
                        Thread.sleep(delaySendingByMilliseconds);
                        clientSocket.getOutputStream().write(raw.getBytes());
                        successCount++;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        private void respond(final String raw) {
            this.commands.add(new Runnable() {
                @Override public void run() {
                    try {
                        clientSocket.getOutputStream().write(raw.getBytes());
                        successCount++;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        public void expect(final MsgTypeValue type) {
            this.commands.add(new Runnable() {
                @Override public void run() {
                    StringBuilder raw = parse();
                    Parser.parse(new silvertip.Message(raw.toString().getBytes()), new Parser.Callback() {
                        @Override public void message(Message m) {
                            if (m.getMsgType().equals(type.value()))
                                successCount++;
                            else
                                failureCount++;
                        }

                        @Override public void unsupportedMsgType(String msgType, int msgSeqNum) {
                        }

                        @Override public void invalidMsgType(String msgType, int msgSeqNum) {
                        }

                        @Override public void invalidMessage(int msgSeqNum, SessionRejectReasonValue reason, String text) {
                        }

                        @Override public void garbledMessage(String text) {
                        }
                    });
                }

                private StringBuilder parse() {
                    StringBuilder raw = new StringBuilder();
                    InputStream reader;
                    try {
                        reader = clientSocket.getInputStream();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    for (;;) {
                        int c = -1;
                        try {
                            c = reader.read();
                        } catch (IOException e) {
                            /* Ignore */
                        }
                        raw.append((char) c);
                        if (raw.toString().contains("10=")) {
                            do {
                                try {
                                    c = reader.read();
                                } catch (IOException e) {
                                    /* Ignore */
                                }
                                raw.append((char) c);
                            } while (c != Field.DELIMITER);
                            break;
                        }
                    }
                    return raw;
                }
            });
        }

        public void stop() {
            try {
                serverStopped.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public void awaitForStart() throws InterruptedException {
            serverStarted.await();
        }

        @Override public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
                serverStarted.countDown();
                clientSocket = serverSocket.accept();
                for (Runnable c : commands) {
                    c.run();
                }
                clientSocket.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    } catch (IOException ex) {
                    }
                }
                serverStopped.countDown();
            }
        }
    }
}
