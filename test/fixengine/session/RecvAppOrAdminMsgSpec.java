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

import fixengine.messages.ExecTypeValue;
import fixengine.messages.Field;
import fixengine.messages.MsgTypeValue;
import fixengine.messages.OrdStatusValue;
import fixengine.messages.OrdTypeValue;
import fixengine.messages.SideValue;
import fixengine.tags.AllocAccount;
import fixengine.tags.AllocID;
import fixengine.tags.AllocShares;
import fixengine.tags.AllocTransType;
import fixengine.tags.AvgPx;
import fixengine.tags.BeginSeqNo;
import fixengine.tags.CheckSum;
import fixengine.tags.ClOrdID;
import fixengine.tags.CumQty;
import fixengine.tags.EncryptMethod;
import fixengine.tags.ExecID;
import fixengine.tags.ExecTransType;
import fixengine.tags.ExecType;
import fixengine.tags.HeartBtInt;
import fixengine.tags.LeavesQty;
import fixengine.tags.MsgSeqNum;
import fixengine.tags.NoAllocs;
import fixengine.tags.NoOrders;
import fixengine.tags.OrdStatus;
import fixengine.tags.OrdType;
import fixengine.tags.OrderID;
import fixengine.tags.OrderQty;
import fixengine.tags.SendingTime;
import fixengine.tags.Shares;
import fixengine.tags.Side;
import fixengine.tags.Symbol;
import fixengine.tags.TestReqID;
import fixengine.tags.TradeDate;

@RunWith(JDaveRunner.class) public class RecvAppOrAdminMsgSpec extends InitiatorSpecification {
    public class InitializedSession {
        /* Ref ID 14: a. Receive field identifier (tag number) not defined in
         * specification. Exception: undefined tag used in testing profile as
         * user-defined. */
        public void tagNotDefinedInSpecification() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(message("72", "0")
                .field(MsgSeqNum.TAG, "2")
                .field(SendingTime.TAG, "20100701-12:09:40")
                .field(TestReqID.TAG, "1")
                .field(9898, "value")
                .field(CheckSum.TAG, "045")
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
                        .integer(BeginSeqNo.TAG, 1)
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
                .field(MsgSeqNum.TAG, "2")
                .field(SendingTime.TAG, "20100701-12:09:40")
                .field(88, "0")
                .field(TestReqID.TAG, "1")
                .field(CheckSum.TAG, "209")
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
                        .string(TestReqID.TAG, "")
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
                .field(MsgSeqNum.TAG, "2")
                .field(SendingTime.TAG, "20100701-12:09:40")
                .field(EncryptMethod.TAG, "7")
                .field(HeartBtInt.TAG, "30")
                .field(CheckSum.TAG, "034")
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
                .field(MsgSeqNum.TAG, "2")
                .field(SendingTime.TAG, "WRONG FORMAT")
                .field(TestReqID.TAG, "1")
                .field(CheckSum.TAG, "012")
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
                .field(MsgSeqNum.TAG, "2")
                .field(TestReqID.TAG, "1000")
                .field(SendingTime.TAG, "20100701-12:09:40")
                .field(CheckSum.TAG, "129")
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
                .field(MsgSeqNum.TAG, "2")
                .field(SendingTime.TAG, "20100701-12:09:40")
                .field(CheckSum.TAG, "207")
                .field(TestReqID.TAG, "1")
                .field(CheckSum.TAG, "045")
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
                .field(MsgSeqNum.TAG, "2")
                .field(SendingTime.TAG, "20100701-12:09:40")
                .field(TestReqID.TAG, "1")
                .field(TestReqID.TAG, "1")
                .field(CheckSum.TAG, "247")
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
                .field(MsgSeqNum.TAG, "2")
                .field(SendingTime.TAG, "20100701-12:09:40")
                .field(AllocID.TAG, "12807331319411")
                .field(AllocTransType.TAG, "0")
                .field(NoOrders.TAG, "1")
                .field(ClOrdID.TAG, "12807331319412")
                .field(Side.TAG, "2")
                .field(Symbol.TAG, "GOOG")
                .field(Shares.TAG, "1000.00")
                .field(AvgPx.TAG, "370.00")
                .field(TradeDate.TAG, "20011004")
                .field(NoAllocs.TAG, "1")
                .field(AllocAccount.TAG, "1234")
                .field(AllocShares.TAG, "900.00")
                .field(AllocAccount.TAG, "2345")
                .field(AllocShares.TAG, "100.00")
                .field(CheckSum.TAG, "159")
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
                .field(MsgSeqNum.TAG, "2")
                .field(SendingTime.TAG, "20100701-12:09:40")
                .field(AllocID.TAG, "12807331319411")
                .field(AllocTransType.TAG, "0")
                .field(NoOrders.TAG, "1")
                .field(ClOrdID.TAG, "12807331319412")
                .field(Side.TAG, "2")
                .field(Symbol.TAG, "GOOG")
                .field(Shares.TAG, "1000.00")
                .field(AvgPx.TAG, "370.00")
                .field(TradeDate.TAG, "20011004")
                .field(NoAllocs.TAG, "3")
                .field(AllocAccount.TAG, "1234")
                .field(AllocShares.TAG, "900.00")
                .field(AllocAccount.TAG, "2345")
                .field(AllocShares.TAG, "100.00")
                .field(CheckSum.TAG, "161")
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
                .field(MsgSeqNum.TAG, "2")
                .field(SendingTime.TAG, "20100701-12:09:40")
                .field(AllocID.TAG, "12807331319411")
                .field(AllocTransType.TAG, "0")
                .field(NoOrders.TAG, "1")
                .field(ClOrdID.TAG, "12807331319412")
                .field(Side.TAG, "2")
                .field(Symbol.TAG, "GOOG")
                .field(Shares.TAG, "1000.00")
                .field(AvgPx.TAG, "370.00")
                .field(TradeDate.TAG, "20011004")
                .field(NoAllocs.TAG, "2")
                .field(AllocShares.TAG, "900.00")
                .field(AllocAccount.TAG, "1234")
                .field(AllocShares.TAG, "100.00")
                .field(AllocAccount.TAG, "2345")
                .field(CheckSum.TAG, "160")
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
                .field(MsgSeqNum.TAG, "2")
                .field(SendingTime.TAG, "20100701-12:09:40")
                .field(TestReqID.TAG, "1" + Field.DELIMITER + "000")
                .field(CheckSum.TAG, "131")
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
                .string(OrderID.TAG, "1278658351213-17")
                .string(ExecID.TAG, "1278658351213-18")
                .string(ExecTransType.TAG, "0")
                .enumeration(ExecType.TAG, ExecTypeValue.NEW)
                .enumeration(OrdStatus.TAG, OrdStatusValue.NEW)
                .string(Symbol.TAG, "PALM")
                .enumeration(Side.TAG, SideValue.BUY)
                .float0(OrderQty.TAG, 1500.0)
                .float0(LeavesQty.TAG, 1500.0)
                .enumeration(OrdType.TAG, OrdTypeValue.LIMIT)
                .float0(CumQty.TAG, .0)
                .float0(AvgPx.TAG, .0)
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
    }
}
