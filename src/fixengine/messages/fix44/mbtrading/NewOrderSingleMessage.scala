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
import fixengine.messages.Required
import fixengine.messages.MessageVisitor
import fixengine.messages.{NewOrderSingleMessage => NewOrderSingleMessageTrait}
import fixengine.tags.Account
import fixengine.tags.ClOrdID
import fixengine.tags.ExecInst
import fixengine.tags.ExpireTime
import fixengine.tags.HandlInst
import fixengine.tags.MaturityMonthYear
import fixengine.tags.MaxFloor
import fixengine.tags.OrdType
import fixengine.tags.OrderQty
import fixengine.tags.PegDifference
import fixengine.tags.SecurityType
import fixengine.tags.SendingTime
import fixengine.tags.Symbol
import fixengine.tags.TransactTime
import fixengine.tags.fix42.ComplianceID
import fixengine.tags.fix42.DiscretionInst
import fixengine.tags.fix42.DiscretionOffset
import fixengine.tags.fix42.EffectiveTime
import fixengine.tags.fix42.ExDestination
import fixengine.tags.fix42.LocateReqd
import fixengine.tags.fix42.PutOrCall
import fixengine.tags.fix42.Side
import fixengine.tags.fix42.StopPx
import fixengine.tags.fix42.StrikePrice
import fixengine.tags.fix42.TimeInForce
import fixengine.tags.fix43.LegPrice
import fixengine.tags.fix43.OrderRestrictions
import fixengine.tags.fix43.Price2
import fixengine.tags.fix43.SecondaryClOrdID
import fixengine.tags.fix44.NoTrdRegTimestamps
import fixengine.tags.fix44.TrdRegTimestamp
import fixengine.tags.fix44.TrdRegTimestampOrigin
import fixengine.tags.fix44.TrdRegTimestampType
import fixengine.tags.fix44.Username
import fixengine.tags.fix44.mbtrading.MBTXAggressive
import fixengine.tags.fix44.mbtrading.OrderGroupID1

class NewOrderSingleMessage(header: MessageHeader) extends AbstractMessage(header) with NewOrderSingleMessageTrait {
  field(Account.Tag)
  field(ClOrdID.Tag)
  field(ExecInst.Tag, Required.NO)
  field(HandlInst.Tag)
  field(OrderQty.Tag)
  field(OrdType.Tag)
  field(StopPx.Tag, Required.NO)
  field(SendingTime.Tag)
  field(Symbol.Tag)
  field(Side.Tag)
  field(TimeInForce.Tag)
  field(TransactTime.Tag)
  field(StopPx.Tag, Required.NO)
  field(ExDestination.Tag)
  field(MaxFloor.Tag, Required.NO)
  field(LocateReqd.Tag, Required.NO)
  field(ExpireTime.Tag, new Required() {
    override def isRequired() = {
      getEnum(TimeInForce.Tag).equals(TimeInForce.GoodTillDate)
    }
  })
  field(SecurityType.Tag, Required.NO)
  field(EffectiveTime.Tag, Required.NO)
  field(MaturityMonthYear.Tag, Required.NO)
  field(PutOrCall.Tag, Required.NO)
  field(StrikePrice.Tag, Required.NO)
  field(PegDifference.Tag, Required.NO)
  field(DiscretionInst.Tag, Required.NO)
  field(DiscretionOffset.Tag, Required.NO)
  field(ComplianceID.Tag, Required.NO)
  field(SecondaryClOrdID.Tag, Required.NO)
  field(OrderRestrictions.Tag, Required.NO)
  field(Username.Tag)
  field(LegPrice.Tag, Required.NO)
  field(Price2.Tag, Required.NO)
  field(NoTrdRegTimestamps.Tag, Required.NO)
  field(TrdRegTimestamp.TAG, Required.NO)
  field(TrdRegTimestampType.Tag, Required.NO)
  field(TrdRegTimestampOrigin.Tag, Required.NO)
  field(MBTXAggressive.Tag, Required.NO)
  field(OrderGroupID1.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
