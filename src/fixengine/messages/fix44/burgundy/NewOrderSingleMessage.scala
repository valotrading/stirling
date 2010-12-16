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

import fixengine.messages.MessageHeader
import fixengine.messages.RepeatingGroup
import fixengine.messages.RepeatingGroupInstance
import fixengine.messages.Required
import fixengine.tags.Account
import fixengine.tags.ClOrdID
import fixengine.tags.Currency
import fixengine.tags.ExpireTime
import fixengine.tags.HandlInst
import fixengine.tags.MaxFloor
import fixengine.tags.MaxShow
import fixengine.tags.MinQty
import fixengine.tags.NoPartyIDs
import fixengine.tags.OrdType
import fixengine.tags.OrderQty
import fixengine.tags.Price
import fixengine.tags.SecurityID
import fixengine.tags.Symbol
import fixengine.tags.Text
import fixengine.tags.TransactTime
import fixengine.tags.fix42.Side
import fixengine.tags.fix42.TimeInForce
import fixengine.tags.fix43.AccountType
import fixengine.tags.fix43.OrderCapacity
import fixengine.tags.fix43.PartyID
import fixengine.tags.fix43.PartyIDSource
import fixengine.tags.fix43.PegOffsetValue
import fixengine.tags.fix43.SecurityIDSource
import fixengine.tags.fix44.PartyRole
import fixengine.tags.fix44.PegMoveType
import fixengine.tags.fix44.PegOffsetType
import fixengine.tags.fix44.PegScope
import fixengine.tags.fix44.burgundy.OrderRestrictions
import fixengine.tags.fix50.DisplayHighQty
import fixengine.tags.fix50.DisplayLowQty
import fixengine.tags.fix50.DisplayMethod
import fixengine.tags.fix50.DisplayMinIncr
import fixengine.tags.fix50.ExecInst
import fixengine.messages.AbstractNewOrderSingleMessage

class NewOrderSingleMessage(header: MessageHeader) extends AbstractNewOrderSingleMessage(header) {
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
  field(TransactTime.TAG)
  field(MinQty.Tag, Required.NO)
  field(DisplayMethod.Tag, Required.NO)
  field(DisplayLowQty.Tag, Required.NO)
  field(DisplayHighQty.Tag, Required.NO)
  field(DisplayMinIncr.Tag, Required.NO)
  field(MaxFloor.Tag, Required.NO)
  field(ExpireTime.TAG, new Required() {
    override def isRequired(): Boolean = getEnum(TimeInForce.Tag) == TimeInForce.GoodTillDate
  })
  field(MaxShow.TAG, Required.NO)
  field(PegOffsetValue.TAG, Required.YES)
  field(PegMoveType.Tag, Required.NO)
  field(PegOffsetType.Tag, Required.NO)
  field(PegScope.Tag, Required.NO)
  group(new RepeatingGroup(NoPartyIDs.Tag) {
    override def newInstance(): RepeatingGroupInstance =
      new RepeatingGroupInstance(PartyID.TAG) {
        field(PartyIDSource.Tag)
        field(PartyRole.Tag, Required.NO)
      }
  }, Required.NO)
  field(OrderCapacity.Tag, Required.NO)
  field(OrderRestrictions.Tag, Required.NO)
  field(AccountType.Tag, Required.NO)
}
