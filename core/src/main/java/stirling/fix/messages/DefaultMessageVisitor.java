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
public class DefaultMessageVisitor implements MessageVisitor {
    @Override public void visit(ExecutionReport message) {
        defaultAction(message);
    }

    @Override public void visit(Heartbeat message) {
        defaultAction(message);
    }

    @Override public void visit(Logon message) {
        defaultAction(message);
    }

    @Override public void visit(Logout message) {
        defaultAction(message);
    }

    @Override public void visit(NewOrderSingle message) {
        defaultAction(message);
    }

    @Override public void visit(OrderCancelRequest message) {
        defaultAction(message);
    }

    @Override public void visit(OrderMassCancelRequest message) {
        defaultAction(message);
    }

    @Override public void visit(OrderMassCancelReport message) {
        defaultAction(message);
    }

    @Override public void visit(OrderMassStatusRequest message) {
        defaultAction(message);
    }

    @Override public void visit(OrderCancelReject message) {
        defaultAction(message);
    }

    @Override public void visit(OrderCancelReplaceRequest message) {
        defaultAction(message);
    }

    @Override public void visit(Reject message) {
        defaultAction(message);
    }

    @Override public void visit(ResendRequest message) {
        defaultAction(message);
    }

    @Override public void visit(SequenceReset message) {
        defaultAction(message);
    }

    @Override public void visit(TestRequest message) {
        defaultAction(message);
    }

    @Override public void visit(Allocation message) {
        defaultAction(message);
    }

    @Override public void visit(OrderStatusRequest message) {
        defaultAction(message);
    }

    @Override public void visit(NewOrderMultiLeg message) {
        defaultAction(message);
    }

    @Override public void visit(TradingSessionStatusRequest message) {
        defaultAction(message);
    }

    @Override public void visit(CollateralInquiry message) {
        defaultAction(message);
    }

    @Override public void visit(RequestForPositions message) {
        defaultAction(message);
    }

    @Override public void visit(TradingSessionStatus message) {
        defaultAction(message);
    }

    @Override public void visit(TradeCancelCorrect message) {
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

    @Override public void visit(NewsMessage message) {
        defaultAction(message);
    }

    @Override public void visit(SecurityListRequest message) {
        defaultAction(message);
    }

    @Override public void visit(SecurityList message) {
        defaultAction(message);
    }

    @Override public void visit(BusinessMessageReject message) {
        defaultAction(message);
    }

    @Override public void visit(DontKnowTrade message) {
        defaultAction(message);
    }

    @Override public void visit(News message) {
        defaultAction(message);
    }

    @Override public void visit(CollateralReport message) {
        defaultAction(message);
    }

    @Override public void visit(CollateralInquiryAcknowledgment message) {
        defaultAction(message);
    }

    @Override public void visit(PositionReport message) {
        defaultAction(message);
    }

    @Override public void visit(RequestForPositionAcknowledgment message) {
        defaultAction(message);
    }

    @Override public void visit(BulkCancelRequest message) {
        defaultAction(message);
    }

    public void defaultAction(Message message) {
    }

}
