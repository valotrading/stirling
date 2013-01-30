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
package stirling.fix.messages.fix44.mbtrading

import stirling.fix.messages.{
  BusinessMessageReject => BusinessMessageRejectTrait,
  CollateralInquiry => CollateralInquiryTrait,
  CollateralInquiryAcknowledgment => CollateralInquiryAcknowledgmentTrait,
  CollateralReport => CollateralReportMessageTrait,
  ExecutionReport => ExecutionReportTrait,
  Logon => LogonTrait,
  NewOrderMultiLeg => NewOrderMultiLegTrait,
  NewOrderSingle => NewOrderSingleTrait,
  NewsMessage => NewsMessageTrait,
  OrderCancelReplaceRequest => OrderCancelReplaceRequestTrait,
  OrderCancelReject => OrderCancelRejectTrait,
  OrderCancelRequest => OrderCancelRequestTrait,
  PositionReport => PositionReportTrait,
  RequestForPositionAcknowledgment => RequestForPositionAcknowledgmentTrait,
  RequestForPositions => RequestForPositionsTrait,
  TradingSessionStatus => TradingSessionStatusTrait
}
import stirling.fix.messages.{
  AbstractMessage,
  MessageHeader,
  MessageVisitor,
  RepeatingGroup,
  RepeatingGroupInstance,
  Required
}
import stirling.fix.tags.fix42.{
  Account,
  AvgPx,
  BusinessRejectRefID,
  ClOrdID,
  ClientID,
  Commission,
  ComplianceID,
  CumQty,
  Currency,
  CxlRejReason,
  CxlRejResponseTo,
  DiscretionInst,
  DiscretionOffset,
  EffectiveTime,
  EncryptMethod,
  ExDestination,
  ExecID,
  ExecInst,
  ExpireTime,
  HandlInst,
  Headline,
  HeartBtInt,
  LastPx,
  LastShares,
  LeavesQty,
  LinesOfText,
  LocateReqd,
  MaturityMonthYear,
  MaxFloor,
  OrdRejReason,
  OrdStatus,
  OrdType,
  OrderID,
  OrderQty,
  OrigClOrdID,
  OrigTime,
  PegDifference,
  Price,
  PutOrCall,
  RefMsgType,
  RefSeqNo,
  ResetSeqNumFlag,
  SecondaryOrderID,
  SecurityType,
  SenderLocationID,
  SendingTime,
  Side,
  StopPx,
  StrikePrice,
  SubscriptionRequestType,
  Symbol,
  SymbolSfx,
  Text,
  TradeDate,
  TradeSesReqID,
  TradingSessionID,
  TransactTime,
  UnderlyingSymbol,
  UnsolicitedIndicator,
  Urgency
}
import stirling.fix.tags.fix43.{
  BusinessRejectReason,
  ClOrdLinkID,
  LegCFICode,
  LegMaturityMonthYear,
  LegPositionEffect,
  LegPrice,
  LegProduct,
  LegRatioQty,
  LegRefID,
  LegSide,
  LegStrikePrice,
  LegSymbol,
  MassStatusReqID,
  MultiLegReportingType,
  NoLegs,
  OrderRestrictions,
  PositionEffect,
  Price2,
  Product,
  SecondaryClOrdID,
  TradSesStatus,
  TradingSessionSubID
}
import stirling.fix.tags.fix44.{
  ClearingBusinessDate,
  CollInquiryID,
  CollInquiryResult,
  CollInquiryStatus,
  CollRptID,
  CollStatus,
  LastRptRequested,
  LongQty,
  MarginExcess,
  MarginRatio,
  MessageEncoding,
  NoTrdRegTimestamps,
  Password,
  PeggedPrice,
  PosMaintRptID,
  PosReqID,
  PosReqResult,
  SettlPrice,
  ShortQty,
  TargetStrategy,
  TotNumReports,
  TotalNetValue,
  TotalNumPosReports,
  TrdRegTimestamp,
  TrdRegTimestampOrigin,
  TrdRegTimestampType,
  Username
}
import stirling.fix.tags.fix44.mbtrading.{
  AccountBank,
  AccountBasedPerms,
  AccountBranch,
  AccountCredit,
  BODOOvernightExcessEq,
  CompanyID,
  ExecType,
  FLID,
  LiquidityTag,
  MBTAccountType,
  MBTInternalOrderId,
  MBTMultifunction,
  MBTXAggressive,
  MorningAccountValue,
  MorningBuyingPower,
  MorningExcessEquity,
  MorningExcessEquity2,
  MultiPrice,
  MultiSymbol,
  OptionStrategyCode,
  OrderGroupID1,
  OvernightBuyingPower,
  OvernightExcess,
  PosBuyPowerUsed,
  PosEquityUsed,
  PosPendBuy,
  PosPendSell,
  PosRealizedPNL,
  PosReqType,
  RealizedPnL,
  TimeInForce,
  TodayRealizedPNL2,
  TriggerFromOrderID,
  UserQuotePerms,
  UserRoutePerm,
  UserSessionID
}

