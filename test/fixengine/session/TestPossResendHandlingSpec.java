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
import org.jmock.Expectations;

import fixengine.messages.ExecTypeValue;
import fixengine.messages.MessageVisitor;
import fixengine.messages.MsgTypeValue;
import fixengine.messages.OrdStatusValue;
import fixengine.messages.OrdTypeValue;
import fixengine.messages.SideValue;
import fixengine.messages.fix42.ExecTransTypeValue;
import fixengine.messages.fix42.ExecutionReportMessage;
import fixengine.messages.fix42.LogonMessage;
import fixengine.tags.AvgPx;
import fixengine.tags.CumQty;
import fixengine.tags.ExecID;
import fixengine.tags.ExecType;
import fixengine.tags.LeavesQty;
import fixengine.tags.OrdStatus;
import fixengine.tags.OrdType;
import fixengine.tags.OrderID;
import fixengine.tags.OrderQty;
import fixengine.tags.Side;
import fixengine.tags.Symbol;
import fixengine.tags.fix42.ExecTransType;

@RunWith(JDaveRunner.class) public class TestPossResendHandlingSpec extends InitiatorSpecification {
    private final MessageVisitor visitor = mock(MessageVisitor.class);

    public class InitializedSession {
        /* Ref ID 19: a. Receive message with PossResend = "Y" and application
         * level check of Message specific ID indicates that it has already
         * been seen on this session */
        public void receiveMessageWithPossResendYAndAlreadySeenInThisSession() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(new MessageBuilder(MsgTypeValue.EXECUTION_REPORT)
                .msgSeqNum(2)
                .string(OrderID.TAG, "1278658351213-17")
                .string(ExecID.TAG, "1278658351213-18")
                .enumeration(ExecTransType.TAG, ExecTransTypeValue.NEW)
                .enumeration(ExecType.TAG, ExecTypeValue.NEW)
                .enumeration(OrdStatus.TAG, OrdStatusValue.NEW)
                .string(Symbol.TAG, "PALM")
                .enumeration(Side.TAG, SideValue.BUY)
                .float0(OrderQty.TAG, 1500.0)
                .float0(LeavesQty.TAG, 1500.0)
                .enumeration(OrdType.TAG, OrdTypeValue.MARKET)
                .float0(CumQty.TAG, .0)
                .float0(AvgPx.TAG, .0)
                .build());
            server.respond(new MessageBuilder(MsgTypeValue.EXECUTION_REPORT)
                .msgSeqNum(3)
                .setPossResend(true)
                .string(OrderID.TAG, "1278658351211-17")
                .string(ExecID.TAG, "1278658351213-18")
                .enumeration(ExecTransType.TAG, ExecTransTypeValue.NEW)
                .enumeration(ExecType.TAG, ExecTypeValue.NEW)
                .enumeration(OrdStatus.TAG, OrdStatusValue.NEW)
                .string(Symbol.TAG, "PALM")
                .enumeration(Side.TAG, SideValue.BUY)
                .float0(OrderQty.TAG, 1500.0)
                .float0(LeavesQty.TAG, 1500.0)
                .enumeration(OrdType.TAG, OrdTypeValue.MARKET)
                .float0(CumQty.TAG, .0)
                .float0(AvgPx.TAG, .0)
                .build());
            checking(new Expectations() {{
                one(visitor).visit(with(any(ExecutionReportMessage.class)));
                one(visitor).visit(with(any(LogonMessage.class)));
            }});
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            }, visitor, false, 1000);
            specify(session.getIncomingSeq().peek(), 4);
        }

        /* Ref ID 19: b. Receive message with PossResend = "Y" and application
         * level check of Message specific ID indicates that it has not yet
         * been seen on this session */
        public void receiveMessageWithPossResendYAndNotYetSeenInThisSession() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(new MessageBuilder(MsgTypeValue.EXECUTION_REPORT)
                .msgSeqNum(2)
                .setPossResend(true)
                .string(OrderID.TAG, "1278658351213-19")
                .string(ExecID.TAG, "1278658351213-20")
                .enumeration(ExecTransType.TAG, ExecTransTypeValue.NEW)
                .enumeration(ExecType.TAG, ExecTypeValue.NEW)
                .enumeration(OrdStatus.TAG, OrdStatusValue.NEW)
                .string(Symbol.TAG, "PALM")
                .enumeration(Side.TAG, SideValue.BUY)
                .float0(OrderQty.TAG, 1500.0)
                .float0(LeavesQty.TAG, 1500.0)
                .enumeration(OrdType.TAG, OrdTypeValue.MARKET)
                .float0(CumQty.TAG, .0)
                .float0(AvgPx.TAG, .0)
                .build());
            checking(new Expectations() {{
                one(visitor).visit(with(any(LogonMessage.class)));
                one(visitor).visit(with(any(ExecutionReportMessage.class)));
            }});
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            }, visitor, false, 1000);
            specify(session.getIncomingSeq().peek(), 3);
        }
    }
}
