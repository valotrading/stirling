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
public interface MessageVisitor {
    void visit(ExecutionReportMessage message);
    void visit(HeartbeatMessage message);
    void visit(LogonMessage message);
    void visit(LogoutMessage message);
    void visit(NewOrderSingleMessage message);
    void visit(OrderCancelRequestMessage message);
    void visit(OrderMassCancelRequestMessage message);
    void visit(OrderMassCancelReportMessage message);
    void visit(OrderMassStatusRequestMessage message);
    void visit(OrderCancelRejectMessage message);
    void visit(OrderCancelReplaceRequestMessage message);
    void visit(OrderModificationRequestMessage message);
    void visit(RejectMessage message);
    void visit(ResendRequestMessage message);
    void visit(SequenceResetMessage message);
    void visit(TestRequestMessage message);
    void visit(AllocationMessage message);
    void visit(DontKnowTradeMessage message);
    void visit(UserDefinedMessage message);
    void visit(OrderStatusRequestMessage message);
    void visit(NewOrderMultiLegMessage message);
    void visit(TradingSessionStatusRequestMessage message);
    void visit(CollateralInquiryMessage message);
    void visit(RequestForPositionsMessage message);
    void visit(TradingSessionStatusMessage message);
    void visit(TradeCaptureReport message);
    void visit(TradeCaptureReportAck message);
    void visit(TradeCaptureReportRequest message);
    void visit(TradeCaptureReportRequestAck message);
    void visit(MassQuote message);
    void visit(MassQuoteAcknowledgement message);
    void visit(NewsMessage message);
    void visit(SecurityListRequest message);
    void visit(SecurityList message);
    void visit(BusinessMessageReject message);
    void visit(DontKnowTrade message);
    void visit(News message);
    void visit(CollateralReportMessage message);
    void visit(CollateralInquiryAcknowledgmentMessage message);
}
