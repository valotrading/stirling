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
package stirling.fix.messages.fix44.burgundy

import stirling.fix.messages.{
  AbstractMessage,
  MessageHeader,
  MessageVisitor,
  OrderCancelReject => OrderCancelRejectTrait,
  Required
}
import stirling.fix.tags.fix42.{
  Account,
  ClOrdID,
  CxlRejReason,
  CxlRejResponseTo,
  OrdStatus,
  OrderID,
  OrigClOrdID,
  Text,
  TransactTime
}

class OrderCancelReject(header: MessageHeader) extends AbstractMessage(header) with OrderCancelRejectTrait {
  field(Account.Tag, Required.NO)
  field(ClOrdID.Tag)
  field(OrderID.Tag, Required.NO)
  field(OrdStatus.Tag)
  field(OrigClOrdID.Tag)
  field(TransactTime.Tag, Required.NO)
  field(CxlRejResponseTo.Tag)
  field(CxlRejReason.Tag, Required.NO)
  field(Text.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
