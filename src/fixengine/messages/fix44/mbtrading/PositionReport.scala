/*
 * Copyright 2008 the original author or authors.
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
package fixengine.messages.fix44.mbtrading

import fixengine.messages.{
  AbstractMessage,
  PositionReport => PositionReportTrait,
  MessageHeader,
  MessageVisitor,
  Required
}
import fixengine.tags.fix42.{
  Account,
  Commission,
  Side,
  SubscriptionRequestType,
  Symbol,
  UnsolicitedIndicator,
  TradeDate
}
import fixengine.tags.fix43.{
  Price2
}
import fixengine.tags.fix44.{
  ClearingBusinessDate,
  LongQty,
  PosMaintRptID,
  PosReqResult,
  TotalNumPosReports,
  PosReqID,
  SettlPrice,
  ShortQty
}
import fixengine.tags.fix44.mbtrading.{
  PosPendSell,
  PosPendBuy,
  PosBuyPowerUsed,
  PosRealizedPNL,
  PosEquityUsed
}

class PositionReport(header: MessageHeader) extends AbstractMessage(header) with PositionReportTrait {
  field(Account.Tag)
  field(Commission.Tag, Required.NO)
  field(Symbol.Tag, Required.NO)
  field(Side.Tag, Required.NO)
  field(TradeDate.Tag, Required.NO)
  field(SubscriptionRequestType.Tag, Required.NO)
  field(UnsolicitedIndicator.Tag, Required.NO)
  field(Price2.Tag, Required.NO)
  field(LongQty.Tag, Required.NO)
  field(ShortQty.Tag, Required.NO)
  field(PosMaintRptID.Tag)
  field(PosReqID.Tag, Required.NO)
  field(ClearingBusinessDate.Tag)
  field(PosMaintRptID.Tag)
  field(TotalNumPosReports.Tag, Required.NO)
  field(PosReqResult.Tag)
  field(SettlPrice.Tag)
  field(PosPendBuy.Tag, Required.NO)
  field(PosPendSell.Tag, Required.NO)
  field(PosBuyPowerUsed.Tag, Required.NO)
  field(PosRealizedPNL.Tag, Required.NO)
  field(PosEquityUsed.Tag, Required.NO)

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
