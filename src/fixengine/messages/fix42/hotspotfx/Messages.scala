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
  ClOrdID,
  Currency,
  ExecInst,
  HandlInst,
  MaxShow,
  MinQty,
  OrdType,
  OrderQty,
  OrigClOrdID,
  PegDifference,
  Price,
  Side,
  Symbol,
  TimeInForce,
  TransactTime
}
import fixengine.tags.fix44.TradeLinkID

class NewOrderSingleMessage(header: MessageHeader) extends fixengine.messages.fix42.NewOrderSingleMessage(header) {
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

class OrderCancelRequestMessage(header: MessageHeader) extends fixengine.messages.fix42.OrderCancelRequestMessage(header) {
  field(Account.Tag, Required.NO)
  field(ClOrdID.Tag)
  field(OrigClOrdID.Tag)
  field(Symbol.Tag)
  field(Side.Tag, Required.NO)
  field(TransactTime.Tag, Required.NO)
  field(Currency.Tag, Required.NO)
}

class OrderModificationRequestMessage(header: MessageHeader) extends fixengine.messages.fix42.OrderModificationRequestMessage(header) {
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
