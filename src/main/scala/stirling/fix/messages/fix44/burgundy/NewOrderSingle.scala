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
  DefaultMessageHeader,
  MessageVisitor,
  Required
}
import stirling.fix.tags.fix42.{
  Account,
  ClOrdID,
  Currency,
  ExpireTime,
  HandlInst,
  MaxFloor,
  MaxShow,
  MinQty,
  OrdType,
  OrderQty,
  Price,
  SecurityID,
  Side,
  Symbol,
  Text,
  TimeInForce,
  TransactTime
}
import stirling.fix.tags.fix43.{
  AccountType,
  OrderCapacity,
  PegOffsetValue,
  SecurityIDSource
}
import stirling.fix.tags.fix44.{
  PegMoveType,
  PegOffsetType,
  PegScope
}
import stirling.fix.tags.fix44.burgundy.OrderRestrictions
import stirling.fix.tags.fix50.{
  DisplayHighQty,
  DisplayLowQty,
  DisplayMethod,
  DisplayMinIncr,
  ExecInst
}

class NewOrderSingle(header: DefaultMessageHeader) extends AbstractMessage(header) with Groups with stirling.fix.messages.NewOrderSingle {
  field(Account.Tag, Required.NO)
  field(ClOrdID.Tag)
  field(Currency.Tag, Required.NO)
  field(ExecInst.Tag, Required.NO)
  field(HandlInst.Tag, Required.NO)
  field(Symbol.Tag)
  field(SecurityIDSource.Tag, Required.NO)
  field(SecurityID.Tag, Required.NO)
  field(OrderQty.Tag, Required.NO)
  field(OrdType.Tag)
  field(Price.Tag)
  field(Side.Tag)
  field(Text.Tag, Required.NO)
  field(TimeInForce.Tag, Required.NO)
  field(TransactTime.Tag)
  field(MinQty.Tag, Required.NO)
  field(DisplayMethod.Tag, Required.NO)
  field(DisplayLowQty.Tag, Required.NO)
  field(DisplayHighQty.Tag, Required.NO)
  field(DisplayMinIncr.Tag, Required.NO)
  field(MaxFloor.Tag, Required.NO)
  field(ExpireTime.Tag, new Required() {
    override def isRequired(): Boolean = getEnum(TimeInForce.Tag) == TimeInForce.GoodTillDate
  })
  field(MaxShow.Tag, Required.NO)
  field(PegOffsetValue.Tag, Required.YES)
  field(PegMoveType.Tag, Required.NO)
  field(PegOffsetType.Tag, Required.NO)
  field(PegScope.Tag, Required.NO)
  parties(Required.NO)
  field(OrderCapacity.Tag, Required.NO)
  field(OrderRestrictions.Tag, Required.NO)
  field(AccountType.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
