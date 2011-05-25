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

import fixengine.messages.{
  AbstractMessage,
  MessageHeader,
  MessageVisitor,
  OrderCancelRequestMessage => OrderCancelRequestMessageTrait,
  Required
}
import fixengine.tags.fix42.{
  ClOrdID,
  OrderID,
  OrderQty,
  OrigClOrdID,
  SecurityID,
  Side,
  Symbol,
  TransactTime
}
import fixengine.tags.fix43.{
  SecurityIDSource
}

class OrderCancelRequest(header: MessageHeader) extends AbstractMessage(header) with OrderCancelRequestMessageTrait {
  field(ClOrdID.Tag)
  field(OrderID.Tag, Required.NO)
  field(OrderQty.Tag)
  field(OrigClOrdID.Tag)
  field(Side.Tag)
  field(Symbol.Tag)
  field(SecurityIDSource.Tag, Required.NO)
  field(SecurityID.Tag, Required.NO)
  field(TransactTime.Tag)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
