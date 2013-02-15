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
package stirling.fix.messages.fix42.ubs

import stirling.fix.messages.MessageHeader
import stirling.fix.messages.Required
import stirling.fix.tags.fix42.{
  AvgPx,
  ClOrdID,
  CumQty,
  DKReason,
  ExecID,
  ExecInst,
  ExecRefID,
  ExecTransType,
  ExecType,
  LastCapacity,
  LastMkt,
  LastPx,
  LastShares,
  LeavesQty,
  MinQty,
  OrdStatus,
  OrdType,
  OrderCapacity,
  OrderID,
  OrderQty,
  OrigClOrdID,
  Price,
  Side,
  Symbol,
  TimeInForce,
  TransactTime
}
import stirling.fix.tags.fix42.ubs.Internalization;

class DontKnowTrade(header: MessageHeader) extends stirling.fix.messages.fix42.DontKnowTrade(header) {
  field(OrderID.Tag)
  field(ExecID.Tag)
  field(DKReason.Tag)
  field(Symbol.Tag)
  field(Side.Tag)
  field(OrderQty.Tag, Required.NO)
  field(LastShares.Tag, Required.NO)
  field(LastPx.Tag, Required.NO)
}

class ExecutionReport(header: MessageHeader) extends stirling.fix.messages.fix42.ExecutionReport(header) {
  field(OrderID.Tag)
  field(ClOrdID.Tag)
  field(OrigClOrdID.Tag, Required.NO)
  field(ExecID.Tag)
  field(ExecType.Tag)
  field(ExecTransType.Tag)
  field(ExecRefID.Tag, new Required {
    override def isRequired: Boolean = {
      val value = getEnum(ExecTransType.Tag)
      value.equals(ExecTransType.Cancel) || value.equals(ExecTransType.Correct)
    }
  })
  field(Symbol.Tag)
  field(Side.Tag)
  field(OrderQty.Tag)
  field(OrdStatus.Tag)
  field(LastShares.Tag, Required.NO)
  field(LastPx.Tag, Required.NO)
  field(LastMkt.Tag, Required.NO)
  field(LastCapacity.Tag, Required.NO)
  field(LeavesQty.Tag)
  field(CumQty.Tag)
  field(AvgPx.Tag)
  field(TransactTime.Tag)
}

class NewOrderSingle(header: MessageHeader) extends stirling.fix.messages.fix42.NewOrderSingle(header) {
  field(ClOrdID.Tag)
  field(ExecInst.Tag)
  field(Symbol.Tag)
  field(Side.Tag)
  field(OrderQty.Tag)
  field(OrdType.Tag)
  field(TransactTime.Tag)
  field(OrderCapacity.Tag)
  field(Price.Tag, new Required {
    override def isRequired: Boolean = getEnum(OrdType.Tag).equals(OrdType.Limit)
  })
  field(TimeInForce.Tag, Required.NO)
  field(MinQty.Tag, Required.NO)
  field(Internalization.Tag, Required.NO)
}

class OrderCancelReject(header: MessageHeader) extends stirling.fix.messages.fix42.OrderCancelReject(header) {
  field(ClOrdID.Tag)
  field(OrigClOrdID.Tag)
  field(OrderID.Tag)
  field(OrdStatus.Tag)
}

class OrderCancelRequest(header: MessageHeader) extends stirling.fix.messages.fix42.OrderCancelRequest(header) {
  field(ClOrdID.Tag)
  field(OrigClOrdID.Tag)
  field(Symbol.Tag)
  field(Side.Tag)
  field(TransactTime.Tag)
}

class OrderCancelReplaceRequest(header: MessageHeader) extends stirling.fix.messages.fix42.OrderCancelReplaceRequest(header) {
  field(ClOrdID.Tag)
  field(OrigClOrdID.Tag)
  field(Symbol.Tag)
  field(Side.Tag)
  field(TransactTime.Tag)
  field(OrderQty.Tag)
  field(OrdType.Tag)
  field(Price.Tag, new Required {
    override def isRequired: Boolean =  getEnum(OrdType.Tag).equals(OrdType.Limit)
  })
  field(ExecInst.Tag, Required.NO)
  field(TimeInForce.Tag, Required.NO)
  field(MinQty.Tag, Required.NO)
}
