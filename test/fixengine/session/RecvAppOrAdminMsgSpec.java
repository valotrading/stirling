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

import java.util.List;
import jdave.junit4.JDaveRunner;
import org.junit.runner.RunWith;

import fixengine.messages.Field;
import fixengine.messages.Message;
import fixengine.messages.MsgTypeValue;
import fixengine.tags.fix42.AllocAccount;
import fixengine.tags.fix42.AllocID;
import fixengine.tags.fix42.AvgPx;
import fixengine.tags.fix42.BeginSeqNo;
import fixengine.tags.fix42.CheckSum;
import fixengine.tags.fix42.ClOrdID;
import fixengine.tags.fix42.CumQty;
import fixengine.tags.fix42.EncryptMethod;
import fixengine.tags.fix42.ExecID;
import fixengine.tags.fix42.ExecType;
import fixengine.tags.fix42.HeartBtInt;
import fixengine.tags.fix42.LeavesQty;
import fixengine.tags.fix42.MsgSeqNum;
import fixengine.tags.fix42.NoAllocs;
import fixengine.tags.fix42.NoOrders;
import fixengine.tags.fix42.OrdStatus;
import fixengine.tags.fix42.OrdType;
import fixengine.tags.fix42.OrderID;
import fixengine.tags.fix42.OrderQty;
import fixengine.tags.fix42.SendingTime;
import fixengine.tags.fix42.Shares;
import fixengine.tags.fix42.Side;
import fixengine.tags.fix42.Symbol;
import fixengine.tags.fix42.TestReqID;
import fixengine.tags.fix42.TradeDate;
import fixengine.tags.fix42.AllocShares;
import fixengine.tags.fix42.AllocTransType;
import fixengine.tags.fix42.ExecTransType;

@RunWith(JDaveRunner.class) public class RecvAppOrAdminMsgSpec extends InitiatorSpecification {
    public class InitializedSession {
        /* Ref ID 14: a. Receive field identifier (tag number) not defined in
         * specification. Exception: undefined tag used in testing profile as
         * user-defined. */
        public void tagNotDefinedInSpecification() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(message("72", "0")
                .field(MsgSeqNum.Tag(), "2")
                .field(SendingTime.Tag(), "20100701-12:09:40")
                .field(TestReqID.Tag(), "1")
                .field(9898, "value")
                .field(CheckSum.Tag(), "045")
                .toString());
            server.expect(MsgTypeValue.REJECT);
            checking(expectLogSevere("Invalid tag number: 9898"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 3);
        }

        /* Ref ID 14: b. Receive message with a required field identifier (tag
         * number) missing. */
        public void requiredFieldMissing() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(MsgTypeValue.RESEND_REQUEST)
                        .msgSeqNum(2)
                        .integer(BeginSeqNo.Tag(), 1)
                    .build());
            server.expect(MsgTypeValue.REJECT);
            checking(expectLogSevere("EndSeqNo(16): Tag missing"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 3);
        }

        /* Ref ID 14: c. Receive message with field identifier (tag number)
         * which is defined in the specification but not defined for this
         * message type. Exception: undefined tag used is specified in testing
         * profile as user-defined for this message type. */
        public void tagDefinedInSpecificationButNotForThisMsgType() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(message("66", "0")
                .field(MsgSeqNum.Tag(), "2")
                .field(SendingTime.Tag(), "20100701-12:09:40")
                .field(88, "0")
                .field(TestReqID.Tag(), "1")
                .field(CheckSum.Tag(), "209")
                .toString());
            server.expect(MsgTypeValue.REJECT);
            checking(expectLogSevere("Tag not defined for this message: 88"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 3);
        }

