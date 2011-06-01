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
package fixengine.messages.fix42;

import fixengine.messages.{
  AbstractMessage,
  BusinessMessageReject => BusinessMessageRejectTrait,
  MessageHeader,
  MessageVisitor,
  RepeatingGroup,
  RepeatingGroupInstance,
  Required
}
import fixengine.tags.fix42.{
  Account,
  AllocAccount,
  AllocID,
  AllocShares,
  AllocTransType,
  AvgPx,
  BusinessRejectReason,
  BusinessRejectRefID,
  ClOrdID,
  ClientID,
  CumQty,
  Currency,
  CustomerOrFirm,
  CxlRejReason,
  CxlRejResponseTo,
  DKReason,
  EncryptMethod,
  ExDestination,
  ExecBroker,
  ExecID,
  ExecTransType,
  ExecType,
  HandlInst,
  HeartBtInt,
  IDSource,
  LastMkt,
  LastPx,
  LastShares,
  LeavesQty,
  MaturityMonthYear,
  NoAllocs,
  NoOrders,
  OrdRejReason,
  OrdStatus,
  OrdType,
  OrderID,
  OrderQty,
  OrigClOrdID,
  Price,
  RefMsgType,
  RefSeqNo,
  ResetSeqNumFlag,
  SecurityExchange,
  SecurityID,
  SecurityType,
  Shares,
  Side,
  SubscriptionRequestType,
  Symbol,
  Text,
  TimeInForce,
  TradSesMethod,
  TradSesMode,
  TradSesReqID,
  TradeDate,
  TradingSessionID,
  TransactTime
}

