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
package stirling.fix.messages.fix42.lime

import stirling.fix.messages.fix42.DefaultMessageFactory
import stirling.fix.messages.fix42.MsgTypeValue._

class MessageFactory extends DefaultMessageFactory {
  import MsgTypeValue._

  message(LOGON,                        classOf[Logon])
  message(REJECT,                       classOf[Reject])
  message(NEW_ORDER_SINGLE,             classOf[NewOrderSingle])
  message(ORDER_CANCEL_REQUEST,         classOf[OrderCancelRequest])
  message(ORDER_CANCEL_REPLACE_REQUEST, classOf[OrderCancelReplaceRequest])
  message(BULK_CANCEL_REQUEST,          classOf[BulkCancelRequest])
  message(EXECUTION_REPORT,             classOf[ExecutionReport])
  message(ORDER_CANCEL_REJECT,          classOf[OrderCancelReject])

  override def createHeader = new MessageHeader

  override def getProfile = "lime"

  override def isValid(msgType: String) = {
    if (msgType.equals(BULK_CANCEL_REQUEST)) true else super.isValid(msgType)
  }
}
