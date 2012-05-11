/*
 * Copyright 2010 the original author or authors.
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
package stirling.fix.messages.fix44.burgundy

import stirling.fix.messages.{
  AbstractMessage,
  BusinessMessageReject => BusinessMessageRejectTrait,
  DontKnowTrade => DontKnowTradeTrait,
  ExecutionReport => ExecutionReportTrait,
  MassQuote => MassQuoteTrait,
  MassQuoteAcknowledgement => MassQuoteAcknowledgementTrait,
  MessageHeader,
  MessageVisitor,
  NewOrderSingle => NewOrderSingleTrait,
  News => NewsTrait,
  OrderCancelReject => OrderCancelRejectTrait,
  OrderCancelReplaceRequest => OrderCancelReplaceRequestTrait,
  OrderCancelRequest => OrderCancelRequestTrait,
  OrderMassCancelReport => OrderMassCancelReportTrait,
  OrderMassCancelRequest => OrderMassCancelRequestTrait,
  OrderMassStatusRequest => OrderMassStatusRequestTrait,
  RepeatingGroup,
  RepeatingGroupInstance,
  Required,
  SecurityList => SecurityListTrait,
  SecurityListRequest => SecurityListRequestTrait,
  TradeCaptureReport => TradeCaptureReportTrait,
  TradeCaptureReportAck => TradeCaptureReportAckTrait,
  TradeCaptureReportRequest => TradeCaptureReportRequestTrait,
  TradeCaptureReportRequestAck => TradeCaptureReportRequestAckTrait
}
import stirling.fix.tags.fix42.{
  Account,
  AvgPx,
  BidPx,
  BidSize,
  BusinessRejectRefID,
  ClOrdID,
  ContractMultiplier,
  CumQty,
  Currency,
  CxlRejReason,
  CxlRejResponseTo,
  DKReason,
  DefBidSize,
  ExecID,
  ExecInst => ExecInst42,
  ExecRefID,
  ExecType,
  ExpireTime,
  HandlInst,
  Issuer,
  LastPx,
  LastShares,
  LeavesQty,
  MaxFloor,
  MaxShow,
  MinQty,
  NoQuoteEntries,
  NoQuoteSets,
  NoRelatedSym,
  OfferPx,
  OfferSize,
  OnBehalfOfCompID,
  OrdRejReason,
  OrdStatus,
  OrdType,
  OrderID,
  OrderQty,
  OrigClOrdID,
  Price,
  PutOrCall,
  QuoteEntryID,
  QuoteID,
  QuoteResponseLevel,
  QuoteSetID,
  RefMsgType,
  RefSeqNo,
  SecondaryOrderID,
  SecurityDesc,
  SecurityExchange,
  SecurityID,
  SecurityReqID,
  SecurityResponseID,
  Side,
  StrikePrice,
  Symbol,
  Text,
  TimeInForce,
  TotQuoteEntries,
  TotalNumSecurities,
  TradeDate,
  TransactTime,
  UnderlyingCurrency,
  UnderlyingSecurityDesc,
  UnderlyingSecurityID,
  UnderlyingSecurityType,
  UnderlyingSymbol
}
import stirling.fix.tags.fix43.{
  AccountType,
  BusinessRejectReason,
  ExecRestatementReason,
  MassStatusReqID,
  MassStatusReqType,
  NoDates,
  NoPartyIDs,
  NoSecurityAltID,
  NoSides,
  NoUnderlyingSecurityAltID,
  OrderCapacity,
  PartyID,
  PartyIDSource,
  PegOffsetValue,
  PreviouslyReported,
  RoundLot,
  SecondaryExecID,
  SecurityAltID,
  SecurityAltIDSource,
  SecurityIDSource,
  SecurityListRequestType,
  SecurityRequestResult,
  TradeReportId,
  TradeReportTransType,
  TradeRequestID,
  TradeRequestType,
  UnderlyingSecurityAltID,
  UnderlyingSecurityAltIDSource
}
import stirling.fix.tags.fix44.{
  EventDate,
  EventType,
  Headline,
  InstrAttribValue,
  LastFragment,
  LastLiquidityInd,
  LastRptRequested,
  MassCancelRejectReason,
  MassCancelRequestType,
  MassCancelResponse,
  NoEvents,
  NoInstrAttrib,
  NoLinesOfText,
  NoUnderlyings,
  PartyRole,
  PegMoveType,
  PegOffsetType,
  PegScope,
  PeggedPrice,
  QuoteAckStatus,
  QuoteEntryRejectReason,
  QuoteRejectReason,
  SecuritySubType,
  StrikeCurrency,
  TotNumReports,
  TradeRequestResult,
  TradeRequestStatus,
  UnderlyingSecurityIDSource
}
import stirling.fix.tags.fix44.burgundy.{
  AllowedOrderTransparency,
  CSD,
  IncludeTickRules,
  InstrAttribType,
  LargeInScaleLimit,
  NoTickRules,
  OrderRestrictions,
  SecurityType,
  SettlType,
  StartTickPriceRange,
  TickIncrement,
  TickRuleID,
  TradeSeqNoSeries,
  TrdType,
  ValidityPeriod
}
import stirling.fix.tags.fix50.{
  DisplayHighQty,
  DisplayLowQty,
  DisplayMethod,
  DisplayMinIncr,
  ExecInst => ExecInst50,
  MatchType
}
import stirling.fix.tags.fix50sp1.{
  ExerciseStyle,
  MarketID,
  MarketSegmentID,
  SettlMethod
}

class BusinessMessageReject(header: MessageHeader) extends AbstractMessage(header) with BusinessMessageRejectTrait {
  field(RefSeqNo.Tag)
  field(Text.Tag, Required.NO)
  field(RefMsgType.Tag)
  field(BusinessRejectRefID.Tag, Required.NO)
  field(BusinessRejectReason.Tag)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class DontKnowTrade(header: MessageHeader) extends AbstractMessage(header) with DontKnowTradeTrait {
  field(ExecID.Tag)
  field(LastPx.Tag, Required.NO)
  field(LastShares.Tag, Required.NO)
  field(OrderID.Tag)
  field(OrderQty.Tag)
  field(Side.Tag)
  field(Symbol.Tag)
  field(SecurityIDSource.Tag, Required.NO)
  field(SecurityID.Tag, Required.NO)
  field(Text.Tag, Required.NO)
  field(DKReason.Tag)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class ExecutionReport(header: MessageHeader) extends AbstractMessage(header) with ExecutionReportTrait with Groups {
  field(Account.Tag, Required.NO)
  field(AvgPx.Tag)
  field(ClOrdID.Tag, Required.NO)
  field(CumQty.Tag)
  field(Currency.Tag, Required.NO)
  field(ExecID.Tag)
  field(ExecInst42.Tag, Required.NO)
  field(ExecRefID.Tag, Required.NO)
  field(HandlInst.Tag, Required.NO)
  field(LastPx.Tag, Required.NO)
  field(LastShares.Tag, Required.NO)
  field(OrderID.Tag)
  field(OrderQty.Tag)
  field(OrdStatus.Tag)
  field(OrdType.Tag, Required.NO)
  field(OrigClOrdID.Tag, Required.NO)
  field(Price.Tag, Required.NO)
  field(Side.Tag, Required.NO)
  field(Symbol.Tag)
  field(SecurityIDSource.Tag, Required.NO)
  field(SecurityID.Tag, Required.NO)
  field(Text.Tag, Required.NO)
  field(TimeInForce.Tag, Required.NO)
  field(TransactTime.Tag, Required.NO)
  field(TradeDate.Tag, Required.NO)
  field(OrdRejReason.Tag, Required.NO)
  field(MinQty.Tag, Required.NO)
  field(DisplayMethod.Tag, Required.NO)
  val requiredWhenDisplayMethodIsRandom = new Required() {
    override def isRequired(): Boolean = getEnum(DisplayMethod.Tag) == DisplayMethod.Random
  }
  field(DisplayLowQty.Tag, requiredWhenDisplayMethodIsRandom)
  field(DisplayHighQty.Tag, requiredWhenDisplayMethodIsRandom)
  field(DisplayMinIncr.Tag, Required.NO)
  field(MaxFloor.Tag, Required.NO)
  field(ExpireTime.Tag, new Required() {
    override def isRequired(): Boolean = getEnum(TimeInForce.Tag) == TimeInForce.GoodTillDate
  })
  field(ExecType.Tag)
  field(LeavesQty.Tag)
  field(SecondaryOrderID.Tag, Required.NO)
  field(MaxShow.Tag, Required.NO)
  field(PegOffsetValue.Tag, Required.YES)
  field(PegMoveType.Tag, Required.NO)
  field(PegOffsetType.Tag, Required.NO)
  field(PegScope.Tag, Required.NO)
  field(ExecRestatementReason.Tag, Required.NO)
  field(SecondaryExecID.Tag, Required.NO)
  field(OrderCapacity.Tag, Required.NO)
  field(OrderRestrictions.Tag, Required.NO)
  field(MatchType.Tag, Required.NO)
  field(AccountType.Tag, Required.NO)
  field(MassStatusReqID.Tag, Required.NO)
  parties(Required.NO)
  field(TrdType.Tag, new Required() {
    override def isRequired(): Boolean = getEnum(MatchType.Tag) == MatchType.TwoPartyTradeReport
  })
  field(PeggedPrice.Tag, Required.NO)
  field(LastLiquidityInd.Tag, Required.NO)
  field(TotNumReports.Tag, Required.NO)
  field(LastRptRequested.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class MassQuote(header: MessageHeader) extends AbstractMessage(header) with MassQuoteTrait with Groups {
  field(QuoteID.Tag)
  field(QuoteResponseLevel.Tag, Required.NO)
  field(DefBidSize.Tag, Required.NO)
  field(NoQuoteEntries.Tag, Required.NO)
  field(Account.Tag, Required.NO)
  def quoteSetGrp() {
    group(new RepeatingGroup(NoQuoteSets.Tag) {
      override def newInstance(): RepeatingGroupInstance =
        new RepeatingGroupInstance(QuoteSetID.Tag) {
          field(TotQuoteEntries.Tag)
          def quoteEntryGrp() {
            group(new RepeatingGroup(NoQuoteEntries.Tag) {
              override def newInstance(): RepeatingGroupInstance =
                new RepeatingGroupInstance(QuoteEntryID.Tag) {
                  field(Symbol.Tag)
                  field(SecurityIDSource.Tag, Required.NO)
                  field(SecurityID.Tag, Required.NO)
                  field(BidPx.Tag, Required.NO)
                  field(OfferPx.Tag, Required.NO)
                  field(BidSize.Tag, Required.NO)
                  field(OfferSize.Tag, Required.NO)
                  field(Currency.Tag)
                }
            })
          }
          quoteEntryGrp()
        }
    })
  }
  quoteSetGrp()
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class MassQuoteAcknowledgement(header: MessageHeader) extends AbstractMessage(header) with MassQuoteAcknowledgementTrait with Groups {
  field(QuoteID.Tag)
  field(QuoteAckStatus.Tag)
  field(QuoteRejectReason.Tag, Required.NO)
  field(Text.Tag, Required.NO)
  def quoteSetAckGrp() {
    group(new RepeatingGroup(NoQuoteSets.Tag) {
      override def newInstance(): RepeatingGroupInstance =
        new RepeatingGroupInstance(QuoteSetID.Tag) {
          field(TotQuoteEntries.Tag, Required.NO)
          def quoteEntryAckGrp() {
            group(new RepeatingGroup(NoQuoteEntries.Tag) {
              override def newInstance(): RepeatingGroupInstance =
                new RepeatingGroupInstance(QuoteEntryID.Tag) {
                  field(Symbol.Tag)
                  field(SecurityIDSource.Tag, Required.NO)
                  field(SecurityID.Tag, Required.NO)
                  field(BidPx.Tag, Required.NO)
                  field(OfferPx.Tag, Required.NO)
                  field(BidSize.Tag, Required.NO)
                  field(OfferSize.Tag, Required.NO)
                  field(Currency.Tag)
                  field(QuoteEntryRejectReason.Tag, Required.NO)
                }
            })
          }
          quoteEntryAckGrp()
        }
    })
  }
  quoteSetAckGrp()
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class NewOrderSingle(header: MessageHeader) extends AbstractMessage(header) with NewOrderSingleTrait with Groups {
  field(Account.Tag, Required.NO)
  field(ClOrdID.Tag)
  field(Currency.Tag, Required.NO)
  field(ExecInst42.Tag, Required.NO)
  field(HandlInst.Tag, Required.NO)
  field(Symbol.Tag)
  field(SecurityIDSource.Tag, Required.NO)
  field(SecurityID.Tag, Required.NO)
  field(OrderQty.Tag, Required.NO)
  field(OrdType.Tag)
  field(Price.Tag)
  field(Side.Tag)
  field(Text.Tag, Required.NO)
  field(TimeInForce.Tag, Required.NO)
  field(TransactTime.Tag)
  field(MinQty.Tag, Required.NO)
  field(DisplayMethod.Tag, Required.NO)
  field(DisplayLowQty.Tag, Required.NO)
  field(DisplayHighQty.Tag, Required.NO)
  field(DisplayMinIncr.Tag, Required.NO)
  field(MaxFloor.Tag, Required.NO)
  field(ExpireTime.Tag, new Required() {
    override def isRequired(): Boolean = getEnum(TimeInForce.Tag) == TimeInForce.GoodTillDate
  })
  field(MaxShow.Tag, Required.NO)
  field(PegOffsetValue.Tag, Required.YES)
  field(PegMoveType.Tag, Required.NO)
  field(PegOffsetType.Tag, Required.NO)
  field(PegScope.Tag, Required.NO)
  parties(Required.NO)
  field(OrderCapacity.Tag, Required.NO)
  field(OrderRestrictions.Tag, Required.NO)
  field(AccountType.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class News(header: MessageHeader) extends AbstractMessage(header) with NewsTrait {
  field(Headline.Tag)
  def linesOfTextGrp() {
    group(new RepeatingGroup(NoLinesOfText.Tag) {
      override def newInstance() =
        new RepeatingGroupInstance(Text.Tag)
    })
  }
  linesOfTextGrp()
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderCancelReject(header: MessageHeader) extends AbstractMessage(header) with OrderCancelRejectTrait {
  field(Account.Tag, Required.NO)
  field(ClOrdID.Tag)
  field(OrderID.Tag, Required.NO)
  field(OrdStatus.Tag)
  field(OrigClOrdID.Tag)
  field(TransactTime.Tag, Required.NO)
  field(CxlRejResponseTo.Tag)
  field(CxlRejReason.Tag, Required.NO)
  field(Text.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderCancelReplaceRequest(header: MessageHeader) extends AbstractMessage(header) with OrderCancelReplaceRequestTrait {
  field(Account.Tag, Required.NO)
  field(ClOrdID.Tag)
  field(Currency.Tag, Required.NO)
  field(ExecInst50.Tag, Required.NO)
  field(OrderID.Tag, Required.NO)
  field(OrderQty.Tag)
  field(OrdType.Tag)
  field(OrigClOrdID.Tag)
  field(Price.Tag)
  field(Side.Tag)
  field(Symbol.Tag)
  field(SecurityIDSource.Tag, Required.NO)
  field(SecurityID.Tag, Required.NO)
  field(Text.Tag, Required.NO)
  field(TimeInForce.Tag, Required.NO)
  field(TransactTime.Tag)
  field(MinQty.Tag, Required.NO)
  field(MaxShow.Tag, Required.NO)
  field(MaxFloor.Tag, Required.NO)
  field(ExpireTime.Tag, new Required() {
    override def isRequired(): Boolean = {
      return getEnum(TimeInForce.Tag).equals(TimeInForce.GoodTillDate)
    }
  })
  field(PegOffsetValue.Tag, Required.YES)
  field(PegMoveType.Tag, Required.NO)
  field(PegOffsetType.Tag, Required.NO)
  field(PegScope.Tag, Required.NO)
  field(OrderCapacity.Tag, Required.NO)
  field(OrderRestrictions.Tag, Required.NO)
  field(AccountType.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderCancelRequest(header: MessageHeader) extends AbstractMessage(header) with OrderCancelRequestTrait {
  field(ClOrdID.Tag)
  field(OrderID.Tag, Required.NO)
  field(OrderQty.Tag)
  field(OrigClOrdID.Tag)
  field(Side.Tag)
  field(Symbol.Tag)
  field(SecurityIDSource.Tag, Required.NO)
  field(SecurityID.Tag, Required.NO)
  field(TransactTime.Tag)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderMassCancelReport(header: MessageHeader) extends AbstractMessage(header) with OrderMassCancelReportTrait {
  field(ClOrdID.Tag)
  field(OrderID.Tag)
  field(MassCancelRequestType.Tag)
  field(MassCancelResponse.Tag)
  field(MassCancelRejectReason.Tag, Required.NO)
  field(Text.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderMassCancelRequest(header: MessageHeader) extends AbstractMessage(header) with OrderMassCancelRequestTrait {
  field(ClOrdID.Tag)
  field(OnBehalfOfCompID.Tag, Required.NO)
  field(MassCancelRequestType.Tag)
  field(Symbol.Tag, Required.NO)
  field(SecurityIDSource.Tag, Required.NO)
  field(SecurityID.Tag, Required.NO)
  field(TransactTime.Tag)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderMassStatusRequest(header: MessageHeader) extends AbstractMessage(header) with OrderMassStatusRequestTrait {
  field(MassStatusReqID.Tag)
  field(MassStatusReqType.Tag)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class SecurityList(header: MessageHeader) extends AbstractMessage(header) with SecurityListTrait {
  field(SecurityReqID.Tag)
  field(SecurityResponseID.Tag)
  def tickRulesGrp() {
    group(new RepeatingGroup(NoTickRules.Tag) {
      override def newInstance() =
        new RepeatingGroupInstance(StartTickPriceRange.Tag) {
          field(TickIncrement.Tag)
          field(TickRuleID.Tag)
        }
    }, Required.NO)
  }
  tickRulesGrp()
  field(TotalNumSecurities.Tag, Required.NO)
  field(LastFragment.Tag, Required.NO)
  field(SecurityRequestResult.Tag, Required.NO)
  field(MarketID.Tag, Required.NO)
  field(MarketSegmentID.Tag, Required.NO)
  group(new RepeatingGroup(NoRelatedSym.Tag) {
    override def newInstance(): RepeatingGroupInstance =
      new RepeatingGroupInstance(Symbol.Tag) {
        def instrument() {
          field(SecurityIDSource.Tag)
          field(SecurityID.Tag)
          def secAltIDGrp() {
            group(new RepeatingGroup(NoSecurityAltID.Tag) {
              override def newInstance() =
                new RepeatingGroupInstance(SecurityAltID.Tag) {
                  field(SecurityAltIDSource.Tag)
                }
            })
          }
          secAltIDGrp()
          field(SecurityType.Tag)
          field(SecuritySubType.Tag, Required.NO)
          field(SecurityDesc.Tag, Required.NO)
          field(StrikePrice.Tag, Required.NO)
          field(StrikeCurrency.Tag, Required.NO)
          field(SettlMethod.Tag, Required.NO)
          field(ExerciseStyle.Tag, Required.NO)
          field(PutOrCall.Tag, Required.NO)
          field(ContractMultiplier.Tag, Required.NO)
          field(SecurityExchange.Tag, Required.NO)
          field(Issuer.Tag, Required.NO)
          def eventGrp() {
            group(new RepeatingGroup(NoEvents.Tag) {
              override def newInstance() =
                new RepeatingGroupInstance(EventType.Tag) {
                  field(EventDate.Tag)
                }
            })
          }
          eventGrp()
        }
        instrument()
        def instrumentExtension() {
          group(new RepeatingGroup(NoInstrAttrib.Tag) {
            override def newInstance() =
              new RepeatingGroupInstance(InstrAttribType.Tag) {
                field(InstrAttribValue.Tag)
              }
          }, Required.NO)
        }
        instrumentExtension()
        def undInstrmtGrp() {
          group(new RepeatingGroup(NoUnderlyings.Tag) {
            override def newInstance() =
              new RepeatingGroupInstance(UnderlyingSymbol.Tag) {
                field(UnderlyingSecurityID.Tag, Required.NO)
                field(UnderlyingSecurityIDSource.Tag, Required.NO)
                def undSecAltIDGrp() {
                  group(new RepeatingGroup(NoUnderlyingSecurityAltID.Tag) {
                    override def newInstance() =
                      new RepeatingGroupInstance(UnderlyingSecurityAltID.Tag) {
                        field(UnderlyingSecurityAltIDSource.Tag)
                      }
                  })
                }
                undSecAltIDGrp()
                field(UnderlyingSecurityType.Tag)
                field(UnderlyingSecurityDesc.Tag, Required.NO)
                field(UnderlyingCurrency.Tag, Required.NO)
              }
          })
        }
        undInstrmtGrp()
        field(Currency.Tag)
        field(TickRuleID.Tag, Required.NO)
        field(CSD.Tag, Required.NO)
        field(AllowedOrderTransparency.Tag, Required.NO)
        field(LargeInScaleLimit.Tag, Required.NO)
        field(TradeSeqNoSeries.Tag, Required.NO)
        field(RoundLot.Tag, Required.NO)
        field(Text.Tag, Required.NO)
        field(SettlType.Tag, Required.NO)
      }
  })
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class SecurityListRequest(header: MessageHeader) extends AbstractMessage(header) with SecurityListRequestTrait {
  field(Currency.Tag, Required.NO)
  field(IncludeTickRules.Tag, Required.NO)
  field(SecurityReqID.Tag)
  field(SecurityListRequestType.Tag)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class TradeCaptureReport(header: MessageHeader) extends AbstractMessage(header) with TradeCaptureReportTrait with Groups {
  field(TradeRequestID.Tag)
  field(PreviouslyReported.Tag)
  field(Symbol.Tag)
  field(SecurityIDSource.Tag, Required.NO)
  field(SecurityID.Tag, Required.NO)
  field(OrderQty.Tag)
  field(LastPx.Tag)
  field(LastShares.Tag)
  field(TradeDate.Tag)
  field(TransactTime.Tag)
  field(TradeReportTransType.Tag)
  field(MatchType.Tag, Required.NO)
  field(TrdType.Tag, new Required() {
    override def isRequired(): Boolean = getEnum(MatchType.Tag) == MatchType.TwoPartyTradeReport
  })
  field(ValidityPeriod.Tag, Required.NO)
  def trdCapRptSideGrp() {
    group(new RepeatingGroup(NoSides.Tag) {
        override def newInstance(): RepeatingGroupInstance =
          new RepeatingGroupInstance(Side.Tag) {
            field(OrderID.Tag)
            field(Currency.Tag)
            field(ClOrdID.Tag, Required.NO)
            field(Account.Tag, Required.NO)
            field(Text.Tag, Required.NO)
            field(OrderCapacity.Tag, Required.NO)
            field(OrderRestrictions.Tag, Required.NO)
            field(AccountType.Tag, Required.NO)
            parties()
          }
      })
  }
  trdCapRptSideGrp()
  field(TradeRequestID.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class TradeCaptureReportAck(header: MessageHeader) extends AbstractMessage(header) with TradeCaptureReportAckTrait with Groups {
  field(TradeReportId.Tag)
  field(ExecType.Tag)
  field(Symbol.Tag)
  field(SecurityIDSource.Tag, Required.NO)
  field(SecurityID.Tag, Required.NO)
  field(TrdType.Tag, Required.NO)
  field(TradeReportTransType.Tag)
  field(Currency.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class TradeCaptureReportRequest(header: MessageHeader) extends AbstractMessage(header) with TradeCaptureReportRequestTrait with Groups {
  field(TradeRequestID.Tag)
  field(TradeRequestType.Tag)
  parties(Required.NO)
  field(Symbol.Tag, Required.NO)
  field(SecurityIDSource.Tag, Required.NO)
  field(SecurityID.Tag, Required.NO)
  field(Currency.Tag, Required.NO)
  group(new RepeatingGroup(NoDates.Tag) {
    override def newInstance(): RepeatingGroupInstance =
      new RepeatingGroupInstance(TradeDate.Tag) {
        field(TransactTime.Tag, Required.NO)
      }
  }, Required.NO)
  field(LastPx.Tag, Required.NO)
  field(LastShares.Tag, Required.NO)
  field(Side.Tag, Required.NO)
  field(ExecType.Tag, Required.NO)
  field(TrdType.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class TradeCaptureReportRequestAck(header: MessageHeader) extends AbstractMessage(header) with TradeCaptureReportRequestAckTrait {
  field(TradeRequestID.Tag)
  field(TradeRequestType.Tag)
  field(TradeRequestResult.Tag)
  field(TradeRequestStatus.Tag)
  field(Symbol.Tag, Required.NO)
  field(SecurityIDSource.Tag, Required.NO)
  field(SecurityID.Tag, Required.NO)
  field(Currency.Tag, Required.NO)
  field(Text.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
