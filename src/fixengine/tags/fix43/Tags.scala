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
package fixengine.tags.fix43

import fixengine.messages.{
  BooleanTag,
  EnumTag,
  FloatTag,
  IntegerTag,
  MonthYearTag,
  NumInGroupTag,
  PriceTag,
  PriceOffsetTag,
  QtyTag,
  StringTag,
  Value
}
import java.lang.{
  Character,
  Integer
}

object SecurityIDSource extends EnumTag[Character](22) {
  val ISIN = Value('4')
  val ExchangeSymbol = Value('8')
}
object CxlRejReason extends EnumTag[Integer](102) {
  val TooLateToCancel = Value(0)
  val UnknownOrder = Value(1)
  val BrokerExchangeOption = Value(2)
  val PendingCancelOrReplace = Value(3)
  val UnableToProcessRequest = Value(4)
  val TimeMismatch = Value(5)
  val DuplicateClOrdId = Value(6)
}
object OrdRejReason extends EnumTag[Integer](103) {
  val BrokerExchangeOption = Value(0)
  val UnknownSymbol = Value(1)
  val ExchangeClosed = Value(2)
  val OrderExceedsLimit = Value(3)
  val TooLateToEnter = Value(4)
  val UnknownOrder = Value(5)
  val DuplicateOrder = Value(6)
  val DuplicateOfVerballyCommunicatedOrder = Value(7)
  val StaleOrder = Value(8)
  val TradeAlongRequired = Value(9)
  val InvalidInvestorId = Value(10)
  val UnsupportedOrderCharacteristic = Value(11)
  val SurveillanceOption = Value(12)
}
object ExecType extends EnumTag[Character](150) {
  val New = Value('0')
  val PartialFill = Value('1')
  val Fill = Value('2')
  val DoneForDay = Value('3')
  val Canceled = Value('4')
  val Replace = Value('5')
  val PendingCancel = Value('6')
  val Stopped = Value('7')
  val Rejected = Value('8')
  val Suspended = Value('9')
  val PendingNew = Value('A')
  val Calculated = Value('B')
  val Expired = Value('C')
  val Restated = Value('D')
  val PendingReplace = Value('E')
  val Trade = Value('F')
  val TradeCorrect = Value('G')
  val TradeCancel = Value('H')
  val OrderStatus = Value('I')
}
object PegOffsetValue extends PriceOffsetTag(211)
object TradSesStatus extends EnumTag(340) {
  val Unknown = Value(0)
  val Halted = Value(1)
  val Open = Value(2)
  val Closed = Value(3)
  val PreOpen = Value(4)
  val PreClose = Value(5)
  val RequestRejected = Value(6)
}