class CollateralInquiry(header: MessageHeader) extends AbstractMessage(header) with CollateralInquiryTrait {
  field(Account.Tag, Required.NO)
  field(SenderLocationID.Tag, Required.NO)
  field(SubscriptionRequestType.Tag, Required.NO)
  field(MessageEncoding.Tag, Required.NO)
  field(Username.Tag, Required.NO)
  field(Password.Tag, Required.NO)
  field(CollInquiryID.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class NewOrderMultiLeg(header: MessageHeader) extends AbstractMessage(header) with NewOrderMultiLegTrait {
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

class NewOrderSingle(header: MessageHeader) extends AbstractMessage(header) with NewOrderSingleTrait {
  field(Account.Tag)
  field(ClOrdID.Tag)
  field(ExecInst.Tag, Required.NO)
  field(HandlInst.Tag)
  field(OrderQty.Tag)
  field(OrdType.Tag)
  field(Price.Tag, Required.NO)
  field(SendingTime.Tag)
  field(Side.Tag)
  field(Symbol.Tag)
  field(TimeInForce.Tag)
  field(TransactTime.Tag)
  field(StopPx.Tag, Required.NO)
  field(ExDestination.Tag)
  field(MaxFloor.Tag, Required.NO)
  field(LocateReqd.Tag, Required.NO)
  field(ExpireTime.Tag, new Required() {
    override def isRequired() = {
      getEnum(TimeInForce.Tag).equals(TimeInForce.GoodTillDate)
    }
  })
  field(SecurityType.Tag, Required.NO)
  field(EffectiveTime.Tag, Required.NO)
  field(MaturityMonthYear.Tag, Required.NO)
  field(PutOrCall.Tag, Required.NO)
  field(StrikePrice.Tag, Required.NO)
  field(PegDifference.Tag, Required.NO)
  field(ComplianceID.Tag, Required.NO)
  field(DiscretionInst.Tag, Required.NO)
  field(DiscretionOffset.Tag, Required.NO)
  field(SecondaryClOrdID.Tag, Required.NO)
  field(OrderRestrictions.Tag, Required.NO)
  field(Username.Tag)
  field(LegPrice.Tag, Required.NO)
  field(ClOrdLinkID.Tag, Required.NO)
  field(Price2.Tag, Required.NO)
  field(NoTrdRegTimestamps.Tag, Required.NO)
  field(TrdRegTimestamp.Tag, Required.NO)
  field(TrdRegTimestampType.Tag, Required.NO)
  field(TrdRegTimestampOrigin.Tag, Required.NO)
  field(PeggedPrice.Tag, Required.NO)
  field(TargetStrategy.Tag, Required.NO)
  field(MBTXAggressive.Tag, Required.NO)
  field(OrderGroupID1.Tag, Required.NO)
  field(MultiSymbol.Tag, Required.NO)
  field(MultiPrice.Tag, Required.NO)
  field(MBTMultifunction.Tag, Required.NO)
  field(TriggerFromOrderID.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderCancelRequest(header: MessageHeader) extends AbstractMessage(header) with OrderCancelRequestTrait {
  field(Account.Tag)
  field(ClOrdID.Tag)
  field(OrigClOrdID.Tag)
  field(Side.Tag)
  field(Symbol.Tag)
  field(TransactTime.Tag)
  field(Username.Tag)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderCancelReplaceRequest(header: MessageHeader) extends AbstractMessage(header) with OrderCancelReplaceRequestTrait {
  field(Account.Tag)
  field(ClOrdID.Tag)
  field(HandlInst.Tag)
  field(OrderQty.Tag)
  field(OrdType.Tag)
  field(OrigClOrdID.Tag)
  field(Price.Tag)
  field(Side.Tag)
  field(Symbol.Tag)
  field(TimeInForce.Tag)
  field(TransactTime.Tag)
  field(Username.Tag)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class ExecutionReport(header: MessageHeader) extends AbstractMessage(header) with ExecutionReportTrait {
  field(Account.Tag)
  field(AvgPx.Tag)
  field(ClOrdID.Tag)
  field(Commission.Tag, Required.NO)
  field(CumQty.Tag)
  field(ExecID.Tag)
  field(ExecInst.Tag, Required.NO)
  field(LastPx.Tag, Required.NO)
  field(LastShares.Tag, Required.NO)
  field(OrderID.Tag)
  field(OrderQty.Tag, Required.NO)
  field(OrdStatus.Tag)
  field(OrdType.Tag, Required.NO)
  field(OrigClOrdID.Tag, Required.NO)
  field(Price.Tag, Required.NO)
  field(Side.Tag)
  field(Symbol.Tag)
  field(Text.Tag, Required.NO)
  field(TimeInForce.Tag, Required.NO)
  field(TransactTime.Tag, Required.NO)
  field(PositionEffect.Tag, Required.NO)
  field(StopPx.Tag, Required.NO)
  field(ExDestination.Tag, Required.NO)
  field(OrdRejReason.Tag, Required.NO)
  field(ClientID.Tag)
  field(MaxFloor.Tag, Required.NO)
  field(ExpireTime.Tag, Required.NO)
  field(ExecType.Tag)
  field(LeavesQty.Tag)
  field(SecurityType.Tag, Required.NO)
  field(EffectiveTime.Tag, Required.NO)
  field(SecondaryOrderID.Tag, Required.NO)
  field(UnderlyingSymbol.Tag, Required.NO)
  field(ComplianceID.Tag, Required.NO)
  field(DiscretionInst.Tag, Required.NO)
  field(DiscretionOffset.Tag, Required.NO)
  field(MultiLegReportingType.Tag, Required.NO)
  field(Product.Tag, Required.NO)
  field(OrderRestrictions.Tag, Required.NO)
  group(new RepeatingGroup(NoLegs.Tag) {
    override def newInstance:RepeatingGroupInstance = {
      return new RepeatingGroupInstance(LegPrice.Tag) {
        field(LegSymbol.Tag, Required.NO)
        field(LegProduct.Tag, Required.NO)
        field(LegStrikePrice.Tag, Required.NO)
        field(LegRatioQty.Tag, Required.NO)
        field(LegSide.Tag, Required.NO)
        field(LegRefID.Tag, Required.NO)
      }
    }
  }, Required.NO)
  field(ClOrdLinkID.Tag, Required.NO)
  field(MassStatusReqID.Tag, Required.NO)
  field(Price2.Tag, Required.NO)
  field(PeggedPrice.Tag, Required.NO)
  field(LastRptRequested.Tag, Required.NO)
  field(LiquidityTag.Tag, Required.NO)
  field(PosRealizedPNL.Tag, Required.NO)
  field(MBTInternalOrderId.Tag, Required.NO)
  field(OrderGroupID1.Tag, Required.NO)
  field(TriggerFromOrderID.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class TradingSessionStatus(header: MessageHeader) extends AbstractMessage(header) with TradingSessionStatusTrait {
  field(TradeSesReqID.Tag, Required.NO)
  field(TradingSessionID.Tag, Required.NO)
  field(TradSesStatus.Tag, Required.NO)
  field(TradingSessionSubID.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class RequestForPositions(header: MessageHeader) extends AbstractMessage(header) with RequestForPositionsTrait {
  field(Account.Tag, Required.NO)
  field(SubscriptionRequestType.Tag, Required.NO)
  field(MessageEncoding.Tag, Required.NO)
  field(Username.Tag, Required.NO)
  field(Password.Tag, Required.NO)
  field(PosReqID.Tag)
  field(PosReqType.Tag)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class News(header: MessageHeader) extends AbstractMessage(header) with NewsMessageTrait {
  field(LinesOfText.Tag)
  field(OrigTime.Tag)
  field(Text.Tag)
  field(Urgency.Tag)
  field(Headline.Tag)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class Logon(header: MessageHeader) extends AbstractMessage(header) with stirling.fix.messages.Logon {
  field(EncryptMethod.Tag)
  field(HeartBtInt.Tag)
  field(ResetSeqNumFlag.Tag, Required.NO)
  field(MessageEncoding.Tag, Required.NO)
  field(Password.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class BusinessMessageReject(header: MessageHeader) extends AbstractMessage(header) with BusinessMessageRejectTrait {
  field(RefSeqNo.Tag)
  field(Text.Tag)
  field(RefMsgType.Tag)
  field(BusinessRejectRefID.Tag, Required.NO)
  field(BusinessRejectReason.Tag)
  field(PosReqID.Tag)
  field(CollInquiryID.Tag)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class CollateralReport(header: MessageHeader) extends AbstractMessage(header) with CollateralReportMessageTrait {
  field(Account.Tag)
  field(Commission.Tag, Required.NO)
  field(Currency.Tag, Required.NO)
  field(MarginRatio.Tag, Required.NO)
  field(MarginExcess.Tag)
  field(TotalNetValue.Tag)
  field(CollRptID.Tag)
  field(CollInquiryID.Tag, Required.NO)
  field(CollStatus.Tag)
  field(MorningBuyingPower.Tag)
  field(MBTAccountType.Tag)
  field(OvernightBuyingPower.Tag, Required.NO)
  field(RealizedPnL.Tag, Required.NO)
  field(MorningAccountValue.Tag, Required.NO)
  field(MorningExcessEquity.Tag, Required.NO)
  field(AccountCredit.Tag, Required.NO)
  field(AccountBasedPerms.Tag, Required.NO)
  field(MorningExcessEquity2.Tag, Required.NO)
  field(OvernightExcess.Tag, Required.NO)
  field(BODOOvernightExcessEq.Tag, Required.NO)
  field(CompanyID.Tag, Required.NO)
  field(AccountBank.Tag, Required.NO)
  field(AccountBranch.Tag, Required.NO)
  field(FLID.Tag, Required.NO)
  field(UserSessionID.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class CollateralInquiryAcknowledgment(header: MessageHeader) extends AbstractMessage(header) with CollateralInquiryAcknowledgmentTrait {
  field(CollInquiryID.Tag, Required.NO)
  field(TotNumReports.Tag)
  field(CollInquiryStatus.Tag)
  field(CollInquiryResult.Tag, Required.NO)
  field(UserSessionID.Tag, Required.NO)
  field(UserQuotePerms.Tag, Required.NO)
  field(UserRoutePerm.Tag)
  field(CompanyID.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class RequestForPositionAcknowledgment(header: MessageHeader) extends AbstractMessage(header) with RequestForPositionAcknowledgmentTrait {
  field(Account.Tag)
  field(SendingTime.Tag)
  field(Text.Tag)
  field(PosReqID.Tag)
  field(PosMaintRptID.Tag)
  field(TotalNumPosReports.Tag)
  field(PosReqResult.Tag)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class PositionReport(header: MessageHeader) extends AbstractMessage(header) with PositionReportTrait {
  field(Account.Tag)
  field(Commission.Tag, Required.NO)
  field(Side.Tag, Required.NO)
  field(Symbol.Tag, Required.NO)
  field(TradeDate.Tag, Required.NO)
  field(MaturityMonthYear.Tag, Required.NO)
  field(PutOrCall.Tag, Required.NO)
  field(StrikePrice.Tag, Required.NO)
  field(SubscriptionRequestType.Tag, Required.NO)
  field(UnsolicitedIndicator.Tag, Required.NO)
  field(Price2.Tag, Required.NO)
  field(LongQty.Tag, Required.NO)
  field(ShortQty.Tag, Required.NO)
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
  field(OptionStrategyCode.Tag, Required.NO)
  field(TodayRealizedPNL2.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderCancelReject(header: MessageHeader) extends AbstractMessage(header) with OrderCancelRejectTrait {
  field(Account.Tag, Required.NO)
  field(ClOrdID.Tag)
  field(OrderID.Tag)
  field(OrdStatus.Tag)
  field(OrigClOrdID.Tag)
  field(Text.Tag)
  field(SymbolSfx.Tag)
  field(CxlRejReason.Tag)
  field(CxlRejResponseTo.Tag)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
