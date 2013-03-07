/*
 * Copyright 2013 the original author or authors.
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
package stirling.mbtrading

import stirling.fix.messages
import stirling.fix.tags._

package object fix {
  type Message = messages.Message

  type Heartbeat     = messages.Heartbeat
  type Logout        = messages.Logout
  type Reject        = messages.Reject
  type ResendRequest = messages.ResendRequest
  type TestRequest   = messages.TestRequest

  val Account                 = fix42.Account
  val AvgPx                   = fix42.AvgPx
  val BusinessRejectRefID     = fix42.BusinessRejectRefID
  val ClOrdID                 = fix42.ClOrdID
  val ClientID                = fix42.ClientID
  val Commission              = fix42.Commission
  val ComplianceID            = fix42.ComplianceID
  val CumQty                  = fix42.CumQty
  val Currency                = fix42.Currency
  val CxlRejReason            = fix42.CxlRejReason
  val CxlRejResponseTo        = fix42.CxlRejResponseTo
  val DiscretionInst          = fix42.DiscretionInst
  val DiscretionOffset        = fix42.DiscretionOffset
  val EffectiveTime           = fix42.EffectiveTime
  val EncryptMethod           = fix42.EncryptMethod
  val ExDestination           = fix42.ExDestination
  val ExecID                  = fix42.ExecID
  val ExecInst                = fix42.ExecInst
  val ExpireTime              = fix42.ExpireTime
  val HandlInst               = fix42.HandlInst
  val Headline                = fix42.Headline
  val HeartBtInt              = fix42.HeartBtInt
  val LastPx                  = fix42.LastPx
  val LastShares              = fix42.LastShares
  val LeavesQty               = fix42.LeavesQty
  val LinesOfText             = fix42.LinesOfText
  val LocateReqd              = fix42.LocateReqd
  val MaturityMonthYear       = fix42.MaturityMonthYear
  val MaxFloor                = fix42.MaxFloor
  val OrdRejReason            = fix42.OrdRejReason
  val OrdStatus               = fix42.OrdStatus
  val OrdType                 = fix42.OrdType
  val OrderID                 = fix42.OrderID
  val OrderQty                = fix42.OrderQty
  val OrigClOrdID             = fix42.OrigClOrdID
  val OrigTime                = fix42.OrigTime
  val PegDifference           = fix42.PegDifference
  val Price                   = fix42.Price
  val PutOrCall               = fix42.PutOrCall
  val RefMsgType              = fix42.RefMsgType
  val RefSeqNo                = fix42.RefSeqNo
  val ResetSeqNumFlag         = fix42.ResetSeqNumFlag
  val SecondaryOrderID        = fix42.SecondaryOrderID
  val SecurityType            = fix42.SecurityType
  val SenderLocationID        = fix42.SenderLocationID
  val SendingTime             = fix42.SendingTime
  val Side                    = fix42.Side
  val StopPx                  = fix42.StopPx
  val StrikePrice             = fix42.StrikePrice
  val SubscriptionRequestType = fix42.SubscriptionRequestType
  val Symbol                  = fix42.Symbol
  val SymbolSfx               = fix42.SymbolSfx
  val Text                    = fix42.Text
  val TradeDate               = fix42.TradeDate
  val TradeSesReqID           = fix42.TradeSesReqID
  val TradingSessionID        = fix42.TradingSessionID
  val TransactTime            = fix42.TransactTime
  val UnderlyingSymbol        = fix42.UnderlyingSymbol
  val UnsolicitedIndicator    = fix42.UnsolicitedIndicator
  val Urgency                 = fix42.Urgency

  val BusinessRejectReason    = fix43.BusinessRejectReason
  val ClOrdLinkID             = fix43.ClOrdLinkID
  val LegCFICode              = fix43.LegCFICode
  val LegMaturityMonthYear    = fix43.LegMaturityMonthYear
  val LegPositionEffect       = fix43.LegPositionEffect
  val LegPrice                = fix43.LegPrice
  val LegProduct              = fix43.LegProduct
  val LegRatioQty             = fix43.LegRatioQty
  val LegRefID                = fix43.LegRefID
  val LegSide                 = fix43.LegSide
  val LegStrikePrice          = fix43.LegStrikePrice
  val LegSymbol               = fix43.LegSymbol
  val MassStatusReqID         = fix43.MassStatusReqID
  val MultiLegReportingType   = fix43.MultiLegReportingType
  val NoLegs                  = fix43.NoLegs
  val OrderRestrictions       = fix43.OrderRestrictions
  val PositionEffect          = fix43.PositionEffect
  val Price2                  = fix43.Price2
  val Product                 = fix43.Product
  val SecondaryClOrdID        = fix43.SecondaryClOrdID
  val TradSesStatus           = fix43.TradSesStatus
  val TradingSessionSubID     = fix43.TradingSessionSubID

  val ClearingBusinessDate    = fix44.ClearingBusinessDate
  val CollInquiryID           = fix44.CollInquiryID
  val CollInquiryResult       = fix44.CollInquiryResult
  val CollInquiryStatus       = fix44.CollInquiryStatus
  val CollRptID               = fix44.CollRptID
  val CollStatus              = fix44.CollStatus
  val LastRptRequested        = fix44.LastRptRequested
  val LongQty                 = fix44.LongQty
  val MarginExcess            = fix44.MarginExcess
  val MarginRatio             = fix44.MarginRatio
  val MessageEncoding         = fix44.MessageEncoding
  val NoTrdRegTimestamps      = fix44.NoTrdRegTimestamps
  val Password                = fix44.Password
  val PeggedPrice             = fix44.PeggedPrice
  val PosMaintRptID           = fix44.PosMaintRptID
  val PosReqID                = fix44.PosReqID
  val PosReqResult            = fix44.PosReqResult
  val SettlPrice              = fix44.SettlPrice
  val ShortQty                = fix44.ShortQty
  val TargetStrategy          = fix44.TargetStrategy
  val TotNumReports           = fix44.TotNumReports
  val TotalNetValue           = fix44.TotalNetValue
  val TotalNumPosReports      = fix44.TotalNumPosReports
  val TrdRegTimestamp         = fix44.TrdRegTimestamp
  val TrdRegTimestampOrigin   = fix44.TrdRegTimestampOrigin
  val TrdRegTimestampType     = fix44.TrdRegTimestampType
  val Username                = fix44.Username
}
