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
package fixengine.tags.fix42

import fixengine.messages.{
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
  Value
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
  val New = Value(0)
  val Cancel = Value(1)
  val Correct = Value(2)
  val Status = Value(3)
}
object HandlInst extends EnumTag[Character](21) {
  val AutomatedOrderPrivate = Value('1')
  val AutomatedOrderPublic = Value('2')
  val ManualOrder = Value('3')
}
object IDSource extends StringTag(22)
object LastCapacity extends EnumTag[Integer](29) {
  val Agent = Value(1)
  val CrossAsAgent = Value(2)
  val CrossAsPrincipal = Value(3)
  val Principal = Value(4)
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
  val New = Value('0')
  val PartiallyFilled = Value('1')
  val Filled = Value('2')
  val DoneForDay = Value('3')
  val Canceled = Value('4')
  val Replaced = Value('5')
  val PendingCancel = Value('6')
  val Stopped = Value('7')
  val Rejected = Value('8')
  val Suspended = Value('9')
  val PendingNew = Value('A')
  val Calculated = Value('B')
  val Expired = Value('C')
  val AcceptedForBidding = Value('D')
  val PendingReplace = Value('E')
}
object OrdType extends EnumTag[Character](40) {
  val Market = Value('1')
  val Limit = Value('2')
  val Stop = Value('3')
  val StopLimit = Value('4')
  val MarketOnClose = Value('5')
  val WithOrWithout = Value('6')
  val LimitOrBetter = Value('7')
  val LimitWithOrWithout = Value('8')
  val OnBasis = Value('9')
  val OnClose = Value('A')
  val LimitOnClose = Value('B')
  val ForexMarket = Value('C')
  val PreviouslyQuoted = Value('D')
  val PreviouslyIndicated = Value('E')
  val ForexLimit = Value('F')
  val ForexSwap = Value('G')
  val ForexPreviouslyQuoted = Value('H')
  val Funari = Value('I')
  val MarketIfTouched = Value('J')
  val MarketWithLeftoverAsLimit = Value('K')
  val PreviousFundValuationPoint = Value('L')
  val NextFundValuationPoint = Value('M')
  val Pegged = Value('P')
}
object OrigClOrdID extends StringTag(41)
object OrigTime extends UtcTimestampTag(42)
object PossDupFlag extends BooleanTag(43)
object Price extends PriceTag(44)
object RefSeqNo extends IntegerTag(45)
object OrderCapacity extends EnumTag[Character](47) {
  val Agency = Value('A')
  val Principal = Value('P')
  val Riskless = Value('R')
}
object SecurityID extends StringTag(48)
object SenderCompID extends StringTag(49)
object SenderSubID extends StringTag(50)
object SendingTime extends UtcTimestampTag(52)
object Shares extends QtyTag(53)
object Side extends EnumTag[Character](54) {
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
object Symbol extends StringTag(55)
object TargetCompID extends StringTag(56)
object TargetSubID extends StringTag(57)
object Text extends StringTag(58)
object TimeInForce extends EnumTag[Integer](59) {
  val Day = Value(0)
  val GoodTillCancel = Value(1)
  val AtTheOpening = Value(2)
  val ImmediateOrCancel = Value(3)
  val FillOrKill = Value(4)
  val GoodTillCrossing = Value(5)
  val GoodTillDate = Value(6)
  val AtTheClose = Value(7)
}
object TransactTime extends UtcTimestampTag(60)
object Urgency extends EnumTag[Character](61) {
  val Normal = Value(0)
  val Flash = Value(1)
  val Background = Value(2)
}
object FutSettDate extends LocalMktDateTag(64)
object AllocID extends StringTag(70)
object AllocTransType extends EnumTag[Integer](71) {
  val New = Value(0)
  val Replace = Value(1)
  val Cancel = Value(2)
  val Preliminary = Value(3)
  val Calculated = Value(4)
  val CalculatedWithoutPrelminary = Value(5)
}
object NoOrders extends IntegerTag(73)
object TradeDate extends LocalMktDateTag(75)
object ExecBroker extends StringTag(76)
object NoAllocs extends IntegerTag(78)
object AllocAccount extends StringTag(79)
object AllocShares extends QtyTag(80)
object PossResend extends BooleanTag(97)
object EncryptMethod extends EnumTag[Integer](98) {
  val None = Value(0)
  val Pkcs = Value(1)
  val Des = Value(2)
  val PkcsDes = Value(3)
  val PgpDes = Value(4)
  val PgpDesMd5 = Value(5)
  val PemDesMd5 = Value(6)
}
object StopPx extends PriceTag(99)
object ExDestination extends ExchangeTag(100)
object CxlRejReason extends EnumTag[Integer](102) {
  val TooLateToCancel = Value(0)
  val UnknownOrder = Value(1)
  val BrokerExchangeOption = Value(2)
  val PendingCancelOrReplace = Value(3)
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
object QuoteID extends CharTag(117)
object SettlCurrAmt extends AmtTag(119)
object SettlCurrency extends CurrencyTag(120)
object OrigSendingTime extends UtcTimestampTag(122)
object GapFillFlag extends BooleanTag(123)
object ExpireTime extends UtcTimestampTag(126)
object DKReason extends EnumTag[Character](127) {
  val UnknownSymbol = Value('A')
  val WrongSide = Value('B')
  val QuantityExceedsOrder = Value('C')
  val NoMatchingOrder = Value('D')
  val PriceExceedsLimit = Value('E')
  val Other = Value('Z')
}
object DeliverToCompID extends StringTag(128)
object DeliverToSubID extends StringTag(129)
object BidPx extends FloatTag(132)
object OfferPx extends FloatTag(133)
object BidSize extends IntegerTag(134)
object OfferSize extends IntegerTag(135)
object ResetSeqNumFlag extends BooleanTag(141)
object SenderLocationID extends StringTag(142)
object NoRelatedSym extends IntegerTag(146)
object Headline extends StringTag(148)
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
}
object LeavesQty extends QtyTag(151)
object SecurityType extends StringTag(167)
object EffectiveTime extends UtcTimestampTag(168)
object OrderQty2 extends QtyTag(192)
object SecondaryOrderID extends StringTag(198)
object MaturityMonthYear extends StringTag(200)
object PutOrCall extends EnumTag[Integer](201) {
  val Put = Value(0)
  val Call = Value(1)
}
object StrikePrice extends PriceTag(202)
object CustomerOrFirm extends EnumTag[Integer](204) {
  val Customer = Value(0)
  val Firm = Value(1)
}
object SecurityExchange extends ExchangeTag(207)
object MaxShow extends QtyTag(210)
object PegDifference extends PriceOffsetTag(211)
object ContractMultiplier extends FloatTag(231)
object SubscriptionRequestType extends EnumTag[Character](263) {
  val Snapshot = Value('0')
  val SnapshotUpdate = Value('1')
  val Unsubscribe = Value('2')
}
object DefBidSize extends QtyTag(293)
object NoQuoteEntries extends IntegerTag(295)
object NoQuoteSets extends IntegerTag(296)
object QuoteEntryID extends StringTag(299)
object QuoteResponseLevel extends EnumTag[Integer](301) {
  val NoAck = Value(0)
  val AckNeg = Value(1)
  val AckEach = Value(2)
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
  val Electronic = Value(1)
  val OpenOutcry = Value(2)
  val TwoParty = Value(3)
}
object TradSesMode extends EnumTag[Integer](339) {
  val Testing = Value(1)
  val Simulated = Value(2)
  val Production = Value(3)
}
object RefTagId extends IntegerTag(371)
object RefMsgType extends StringTag(372)
object ContraBroker extends StringTag(375)
object ComplianceID extends StringTag(376)
object BusinessRejectRefID extends StringTag(379)
object BusinessRejectReason extends EnumTag[Integer](380) {
  val Other = Value(0)
  val UnknownId = Value(1)
  val UnknownSecurity = Value(2)
  val UnknownMessageType = Value(3)
  val ApplicationNotAvailable = Value(4)
  val ConditionallyRequiredFieldMissing = Value(5)
}
object NoContraBrokers extends IntegerTag(382)
object DiscretionInst extends EnumTag[Integer](388) {
  val RelatedToDisplayedPrice = Value(0)
  val RelatedToMarketPrice = Value(1)
  val RelatedToPrimaryPrice = Value(2)
  val RelatedToLocalPrimaryPrice = Value(3)
  val RelatedToMidpoint = Value(4)
  val RelatedToLastTradePRice = Value(5)
}
object DiscretionOffset extends PriceOffsetTag(389)
object TotalNumSecurities extends IntegerTag(393)
object CxlRejResponseTo extends EnumTag[Character](434) {
  val OrderCancelRequest = Value('1')
  val OrderModificationRequest = Value('2')
}
object ClearingFirm extends StringTag(439)
object ClearingAccount extends StringTag(440)