object SessionRejectReason extends EnumTag[Integer](373) {
  val InvalidTagNumber = Value(0)
  val TagMissing = Value(1)
  val InvalidTag = Value(2)
  val UndefinedTag = Value(3)
  val EmptyTag = Value(4)
  val InvalidValue = Value(5)
  val InvalidValueFormat = Value(6)
  val DecryptionProblem = Value(7)
  val SignatureProblem = Value(8)
  val CompIdProblem = Value(9)
  val SendingTimeAccuracyProblem = Value(10)
  val InvalidMsgType = Value(11)
  val XmlValidationError = Value(12)
  val TagMultipleTimes = Value(13)
  val OutOfOrderTag = Value(14)
  val OutOfOrderGroupField = Value(15)
  val NumInGroupMismatch = Value(16)
  val FieldDelimiterInValue = Value(17)
}
object ExecRestatementReason extends EnumTag[Integer](378) {
  val GtCorporateAction = Value(0)
  val GtRenewalRestatement = Value(1) /* (no corporate action) */
  val VerbalChange = Value(2)
  val RepricingOfOrder = Value(3)
  val BrokerOption = Value(4)
  val PartialDeclineOfOrderQty = Value(5) /* (e.g. exchange-initiated partial cancel) */
  val CancelOnTradingHalt = Value(6)
  val CancelOnSystemFailure = Value(7)
  val MarketOption = Value(8)
}
object BusinessRejectReason extends EnumTag[Integer](380) {
  val Other = Value(0)
  val UnknownId = Value(1)
  val UnknownSecurity = Value(2)
  val UnknownMessageType = Value(3)
  val ApplicationNotAvailable = Value(4)
  val ConditionallyRequiredFieldMissing = Value(5)
  val NotAuthorized = Value(6)
  val DeliverToFirmNotAvailable = Value(7)
}
object PartyIDSource extends EnumTag[Character](447) {
  val Custom = Value('D')
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
  val Cancel = Value('C')
  val New = Value('N')
  val Replace = Value('R')
}
object SecondaryClOrdID extends StringTag(526)
object SecondaryExecID extends StringTag(527)
object OrderCapacity extends EnumTag[Character](528) {
  val Agency = Value('A')
  val Principal = Value('P')
  val Riskless = Value('R')
}
object OrderRestrictions extends EnumTag[Character](529) {
  val ProgramTrade = Value(1)
  val IndexArbitrage = Value(2)
  val NonIndexArbitrage = Value(3)
  val CompetingMarketMaker = Value(4)
}
object NoSides extends NumInGroupTag(552)
object NoLegs extends NumInGroupTag(555)
object SecurityListRequestType extends EnumTag[Integer](559) {
  val Symbol = Value(0)
  val SecurityTypeCFICode = Value(1)
  val Product = Value(2)
  val TradingSessionID = Value(3)
  val AllSecurities = Value(4)
}
object SecurityRequestResult extends EnumTag[Integer](560) {
  val ValidReq = Value(0)
  val InvalidReq = Value(1)
  val NoInstrumentsFound = Value(2)
  val NotAuthorized = Value(3)
  val InstrumentUnavailable = Value(4)
  val NotSupported = Value(5)
}
object RoundLot extends QtyTag(561)
object LegPositionEffect extends EnumTag[Character](564) {
  val Close = Value('C')
  val FIFO = Value('F')
  val Open = Value('O')
  val Rolled = Value('R')
}
object LegPrice extends PriceTag(566)
object TradeRequestID extends StringTag(568)
object TradeRequestType extends EnumTag[Integer](569) {
  val AllTrades = Value(0)
  val MatchedTrades = Value(1)
  val UnmatchedTrades = Value(2)
  val UnreportedTrades = Value(3)
  val AdvisoriesMatch = Value(4)
}
object PreviouslyReported extends BooleanTag(570)
object TradeReportId extends StringTag(571)
object NoDates extends IntegerTag(580)
object AccountType extends EnumTag[Integer](581) {
  val AccountCustomer = Value(1)
  val AccountNonCustomer = Value(2)
  val HouseTrader = Value(3)
  val FloorTrader = Value(4)
  val AccountNonCustomerCross = Value(6)
  val HouseTraderCross = Value(7)
  val JointBOAcct = Value(8)
}
object MassStatusReqID extends StringTag(584)
object MassStatusReqType extends EnumTag[Integer](585) {
  val StatusSecurity = Value(1)
  val StatusUnderlyingSecurity = Value(2)
  val StatusProduct = Value(3)
  val StatusCFICode = Value(4)
  val StatusSecurityType = Value(5)
  val StatusTrdSession = Value(6)
  val StatusAllOrders = Value(7)
  val StatusPartyID = Value(8)
}
object LegSymbol extends StringTag(600)
object LegCFICode extends StringTag(608)
object LegMaturityMonthYear extends MonthYearTag(610)
object LegStrikePrice extends PriceTag(612)
object LegRatioQty extends FloatTag(623)
object LegSide extends EnumTag[Character](624) {
  val Buy = Value('1')
  val Sell = Value('2')
  val BuyMinus = Value('3')
  val SellPlus = Value('4')
  val SellShort = Value('5')
  val SellShortExempt = Value('6')
  val Undisclosed = Value('7')
  val Cross = Value('8')
  val CrossShort = Value('9')
  val CrossShortExempt = Value('A')
  val AsDefined = Value('B')
  val Opposite = Value('C')
}
object TradingSessionSubID extends StringTag(625)
object Price2 extends PriceTag(640)
object LegRefID extends StringTag(654)
