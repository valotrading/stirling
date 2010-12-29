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
import fixengine.messages.fix42.OrderStatusRequestMessage;

/**
 * @author Pekka Enberg
 */
public class DefaultMessageVisitor implements MessageVisitor {
    @Override public void visit(ExecutionReportMessage message) {
        defaultAction(message);
    }

    @Override public void visit(HeartbeatMessage message) {
        defaultAction(message);
    }

    @Override public void visit(LogonMessage message) {
        defaultAction(message);
    }

    @Override public void visit(LogoutMessage message) {
        defaultAction(message);
    }

    @Override public void visit(NewOrderSingleMessage message) {
        defaultAction(message);
    }

    @Override public void visit(OrderCancelRequestMessage message) {
        defaultAction(message);
    }

    @Override public void visit(OrderMassCancelRequestMessage message) {
        defaultAction(message);
    }

    @Override public void visit(OrderMassCancelReportMessage message) {
        defaultAction(message);
    }

    @Override public void visit(OrderMassStatusRequestMessage message) {
        defaultAction(message);
    }

    @Override public void visit(OrderCancelRejectMessage message) {
        defaultAction(message);
    }

    @Override public void visit(OrderModificationRequestMessage message) {
        defaultAction(message);
    }

    @Override public void visit(OrderCancelReplaceRequestMessage message) {
        defaultAction(message);
    }

    @Override public void visit(BusinessMessageRejectMessage message) {
        defaultAction(message);
    }

    @Override public void visit(RejectMessage message) {
        defaultAction(message);
    }

    @Override public void visit(ResendRequestMessage message) {
        defaultAction(message);
    }

    @Override public void visit(SequenceResetMessage message) {
        defaultAction(message);
    }

    @Override public void visit(TestRequestMessage message) {
        defaultAction(message);
    }

    @Override public void visit(AllocationMessage message) {
        defaultAction(message);
    }

    @Override public void visit(DontKnowTradeMessage message) {
        defaultAction(message);
    }

    @Override public void visit(UserDefinedMessage message) {
        defaultAction(message);
    }

    @Override public void visit(OrderStatusRequestMessage message) {
        defaultAction(message);
    }

    @Override public void visit(NewOrderMultiLegMessage message) {
        defaultAction(message);
    }

    @Override public void visit(TradingSessionStatusRequestMessage message) {
        defaultAction(message);
    }

    @Override public void visit(CollateralInquiryMessage message) {
        defaultAction(message);
    }

    @Override public void visit(RequestForPositionsMessage message) {
        defaultAction(message);
    }

    @Override public void visit(TradingSessionStatusMessage message) {
        defaultAction(message);
    }

    @Override public void visit(TradeCaptureReport message) {
        defaultAction(message);
    }

    @Override public void visit(TradeCaptureReportAck message) {
        defaultAction(message);
    }

    @Override public void visit(TradeCaptureReportRequest message) {
        defaultAction(message);
    }

    @Override public void visit(TradeCaptureReportRequestAck message) {
        defaultAction(message);
    }

    @Override public void visit(MassQuote message) {
        defaultAction(message);
    }

    @Override public void visit(MassQuoteAcknowledgement message) {
        defaultAction(message);
    }

    @Override public void visit(SecurityListRequest message) {
        defaultAction(message);
    }

    @Override public void visit(NewsMessage message) {
        defaultAction(message);
    }

    public void defaultAction(Message message) {
    }

}
