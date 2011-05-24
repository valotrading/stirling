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
  CollateralInquiry => CollateralInquiryTrait,
  LogonMessage => LogonTrait,
  NewOrderMultiLegMessage => NewOrderMultiLegTrait,
  NewOrderSingleMessage => NewOrderSingleTrait,
  NewsMessage => NewsMessageTrait,
  OrderCancelRequestMessage => OrderCancelRequestTrait,
  OrderModificationRequestMessage => OrderModificationRequestTrait,
  RequestForPositionsMessage => RequestForPositionsTrait,
  TradingSessionStatusMessage => TradingSessionStatusTrait
}
import fixengine.messages.{
  AbstractMessage,
  MessageHeader,
  MessageVisitor,
  RepeatingGroup,
  RepeatingGroupInstance,
  Required
}
import fixengine.tags.fix42.{
  Account,
  ClOrdID,
  Commission,
  ComplianceID,
  DiscretionInst,
  DiscretionOffset,
  EffectiveTime,
  EncryptMethod,
  ExDestination,
  ExecInst,
  ExpireTime,
  HandlInst,
  Headline,
  HeartBtInt,
  LinesOfText,
  LocateReqd,
  MaturityMonthYear,
  MaxFloor,
  OrdType,
  OrderQty,
  OrigClOrdID,
  OrigTime,
  PegDifference,
  Price,
  PutOrCall,
  ResetSeqNumFlag,
  SecurityType,
  SendingTime,
  Side,
  StopPx,
  StrikePrice,
  SubscriptionRequestType,
  Symbol,
  Text,
  TimeInForce,
  TradeDate,
  TradeSesReqID,
  TradingSessionID,
  TransactTime,
  UnsolicitedIndicator,
  Urgency
}
import fixengine.tags.fix43.{
  LegCFICode,
  LegMaturityMonthYear,
  LegPositionEffect,
  LegPrice,
  LegRatioQty,
  LegRefID,
  LegSide,
  LegStrikePrice,
  LegSymbol,
  NoLegs,
  OrderRestrictions,
  Price2,
  SecondaryClOrdID,
  TradSesStatus,
  TradingSessionSubID
}
import fixengine.tags.fix44.{
  ClearingBusinessDate,
  CollInquiryID,
  LongQty,
  MessageEncoding,
  NoTrdRegTimestamps,
  Password,
  PosMaintRptID,
  PosReqID,
  PosReqResult,
  SettlPrice,
  ShortQty,
  TotalNumPosReports,
  TrdRegTimestamp,
  TrdRegTimestampOrigin,
  TrdRegTimestampType,
  Username
}
import fixengine.tags.fix44.mbtrading.{
  MBTXAggressive,
  OrderGroupID1,
  PosBuyPowerUsed,
  PosEquityUsed,
  PosPendBuy,
  PosPendSell,
  PosRealizedPNL
}

class CollateralInquiry(header: MessageHeader) extends AbstractMessage(header) with CollateralInquiryTrait {
  field(Account.Tag)
  field(SubscriptionRequestType.Tag)
  field(Username.Tag)
  field(Password.Tag)
  field(CollInquiryID.Tag)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class Logon(header: MessageHeader) extends AbstractMessage(header) with LogonTrait {
  field(EncryptMethod.Tag)
  field(HeartBtInt.Tag)
  field(ResetSeqNumFlag.Tag, Required.NO)
  field(Password.Tag)
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
  field(StopPx.Tag, Required.NO)
  field(SendingTime.Tag)
  field(Symbol.Tag)
  field(Side.Tag)
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
  field(DiscretionInst.Tag, Required.NO)
  field(DiscretionOffset.Tag, Required.NO)
  field(ComplianceID.Tag, Required.NO)
  field(SecondaryClOrdID.Tag, Required.NO)
  field(OrderRestrictions.Tag, Required.NO)
  field(Username.Tag)
  field(LegPrice.Tag, Required.NO)
  field(Price2.Tag, Required.NO)
  field(NoTrdRegTimestamps.Tag, Required.NO)
  field(TrdRegTimestamp.Tag, Required.NO)
  field(TrdRegTimestampType.Tag, Required.NO)
  field(TrdRegTimestampOrigin.Tag, Required.NO)
  field(MBTXAggressive.Tag, Required.NO)
  field(OrderGroupID1.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderCancelRequest(header: MessageHeader) extends AbstractMessage(header) with OrderCancelRequestTrait {
  field(ClOrdID.Tag)
  field(OrigClOrdID.Tag)
  field(Side.Tag)
  field(Symbol.Tag)
  field(TransactTime.Tag)
  field(Username.Tag)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderModificationRequest(header: MessageHeader) extends AbstractMessage(header) with OrderModificationRequestTrait {
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

class TradingSessionStatus(header: MessageHeader) extends AbstractMessage(header) with TradingSessionStatusTrait {
  field(TradeSesReqID.Tag, Required.NO)
  field(TradingSessionID.Tag, Required.NO)
  field(TradSesStatus.Tag, Required.NO)
  field(TradingSessionSubID.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class RequestForPositions(header: MessageHeader) extends AbstractMessage(header) with RequestForPositionsTrait {
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

class NewsMessage(header: MessageHeader) extends AbstractMessage(header) with NewsMessageTrait {
  field(LinesOfText.Tag)
  field(OrigTime.Tag)
  field(Text.Tag)
  field(Urgency.Tag)
  field(Headline.Tag)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class LogonMessage(header: MessageHeader) extends AbstractMessage(header) with fixengine.messages.LogonMessage {
  field(EncryptMethod.Tag)
  field(HeartBtInt.Tag)
  field(ResetSeqNumFlag.Tag, Required.NO)
  field(MessageEncoding.Tag, Required.NO)
  field(Password.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
