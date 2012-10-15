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
package stirling.fix.messages.fix42.lime

import stirling.fix.messages.{
  AbstractMessage,
  BulkCancelRequest => BulkCancelRequestTrait,
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
import stirling.fix.tags.fix42.lime._

class Logon(header: MessageHeaderTrait) extends AbstractMessage(header) with LogonTrait {
  field(EncryptMethod.Tag)
  field(HeartBtInt.Tag)
  field(Username.Tag,              Required.NO)
  field(Password.Tag,              Required.NO)
  field(CancelAllOnDisconnect.Tag, Required.NO)

  def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class Reject(header: MessageHeaderTrait) extends AbstractMessage(header) with RejectTrait {
  field(RefSeqNo.Tag)
  field(Text.Tag,                Required.NO)
  field(RefTagId.Tag,            Required.NO)
  field(RefMsgType.Tag,          Required.NO)
  field(SessionRejectReason.Tag, Required.NO)

  def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class NewOrderSingle(header: MessageHeaderTrait) extends AbstractMessage(header) with NewOrderSingleTrait {
  field(ClOrdID.Tag)
  field(Symbol.Tag)
  field(SymbolSfx.Tag,                   Required.NO)
  field(SecurityID.Tag,                  Required.NO)
  field(SecurityIDSource.Tag,            Required.NO)
  field(Side.Tag)
  field(OrderQty.Tag)
  field(OrdType.Tag)
  field(Price.Tag,                       Required.NO)
  field(ExDestination.Tag,               Required.NO)
  field(TimeInForce.Tag,                 Required.NO)
  field(MinQty.Tag,                      Required.NO)
  field(MaxFloor.Tag,                    Required.NO)
  field(ExpireTime.Tag,                  Required.NO)
  field(PegDifference.Tag,               Required.NO)
  field(DiscretionOffset.Tag,            Required.NO)
  field(Invisible.Tag,                   Required.NO)
  field(PostOnly.Tag,                    Required.NO)
  field(ShortSaleAffirm.Tag,             Required.NO)
  field(ShortSaleAffirmLongQuantity.Tag, Required.NO)
  field(LongSaleAffirm.Tag,              Required.NO)
  field(AllowRouting.Tag,                Required.NO)
  field(AlternateExDestination.Tag,      Required.NO)
  field(RouteToNYSE.Tag,                 Required.NO)
  field(ISO.Tag,                         Required.NO)
  field(PegType.Tag,                     Required.NO)
  field(LockedOrCrossedAction.Tag,       Required.NO)
  field(RegularSessionOnly.Tag,          Required.NO)

  def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderCancelRequest(header: MessageHeaderTrait) extends AbstractMessage(header) with OrderCancelRequestTrait {
  field(ClOrdID.Tag)
  field(OrderID.Tag,       Required.NO)
  field(OrigClOrdID.Tag)
  field(CancelAllOpen.Tag, Required.NO)

  def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderCancelReplaceRequest(header: MessageHeaderTrait) extends AbstractMessage(header) with OrderCancelReplaceRequestTrait {
  field(OrdType.Tag,           Required.NO)
  field(OrigClOrdID.Tag)
  field(OrderID.Tag,           Required.NO)
  field(ClOrdID.Tag)
  field(OrderQty.Tag,          Required.NO)
  field(Price.Tag,             Required.NO)
  field(ParticipationRate.Tag, Required.NO)

  def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class BulkCancelRequest(header: MessageHeaderTrait) extends AbstractMessage(header) with BulkCancelRequestTrait {
  field(ClOrdID.Tag)
  field(CancelPairs.Tag)

  def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class ExecutionReport(header: MessageHeaderTrait) extends AbstractMessage(header) with ExecutionReportTrait {
  field(ExecType.Tag)
  field(OrdStatus.Tag)
  field(OrderID.Tag)
  field(ClOrdID.Tag)
  field(OrigClOrdID.Tag,         Required.NO)
  field(ExecID.Tag)
  field(ExecBroker.Tag)
  field(ExecTransType.Tag)
  field(ExecRefID.Tag,           Required.NO)
  field(Symbol.Tag)
  field(SymbolSfx.Tag,           Required.NO)
  field(Side.Tag)
  field(SecurityID.Tag,          Required.NO)
  field(SecurityIDSource.Tag,    Required.NO)
  field(OrderQty.Tag)
  field(Price.Tag,               Required.NO)
  field(LastShares.Tag)
  field(LastPx.Tag)
  field(LastMkt.Tag,             Required.NO)
  field(LeavesQty.Tag)
  field(CumQty.Tag)
  field(AvgPx.Tag)
  field(TransactTime.Tag)
  field(Text.Tag,                Required.NO)
  field(Liquidity.Tag,           Required.NO)
  field(Position.Tag,            Required.NO)
  field(BuyingPower.Tag,         Required.NO)
  field(MarketConfirmPrices.Tag, Required.NO)
  field(ContraBroker.Tag,        Required.NO)
  field(NoContraBrokers.Tag,     Required.NO)
  field(ClientOrderData.Tag,     Required.NO)
  field(ExternalClOrdId.Tag,     Required.NO)
  field(OrdType.Tag,             Required.NO)

  def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderCancelReject(header: MessageHeaderTrait) extends AbstractMessage(header) with OrderCancelRejectTrait {
  field(OrderID.Tag)
  field(ClOrdID.Tag)
  field(OrigClOrdID.Tag)
  field(OrdStatus.Tag)
  field(Text.Tag,             Required.NO)
  field(CxlRejReason.Tag,     Required.NO)
  field(CxlRejResponseTo.Tag, Required.NO)
  field(TransactTime.Tag,     Required.NO)

  def apply(visitor: MessageVisitor) = visitor.visit(this)
}