class BusinessMessageReject(header: MessageHeader) extends AbstractMessage(header) with BusinessMessageRejectTrait {
  field(RefSeqNo.Tag, Required.NO)
  field(RefMsgType.Tag)
  field(Text.Tag, Required.NO)
  field(BusinessRejectReason.Tag)
  field(BusinessRejectRefID.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class ExecutionReport(header: MessageHeader) extends AbstractMessage(header) with fixengine.messages.ExecutionReport {
  field(OrderID.Tag)
  field(ClOrdID.Tag, Required.NO)
  field(OrigClOrdID.Tag, Required.NO)
  field(ExecID.Tag)
  field(ExecTransType.Tag)
  field(ExecType.Tag)
  field(OrdStatus.Tag)
  field(OrdRejReason.Tag, Required.NO)
  field(Symbol.Tag)
  field(SecurityType.Tag, Required.NO)
  field(MaturityMonthYear.Tag, Required.NO)
  field(Side.Tag)
  field(OrderQty.Tag)
  field(LastShares.Tag, Required.NO)
  field(LastPx.Tag, Required.NO)
  field(LeavesQty.Tag)
  field(OrdType.Tag, Required.NO)
  field(Price.Tag, new Required {
    override def isRequired = OrdType.Limit.equals(getEnum(OrdType.Tag))
  })
  field(TimeInForce.Tag, Required.NO)
  field(CumQty.Tag)
  field(AvgPx.Tag)
  field(TransactTime.Tag, Required.NO)
  field(Text.Tag, Required.NO)
  field(ExDestination.Tag, Required.NO)
  field(LastMkt.Tag, Required.NO)
  field(Currency.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class Logon(header: MessageHeader) extends AbstractMessage(header) with fixengine.messages.Logon {
  field(EncryptMethod.Tag)
  field(HeartBtInt.Tag)
  field(ResetSeqNumFlag.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class DontKnowTrade(header: MessageHeader) extends AbstractMessage(header) with fixengine.messages.DontKnowTrade {
  field(OrderID.Tag)
  field(ExecID.Tag)
  field(DKReason.Tag)
  field(Symbol.Tag)
  /* SymbolSfx(65) */
  field(SecurityID.Tag, Required.NO)
  field(IDSource.Tag, Required.NO)
  field(SecurityType.Tag, Required.NO)
  field(MaturityMonthYear.Tag, Required.NO)
  /* MaturityDay(205) */
  /* PutOrCall(201) */
  /* StrikePrice(202) */
  /* OptAttribute(206) */
  /* ContractMultiplier(201) */
  /* CouponRate(223) */
  field(SecurityExchange.Tag, Required.NO)
  /* Issuer(106) */
  /* EncodedIssuerLen(348) */
  /* EncodedIssuer(349) */
  /* SecurityDesc(107) */
  /* EncodedSecurityDescLen(350) */
  /* EncodedSecurityDesc(351) */
  field(Side.Tag)
  field(OrderQty.Tag, Required.NO)
  /* CashOrderQty(152) */
  field(LastShares.Tag, Required.NO)
  field(LastPx.Tag, Required.NO)
  field(Text.Tag, Required.NO)
  /* EncodedTextLen(354) */
  /* EncodedText(355) */
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class Allocation(header: MessageHeader) extends AbstractMessage(header) with fixengine.messages.Allocation {
  field(AllocID.Tag)
  field(AllocTransType.Tag)
  field(NoOrders.Tag);
  field(ClOrdID.Tag)
  field(Side.Tag)
  field(Symbol.Tag)
  field(Shares.Tag)
  field(AvgPx.Tag)
  field(TradeDate.Tag)
  group(new RepeatingGroup(NoAllocs.Tag) {
    override def newInstance:RepeatingGroupInstance = {
      return new RepeatingGroupInstance(AllocAccount.Tag) {
        field(AllocShares.Tag)
      }
    }
  }, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class NewOrderSingle(header: MessageHeader) extends AbstractMessage(header) with fixengine.messages.NewOrderSingle {
  field(ClOrdID.Tag)
  field(Currency.Tag, Required.NO)
  field(HandlInst.Tag)
  field(ExDestination.Tag)
  field(Side.Tag)
  field(TransactTime.Tag)
  field(OrdType.Tag)
  field(Symbol.Tag)
  field(SecurityType.Tag, Required.NO)
  field(MaturityMonthYear.Tag, Required.NO)
  field(OrderQty.Tag)
  field(CustomerOrFirm.Tag, Required.NO)
  field(Price.Tag, Required.NO)
  field(TimeInForce.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderCancelReject(header: MessageHeader) extends AbstractMessage(header) with fixengine.messages.OrderCancelReject {
  field(OrderID.Tag)
  field(ClOrdID.Tag)
  field(OrigClOrdID.Tag)
  field(OrdStatus.Tag)
  field(CxlRejResponseTo.Tag)
  field(CxlRejReason.Tag, Required.NO)
  field(Text.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderCancelRequest(header: MessageHeader) extends AbstractMessage(header) with fixengine.messages.OrderCancelRequest {
  field(OrigClOrdID.Tag)
  field(ClOrdID.Tag)
  field(Symbol.Tag)
  field(SecurityType.Tag, Required.NO)
  field(MaturityMonthYear.Tag, Required.NO)
  field(Side.Tag)
  field(TransactTime.Tag)
  field(OrderQty.Tag)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderModificationRequest(header: MessageHeader) extends AbstractMessage(header) with fixengine.messages.OrderModificationRequest {
  field(OrigClOrdID.Tag)
  field(ClOrdID.Tag)
  field(HandlInst.Tag)
  field(Symbol.Tag)
  field(SecurityType.Tag, Required.NO)
  field(MaturityMonthYear.Tag, Required.NO)
  field(Side.Tag)
  field(TransactTime.Tag)
  field(OrderQty.Tag)
  field(OrdType.Tag)
  field(Currency.Tag, Required.NO)
  field(Price.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderStatusRequest(header: MessageHeader) extends AbstractMessage(header) with fixengine.messages.OrderStatusRequest {
  field(OrderID.Tag, Required.NO)
  field(ClOrdID.Tag)
  field(ClientID.Tag, Required.NO)
  field(Account.Tag, Required.NO)
  field(ExecBroker.Tag, Required.NO)
  field(Symbol.Tag)
  /* SymbolSfx(65) */
  field(SecurityID.Tag, Required.NO)
  field(IDSource.Tag, Required.NO)
  field(SecurityType.Tag, Required.NO)
  field(MaturityMonthYear.Tag, Required.NO)
  /* MaturityDay(205) */
  /* PutOrCall(201) */
  /* StrikePrice(202) */
  /* OptAttribute(206) */
  /* ContractMultiplier(201) */
  /* CouponRate(223) */
  field(SecurityExchange.Tag, Required.NO)
  /* Issuer(106) */
  /* EncodedIssuerLen(348) */
  /* EncodedIssuer(349) */
  /* SecurityDesc(107) */
  /* EncodedSecurityDescLen(350) */
  /* EncodedSecurityDesc(351) */
  field(Side.Tag)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class TradingSessionStatusRequest(header: MessageHeader) extends AbstractMessage(header) with fixengine.messages.TradingSessionStatusRequest {
  field(TradSesReqID.Tag)
  field(TradingSessionID.Tag)
  field(TradSesMethod.Tag)
  field(TradSesMode.Tag)
  field(SubscriptionRequestType.Tag)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
