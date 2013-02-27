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
package stirling.fix.tags.fix42

import stirling.fix.messages.{
  AmtTag,
  BooleanTag,
  CharTag,
  EnumTag,
  ExchangeTag,
  FloatTag,
  IntegerTag,
  LocalMktDateTag,
  PriceTag,
  PriceOffsetTag,
  QtyTag,
  StringTag,
  CurrencyTag,
  UtcTimestampTag,
  CharValue,
  IntegerValue
}
import java.lang.{
  Character,
  Integer
}

object Account extends StringTag(1)
object AvgPx extends PriceTag(6)
object BeginSeqNo extends IntegerTag(7)
object BeginString extends StringTag(8)
object BodyLength extends IntegerTag(9)
object CheckSum extends StringTag(10) {
  def format(checksum: Integer) = String.format("%03d", checksum)
}
object ClOrdID extends StringTag(11)
object Commission extends AmtTag(12)
object CumQty extends QtyTag(14)
object Currency extends CurrencyTag(15)
object EndSeqNo extends IntegerTag(16)
object ExecID extends StringTag(17)
object ExecInst extends StringTag(18)
object ExecRefID extends StringTag(19)
object ExecTransType extends EnumTag[Integer](20) {
  val New = IntegerValue(0)
  val Cancel = IntegerValue(1)
  val Correct = IntegerValue(2)
  val Status = IntegerValue(3)
}
object HandlInst extends EnumTag[Character](21) {
  val AutomatedOrderPrivate = CharValue('1')
  val AutomatedOrderPublic = CharValue('2')
  val ManualOrder = CharValue('3')
}
object IDSource extends StringTag(22)
object LastCapacity extends EnumTag[Integer](29) {
  val Agent = IntegerValue(1)
  val CrossAsAgent = IntegerValue(2)
  val CrossAsPrincipal = IntegerValue(3)
  val Principal = IntegerValue(4)
}
object LastMkt extends ExchangeTag(30)
object LastPx extends PriceTag(31)
object LastShares extends QtyTag(32)
object LinesOfText extends IntegerTag(33)
object MsgSeqNum extends IntegerTag(34)
object MsgType extends StringTag(35)
object NewSeqNo extends IntegerTag(36)
object OrderID extends StringTag(37)
object OrderQty extends QtyTag(38)
object OrdStatus extends EnumTag[Character](39) {
  val New = CharValue('0')
  val PartiallyFilled = CharValue('1')
  val Filled = CharValue('2')
  val DoneForDay = CharValue('3')
  val Canceled = CharValue('4')
  val Replaced = CharValue('5')
  val PendingCancel = CharValue('6')
  val Stopped = CharValue('7')
  val Rejected = CharValue('8')
  val Suspended = CharValue('9')
  val PendingNew = CharValue('A')
  val Calculated = CharValue('B')
  val Expired = CharValue('C')
  val AcceptedForBidding = CharValue('D')
  val PendingReplace = CharValue('E')
}
object OrdType extends EnumTag[Character](40) {
  val Market = CharValue('1')
  val Limit = CharValue('2')
  val Stop = CharValue('3')
  val StopLimit = CharValue('4')
  val MarketOnClose = CharValue('5')
  val WithOrWithout = CharValue('6')
  val LimitOrBetter = CharValue('7')
  val LimitWithOrWithout = CharValue('8')
  val OnBasis = CharValue('9')
  val OnClose = CharValue('A')
  val LimitOnClose = CharValue('B')
  val ForexMarket = CharValue('C')
  val PreviouslyQuoted = CharValue('D')
  val PreviouslyIndicated = CharValue('E')
  val ForexLimit = CharValue('F')
  val ForexSwap = CharValue('G')
  val ForexPreviouslyQuoted = CharValue('H')
  val Funari = CharValue('I')
  val MarketIfTouched = CharValue('J')
  val MarketWithLeftoverAsLimit = CharValue('K')
  val PreviousFundValuationPoint = CharValue('L')
  val NextFundValuationPoint = CharValue('M')
  val Pegged = CharValue('P')
}
object OrigClOrdID extends StringTag(41)
object OrigTime extends UtcTimestampTag(42)
object PossDupFlag extends BooleanTag(43)
object Price extends PriceTag(44)
object RefSeqNo extends IntegerTag(45)
object OrderCapacity extends EnumTag[Character](47) {
  val Agency = CharValue('A')
  val Principal = CharValue('P')
  val Riskless = CharValue('R')
}
object SecurityID extends StringTag(48)
object SenderCompID extends StringTag(49)
object SenderSubID extends StringTag(50)
object SendingTime extends UtcTimestampTag(52)
object Shares extends QtyTag(53)
object Side extends EnumTag[Character](54) {
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
object Symbol extends StringTag(55)
object TargetCompID extends StringTag(56)
object TargetSubID extends StringTag(57)
object Text extends StringTag(58)
object TimeInForce extends EnumTag[Integer](59) {
  val Day = IntegerValue(0)
  val GoodTillCancel = IntegerValue(1)
  val AtTheOpening = IntegerValue(2)
  val ImmediateOrCancel = IntegerValue(3)
  val FillOrKill = IntegerValue(4)
  val GoodTillCrossing = IntegerValue(5)
  val GoodTillDate = IntegerValue(6)
  val AtTheClose = IntegerValue(7)
}
object TransactTime extends UtcTimestampTag(60)
object Urgency extends EnumTag[Character](61) {
  val Normal = IntegerValue(0)
  val Flash = IntegerValue(1)
  val Background = IntegerValue(2)
}
object FutSettDate extends LocalMktDateTag(64)
object SymbolSfx extends StringTag(65)
object AllocID extends StringTag(70)
object AllocTransType extends EnumTag[Integer](71) {
  val New = IntegerValue(0)
  val Replace = IntegerValue(1)
  val Cancel = IntegerValue(2)
  val Preliminary = IntegerValue(3)
  val Calculated = IntegerValue(4)
  val CalculatedWithoutPrelminary = IntegerValue(5)
}
object NoOrders extends IntegerTag(73)
object TradeDate extends LocalMktDateTag(75)
object ExecBroker extends StringTag(76)
object NoAllocs extends IntegerTag(78)
object AllocAccount extends StringTag(79)
object AllocShares extends QtyTag(80)
object PossResend extends BooleanTag(97)
object EncryptMethod extends EnumTag[Integer](98) {
  val None = IntegerValue(0)
  val Pkcs = IntegerValue(1)
  val Des = IntegerValue(2)
  val PkcsDes = IntegerValue(3)
  val PgpDes = IntegerValue(4)
  val PgpDesMd5 = IntegerValue(5)
  val PemDesMd5 = IntegerValue(6)
}
object StopPx extends PriceTag(99)
object ExDestination extends ExchangeTag(100)
object CxlRejReason extends EnumTag[Integer](102) {
  val TooLateToCancel = IntegerValue(0)
  val UnknownOrder = IntegerValue(1)
  val BrokerExchangeOption = IntegerValue(2)
  val PendingCancelOrReplace = IntegerValue(3)
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
}
object Issuer extends CharTag(106)
object SecurityDesc extends CharTag(107)
object HeartBtInt extends IntegerTag(108)
object ClientID extends StringTag(109)
object MinQty extends QtyTag(110)
object MaxFloor extends QtyTag(111)
object TestReqID extends StringTag(112)
object LocateReqd extends BooleanTag(114)
object OnBehalfOfCompID extends StringTag(115)
object OnBehalfOfSubID extends StringTag(116)
object QuoteID extends CharTag(117)
object SettlCurrAmt extends AmtTag(119)
object SettlCurrency extends CurrencyTag(120)
object OrigSendingTime extends UtcTimestampTag(122)
object GapFillFlag extends BooleanTag(123)
object ExpireTime extends UtcTimestampTag(126)
object DKReason extends EnumTag[Character](127) {
  val UnknownSymbol = CharValue('A')
  val WrongSide = CharValue('B')
  val QuantityExceedsOrder = CharValue('C')
  val NoMatchingOrder = CharValue('D')
  val PriceExceedsLimit = CharValue('E')
  val Other = CharValue('Z')
}
object DeliverToCompID extends StringTag(128)
object DeliverToSubID extends StringTag(129)
object BidPx extends FloatTag(132)
object OfferPx extends FloatTag(133)
object BidSize extends IntegerTag(134)
object OfferSize extends IntegerTag(135)
object ResetSeqNumFlag extends BooleanTag(141)
object SenderLocationID extends StringTag(142)
object TargetLocationID extends StringTag(143)
object NoRelatedSym extends IntegerTag(146)
object Headline extends StringTag(148)
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
}
object LeavesQty extends QtyTag(151)
object SecurityType extends StringTag(167)
object EffectiveTime extends UtcTimestampTag(168)
object OrderQty2 extends QtyTag(192)
object SecondaryOrderID extends StringTag(198)
object MaturityMonthYear extends StringTag(200)
object PutOrCall extends EnumTag[Integer](201) {
  val Put = IntegerValue(0)
  val Call = IntegerValue(1)
}
object StrikePrice extends PriceTag(202)
object CustomerOrFirm extends EnumTag[Integer](204) {
  val Customer = IntegerValue(0)
  val Firm = IntegerValue(1)
}
object SecurityExchange extends ExchangeTag(207)
object MaxShow extends QtyTag(210)
object PegDifference extends PriceOffsetTag(211)
object ContractMultiplier extends FloatTag(231)
object SubscriptionRequestType extends EnumTag[Character](263) {
  val Snapshot = CharValue('0')
  val SnapshotUpdate = CharValue('1')
  val Unsubscribe = CharValue('2')
}
object DefBidSize extends QtyTag(293)
object NoQuoteEntries extends IntegerTag(295)
object NoQuoteSets extends IntegerTag(296)
object QuoteEntryID extends StringTag(299)
object QuoteResponseLevel extends EnumTag[Integer](301) {
  val NoAck = IntegerValue(0)
  val AckNeg = IntegerValue(1)
  val AckEach = IntegerValue(2)
}
object QuoteSetID extends StringTag(302)
object TotQuoteEntries extends IntegerTag(304)
object UnderlyingSecurityDesc extends StringTag(307)
object UnderlyingSecurityID extends StringTag(309)
object UnderlyingSecurityType extends StringTag(310)
object UnderlyingSymbol extends StringTag(311)
object UnderlyingCurrency extends CurrencyTag(318)
object SecurityReqID extends StringTag(320)
object SecurityResponseID extends StringTag(322)
object UnsolicitedIndicator extends BooleanTag(325)
object TradSesReqID extends StringTag(335)
object TradeSesReqID extends StringTag(335)
object TradingSessionID extends StringTag(336)
object TradSesMethod extends EnumTag[Integer](338) {
  val Electronic = IntegerValue(1)
  val OpenOutcry = IntegerValue(2)
  val TwoParty = IntegerValue(3)
}
object TradSesMode extends EnumTag[Integer](339) {
  val Testing = IntegerValue(1)
  val Simulated = IntegerValue(2)
  val Production = IntegerValue(3)
}
object RefTagId extends IntegerTag(371)
object RefMsgType extends StringTag(372)
object ContraBroker extends StringTag(375)
object ComplianceID extends StringTag(376)
object BusinessRejectRefID extends StringTag(379)
object BusinessRejectReason extends EnumTag[Integer](380) {
  val Other = IntegerValue(0)
  val UnknownId = IntegerValue(1)
  val UnknownSecurity = IntegerValue(2)
  val UnknownMessageType = IntegerValue(3)
  val ApplicationNotAvailable = IntegerValue(4)
  val ConditionallyRequiredFieldMissing = IntegerValue(5)
}
object NoContraBrokers extends IntegerTag(382)
object DiscretionInst extends EnumTag[Integer](388) {
  val RelatedToDisplayedPrice = IntegerValue(0)
  val RelatedToMarketPrice = IntegerValue(1)
  val RelatedToPrimaryPrice = IntegerValue(2)
  val RelatedToLocalPrimaryPrice = IntegerValue(3)
  val RelatedToMidpoint = IntegerValue(4)
  val RelatedToLastTradePRice = IntegerValue(5)
}
object DiscretionOffset extends PriceOffsetTag(389)
object TotalNumSecurities extends IntegerTag(393)
object CxlRejResponseTo extends EnumTag[Character](434) {
  val OrderCancelRequest = CharValue('1')
  val OrderCancelReplaceRequest = CharValue('2')
}
object ClearingFirm extends StringTag(439)
object ClearingAccount extends StringTag(440)
