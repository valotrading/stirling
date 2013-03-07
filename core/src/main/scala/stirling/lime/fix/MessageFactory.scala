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

import stirling.fix.messages.fix42.DefaultMessageFactory

object MessageFactory extends DefaultMessageFactory {
  message(MsgTypeValue.Logon,                     classOf[Logon])
  message(MsgTypeValue.Reject,                    classOf[Reject])
  message(MsgTypeValue.NewOrderSingle,            classOf[NewOrderSingle])
  message(MsgTypeValue.OrderCancelRequest,        classOf[OrderCancelRequest])
  message(MsgTypeValue.OrderCancelReplaceRequest, classOf[OrderCancelReplaceRequest])
  message(MsgTypeValue.BulkCancelRequest,         classOf[BulkCancelRequest])
  message(MsgTypeValue.ExecutionReport,           classOf[ExecutionReport])
  message(MsgTypeValue.OrderCancelReject,         classOf[OrderCancelReject])

  override def createHeader = new MessageHeader

  override def getProfile = "lime"

  override def isValid(msgType: String) = {
    if (msgType == MsgTypeValue.BulkCancelRequest) true else super.isValid(msgType)
  }
}
