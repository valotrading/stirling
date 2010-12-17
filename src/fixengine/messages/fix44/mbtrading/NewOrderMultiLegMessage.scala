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
  NewOrderMultiLegMessage => NewOrderMultiLegMessageTrait,
  RepeatingGroup,
  RepeatingGroupInstance,
  Required
}
import fixengine.tags.fix42.{
  Account,
  ClOrdID,
  ExDestination,
  ExecInst,
  HandlInst,
  OrdType,
  OrderQty,
  Side,
  StopPx,
  TimeInForce,
  TransactTime
}
import fixengine.tags.fix43.{
  LegCFICode,
  LegMaturityMonthYear,
  LegPositionEffect,
  LegRatioQty,
  LegRefID,
  LegSide,
  LegStrikePrice,
  LegSymbol,
  NoLegs
}
import fixengine.tags.fix44.{
  NoTrdRegTimestamps,
  Password,
  TrdRegTimestamp,
  TrdRegTimestampOrigin,
  TrdRegTimestampType,
  Username
}

class NewOrderMultiLegMessage(header: MessageHeader) extends AbstractMessage(header) with NewOrderMultiLegMessageTrait {
  field(Account.Tag)
  field(ClOrdID.Tag)
  field(ExecInst.Tag, Required.NO)
  field(HandlInst.Tag)
  field(OrderQty.Tag)
  field(OrdType.Tag)
  field(StopPx.Tag, Required.NO)
  field(Side.Tag)
  field(TimeInForce.Tag)
  field(TransactTime.Tag)
  field(ExDestination.Tag)
  field(NoTrdRegTimestamps.Tag, Required.NO)
  field(TrdRegTimestamp.Tag, Required.NO)
  field(TrdRegTimestampType.Tag, Required.NO)
  field(TrdRegTimestampOrigin.Tag, Required.NO)
  group(new RepeatingGroup(NoLegs.Tag) {
    override def newInstance = new RepeatingGroupInstance(LegSymbol.Tag) {
      field(LegCFICode.Tag)
      field(LegMaturityMonthYear.Tag)
      field(LegStrikePrice.Tag)
      field(LegPositionEffect.Tag)
      field(LegSide.Tag)
      field(LegRatioQty.Tag)
      field(LegRefID.Tag)
      field(Username.Tag)
      field(Password.Tag)
    }
  }, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
