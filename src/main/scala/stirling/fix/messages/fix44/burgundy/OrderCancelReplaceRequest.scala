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
package stirling.fix.messages.fix44.burgundy

import stirling.fix.messages.{
  AbstractMessage,
  MessageHeader,
  MessageVisitor,
  OrderCancelReplaceRequest => OrderCancelReplaceRequestTrait,
  Required
}
import stirling.fix.tags.fix42.{
  Side,
  TimeInForce,
  Account,
  ClOrdID,
  Currency,
  ExpireTime,
  MaxFloor,
  MaxShow,
  MinQty,
  OrdType,
  OrderCapacity,
  OrderID,
  OrderQty,
  OrigClOrdID,
  Price,
  SecurityID,
  Symbol,
  Text,
  TransactTime
}
import stirling.fix.tags.fix43.{
  AccountType,
  PegOffsetValue,
  SecurityIDSource
}
import stirling.fix.tags.fix44.{
  PegMoveType,
  PegOffsetType,
  PegScope
}
import stirling.fix.tags.fix44.burgundy.{
  OrderRestrictions
}
import stirling.fix.tags.fix50.{
  ExecInst
}

class OrderCancelReplaceRequest(header: MessageHeader) extends AbstractMessage(header) with OrderCancelReplaceRequestTrait {
  field(Account.Tag, Required.NO)
  field(ClOrdID.Tag)
  field(Currency.Tag, Required.NO)
  field(ExecInst.Tag, Required.NO)
  field(OrderID.Tag, Required.NO)
  field(OrderQty.Tag)
  field(OrdType.Tag)
  field(OrigClOrdID.Tag)
  field(Price.Tag)
  field(Side.Tag)
  field(Symbol.Tag)
  field(SecurityIDSource.Tag, Required.NO)
  field(SecurityID.Tag, Required.NO)
  field(Text.Tag, Required.NO)
  field(TimeInForce.Tag, Required.NO)
  field(TransactTime.Tag)
  field(MinQty.Tag, Required.NO)
  field(MaxShow.Tag, Required.NO)
  field(MaxFloor.Tag, Required.NO)
  field(ExpireTime.Tag, new Required() {
    override def isRequired(): Boolean = {
      return getEnum(TimeInForce.Tag).equals(TimeInForce.GoodTillDate)
    }
  })
  field(PegOffsetValue.Tag, Required.YES)
  field(PegMoveType.Tag, Required.NO)
  field(PegOffsetType.Tag, Required.NO)
  field(PegScope.Tag, Required.NO)
  field(OrderCapacity.Tag, Required.NO)
  field(OrderRestrictions.Tag, Required.NO)
  field(AccountType.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
