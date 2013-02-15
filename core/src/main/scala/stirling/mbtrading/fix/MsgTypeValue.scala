/*
 * Copyright 2013 the original author or authors.
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

object MsgTypeValue {
  val Logon                     = LOGON
  val ResendRequest             = RESEND_REQUEST
  val Reject                    = REJECT
  val Logout                    = LOGOUT
  val Heartbeat                 = HEARTBEAT
  val TestRequest               = TEST_REQUEST
  val TradingSessionStatus      = TRADING_SESSION_STATUS
  val NewOrderSingle            = NEW_ORDER_SINGLE
  val OrderCancelRequest        = ORDER_CANCEL_REQUEST
  val OrderCancelReplaceRequest = ORDER_CANCEL_REPLACE_REQUEST
  val NewOrderMultiLeg          = NEW_ORDER_MULTILEG
  //  OrderMassStatusRequest
  val CollateralInquiry         = COLLATERAL_INQUIRY
  val RequestForPositions       = REQUEST_FOR_POSITIONS
  val News                      = NEWS
  val BusinessMessageReject     = BUSINESS_MESSAGE_REJECT
  val OrderCancelReject         = ORDER_CANCEL_REJECT
  val ExecutionReport           = EXECUTION_REPORT
  val CollateralReport          = COLLATERAL_REPORT
  val CollateralInquiryAck      = COLLATERAL_INQUIRY_ACK
  val PositionReport            = POSITION_REPORT
  val RequestForPositionsAck    = REQUEST_FOR_POSITIONS_ACK
}
