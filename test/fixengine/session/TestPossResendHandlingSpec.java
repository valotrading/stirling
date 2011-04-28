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

import fixengine.messages.MessageVisitor;
import fixengine.messages.MsgTypeValue;
import fixengine.messages.fix42.ExecutionReportMessage;
import fixengine.messages.fix42.LogonMessage;
import fixengine.tags.fix42.AvgPx;
import fixengine.tags.fix42.CumQty;
import fixengine.tags.fix42.ExecID;
import fixengine.tags.fix42.ExecTransType;
import fixengine.tags.fix42.ExecType;
import fixengine.tags.fix42.LeavesQty;
import fixengine.tags.fix42.OrdStatus;
import fixengine.tags.fix42.OrdType;
import fixengine.tags.fix42.OrderID;
import fixengine.tags.fix42.OrderQty;
import fixengine.tags.fix42.Side;
import fixengine.tags.fix42.Symbol;

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
                .string(OrderID.Tag(), "1278658351213-17")
                .string(ExecID.Tag(), "1278658351213-18")
                .enumeration(ExecTransType.Tag(), ExecTransType.New())
                .enumeration(ExecType.Tag(), ExecType.New())
                .enumeration(OrdStatus.Tag(), OrdStatus.New())
                .string(Symbol.Tag(), "PALM")
                .enumeration(Side.Tag(), Side.Buy())
                .float0(OrderQty.Tag(), 1500.0)
                .float0(LeavesQty.Tag(), 1500.0)
                .enumeration(OrdType.Tag(), OrdType.Market())
                .float0(CumQty.Tag(), .0)
                .float0(AvgPx.Tag(), .0)
                .build());
            server.respond(new MessageBuilder(MsgTypeValue.EXECUTION_REPORT)
                .msgSeqNum(3)
                .setPossResend(true)
                .string(OrderID.Tag(), "1278658351211-17")
                .string(ExecID.Tag(), "1278658351213-18")
                .enumeration(ExecTransType.Tag(), ExecTransType.New())
                .enumeration(ExecType.Tag(), ExecType.New())
                .enumeration(OrdStatus.Tag(), OrdStatus.New())
                .string(Symbol.Tag(), "PALM")
                .enumeration(Side.Tag(), Side.Buy())
                .float0(OrderQty.Tag(), 1500.0)
                .float0(LeavesQty.Tag(), 1500.0)
                .enumeration(OrdType.Tag(), OrdType.Market())
                .float0(CumQty.Tag(), .0)
                .float0(AvgPx.Tag(), .0)
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
                .string(OrderID.Tag(), "1278658351213-19")
                .string(ExecID.Tag(), "1278658351213-20")
                .enumeration(ExecTransType.Tag(), ExecTransType.New())
                .enumeration(ExecType.Tag(), ExecType.New())
                .enumeration(OrdStatus.Tag(), OrdStatus.New())
                .string(Symbol.Tag(), "PALM")
                .enumeration(Side.Tag(), Side.Buy())
                .float0(OrderQty.Tag(), 1500.0)
                .float0(LeavesQty.Tag(), 1500.0)
                .enumeration(OrdType.Tag(), OrdType.Market())
                .float0(CumQty.Tag(), .0)
                .float0(AvgPx.Tag(), .0)
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
