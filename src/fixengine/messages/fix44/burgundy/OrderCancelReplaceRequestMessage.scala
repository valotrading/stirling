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
package fixengine.messages.fix44.burgundy

import fixengine.tags.fix42.Side
import fixengine.tags.fix42.TimeInForce
import fixengine.tags.fix43.AccountType
import fixengine.tags.fix43.OrderCapacity
import fixengine.tags.fix43.PegOffsetValue
import fixengine.tags.fix43.SecurityIDSource
import fixengine.tags.fix44.PegMoveType
import fixengine.tags.fix44.PegOffsetType
import fixengine.tags.fix44.PegScope
import fixengine.tags.fix44.burgundy.OrderRestrictions
import fixengine.tags.fix50.ExecInst
import fixengine.tags.{ExpireTime, MaxFloor, MaxShow, MinQty, TransactTime}
import fixengine.tags.{Text, SecurityID, Price, OrigClOrdID}
import fixengine.tags.{OrdType, OrderQty, OrderID, Currency, ClOrdID, Account, Symbol}
import fixengine.messages.{MessageVisitor, AbstractMessage, MessageHeader, Required}
import fixengine.messages.{OrderCancelReplaceRequestMessage => OrderCancelReplaceRequestMessageTrait}

class OrderCancelReplaceRequestMessage(header: MessageHeader) extends AbstractMessage(header) with OrderCancelReplaceRequestMessageTrait {
  field(Account.TAG, Required.NO)
  field(ClOrdID.TAG)
  field(Currency.TAG, Required.NO)
  field(ExecInst.Tag, Required.NO)
  field(OrderID.TAG, Required.NO)
  field(OrderQty.TAG)
  field(OrdType.TAG)
  field(OrigClOrdID.TAG)
  field(Price.TAG)
  field(Side.Tag)
  field(Symbol.TAG)
  field(SecurityIDSource.Tag, Required.NO)
  field(SecurityID.TAG, Required.NO)
  field(Text.TAG, Required.NO)
  field(TimeInForce.Tag, Required.NO)
  field(TransactTime.TAG)
  field(MinQty.TAG, Required.NO)
  field(MaxShow.TAG, Required.NO)
  field(MaxFloor.TAG, Required.NO)
  field(ExpireTime.TAG, new Required() {
    override def isRequired(): Boolean = {
      return getEnum(TimeInForce.Tag).equals(TimeInForce.GoodTillDate)
    }
  })
  field(PegOffsetValue.TAG, Required.YES)
  field(PegMoveType.Tag, Required.NO)
  field(PegOffsetType.Tag, Required.NO)
  field(PegScope.Tag, Required.NO)
  field(OrderCapacity.Tag, Required.NO)
  field(OrderRestrictions.Tag, Required.NO)
  field(AccountType.TAG, Required.NO)

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
