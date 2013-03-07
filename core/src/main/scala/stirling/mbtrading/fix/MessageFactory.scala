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

import stirling.fix.messages.fix42.DefaultMessageFactory

object MessageFactory extends DefaultMessageFactory {
  message(MsgTypeValue.ExecutionReport,           classOf[ExecutionReport])
  message(MsgTypeValue.OrderCancelRequest,        classOf[OrderCancelRequest])
  message(MsgTypeValue.OrderCancelReject,         classOf[OrderCancelReject])
  message(MsgTypeValue.BusinessMessageReject,     classOf[BusinessMessageReject])
  message(MsgTypeValue.NewOrderSingle,            classOf[NewOrderSingle])
  message(MsgTypeValue.OrderCancelReplaceRequest, classOf[OrderCancelReplaceRequest])
  message(MsgTypeValue.Logon,                     classOf[Logon])
  message(MsgTypeValue.NewOrderMultiLeg,          classOf[NewOrderMultiLeg])
  message(MsgTypeValue.RequestForPositions,       classOf[RequestForPositions])
  message(MsgTypeValue.PositionReport,            classOf[PositionReport])
  message(MsgTypeValue.TradingSessionStatus,      classOf[TradingSessionStatus])
  message(MsgTypeValue.News,                      classOf[News])
  message(MsgTypeValue.CollateralReport,          classOf[CollateralReport])
  message(MsgTypeValue.CollateralInquiry,         classOf[CollateralInquiry])
  message(MsgTypeValue.CollateralInquiryAck,      classOf[CollateralInquiryAcknowledgment])
  message(MsgTypeValue.RequestForPositionsAck,    classOf[RequestForPositionAcknowledgment])

  override def getProfile = "mb-trading"

  override def isValid(msgType: String) = isSupported(msgType)
}
