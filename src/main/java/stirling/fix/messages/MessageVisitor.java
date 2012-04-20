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
package stirling.fix.messages;


/**
 * @author Pekka Enberg
 */
public interface MessageVisitor {
    void visit(ExecutionReport message);
    void visit(Heartbeat message);
    void visit(Logon message);
    void visit(Logout message);
    void visit(NewOrderSingle message);
    void visit(OrderCancelRequest message);
    void visit(OrderMassCancelRequest message);
    void visit(OrderMassCancelReport message);
    void visit(OrderMassStatusRequest message);
    void visit(OrderCancelReject message);
    void visit(OrderCancelReplaceRequest message);
    void visit(Reject message);
    void visit(ResendRequest message);
    void visit(SequenceReset message);
    void visit(TestRequest message);
    void visit(Allocation message);
    void visit(UserDefinedMessage message);
    void visit(OrderStatusRequest message);
    void visit(NewOrderMultiLeg message);
    void visit(TradingSessionStatusRequest message);
    void visit(CollateralInquiry message);
    void visit(RequestForPositions message);
    void visit(TradingSessionStatus message);
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
    void visit(CollateralReport message);
    void visit(CollateralInquiryAcknowledgment message);
    void visit(PositionReport message);
    void visit(RequestForPositionAcknowledgment message);
    void visit(BulkCancelRequest message);
}
