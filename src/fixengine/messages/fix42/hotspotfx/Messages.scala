/*
 * Copyright 2011 the original author or authors.
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
package fixengine.messages.fix42.hotspotfx

import fixengine.messages.{
  MessageHeader,
  Required
}
import fixengine.tags.fix42.{
  Account,
  AvgPx,
  ClOrdID,
  CxlRejReason,
  CxlRejResponseTo,
  ContraBroker,
  CumQty,
  Currency,
  ExecBroker,
  ExecID,
  ExecInst,
  ExecTransType,
  ExpireTime,
  FutSettDate,
  HandlInst,
  LastPx,
  LastShares,
  LeavesQty,
  MaxShow,
  MinQty,
  NoContraBrokers,
  OrdStatus,
  OrdType,
  OrderID,
  OrderQty,
  OrderQty2,
  OrigClOrdID,
  PegDifference,
  Price,
  SecurityType,
  SettlCurrAmt,
  SettlCurrency,
  Side,
  Symbol,
  Text,
  TimeInForce,
  TradeDate,
  TransactTime
}
import fixengine.tags.fix44.{TradeLinkID, ExecType}

class NewOrderSingle(header: MessageHeader) extends fixengine.messages.fix42.NewOrderSingle(header) {
  field(Account.Tag, Required.NO)
  field(ClOrdID.Tag)
  field(ExecInst.Tag, Required.NO)
  field(HandlInst.Tag)
  field(Currency.Tag, Required.NO)
  field(OrderQty.Tag)
  field(MaxShow.Tag, Required.NO)
  field(MinQty.Tag, Required.NO)
  field(Price.Tag)
  field(Side.Tag)
  field(Symbol.Tag)
  field(OrdType.Tag)
  field(TimeInForce.Tag, Required.NO)
  field(TransactTime.Tag, Required.NO)
  field(PegDifference.Tag, Required.NO)
  field(TradeLinkID.Tag, Required.NO)
}

class OrderCancelRequest(header: MessageHeader) extends fixengine.messages.fix42.OrderCancelRequestMessage(header) {
  field(Account.Tag, Required.NO)
  field(ClOrdID.Tag)
  field(OrigClOrdID.Tag)
  field(Symbol.Tag)
  field(Side.Tag, Required.NO)
  field(TransactTime.Tag, Required.NO)
  field(Currency.Tag, Required.NO)
}

class OrderModificationRequest(header: MessageHeader) extends fixengine.messages.fix42.OrderModificationRequestMessage(header) {
  field(Account.Tag, Required.NO)
  field(ClOrdID.Tag)
  field(OrigClOrdID.Tag)
  field(HandlInst.Tag)
  field(OrderQty.Tag)
  field(MaxShow.Tag, Required.NO)
  field(MinQty.Tag, Required.NO)
  field(Price.Tag)
  field(Symbol.Tag, Required.NO)
  field(OrdType.Tag, Required.NO)
  field(Side.Tag, Required.NO)
  field(TransactTime.Tag, Required.NO)
}

class ExecutionReport(header: MessageHeader) extends fixengine.messages.fix42.ExecutionReport(header) {
  field(Account.Tag, Required.NO)
  field(ClOrdID.Tag)
  field(ExecID.Tag)
  field(ExecInst.Tag, Required.NO)
  field(ExecTransType.Tag)
  field(OrderID.Tag)
  field(OrdStatus.Tag)
  field(OrigClOrdID.Tag, Required.NO)
  field(OrderQty.Tag, Required.NO)
  field(Price.Tag, Required.NO)
  field(TimeInForce.Tag, Required.NO)
  field(Currency.Tag, Required.NO)
  field(Side.Tag)
  field(Symbol.Tag)
  field(ExecBroker.Tag, Required.NO)
  field(LeavesQty.Tag)
  field(CumQty.Tag)
  field(MinQty.Tag, Required.NO)
  field(OrderQty2.Tag, Required.NO)
  field(FutSettDate.Tag, Required.NO)
  field(TradeDate.Tag, Required.NO)
  field(SettlCurrAmt.Tag, Required.NO)
  field(SettlCurrency.Tag, Required.NO)
  field(SecurityType.Tag)
  field(MaxShow.Tag, Required.NO)
  field(ExecType.Tag)
  field(AvgPx.Tag)
  field(LastShares.Tag, Required.NO)
  field(LastPx.Tag, Required.NO)
  field(NoContraBrokers.Tag, Required.NO)
  field(ContraBroker.Tag, Required.NO)
  field(Text.Tag, Required.NO)
  field(TransactTime.Tag)
  field(ExpireTime.Tag, Required.NO)
  field(TradeLinkID.Tag, Required.NO)
}

class OrderCancelReject(header: MessageHeader) extends fixengine.messages.fix42.OrderCancelReject(header) {
  field(Account.Tag, Required.NO)
  field(ClOrdID.Tag)
  field(OrderID.Tag)
  field(OrigClOrdID.Tag)
  field(Symbol.Tag)
  field(CxlRejResponseTo.Tag)
  field(CxlRejReason.Tag, Required.NO)
  field(Text.Tag, Required.NO)
}

class OrderStatusRequest(header: MessageHeader) extends fixengine.messages.fix42.OrderStatusRequest(header) {
  field(Account.Tag, Required.NO)
  field(ClOrdID.Tag)
  field(Symbol.Tag, Required.NO)
  field(Side.Tag, Required.NO)
  field(TransactTime.Tag, Required.NO)
}
