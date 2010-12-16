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
package fixengine.messages.fix44.burgundy

import fixengine.messages.AbstractMessage
import fixengine.messages.MessageHeader
import fixengine.messages.MessageVisitor
import fixengine.messages.Required
import fixengine.tags.Account
import fixengine.tags.ClOrdID
import fixengine.tags.CxlRejReason
import fixengine.tags.CxlRejResponseTo
import fixengine.tags.OrdStatus
import fixengine.tags.OrderID
import fixengine.tags.OrigClOrdID
import fixengine.tags.Text
import fixengine.tags.TransactTime
import fixengine.messages.{OrderCancelRejectMessage => OrderCancelRejectMessageTrait}

class OrderCancelRejectMessage(header: MessageHeader) extends AbstractMessage(header) with OrderCancelRejectMessageTrait {
  field(Account.Tag, Required.NO)
  field(ClOrdID.Tag)
  field(OrderID.Tag, Required.NO)
  field(OrdStatus.Tag)
  field(OrigClOrdID.Tag)
  field(TransactTime.TAG, Required.NO)
  field(CxlRejResponseTo.Tag)
  field(CxlRejReason.Tag, Required.NO)
  field(Text.Tag, Required.NO)

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
