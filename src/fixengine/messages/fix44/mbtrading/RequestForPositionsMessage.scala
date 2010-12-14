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
  RequestForPositionsMessage => RequestForPositionsMessageTrait,
  MessageHeader,
  MessageVisitor
}
import fixengine.tags.fix42.{
  Account,
  Commission,
  Side,
  Symbol,
  SubscriptionRequestType,
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
  PosReqID,
  PosReqResult,
  SettlPrice,
  ShortQty,
  TotalNumPosReports
}
import fixengine.tags.fix44.mbtrading.{
  PosBuyPowerUsed,
  PosEquityUsed,
  PosPendBuy,
  PosPendSell,
  PosRealizedPNL
}

class RequestForPositionsMessage(header: MessageHeader) extends AbstractMessage(header) with RequestForPositionsMessageTrait {
  field(Account.Tag)
  field(Commission.Tag)
  field(Side.Tag)
  field(Symbol.Tag)
  field(TradeDate.Tag)
  field(SubscriptionRequestType.Tag)
  field(UnsolicitedIndicator.Tag)
  field(Price2.Tag)
  field(LongQty.Tag)
  field(ShortQty.Tag)
  field(PosReqID.Tag)
  field(ClearingBusinessDate.Tag)
  field(PosMaintRptID.Tag)
  field(TotalNumPosReports.Tag)
  field(SettlPrice.Tag)
  field(PosPendBuy.Tag)
  field(PosPendSell.Tag)
  field(PosBuyPowerUsed.Tag)
  field(PosRealizedPNL.Tag)
  field(PosEquityUsed.Tag)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
