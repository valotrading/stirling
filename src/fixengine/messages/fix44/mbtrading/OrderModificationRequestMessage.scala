/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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
package fixengine.messages.fix44.mbtrading

import fixengine.messages.AbstractMessage
import fixengine.messages.MessageHeader
import fixengine.messages.MessageVisitor
import fixengine.messages.{OrderModificationRequestMessage => OrderModificationRequestMessageTrait}
import fixengine.tags.Account
import fixengine.tags.ClOrdID
import fixengine.tags.HandlInst
import fixengine.tags.OrdType
import fixengine.tags.OrderQty
import fixengine.tags.OrigClOrdID
import fixengine.tags.Price
import fixengine.tags.Symbol
import fixengine.tags.TransactTime
import fixengine.tags.fix42.Side
import fixengine.tags.fix42.TimeInForce
import fixengine.tags.fix44.Username

class OrderModificationRequestMessage(header: MessageHeader) extends AbstractMessage(header) with OrderModificationRequestMessageTrait {
  field(Account.TAG)
  field(ClOrdID.TAG)
  field(HandlInst.Tag)
  field(OrderQty.TAG)
  field(OrdType.Tag)
  field(OrigClOrdID.TAG)
  field(Price.TAG)
  field(Side.Tag)
  field(Symbol.TAG)
  field(TimeInForce.Tag)
  field(TransactTime.TAG)
  field(Username.TAG)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
