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
package stirling.fix.messages.fix42;

import stirling.fix.messages.{
  AbstractMessage,
  Allocation => AllocationTrait,
  BusinessMessageReject => BusinessMessageRejectTrait,
  DontKnowTrade => DontKnowTradeTrait,
  ExecutionReport => ExecutionReportTrait,
  Logon => LogonTrait,
  MessageHeader,
  MessageVisitor,
  NewOrderSingle => NewOrderSingleTrait,
  OrderCancelReject => OrderCancelRejectTrait,
  OrderCancelReplaceRequest => OrderCancelReplaceRequestTrait,
  OrderCancelRequest => OrderCancelRequestTrait,
  OrderStatusRequest => OrderStatusRequestTrait,
  Reject => RejectTrait,
  RepeatingGroup,
  RepeatingGroupInstance,
  Required,
  TradingSessionStatusRequest => TradingSessionStatusRequestTrait
}
import stirling.fix.tags.fix42._

class Reject(header: MessageHeader) extends AbstractMessage(header) with RejectTrait {
  field(RefSeqNo.Tag)
  field(RefTagId.Tag, Required.NO)
  field(SessionRejectReason.Tag, Required.NO)
  field(Text.Tag, Required.NO)

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class BusinessMessageReject(header: MessageHeader) extends AbstractMessage(header) with BusinessMessageRejectTrait {
  field(RefSeqNo.Tag, Required.NO)
  field(RefMsgType.Tag)
  field(Text.Tag, Required.NO)
  field(BusinessRejectReason.Tag)
  field(BusinessRejectRefID.Tag, Required.NO)

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class ExecutionReport(header: MessageHeader) extends AbstractMessage(header) with ExecutionReportTrait {
  field(OrderID.Tag)
  field(SecondaryOrderID.Tag, Required.NO)
  field(ClOrdID.Tag, Required.NO)
  field(OrigClOrdID.Tag, Required.NO)
  field(ClientID.Tag, Required.NO)
  field(ExecBroker.Tag, Required.NO)
  /* NoContraBrokers(382) */
  /* ContraBroker(375) */
  /* ContraTrader(377) */
  /* ContraTradeQty(437) */
  /* ContraTradeTime(438) */
  /* ListID(66) */
  field(ExecID.Tag)
  field(ExecTransType.Tag)
  field(ExecRefID.Tag, Required.NO)
  field(ExecType.Tag)
  field(OrdStatus.Tag)
  field(OrdRejReason.Tag, Required.NO)
  /* ExecRestatementReason(378) */
  field(Account.Tag, Required.NO)
  /* SttlmntTyp(63) */
  field(FutSettDate.Tag, Required.NO)
  field(Symbol.Tag)
  field(SymbolSfx.Tag, Required.NO)
  field(SecurityID.Tag, Required.NO)
  field(IDSource.Tag, Required.NO)
  field(SecurityType.Tag, Required.NO)
  field(MaturityMonthYear.Tag, Required.NO)
  /* MaturityDay(205) */
  field(PutOrCall.Tag, Required.NO)
  field(StrikePrice.Tag, Required.NO)
  /* OptAttribute(205) */
  field(ContractMultiplier.Tag, Required.NO)
  /* CouponRate(223) */
  field(SecurityExchange.Tag, Required.NO)
  field(Issuer.Tag, Required.NO)
  /* EncodedIssuerLen(348) */
  /* EncodedIssuer(349) */
  field(SecurityDesc.Tag, Required.NO)
  /* EncodedSecurityDescLen(350) */
  /* EncodedSecurityDesc(351) */
  field(Side.Tag)
  field(OrderQty.Tag, Required.NO)
  /* CashOrderQty(152) */
  field(OrdType.Tag, Required.NO)
  field(Price.Tag, Required.NO)
  field(StopPx.Tag, Required.NO)
  field(PegDifference.Tag, Required.NO)
  field(DiscretionInst.Tag, Required.NO)
  field(DiscretionOffset.Tag, Required.NO)
  field(Currency.Tag, Required.NO)
  field(ComplianceID.Tag, Required.NO)
  /* SolicitedFlag(377) */
  field(TimeInForce.Tag, Required.NO)
  field(EffectiveTime.Tag, Required.NO)
  /* ExpireDate(432) */
  field(ExpireTime.Tag, Required.NO)
  field(ExecInst.Tag, Required.NO)
  field(OrderCapacity.Tag, Required.NO)
  field(LastShares.Tag, Required.NO)
  field(LastPx.Tag, Required.NO)
  /* LastSpotRate(194) */
  /* LastForwardPoints(195) */
  field(LastMkt.Tag, Required.NO)
  field(TradingSessionID.Tag, Required.NO)
  field(LastCapacity.Tag, Required.NO)
  field(LeavesQty.Tag)
  field(CumQty.Tag)
  field(AvgPx.Tag)
  /* DayOrderQty(424) */
  /* DayCumQty(425) */
  /* DayAvgPx(426) */
  /* GTBookingInst(427) */
  field(TradeDate.Tag, Required.NO)
  /* ReportToExch(113) */
  field(Commission.Tag, Required.NO)
  /* CommType(13) */
  /* GrossTradeAmt(381) */
  field(SettlCurrAmt.Tag, Required.NO)
  field(SettlCurrency.Tag, Required.NO)
  /* SettlCurrFxRate(155) */
  /* SettlCurrFxRateCalc(156) */
  field(HandlInst.Tag, Required.NO)
  field(MinQty.Tag, Required.NO)
  field(MaxFloor.Tag, Required.NO)
  /* OpenClose(77) */
  field(MaxShow.Tag, Required.NO)
  field(Text.Tag, Required.NO)
  /* EncodedTextLen(354) */
  /* EncodedText(355) */
  /* FutSettDate2(193) */
  field(OrderQty2.Tag, Required.NO)
  field(ClearingFirm.Tag, Required.NO)
  field(ClearingAccount.Tag, Required.NO)
  /* MultiLegReportingType(442) */

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class Logon(header: MessageHeader) extends AbstractMessage(header) with LogonTrait {
  field(EncryptMethod.Tag)
  field(HeartBtInt.Tag)
  field(ResetSeqNumFlag.Tag, Required.NO)

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class DontKnowTrade(header: MessageHeader) extends AbstractMessage(header) with DontKnowTradeTrait {
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

class Allocation(header: MessageHeader) extends AbstractMessage(header) with AllocationTrait {
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

class NewOrderSingle(header: MessageHeader) extends AbstractMessage(header) with NewOrderSingleTrait {
  field(ClOrdID.Tag)
  field(ClientID.Tag, Required.NO)
  field(ExecBroker.Tag, Required.NO)
  field(Account.Tag, Required.NO)
  /* NoAllocs(78) */
  /* AllocAccount(79) */
  /* AllocShares(80) */
  /* SttlmntTyp(63) */
  field(FutSettDate.Tag, Required.NO)
  field(HandlInst.Tag)
  field(ExecInst.Tag, Required.NO)
  field(MinQty.Tag, Required.NO)
  field(MaxFloor.Tag, Required.NO)
  field(ExDestination.Tag, Required.NO)
  /* NoTradingSessions(386) */
  /* TradingSessionID(336) */
  /* ProcessCode(81) */
  field(Symbol.Tag)
  field(SymbolSfx.Tag, Required.NO)
  field(SecurityID.Tag, Required.NO)
  field(IDSource.Tag, Required.NO)
  field(SecurityType.Tag, Required.NO)
  field(MaturityMonthYear.Tag, Required.NO)
  /* MaturityDay(205) */
  field(PutOrCall.Tag, Required.NO)
  field(StrikePrice.Tag, Required.NO)
  /* OptAttribute(206) */
  field(ContractMultiplier.Tag, Required.NO)
  /* CouponRate(223) */
  field(SecurityExchange.Tag, Required.NO)
  field(Issuer.Tag, Required.NO)
  /* EncodedIssuerLen(348) */
  /* EncodedIssuer(349) */
  field(SecurityDesc.Tag, Required.NO)
  /* EncodedSecurityDescLen(350) */
  /* EncodedSecurityDesc(351) */
  /* PrevClosePx(140) */
  field(Side.Tag, Required.NO)
  field(LocateReqd.Tag, Required.NO)
  field(TransactTime.Tag)
  field(OrderQty.Tag, Required.NO)
  /* CashOrderQty(152) */
  field(OrdType.Tag)
  field(Price.Tag, Required.NO)
  field(StopPx.Tag, Required.NO)
  field(Currency.Tag, Required.NO)
  field(ComplianceID.Tag, Required.NO)
  /* SolicitedFlag(377) */
  /* IOIid(23) */
  field(QuoteID.Tag, Required.NO)
  field(TimeInForce.Tag, Required.NO)
  field(EffectiveTime.Tag, Required.NO)
  /* ExpireDate(432) */
  field(ExpireTime.Tag, Required.NO)
  /* GTBookingInst(427) */
  field(Commission.Tag, Required.NO)
  /* CommType(13) */
  field(OrderCapacity.Tag, Required.NO)
  /* ForexReq(121) */
  field(SettlCurrency.Tag, Required.NO)
  field(Text.Tag, Required.NO)
  /* EncodedTextLen(354) */
  /* EncodedText(355) */
  /* FutSettDate2(193) */
  field(OrderQty2.Tag, Required.NO)
  /* OpenClose(77) */
  /* CoveredOrUncovered(203) */
  field(CustomerOrFirm.Tag, Required.NO)
  field(MaxShow.Tag, Required.NO)
  field(PegDifference.Tag, Required.NO)
  field(DiscretionInst.Tag, Required.NO)
  field(DiscretionOffset.Tag, Required.NO)
  field(ClearingFirm.Tag, Required.NO)
  field(ClearingAccount.Tag, Required.NO)

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderCancelReject(header: MessageHeader) extends AbstractMessage(header) with OrderCancelRejectTrait {
  field(OrderID.Tag)
  field(ClOrdID.Tag)
  field(OrigClOrdID.Tag)
  field(OrdStatus.Tag)
  field(CxlRejResponseTo.Tag)
  field(CxlRejReason.Tag, Required.NO)
  field(Text.Tag, Required.NO)

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderCancelRequest(header: MessageHeader) extends AbstractMessage(header) with OrderCancelRequestTrait {
  field(OrigClOrdID.Tag)
  field(OrderID.Tag, Required.NO)
  field(ClOrdID.Tag)
  /* ListID(66) */
  field(Account.Tag, Required.NO)
  field(ClientID.Tag, Required.NO)
  field(ExecBroker.Tag, Required.NO)
  field(Symbol.Tag)
  field(SymbolSfx.Tag, Required.NO)
  field(SecurityID.Tag, Required.NO)
  field(IDSource.Tag, Required.NO)
  field(SecurityType.Tag, Required.NO)
  field(MaturityMonthYear.Tag, Required.NO)
  /* MaturityDay(205) */
  field(PutOrCall.Tag, Required.NO)
  field(StrikePrice.Tag, Required.NO)
  /* OptAttribute(206) */
  field(ContractMultiplier.Tag, Required.NO)
  /* CouponRate(223) */
  field(SecurityExchange.Tag, Required.NO)
  field(Issuer.Tag, Required.NO)
  /* EncodedIssuerLen(348) */
  /* EncodedIssuer(349) */
  field(SecurityDesc.Tag, Required.NO)
  /* EncodedSecurityDescLen(350) */
  /* EncodedSecurityDesc(351) */
  field(Side.Tag)
  field(TransactTime.Tag)
  field(OrderQty.Tag, Required.NO)
  /* CashOrderQty(152) */
  field(ComplianceID.Tag, Required.NO)
  /* SolicitedFlag(377) */
  field(Text.Tag, Required.NO)
  /* EncodedTextLen(354) */
  /* EncodedText(355) */

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderCancelReplaceRequest(header: MessageHeader) extends AbstractMessage(header) with OrderCancelReplaceRequestTrait {
  field(OrderID.Tag, Required.NO)
  field(ClientID.Tag, Required.NO)
  field(ExecBroker.Tag, Required.NO)
  field(OrigClOrdID.Tag)
  field(ClOrdID.Tag)
  /* ListID(66) */
  field(Account.Tag, Required.NO)
  /* NoAllocs(78) */
  /* AllocAccount(79) */
  /* AllocShares(80) */
  /* SttlmntTyp(63) */
  field(FutSettDate.Tag, Required.NO)
  field(HandlInst.Tag)
  field(ExecInst.Tag, Required.NO)
  field(MinQty.Tag, Required.NO)
  field(MaxFloor.Tag, Required.NO)
  field(ExDestination.Tag, Required.NO)
  /* NoTradingSessions(386) */
  /* TradingSessionID(336) */
  field(Symbol.Tag)
  field(SymbolSfx.Tag, Required.NO)
  field(SecurityID.Tag, Required.NO)
  field(IDSource.Tag, Required.NO)
  field(SecurityType.Tag, Required.NO)
  field(MaturityMonthYear.Tag, Required.NO)
  /* MaturityDay(205) */
  field(PutOrCall.Tag, Required.NO)
  field(StrikePrice.Tag, Required.NO)
  /* OptAttribute(206) */
  field(ContractMultiplier.Tag, Required.NO)
  /* CouponRate(223) */
  field(SecurityExchange.Tag, Required.NO)
  field(Issuer.Tag, Required.NO)
  /* EncodedIssuerLen(348) */
  /* EncodedIssuer(349) */
  field(SecurityDesc.Tag, Required.NO)
  /* EncodedSecurityDescLen(350) */
  /* EncodedSecurityDesc(351) */
  field(Side.Tag)
  field(TransactTime.Tag)
  field(OrderQty.Tag, Required.NO)
  /* CashOrderQty(152) */
  field(OrdType.Tag)
  field(Price.Tag, Required.NO)
  field(StopPx.Tag, Required.NO)
  field(PegDifference.Tag, Required.NO)
  field(DiscretionInst.Tag, Required.NO)
  field(DiscretionOffset.Tag, Required.NO)
  field(ComplianceID.Tag, Required.NO)
  /* SolicitedFlag(377) */
  field(Currency.Tag, Required.NO)
  field(TimeInForce.Tag, Required.NO)
  field(EffectiveTime.Tag, Required.NO)
  /* ExpireDate(432) */
  field(ExpireTime.Tag, Required.NO)
  /* GTBookingInst(427) */
  field(Commission.Tag, Required.NO)
  /* CommType(13) */
  field(OrderCapacity.Tag, Required.NO)
  field(SettlCurrency.Tag, Required.NO)
  field(Text.Tag, Required.NO)
  /* EncodedTextLen(354) */
  /* EncodedText(355) */
  /* FutSettDate2(193) */
  field(OrderQty2.Tag, Required.NO)
  /* OpenClose(77) */
  /* CoveredOrUncovered(203) */
  field(CustomerOrFirm.Tag, Required.NO)
  field(MaxShow.Tag, Required.NO)
  field(LocateReqd.Tag, Required.NO)
  field(ClearingFirm.Tag, Required.NO)
  field(ClearingAccount.Tag, Required.NO)

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderStatusRequest(header: MessageHeader) extends AbstractMessage(header) with OrderStatusRequestTrait {
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

class TradingSessionStatusRequest(header: MessageHeader) extends AbstractMessage(header) with TradingSessionStatusRequestTrait {
  field(TradSesReqID.Tag)
  field(TradingSessionID.Tag)
  field(TradSesMethod.Tag)
  field(TradSesMode.Tag)
  field(SubscriptionRequestType.Tag)

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
