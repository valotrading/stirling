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
package fixengine.tags.fix44

import fixengine.messages.{
  AmtTag,
  BooleanTag,
  EnumTag,
  IntegerTag,
  LocalMktDateTag,
  NumInGroupTag,
  PriceTag,
  QtyTag,
  SeqNumTag,
  StringTag,
  UtcTimestampTag,
  Value
}
import java.lang.{
  Character,
  Integer,
  String
}

object QuoteAckStatus extends EnumTag[Integer](297) {
  val Accpt = Value(0)
  val CxlSym = Value(1)
  val CxlSecType = Value(2)
  val CxlUnder = Value(3)
  val CxlAll = Value(4)
  val Rej = Value(5)
  val Removed = Value(6)
  val Expired = Value(7)
  val Query = Value(8)
  val QuoteNotFound = Value(9)
  val Pending = Value(10)
  val Pass = Value(11)
  val LockedMarketWarning = Value(12)
  val CrossMarketWarning = Value(13)
  val CanceledDueToLockMarket = Value(14)
  val CanceledDueToCrossMarket = Value(15)
}
object QuoteRejectReason extends EnumTag[Integer](300) {
  val UnknSym = Value(1)
  val ExchClsd = Value(2)
  val OrdExLim = Value(3)
  val TooLate = Value(4)
  val UnknOrd = Value(5)
  val DupOrd = Value(6)
  val InvSpread = Value(7)
  val InvPx = Value(8)
  val NotAuth = Value(9)
  val Other = Value(99)
}
object QuoteEntryRejectReason extends EnumTag[Integer](368) {
  val UnknwnSym = Value(1)
  val ExchClsd = Value(2)
  val OrdExcLim = Value(3)
  val TooLate = Value(4)
  val UnknOrd = Value(5)
  val DupOrd = Value(6)
  val InvBidAsk = Value(7)
  val InvPx = Value(8)
  val NotAuth = Value(9)
  val Other = Value(99)
}
object PartyRole extends EnumTag[Integer](452) {
  val ExecutingFirm = Value(1)
  val ClearingFirm = Value(4)
  val EnteringFirm = Value(7)
  val ContraFirm = Value(17)
  val ContraClearingFirmOrCcp = Value(18)
  val ClearingOrganization = Value(21)
  val EnteringTrader = Value(36)
}
object MassCancelRequestType extends EnumTag[Character](530) {
  val CxlOrdersSecurity = Value('1')
  val CxlOrdersUnderlyingSecurity = Value('2')
  val CxlOrdersProduct = Value('3')
  val CxlOrdersCFICode = Value('4')
  val CxlOrdersSecurityType = Value('5')
  val CxlOrdersTrdSession = Value('6')
  val CxlAllOrders = Value('7')
}
object MassCancelResponse extends EnumTag[Character](531) {
  val CxlReqRej = Value('0')
  val CxlOrdersSecurity = Value('1')
  val CxlOrdersUnderlyingSecurity = Value('2')
  val CxlOrdersProduct = Value('3')
  val CxlOrdersCFICode = Value('4')
  val CxlOrdersSecurityType = Value('5')
  val CxlOrdersTrdSession = Value('6')
  val CxlAllOrders = Value('7')
}
object MassCancelRejectReason extends EnumTag[String](532) {
  val MassCxlNotSupported = Value("0")
  val InvalidSecurity = Value("1")
  val InvalidUnderlying = Value("2")
  val InvalidProduct = Value("3")
  val InvalidCFICode = Value("4")
  val InvalidSecurityType = Value("5")
  val InvalidTrdSession = Value("6")
  val Other = Value("99")
}
object Username extends StringTag(553)
object Password extends StringTag(554)
object LongQty extends QtyTag(704)
object ShortQty extends QtyTag(705)
object PosReqID extends StringTag(710)
object ClearingBusinessDate extends LocalMktDateTag(715)
object PosMaintRptID extends StringTag(721)
object TotalNumPosReports extends IntegerTag(727)
object PosReqResult extends IntegerTag(728)
object SettlPrice extends PriceTag(730)
object TradeRequestResult extends EnumTag[Integer](749) {
  val Successful = Value(0)
  val InvalidOrUnknownInstrument = Value(1)
  val InvalidTypeOfTradeRequested = Value(2)
  val InvalidParties = Value(3)
  val InvalidTransportTypeRequested = Value(4)
  val InvalidDestinationRequested = Value(5)
  val TradeRequestTypeNotSupported = Value(8)
  val UnauthorizedForTradeCaptureReportRequest = Value(9)
  val Other = Value(99)
}
object TradeRequestStatus extends EnumTag[Integer](750) {
  val Accepted = Value(0)
  val Completed = Value(1)
  val Rejected = Value(2)
}
object NoTrdRegTimestamps extends NumInGroupTag(768)
object TrdRegTimestamp extends UtcTimestampTag(769)
object TrdRegTimestampType extends IntegerTag(770)
object TrdRegTimestampOrigin extends StringTag(771)
object NextExpectedMsgSeqNum extends SeqNumTag(789)
object TradeLinkID extends StringTag(820)
object PegMoveType extends EnumTag[Integer](835) {
  val Floating = Value(0)
  val Fixed = Value(1)
}
object PegOffsetType extends EnumTag[Integer](836) {
  val Price = Value(0)
  val BasisPoints = Value(1)
  val Ticks = Value(2)
  val PriceTierLevel = Value(3)
}
object PeggedPrice extends PriceTag(839)
object PegScope extends EnumTag[Integer](840) {
  val Local = Value(1)
  val National = Value(2)
  val Global = Value(3)
  val NationalExcludingLocal = Value(4)
}
object LastLiquidityInd extends EnumTag[Integer](851) {
  val AddedLiquidity = Value(1)
  val RemovedLiquidity = Value(2)
  val LiquidityRoutedOut = Value(3)
}
object CollInquiryID extends StringTag(909)
object TotNumReports extends IntegerTag(911)
object LastRptRequested extends BooleanTag(912)
