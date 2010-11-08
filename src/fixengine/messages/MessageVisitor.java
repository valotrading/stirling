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
package fixengine.messages;

import fixengine.messages.fix42.AllocationMessage;
import fixengine.messages.fix42.DontKnowTradeMessage;
import fixengine.messages.fix42.ExecutionReportMessage;
import fixengine.messages.fix42.LogonMessage;
import fixengine.messages.fix42.NewOrderSingleMessage;
import fixengine.messages.fix42.OrderCancelRejectMessage;
import fixengine.messages.fix42.OrderCancelRequestMessage;
import fixengine.messages.fix42.OrderModificationRequestMessage;

/**
 * @author Pekka Enberg 
 */
public interface MessageVisitor {
    void visit(ExecutionReportMessage message);
    void visit(HeartbeatMessage message);
    void visit(LogonMessage message);
    void visit(LogoutMessage message);
    void visit(NewOrderSingleMessage message);
    void visit(OrderCancelRequestMessage message);
    void visit(OrderCancelRejectMessage message);
    void visit(OrderModificationRequestMessage message);
    void visit(BusinessMessageRejectMessage message);
    void visit(RejectMessage message);
    void visit(ResendRequestMessage message);
    void visit(SequenceResetMessage message);
    void visit(TestRequestMessage message);
    void visit(AllocationMessage message);
    void visit(DontKnowTradeMessage message);
    void visit(UserDefinedMessage message);
}
