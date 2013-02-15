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
package stirling.mbtrading.fix

import stirling.fix.messages.fix44.MsgTypeValue._

class MessageFactory extends stirling.fix.messages.fix42.DefaultMessageFactory {
  message(EXECUTION_REPORT, classOf[ExecutionReport])
  message(ORDER_CANCEL_REQUEST, classOf[OrderCancelRequest])
  message(ORDER_CANCEL_REJECT, classOf[OrderCancelReject])
  message(BUSINESS_MESSAGE_REJECT, classOf[BusinessMessageReject])
  message(NEW_ORDER_SINGLE, classOf[NewOrderSingle])
  message(ORDER_CANCEL_REPLACE_REQUEST, classOf[OrderCancelReplaceRequest])
  message(LOGON, classOf[Logon])
  message(NEW_ORDER_MULTILEG, classOf[NewOrderMultiLeg])
  message(REQUEST_FOR_POSITIONS, classOf[RequestForPositions])
  message(POSITION_REPORT, classOf[PositionReport])
  message(TRADING_SESSION_STATUS, classOf[TradingSessionStatus])
  message(NEWS, classOf[News])
  message(COLLATERAL_REPORT, classOf[CollateralReport])
  message(COLLATERAL_INQUIRY, classOf[CollateralInquiry])
  message(COLLATERAL_INQUIRY_ACK, classOf[CollateralInquiryAcknowledgment])
  message(REQUEST_FOR_POSITIONS_ACK, classOf[RequestForPositionAcknowledgment])
  override def getProfile = "mb-trading"
  override def isValid(msgType: String) = isSupported(msgType)
}
