/*
 * Copyright 2012 the original author or authors.
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
package stirling.lime.fix

import stirling.fix.messages.fix42.MsgTypeValue._

object MsgTypeValue {
  val Heartbeat                 = HEARTBEAT
  val Logon                     = LOGON
  val TestRequest               = TEST_REQUEST
  val ResendRequest             = RESEND_REQUEST
  val Reject                    = REJECT
  val SequenceReset             = SEQUENCE_RESET
  val Logout                    = LOGOUT
  val BusinessMessageReject     = BUSINESS_MESSAGE_REJECT
  val NewOrderSingle            = NEW_ORDER_SINGLE
  val OrderCancelRequest        = ORDER_CANCEL_REQUEST
  val OrderCancelReplaceRequest = ORDER_CANCEL_REPLACE_REQUEST
  val BulkCancelRequest         = "s"
  val OrderCancelReject         = ORDER_CANCEL_REJECT
  val ExecutionReport           = EXECUTION_REPORT
}
