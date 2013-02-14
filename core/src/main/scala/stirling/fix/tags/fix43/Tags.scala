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
package stirling.fix.tags.fix43

import stirling.fix.messages.{
  BooleanTag,
  CharValue,
  EnumTag,
  FloatTag,
  IntegerTag,
  IntegerValue,
  MonthYearTag,
  NumInGroupTag,
  PriceOffsetTag,
  PriceTag,
  QtyTag,
  StringTag,
  StringValue
}
import java.lang.{
  Character,
  Integer
}

object SecurityIDSource extends EnumTag[Character](22) {
  val ISIN = CharValue('4')
  val ExchangeSymbol = CharValue('8')
}
object CxlRejReason extends EnumTag[Integer](102) {
  val TooLateToCancel = IntegerValue(0)
  val UnknownOrder = IntegerValue(1)
  val BrokerExchangeOption = IntegerValue(2)
  val PendingCancelOrReplace = IntegerValue(3)
  val UnableToProcessRequest = IntegerValue(4)
  val TimeMismatch = IntegerValue(5)
  val DuplicateClOrdId = IntegerValue(6)
}
object OrdRejReason extends EnumTag[Integer](103) {
  val BrokerExchangeOption = IntegerValue(0)
  val UnknownSymbol = IntegerValue(1)
  val ExchangeClosed = IntegerValue(2)
  val OrderExceedsLimit = IntegerValue(3)
  val TooLateToEnter = IntegerValue(4)
  val UnknownOrder = IntegerValue(5)
  val DuplicateOrder = IntegerValue(6)
  val DuplicateOfVerballyCommunicatedOrder = IntegerValue(7)
  val StaleOrder = IntegerValue(8)
  val TradeAlongRequired = IntegerValue(9)
  val InvalidInvestorId = IntegerValue(10)
  val UnsupportedOrderCharacteristic = IntegerValue(11)
  val SurveillanceOption = IntegerValue(12)
}
object ExecType extends EnumTag[Character](150) {
  val New = CharValue('0')
  val PartialFill = CharValue('1')
  val Fill = CharValue('2')
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
object PegOffsetValue extends PriceOffsetTag(211)
object TradSesStatus extends EnumTag[Integer](340) {
  val Unknown = IntegerValue(0)
  val Halted = IntegerValue(1)
  val Open = IntegerValue(2)
  val Closed = IntegerValue(3)
  val PreOpen = IntegerValue(4)
  val PreClose = IntegerValue(5)
  val RequestRejected = IntegerValue(6)
}
object SessionRejectReason extends EnumTag[Integer](373) {
  val InvalidTagNumber = IntegerValue(0)
  val TagMissing = IntegerValue(1)
  val InvalidTag = IntegerValue(2)
  val UndefinedTag = IntegerValue(3)
  val EmptyTag = IntegerValue(4)
  val InvalidValue = IntegerValue(5)
  val InvalidValueFormat = IntegerValue(6)
  val DecryptionProblem = IntegerValue(7)
  val SignatureProblem = IntegerValue(8)
  val CompIdProblem = IntegerValue(9)
  val SendingTimeAccuracyProblem = IntegerValue(10)
  val InvalidMsgType = IntegerValue(11)
  val XmlValidationError = IntegerValue(12)
  val TagMultipleTimes = IntegerValue(13)
  val OutOfOrderTag = IntegerValue(14)
  val OutOfOrderGroupField = IntegerValue(15)
  val NumInGroupMismatch = IntegerValue(16)
  val FieldDelimiterInValue = IntegerValue(17)
}
object ExecRestatementReason extends EnumTag[Integer](378) {
  val GtCorporateAction = IntegerValue(0)
  val GtRenewalRestatement = IntegerValue(1) /* (no corporate action) */
  val VerbalChange = IntegerValue(2)
  val RepricingOfOrder = IntegerValue(3)
  val BrokerOption = IntegerValue(4)
  val PartialDeclineOfOrderQty = IntegerValue(5) /* (e.g. exchange-initiated partial cancel) */
  val CancelOnTradingHalt = IntegerValue(6)
  val CancelOnSystemFailure = IntegerValue(7)
  val MarketOption = IntegerValue(8)
}
object BusinessRejectReason extends EnumTag[Integer](380) {
  val Other = IntegerValue(0)
  val UnknownId = IntegerValue(1)
  val UnknownSecurity = IntegerValue(2)
  val UnknownMessageType = IntegerValue(3)
  val ApplicationNotAvailable = IntegerValue(4)
  val ConditionallyRequiredFieldMissing = IntegerValue(5)
  val NotAuthorized = IntegerValue(6)
  val DeliverToFirmNotAvailable = IntegerValue(7)
}
object PartyIDSource extends EnumTag[Character](447) {
  val Custom = CharValue('D')
}
object PartyID extends StringTag(448)
object NoPartyIDs extends IntegerTag(453)
object NoSecurityAltID extends NumInGroupTag(454)
object SecurityAltID extends StringTag(455)
object SecurityAltIDSource extends StringTag(456)
object NoUnderlyingSecurityAltID extends NumInGroupTag(457)
object UnderlyingSecurityAltID extends StringTag(458)
object UnderlyingSecurityAltIDSource extends StringTag(459)
object TradeReportTransType extends EnumTag[Character](487) {
  val Cancel = IntegerValue('C')
  val New = IntegerValue('N')
  val Replace = IntegerValue('R')
}
object SecondaryClOrdID extends StringTag(526)
object SecondaryExecID extends StringTag(527)
object OrderCapacity extends EnumTag[Character](528) {
  val Agency = CharValue('A')
  val Principal = CharValue('P')
  val Riskless = CharValue('R')
}
object OrderRestrictions extends EnumTag[Character](529) {
  val ProgramTrade = IntegerValue(1)
  val IndexArbitrage = IntegerValue(2)
  val NonIndexArbitrage = IntegerValue(3)
  val CompetingMarketMaker = IntegerValue(4)
}
object NoSides extends NumInGroupTag(552)
object NoLegs extends NumInGroupTag(555)
object SecurityListRequestType extends EnumTag[Integer](559) {
  val Symbol = IntegerValue(0)
  val SecurityTypeCFICode = IntegerValue(1)
  val Product = IntegerValue(2)
  val TradingSessionID = IntegerValue(3)
  val AllSecurities = IntegerValue(4)
}
object SecurityRequestResult extends EnumTag[Integer](560) {
  val ValidReq = IntegerValue(0)
  val InvalidReq = IntegerValue(1)
  val NoInstrumentsFound = IntegerValue(2)
  val NotAuthorized = IntegerValue(3)
  val InstrumentUnavailable = IntegerValue(4)
  val NotSupported = IntegerValue(5)
}
object RoundLot extends QtyTag(561)
object LegPositionEffect extends EnumTag[Character](564) {
  val Close = CharValue('C')
  val FIFO = CharValue('F')
  val Open = CharValue('O')
  val Rolled = CharValue('R')
}
object LegPrice extends PriceTag(566)
object TradeRequestID extends StringTag(568)
object TradeRequestType extends EnumTag[Integer](569) {
  val AllTrades = IntegerValue(0)
  val MatchedTrades = IntegerValue(1)
  val UnmatchedTrades = IntegerValue(2)
  val UnreportedTrades = IntegerValue(3)
  val AdvisoriesMatch = IntegerValue(4)
}
object PreviouslyReported extends BooleanTag(570)
object TradeReportId extends StringTag(571)
object NoDates extends IntegerTag(580)
object AccountType extends EnumTag[Integer](581) {
  val AccountCustomer = IntegerValue(1)
  val AccountNonCustomer = IntegerValue(2)
  val HouseTrader = IntegerValue(3)
  val FloorTrader = IntegerValue(4)
  val AccountNonCustomerCross = IntegerValue(6)
  val HouseTraderCross = IntegerValue(7)
  val JointBOAcct = IntegerValue(8)
}
object ClOrdLinkID extends StringTag(583)
object MassStatusReqID extends StringTag(584)
object MassStatusReqType extends EnumTag[Integer](585) {
  val StatusSecurity = IntegerValue(1)
  val StatusUnderlyingSecurity = IntegerValue(2)
  val StatusProduct = IntegerValue(3)
  val StatusCFICode = IntegerValue(4)
  val StatusSecurityType = IntegerValue(5)
  val StatusTrdSession = IntegerValue(6)
  val StatusAllOrders = IntegerValue(7)
  val StatusPartyID = IntegerValue(8)
}
object LegSymbol extends StringTag(600)
object LegProduct extends EnumTag[Int](607) {
  val Agency = IntegerValue(1)
  val Commodity = IntegerValue(2)
  val Corporate = IntegerValue(3)
  val Currency = IntegerValue(4)
  val Equity = IntegerValue(5)
  val Government = IntegerValue(6)
  val Index = IntegerValue(7)
  val Loan = IntegerValue(8)
  val MoneyMarket = IntegerValue(9)
  val Mortgage = IntegerValue(10)
  val Municipal = IntegerValue(11)
  val Other = IntegerValue(12)
}
object LegCFICode extends StringTag(608)
object LegMaturityMonthYear extends MonthYearTag(610)
object LegStrikePrice extends PriceTag(612)
object LegRatioQty extends FloatTag(623)
object LegSide extends EnumTag[Character](624) {
  val Buy = CharValue('1')
  val Sell = CharValue('2')
  val BuyMinus = CharValue('3')
  val SellPlus = CharValue('4')
  val SellShort = CharValue('5')
  val SellShortExempt = CharValue('6')
  val Undisclosed = CharValue('7')
  val Cross = CharValue('8')
  val CrossShort = CharValue('9')
  val CrossShortExempt = CharValue('A')
  val AsDefined = CharValue('B')
  val Opposite = CharValue('C')
}
object TradingSessionSubID extends StringTag(625)
object Price2 extends PriceTag(640)
object LegRefID extends StringTag(654)
object PositionEffect extends EnumTag[Character](77) {
  val Open = CharValue('O')
  val Close = CharValue('C')
  val Rolled = CharValue('R')
  val FIFO = CharValue('F')
}
object MultiLegReportingType extends EnumTag[Character](442) {
  val Single = CharValue('1')
  val Individual = CharValue('2')
  val Multileg = CharValue('3')
}
object Product extends EnumTag[Int](460) {
  val Agency = IntegerValue(1)
  val Commodity = IntegerValue(2)
  val Corporate = IntegerValue(3)
  val Currency = IntegerValue(4)
  val Equity = IntegerValue(5)
  val Government = IntegerValue(6)
  val Index = IntegerValue(7)
  val Loan = IntegerValue(8)
  val MoneyMarket = IntegerValue(9)
  val Mortgage = IntegerValue(10)
  val Municipal = IntegerValue(11)
  val Other = IntegerValue(12)
}
