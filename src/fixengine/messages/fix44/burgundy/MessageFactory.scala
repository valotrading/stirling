/*
 * Copyright 2011 the original author or authors.
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
package fixengine.messages.fix44.burgundy

import fixengine.messages.MsgTypeValue._
import fixengine.messages.fix42.OrderCancelRequestMessage
import fixengine.messages.fix44.NewOrderSingleMessage
import fixengine.messages.fix44.burgundy.MsgTypeValue._

class MessageFactory extends fixengine.messages.fix42.DefaultMessageFactory {
  message(BUSINESS_MESSAGE_REJECT, classOf[BusinessMessageReject])
  message(DONT_KNOW_TRADE, classOf[DontKnowTrade])
  message(EXECUTION_REPORT, classOf[ExecutionReport])
  message(MASS_QUOTE, classOf[MassQuote])
  message(MASS_QUOTE_ACKNOWLEDGEMENT, classOf[MassQuoteAcknowledgement])
  message(NEW_ORDER_SINGLE, classOf[NewOrderSingleMessage])
  message(NEWS, classOf[News])
  message(ORDER_CANCEL_REJECT, classOf[OrderCancelReject])
  message(ORDER_CANCEL_REPLACE_REQUEST, classOf[OrderCancelReplaceRequest])
  message(ORDER_CANCEL_REQUEST, classOf[OrderCancelRequestMessage])
  message(ORDER_MASS_CANCEL_REPORT, classOf[OrderMassCancelReport])
  message(ORDER_MASS_CANCEL_REQUEST, classOf[OrderMassCancelRequest])
  message(ORDER_MASS_STATUS_REQUEST, classOf[OrderMassStatusRequest])
  message(SECURITY_LIST, classOf[SecurityList])
  message(SECURITY_LIST_REQUEST, classOf[SecurityListRequest])
  message(TRADE_CAPTURE_REPORT, classOf[TradeCaptureReport])
  message(TRADE_CAPTURE_REPORT_ACK, classOf[TradeCaptureReportAck])
  message(TRADE_CAPTURE_REPORT_REQUEST, classOf[TradeCaptureReportRequest])
  message(TRADE_CAPTURE_REPORT_REQUEST_ACK, classOf[TradeCaptureReportRequestAck])
  override def getProfile = "burgundy"
}
