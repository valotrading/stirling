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
package stirling.fix.messages

import stirling.fix.tags.fix42.TestReqID

trait Allocation extends Message

trait BulkCancelRequest extends Message

trait BusinessMessageReject extends Message

trait CollateralInquiry extends Message

trait CollateralInquiryAcknowledgment extends Message

trait CollateralReport extends Message

trait DontKnowTrade extends Message

trait ExecutionReport extends Message

class Heartbeat(header: MessageHeader) extends AbstractMessage(header) {
  field(TestReqID.Tag, Required.NO)

  override def apply(visitor: MessageVisitor) = visitor.visit(this)

  override def isAdminMessage = true
}

trait Logon extends Message {
  override def isAdminMessage = true
}

trait MassQuote extends Message

trait MassQuoteAcknowledgement extends Message

trait NewOrderMultiLeg extends Message

trait NewOrderSingle extends Message

trait News extends Message

trait NewsMessage extends Message

trait OrderCancelReject extends Message

trait OrderCancelReplaceRequest extends Message

trait OrderCancelRequest extends Message

trait OrderMassCancelReport extends Message

trait OrderMassCancelRequest extends Message

trait OrderMassStatusRequest extends Message

trait OrderStatusRequest extends Message

trait PositionReport extends Message

trait Reject extends Message {
  override def isAdminMessage = true
}

trait RequestForPositionAcknowledgment extends Message

trait RequestForPositions extends Message

trait SecurityList extends Message

trait SecurityListRequest extends Message

trait TradeCancelCorrect extends Message

trait TradeCaptureReport extends Message

trait TradeCaptureReportAck extends Message

trait TradeCaptureReportRequest extends Message

trait TradeCaptureReportRequestAck extends Message

trait TradingSessionStatus extends Message