        /* Ref ID 14: d. Receive message with field identifier (tag number)
         * specified but no value (e.g. "55=<SOH>" vs. "55=IBM<SOH>"). */
        public void fieldWithoutValue() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(MsgTypeValue.TEST_REQUEST)
                        .msgSeqNum(2)
                        .string(TestReqID.Tag(), "")
                    .build());
            server.expect(MsgTypeValue.REJECT);
            checking(expectLogSevere("TestReqID(112): Empty tag"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 3);
        }

        /* Ref ID 14: e. Receive message with incorrect value (out of range or
         * not part of valid list of enumated values) for a particular field
         * identifier (tag number). Exception: undefined enumerated values used
         * are specified in testing profile as user-defined. */
        public void fieldWithIncorrectValue() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(message("67", "A")
                .field(MsgSeqNum.Tag(), "2")
                .field(SendingTime.Tag(), "20100701-12:09:40")
                .field(EncryptMethod.Tag(), "7")
                .field(HeartBtInt.Tag(), "30")
                .field(CheckSum.Tag(), "034")
                .toString());
            server.expect(MsgTypeValue.REJECT);
            checking(expectLogSevere("EncryptMethod(98): Invalid value"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 3);
        }

        /* Ref ID 14: f. Receive message with a value in an incorrect data
         * format (syntax) for a particular field identifier (tag number). */
        public void fieldWithIncorrectDataFormat() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(message("56", "0")
                .field(MsgSeqNum.Tag(), "2")
                .field(SendingTime.Tag(), "WRONG FORMAT")
                .field(TestReqID.Tag(), "1")
                .field(CheckSum.Tag(), "012")
                .toString());
            server.expect(MsgTypeValue.REJECT);
            checking(expectLogSevere("SendingTime(52): Invalid value format"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 3);
        }

        /* Ref ID 14: g. Receive a message in which the following is not true:
         * Standard Header fields appear before Body fields which appear before
         * Standard Trailer fields. */
        public void standardHeaderFieldInBody() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(message("64", "0")
                .field(MsgSeqNum.Tag(), "2")
                .field(TestReqID.Tag(), "1000")
                .field(SendingTime.Tag(), "20100701-12:09:40")
                .field(CheckSum.Tag(), "129")
                .toString());
            server.expect(MsgTypeValue.REJECT);
            checking(expectLogSevere("SendingTime(52): Out of order tag"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 3);
        }

        /* Ref ID 14: g. Receive a message in which the following is not true:
         * Standard Header fields appear before Body fields which appear before
         * Standard Trailer fields. */
        public void standardTrailerFieldBeforeBody() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(message("68", "0")
                .field(MsgSeqNum.Tag(), "2")
                .field(SendingTime.Tag(), "20100701-12:09:40")
                .field(CheckSum.Tag(), "207")
                .field(TestReqID.Tag(), "1")
                .field(CheckSum.Tag(), "045")
                .toString());
            server.expect(MsgTypeValue.REJECT);
            checking(expectLogSevere("CheckSum(10): Out of order tag"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 3);
        }

        /* Ref ID 14: h. Receive a message in which a field identifier (tag
         * number) which is not part of a repeating group is specified more
         * than once. */
        public void duplicateFields() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(message("67", "0")
                .field(MsgSeqNum.Tag(), "2")
                .field(SendingTime.Tag(), "20100701-12:09:40")
                .field(TestReqID.Tag(), "1")
                .field(TestReqID.Tag(), "1")
                .field(CheckSum.Tag(), "247")
                .toString());
            server.expect(MsgTypeValue.REJECT);
            checking(expectLogSevere("TestReqID(112): Tag multiple times"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 3);
        }

        /* Ref ID 14: i. Receive a message with repeating groups in which the
         * "count" field value for a repeating group is incorrect. */
        public void tooManyInstancesInRepeatingGroup() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(message("187", "J")
                .field(MsgSeqNum.Tag(), "2")
                .field(SendingTime.Tag(), "20100701-12:09:40")
                .field(AllocID.Tag(), "12807331319411")
                .field(AllocTransType.Tag(), "0")
                .field(NoOrders.Tag(), "1")
                .field(ClOrdID.Tag(), "12807331319412")
                .field(Side.Tag(), "2")
                .field(Symbol.Tag(), "GOOG")
                .field(Shares.Tag(), "1000.00")
                .field(AvgPx.Tag(), "370.00")
                .field(TradeDate.Tag(), "20011004")
                .field(NoAllocs.Tag(), "1")
                .field(AllocAccount.Tag(), "1234")
                .field(AllocShares.Tag(), "900.00")
                .field(AllocAccount.Tag(), "2345")
                .field(AllocShares.Tag(), "100.00")
                .field(CheckSum.Tag(), "159")
                .toString());
            server.expect(MsgTypeValue.REJECT);
            checking(expectLogSevere("NoAllocs(78): Incorrect NumInGroup count for repeating group. Expected: 1, but was: 2"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 3);
        }

        /* Ref ID 14: i. Receive a message with repeating groups in which the
         * "count" field value for a repeating group is incorrect. */
        public void tooFewInstancesInRepeatingGroup() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(message("187", "J")
                .field(MsgSeqNum.Tag(), "2")
                .field(SendingTime.Tag(), "20100701-12:09:40")
                .field(AllocID.Tag(), "12807331319411")
                .field(AllocTransType.Tag(), "0")
                .field(NoOrders.Tag(), "1")
                .field(ClOrdID.Tag(), "12807331319412")
                .field(Side.Tag(), "2")
                .field(Symbol.Tag(), "GOOG")
                .field(Shares.Tag(), "1000.00")
                .field(AvgPx.Tag(), "370.00")
                .field(TradeDate.Tag(), "20011004")
                .field(NoAllocs.Tag(), "3")
                .field(AllocAccount.Tag(), "1234")
                .field(AllocShares.Tag(), "900.00")
                .field(AllocAccount.Tag(), "2345")
                .field(AllocShares.Tag(), "100.00")
                .field(CheckSum.Tag(), "161")
                .toString());
            server.expect(MsgTypeValue.REJECT);
            checking(expectLogSevere("NoAllocs(78): Incorrect NumInGroup count for repeating group. Expected: 3, but was: 2"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 3);
        }

        /* Ref ID 14: j. Receive a message with repeating groups in which the
         * order of repeating group fields does not match specification. */
        public void repeatingGroupFieldsOutOfOrder() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(message("187", "J")
                .field(MsgSeqNum.Tag(), "2")
                .field(SendingTime.Tag(), "20100701-12:09:40")
                .field(AllocID.Tag(), "12807331319411")
                .field(AllocTransType.Tag(), "0")
                .field(NoOrders.Tag(), "1")
                .field(ClOrdID.Tag(), "12807331319412")
                .field(Side.Tag(), "2")
                .field(Symbol.Tag(), "GOOG")
                .field(Shares.Tag(), "1000.00")
                .field(AvgPx.Tag(), "370.00")
                .field(TradeDate.Tag(), "20011004")
                .field(NoAllocs.Tag(), "2")
                .field(AllocShares.Tag(), "900.00")
                .field(AllocAccount.Tag(), "1234")
                .field(AllocShares.Tag(), "100.00")
                .field(AllocAccount.Tag(), "2345")
                .field(CheckSum.Tag(), "160")
                .toString());
            server.expect(MsgTypeValue.REJECT);
            checking(expectLogSevere("AllocShares(80): Repeating group fields out of order"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 3);
        }

        /* Ref ID 14: k. Receive a message with a field of a data type other
         * than "data" which contains one or more embedded <SOH> values. */
        public void fieldValueWithEmbeddedSOHs() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(message("65", "0")
                .field(MsgSeqNum.Tag(), "2")
                .field(SendingTime.Tag(), "20100701-12:09:40")
                .field(TestReqID.Tag(), "1" + Field.DELIMITER + "000")
                .field(CheckSum.Tag(), "131")
                .toString());
            server.expect(MsgTypeValue.REJECT);
            checking(expectLogSevere("Non-data value includes field delimiter (SOH character)"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 3);
        }

        /* Ref ID 14: l. Receive a message when application-level processing or
         * system is not available (Optional). */
        public void systemNotAvailable() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.expect(MsgTypeValue.BUSINESS_MESSAGE_REJECT);
            checking(expectLogWarning("Application not available"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                    session.setAvailable(false);
                }
            });
            specify(session.getIncomingSeq().peek(), 2);
        }

        /* Ref ID 14: m. Receive a message in which a conditionally required
         * field is missing. */
        public void conditionallyRequiredFieldMissing() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(new MessageBuilder(MsgTypeValue.EXECUTION_REPORT)
                .msgSeqNum(2)
                .string(OrderID.Tag(), "1278658351213-17")
                .string(ExecID.Tag(), "1278658351213-18")
                .enumeration(ExecTransType.Tag(), ExecTransType.New())
                .enumeration(ExecType.Tag(), ExecType.New())
                .enumeration(OrdStatus.Tag(), OrdStatus.New())
                .string(Symbol.Tag(), "PALM")
                .enumeration(Side.Tag(), Side.Buy())
                .float0(OrderQty.Tag(), 1500.0)
                .float0(LeavesQty.Tag(), 1500.0)
                .enumeration(OrdType.Tag(), OrdType.Limit())
                .float0(CumQty.Tag(), .0)
                .float0(AvgPx.Tag(), .0)
                .build());
            server.expect(MsgTypeValue.BUSINESS_MESSAGE_REJECT);
            checking(expectLogSevere("Price(44): Conditionally required field missing"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 3);
        }

        /* Ref ID 14: n. Receive a message in which a field identifier (tag
         * number) appears in both cleartext and encrypted section but has
         * different values. */
        private void cleartextAndEncryptedSectionDiffer() throws Exception {
            // TODO: Currently, FIX engine does not support encrypted sections.
        }

        public void persistAdminMessages() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(MsgTypeValue.HEARTBEAT)
                        .msgSeqNum(2)
                    .build(), getHearbeatIntervalInMillis() / 2);
            server.respondLogout(3);
            server.expect(MsgTypeValue.LOGOUT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            List<Message> messages = session.getStore().getIncomingMessages(session, 1, 0);
            specify(messages.size(), 3);
            specify(messages.get(0).getMsgType(), must.equal(MsgTypeValue.LOGON));
            specify(messages.get(1).getMsgType(), must.equal(MsgTypeValue.HEARTBEAT));
            specify(messages.get(2).getMsgType(), must.equal(MsgTypeValue.LOGOUT));
        }
    }
}
