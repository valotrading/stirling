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

import fixengine.messages.{
  AbstractMessage,
  MessageHeader,
  MessageVisitor,
  NewOrderSingleMessage => NewOrderSingleMessageTrait,
  Required
}
import fixengine.tags.fix42.{
  Account,
  ClOrdID,
  ComplianceID,
  DiscretionInst,
  DiscretionOffset,
  EffectiveTime,
  ExDestination,
  ExecInst,
  ExpireTime,
  HandlInst,
  LocateReqd,
  MaturityMonthYear,
  MaxFloor,
  OrdType,
  OrderQty,
  PegDifference,
  PutOrCall,
  SecurityType,
  SendingTime,
  Side,
  StopPx,
  StrikePrice,
  Symbol,
  TimeInForce,
  TransactTime
}
import fixengine.tags.fix43.{
  LegPrice,
  OrderRestrictions,
  Price2,
  SecondaryClOrdID
}
import fixengine.tags.fix44.{
  NoTrdRegTimestamps,
  TrdRegTimestamp,
  TrdRegTimestampOrigin,
  TrdRegTimestampType,
  Username
}
import fixengine.tags.fix44.mbtrading.{
  MBTXAggressive,
  OrderGroupID1
}

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
  field(TrdRegTimestamp.Tag, Required.NO)
  field(TrdRegTimestampType.Tag, Required.NO)
  field(TrdRegTimestampOrigin.Tag, Required.NO)
  field(MBTXAggressive.Tag, Required.NO)
  field(OrderGroupID1.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
