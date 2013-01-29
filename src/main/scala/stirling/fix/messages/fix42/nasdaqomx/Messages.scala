/*
 * Copyright 2012 the original author or authors.
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
package stirling.fix.messages.fix42.nasdaqomx

import stirling.fix.messages.{
  AbstractMessage,
  ExecutionReport => ExecutionReportTrait,
  Logon => LogonTrait,
  MessageHeader => MessageHeaderTrait,
  MessageVisitor,
  NewOrderSingle => NewOrderSingleTrait,
  OrderCancelReplaceRequest => OrderCancelReplaceRequestTrait,
  OrderCancelRequest => OrderCancelRequestTrait,
  OrderCancelReject => OrderCancelRejectTrait,
  Reject => RejectTrait,
  Required
}
import stirling.fix.tags.fix42.nasdaqomx._

class Logon(header: MessageHeaderTrait) extends AbstractMessage(header) with LogonTrait {
  field(EncryptMethod.Tag)
  field(HeartBtInt.Tag)

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class Reject(header: MessageHeaderTrait) extends AbstractMessage(header) with RejectTrait {
  field(RefSeqNo.Tag)
  field(Text.Tag)
  field(RefTagId.Tag,   Required.NO)
  field(RefMsgType.Tag, Required.NO)
  field(SessionRejectReason.Tag)

  def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class NewOrderSingle(header: MessageHeaderTrait) extends AbstractMessage(header) with NewOrderSingleTrait {
  field(ClOrdID.Tag)
  field(Currency.Tag,          Required.NO)
  field(ExecInst.Tag,          Required.NO)
  field(HandlInst.Tag)
  field(IDSource.Tag,          Required.NO)
  field(OrderQty.Tag)
  field(OrdType.Tag)
  field(Price.Tag,             Required.NO)
  field(SecurityID.Tag,        Required.NO)
  field(Side.Tag)
  field(Symbol.Tag)
  field(TimeInForce.Tag,       Required.NO)
  field(TransactTime.Tag)
  field(ExecBroker.Tag,        Required.NO)
  field(MinQty.Tag,            Required.NO)
  field(MaxFloor.Tag,          Required.NO)
  field(ExpireTime.Tag,        Required.NO)
  field(PegDifference.Tag,     Required.NO)
  field(ClearingFirm.Tag,      Required.NO)
  field(ClearingAccount.Tag,   Required.NO)
  field(OrderCapacity.Tag,     Required.NO)
  field(OrderRestrictions.Tag, Required.NO)
  field(SubMktID.Tag,          Required.NO)
  field(ClRefID.Tag,           Required.NO)
  field(DisplayInst.Tag,       Required.NO)
  field(CrossTradeFlag.Tag,    Required.NO)
  field(BrSeqNbr.Tag,          Required.NO)

  def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderCancelRequest(header: MessageHeaderTrait) extends AbstractMessage(header) with OrderCancelRequestTrait {
  field(ClOrdID.Tag)
  field(Currency.Tag,   Required.NO)
  field(IDSource.Tag,   Required.NO)
  field(OrderID.Tag,    Required.NO)
  field(OrderQty.Tag,   Required.NO)
  field(OrigClOrdID.Tag)
  field(SecurityID.Tag, Required.NO)
  field(Side.Tag)
  field(Symbol.Tag)
  field(TransactTime.Tag)
  field(SubMktID.Tag,   Required.NO)

  def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderCancelReplaceRequest(header: MessageHeaderTrait) extends AbstractMessage(header) with OrderCancelReplaceRequestTrait {
  field(ClOrdID.Tag)
  field(Currency.Tag,        Required.NO)
  field(HandlInst.Tag)
  field(IDSource.Tag,        Required.NO)
  field(OrderID.Tag,         Required.NO)
  field(OrderQty.Tag)
  field(OrdType.Tag)
  field(OrigClOrdID.Tag)
  field(Price.Tag,           Required.NO)
  field(SecurityID.Tag,      Required.NO)
  field(Side.Tag)
  field(Symbol.Tag)
  field(TimeInForce.Tag,     Required.NO)
  field(TransactTime.Tag)
  field(MaxFloor.Tag,        Required.NO)
  field(ExpireTime.Tag,      Required.NO)
  field(ClearingFirm.Tag,    Required.NO)
  field(ClearingAccount.Tag, Required.NO)
  field(SubMktID.Tag,        Required.NO)
  field(BrSeqNbr.Tag,        Required.NO)
  field(ClRefID.Tag,         Required.NO)

  def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class ExecutionReport(header: MessageHeaderTrait) extends AbstractMessage(header) with ExecutionReportTrait {
  field(AvgPx.Tag)
  field(ClOrdID.Tag)
  field(CumQty.Tag)
  field(Currency.Tag,              Required.NO)
  field(ExecID.Tag)
  field(ExecInst.Tag,              Required.NO)
  field(ExecRefID.Tag,             Required.NO)
  field(ExecTransType.Tag)
  field(IDSource.Tag,              Required.NO)
  field(LastPx.Tag,                Required.NO)
  field(LastShares.Tag,            Required.NO)
  field(OrderID.Tag)
  field(OrderQty.Tag,              Required.NO)
  field(OrdStatus.Tag)
  field(OrdType.Tag,               Required.NO)
  field(OrigClOrdID.Tag,           Required.NO)
  field(Price.Tag,                 Required.NO)
  field(SecurityID.Tag,            Required.NO)
  field(Side.Tag)
  field(Symbol.Tag)
  field(Text.Tag,                  Required.NO)
  field(TimeInForce.Tag,           Required.NO)
  field(TransactTime.Tag,          Required.NO)
  field(TradeDate.Tag,             Required.NO)
  field(ExecBroker.Tag,            Required.NO)
  field(OrdRejReason.Tag,          Required.NO)
  field(ClientID.Tag)
  field(MinQty.Tag,                Required.NO)
  field(MaxFloor.Tag,              Required.NO)
  field(ExpireTime.Tag,            Required.NO)
  field(ExecType.Tag)
  field(LeavesQty.Tag)
  field(SecondaryOrderID.Tag,      Required.NO)
  field(PegDifference.Tag,         Required.NO)
  field(NoContraBrokers.Tag,       Required.NO)
  field(ContraBroker.Tag,          Required.NO)
  field(ExecRestatementReason.Tag, Required.NO)
  field(ClearingFirm.Tag,          Required.NO)
  field(ClearingAccount.Tag,       Required.NO)
  field(SecondaryExecID.Tag,       Required.NO)
  field(OrderCapacity.Tag,         Required.NO)
  field(OrderRestrictions.Tag,     Required.NO)
  field(TradeID.Tag,               Required.NO)
  field(SubMktID.Tag,              Required.NO)
  field(ClRefID.Tag,               Required.NO)
  field(DisplayInst.Tag,           Required.NO)
  field(CrossTradeFlag.Tag,        Required.NO)
  field(BrSeqNbr.Tag,              Required.NO)
  field(LiquidityFlag.Tag,         Required.NO)

  def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderCancelReject(header: MessageHeaderTrait) extends AbstractMessage(header) with OrderCancelRejectTrait {
  field(ClOrdID.Tag)
  field(OrderID.Tag)
  field(OrdStatus.Tag)
  field(OrigClOrdID.Tag)
  field(Text.Tag,             Required.NO)
  field(CxlRejReason.Tag,     Required.NO)
  field(ClientID.Tag)
  field(SecondaryOrderID.Tag, Required.NO)
  field(CxlRejResponseTo.Tag, Required.NO)
  field(TransactTime.Tag,     Required.NO)

  def apply(visitor: MessageVisitor) = visitor.visit(this)
}
