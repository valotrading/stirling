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
package stirling.fix.tags.fix44

import java.lang.{
  Character,
  Integer,
  String
}
import stirling.fix.messages.{
  AmtTag,
  BooleanTag,
  BooleanValue,
  CharValue,
  CurrencyTag,
  EnumTag,
  IntegerTag,
  IntegerValue,
  LocalMktDateTag,
  NumInGroupTag,
  PriceTag,
  QtyTag,
  SeqNumTag,
  StringTag,
  StringValue,
  UtcTimestampTag
}

object CxlRejReason extends EnumTag[Integer](102) {
  val TooLate = IntegerValue(0)
  val Unknown = IntegerValue(1)
  val BrokerOpt = IntegerValue(2)
  val AlreadyPendingCxl = IntegerValue(3)
  val UnableToProcess = IntegerValue(4)
  val OrigOrdModTimeMismatch = IntegerValue(5)
  val DupClOrdID = IntegerValue(6)
  val Other = IntegerValue(99)
}
object Headline extends StringTag(148)
object ExecType extends EnumTag[Character](150) {
  val New = CharValue('0')
  val DoneForDay = CharValue('3')
  val Canceled = CharValue('4')
  val Replace = CharValue('5')
  val PendingCancel = CharValue('6')
  val Stopped = CharValue('7')
  val Rejected = CharValue('8')
  val Suspended = CharValue('9')
  val PendingNew = CharValue('A')
  val Calculated = CharValue('B')
  val Expired = CharValue('C')
  val Restated = CharValue('D')
  val PendingReplace = CharValue('E')
  val Trade = CharValue('F')
  val TradeCorrect = CharValue('G')
  val TradeCancel = CharValue('H')
  val OrderStatus = CharValue('I')
}
object QuoteAckStatus extends EnumTag[Integer](297) {
  val Accpt = IntegerValue(0)
  val CxlSym = IntegerValue(1)
  val CxlSecType = IntegerValue(2)
  val CxlUnder = IntegerValue(3)
  val CxlAll = IntegerValue(4)
  val Rej = IntegerValue(5)
  val Removed = IntegerValue(6)
  val Expired = IntegerValue(7)
  val Query = IntegerValue(8)
  val QuoteNotFound = IntegerValue(9)
  val Pending = IntegerValue(10)
  val Pass = IntegerValue(11)
  val LockedMarketWarning = IntegerValue(12)
  val CrossMarketWarning = IntegerValue(13)
  val CanceledDueToLockMarket = IntegerValue(14)
  val CanceledDueToCrossMarket = IntegerValue(15)
}
object QuoteRejectReason extends EnumTag[Integer](300) {
  val UnknSym = IntegerValue(1)
  val ExchClsd = IntegerValue(2)
  val OrdExLim = IntegerValue(3)
  val TooLate = IntegerValue(4)
  val UnknOrd = IntegerValue(5)
  val DupOrd = IntegerValue(6)
  val InvSpread = IntegerValue(7)
  val InvPx = IntegerValue(8)
  val NotAuth = IntegerValue(9)
  val Other = IntegerValue(99)
}
object UnderlyingSecurityIDSource extends StringTag(305)
object MessageEncoding extends StringTag(347)
object QuoteEntryRejectReason extends EnumTag[Integer](368) {
  val UnknwnSym = IntegerValue(1)
  val ExchClsd = IntegerValue(2)
  val OrdExcLim = IntegerValue(3)
  val TooLate = IntegerValue(4)
  val UnknOrd = IntegerValue(5)
  val DupOrd = IntegerValue(6)
  val InvBidAsk = IntegerValue(7)
  val InvPx = IntegerValue(8)
  val NotAuth = IntegerValue(9)
  val Other = IntegerValue(99)
}
object PartyRole extends EnumTag[Integer](452) {
  val ExecutingFirm = IntegerValue(1)
  val ClearingFirm = IntegerValue(4)
  val EnteringFirm = IntegerValue(7)
  val ContraFirm = IntegerValue(17)
  val ContraClearingFirmOrCcp = IntegerValue(18)
  val ClearingOrganization = IntegerValue(21)
  val EnteringTrader = IntegerValue(36)
}
object MassCancelRequestType extends EnumTag[Character](530) {
  val CxlOrdersSecurity = CharValue('1')
  val CxlOrdersUnderlyingSecurity = CharValue('2')
  val CxlOrdersProduct = CharValue('3')
  val CxlOrdersCFICode = CharValue('4')
  val CxlOrdersSecurityType = CharValue('5')
  val CxlOrdersTrdSession = CharValue('6')
  val CxlAllOrders = CharValue('7')
}
object MassCancelResponse extends EnumTag[Character](531) {
  val CxlReqRej = CharValue('0')
  val CxlOrdersSecurity = CharValue('1')
  val CxlOrdersUnderlyingSecurity = CharValue('2')
  val CxlOrdersProduct = CharValue('3')
  val CxlOrdersCFICode = CharValue('4')
  val CxlOrdersSecurityType = CharValue('5')
  val CxlOrdersTrdSession = CharValue('6')
  val CxlAllOrders = CharValue('7')
}
object MassCancelRejectReason extends EnumTag[String](532) {
  val MassCxlNotSupported = StringValue("0")
  val InvalidSecurity = StringValue("1")
  val InvalidUnderlying = StringValue("2")
  val InvalidProduct = StringValue("3")
  val InvalidCFICode = StringValue("4")
  val InvalidSecurityType = StringValue("5")
  val InvalidTrdSession = StringValue("6")
  val Other = StringValue("99")
}
object Username extends StringTag(553)
object Password extends StringTag(554)
object LongQty extends QtyTag(704)
object ShortQty extends QtyTag(705)
object PosReqID extends StringTag(710)
object NoUnderlyings extends NumInGroupTag(711)
object ClearingBusinessDate extends LocalMktDateTag(715)
object PosMaintRptID extends StringTag(721)
object TotalNumPosReports extends IntegerTag(727)
object PosReqResult extends IntegerTag(728)
object SettlPrice extends PriceTag(730)
object TradeRequestResult extends EnumTag[Integer](749) {
  val Successful = IntegerValue(0)
  val InvalidOrUnknownInstrument = IntegerValue(1)
  val InvalidTypeOfTradeRequested = IntegerValue(2)
  val InvalidParties = IntegerValue(3)
  val InvalidTransportTypeRequested = IntegerValue(4)
  val InvalidDestinationRequested = IntegerValue(5)
  val TradeRequestTypeNotSupported = IntegerValue(8)
  val UnauthorizedForTradeCaptureReportRequest = IntegerValue(9)
  val Other = IntegerValue(99)
}
object TradeRequestStatus extends EnumTag[Integer](750) {
  val Accepted = IntegerValue(0)
  val Completed = IntegerValue(1)
  val Rejected = IntegerValue(2)
}
object SecuritySubType extends StringTag(762)
object NoTrdRegTimestamps extends NumInGroupTag(768)
object TrdRegTimestamp extends UtcTimestampTag(769)
object TrdRegTimestampType extends IntegerTag(770)
object TrdRegTimestampOrigin extends StringTag(771)
object NextExpectedMsgSeqNum extends SeqNumTag(789)
object TradeLinkID extends StringTag(820)
object PegMoveType extends EnumTag[Integer](835) {
  val Floating = IntegerValue(0)
  val Fixed = IntegerValue(1)
}
object PegOffsetType extends EnumTag[Integer](836) {
  val Price = IntegerValue(0)
  val BasisPoints = IntegerValue(1)
  val Ticks = IntegerValue(2)
  val PriceTierLevel = IntegerValue(3)
}
object PeggedPrice extends PriceTag(839)
object PegScope extends EnumTag[Integer](840) {
  val Local = IntegerValue(1)
  val National = IntegerValue(2)
  val Global = IntegerValue(3)
  val NationalExcludingLocal = IntegerValue(4)
}
object LastLiquidityInd extends EnumTag[Integer](851) {
  val AddedLiquidity = IntegerValue(1)
  val RemovedLiquidity = IntegerValue(2)
  val LiquidityRoutedOut = IntegerValue(3)
}
object NoEvents extends NumInGroupTag(864)
object EventType extends EnumTag[Integer](865) {
  val Put = IntegerValue(1)
  val Call = IntegerValue(2)
  val Tender = IntegerValue(3)
  val SinkingFundCall = IntegerValue(4)
  val Other = IntegerValue(99)
}
object EventDate extends LocalMktDateTag(866)
object NoInstrAttrib extends NumInGroupTag(870)
object InstrAttribValue extends StringTag(872)
object LastFragment extends EnumTag[Boolean](893) {
  val NotLastMessage = BooleanValue(false)
  val LastMessage = BooleanValue(true)
}
object CollInquiryID extends StringTag(909)
object TotNumReports extends IntegerTag(911)
object LastRptRequested extends BooleanTag(912)
object StrikeCurrency extends CurrencyTag(947)
object NoLinesOfText extends NumInGroupTag(33)
object MarginRatio extends IntegerTag(898)
object MarginExcess extends AmtTag(899)
object TotalNetValue extends StringTag(900)
object CollRptID extends StringTag(908)
object CollStatus extends IntegerTag(910)
object CollInquiryStatus extends EnumTag[Integer](945) {
  val Accepted = IntegerValue(0)
  val AcceptedWithWarnings = IntegerValue(1)
  val Completed = IntegerValue(2)
  val CompletedWithWarnings = IntegerValue(3)
  val Rejected = IntegerValue(4)
}
