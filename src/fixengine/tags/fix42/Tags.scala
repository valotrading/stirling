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
  EnumTag,
  ExchangeTag,
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

object Account extends StringTag(1) {
  val Tag = this
}

object AvgPx extends PriceTag(6) {
  val Tag = this
}

object BeginSeqNo extends IntegerTag(7) {
  val Tag = this
}
object BeginString extends StringTag(8) {
  val Tag = this
}

object BodyLength extends IntegerTag(9) {
  val Tag = this
}

object CheckSum extends StringTag(10) {
  val Tag = this
  def format(checksum: Integer) = String.format("%03d", checksum)
}

object ClOrdID extends StringTag(11) {
  val Tag = this
}

object Commission extends AmtTag(12) {
  val Tag = this
}

object CumQty extends QtyTag(14) {
  val Tag = this
}

object Currency extends CurrencyTag(15) {
  val Tag = this
}

object EndSeqNo extends IntegerTag(16) {
  val Tag = this
}

object ExecID extends StringTag(17) {
  val Tag = this
}

object ExecInst extends StringTag(18) {
  val Tag = this
}

object ExecRefID extends StringTag(19) {
  val Tag = this
}

object ExecTransType extends EnumTag[Integer](20) {
  val Tag = this
  val New = Value(0)
  val Cancel = Value(1)
  val Correct = Value(2)
  val Status = Value(3)
}

object HandlInst extends EnumTag[Character](21) {
  val Tag = this

  /** Automated execution order, private, no Broker intervention */
  val AutomatedOrderPrivate = Value('1')

  /** Automated execution order, public, Broker intervention OK */
  val AutomatedOrderPublic = Value('2')

  /** Manual order, best execution */
  val ManualOrder = Value('3')
}

object IDSource extends StringTag(22) {
  val Tag = this
}

object LastCapacity extends EnumTag[Integer](29) {
  val Tag = this
  val Agent = Value(1)
  val CrossAsAgent = Value(2)
  val CrossAsPrincipal = Value(3)
  val Principal = Value(4)
}

object LastMkt extends ExchangeTag(30) {
  val Tag = this
}

object LastPx extends PriceTag(31) {
  val Tag = this
}

object LastShares extends QtyTag(32) {
  val Tag = this
}

object MsgSeqNum extends IntegerTag(34) {
  val Tag = this
}

object MsgType extends StringTag(35) {
  val Tag = this
}

object NewSeqNo extends IntegerTag(36) {
  val Tag = this
}

object OrderID extends StringTag(37) {
  val Tag = this
}

object OrderQty extends QtyTag(38) {
  val Tag = this
}

object OrdStatus extends EnumTag[Character](39) {
  val Tag = this
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
  val Tag = this
  val Market = Value('1')
  val Limit = Value('2')
  val Stop = Value('3')
  val StopLimit = Value('4')
  val MarketOnClose = Value('5') /* Deprecated */
  val WithOrWithout = Value('6')
  val LimitOrBetter = Value('7')
  val LimitWithOrWithout = Value('8')
  val OnBasis = Value('9')
  val OnClose = Value('A') /* Deprecated */
  val LimitOnClose = Value('B') /* Deprecated */
  val ForexMarket = Value('C') /* Deprecated */
  val PreviouslyQuoted = Value('D')
  val PreviouslyIndicated = Value('E')
  val ForexLimit = Value('F') /* Deprecated */
  val ForexSwap = Value('G')
  val ForexPreviouslyQuoted = Value('H') /* Deprecated */
  val Funari = Value('I')
  val MarketIfTouched = Value('J')
  val MarketWithLeftoverAsLimit = Value('K')
  val PreviousFundValuationPoint = Value('L')
  val NextFundValuationPoint = Value('M')
  val Pegged = Value('P')
}

object OrigClOrdID extends StringTag(41) {
  val Tag = this
}

object OrigTime extends UtcTimestampTag(42) {
  val Tag = this
}

object PossDupFlag extends BooleanTag(43) {
  val Tag = this
}

object Price extends PriceTag(44) {
  val Tag = this
}

object RefSeqNo extends IntegerTag(45) {
  val Tag = this
}

object OrderCapacity extends EnumTag[Character](47) {
  val Tag = this
  val Agency = Value('A') /* Agency single order */
  val Principal = Value('P') /* Principal */
  val Riskless = Value('R') /* Riskless */
}

object SecurityID extends StringTag(48) {
  val Tag = this
}

object SenderCompID extends StringTag(49) {
  val Tag = this
}

object SenderSubID extends StringTag(50) {
  val Tag = this
}

object SendingTime extends UtcTimestampTag(52) {
  val Tag = this
}

object Shares extends QtyTag(53) {
  val Tag = this
}

object Side extends EnumTag[Character](54) {
  val Tag = this
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

object Symbol extends StringTag(55) {
  val Tag = this
}

object TargetCompID extends StringTag(56) {
  val Tag = this
}

object TargetSubID extends StringTag(57) {
  val Tag = this
}

object Text extends StringTag(58) {
  val Tag = this
}

object TimeInForce extends EnumTag[Integer](59) {
  val Tag = this
  val Day = Value(0)
  val GoodTillCancel = Value(1)
  val AtTheOpening = Value(2)
  val ImmediateOrCancel = Value(3)
  val FillOrKill = Value(4)
  val GoodTillCrossing = Value(5)
  val GoodTillDate = Value(6)
  val AtTheClose = Value(7)
}

object TransactTime extends UtcTimestampTag(60) {
  val Tag = this
}

object FutSettDate extends LocalMktDateTag(64) {
  val Tag = this
}

object AllocID extends StringTag(70) {
  val Tag = this
}

object AllocTransType extends EnumTag[Integer](71) {
  val Tag = this
  val New = Value(0)
  val Replace = Value(1)
  val Cancel = Value(2)
  val Preliminary = Value(3)                 /* (without MiscFees and NetMoney) */
  val Calculated = Value(4)                  /* (includes MiscFees and NetMoney) */
  val CalculatedWithoutPrelminary = Value(5) /* Calculated without Preliminary (sent unsolicited by broker, includes MiscFees and NetMoney) */
}

object NoOrders extends IntegerTag(73) {
  val Tag = this
}

object TradeDate extends LocalMktDateTag(75) {
  val Tag = this
}

object ExecBroker extends StringTag(76) {
  val Tag = this
}

object NoAllocs extends IntegerTag(78) {
  val Tag = this
}

object AllocAccount extends StringTag(79) {
  val Tag = this
}

object AllocShares extends QtyTag(80) {
  val Tag = this
}

object PossResend extends BooleanTag(97) {
  val Tag = this
}

object EncryptMethod extends EnumTag[Integer](98) {
  val Tag = this

  /** None / other */
  val None = Value(0)

  /** PKCS (proprietary) */
  val Pkcs = Value(1)

  /** DES (ECB mode) */
  val Des = Value(2)

  /** PKCS/DES (proprietary) */
  val PkcsDes = Value(3)

  /** PGP/DES (defunct) */
  val PgpDes = Value(4)

  /** PGP/DES-MD5 */
  val PgpDesMd5 = Value(5)

  /** PEM/DES-MD5 */
  val PemDesMd5 = Value(6)
}

object StopPx extends PriceTag(99) {
  val Tag = this
}

object ExDestination extends ExchangeTag(100) {
  val Tag = this
}

object CxlRejReason extends EnumTag[Integer](102) {
  val Tag = this

  /** Too late to cancel.  */
  val TooLateToCancel = Value(0)

  /** Unknown order.  */
  val UnknownOrder = Value(1)

  /** Broker/Exchange Option.  */
  val BrokerExchangeOption = Value(2)

  /** Order already in Pending Cancel or Pending Replace status.  */
  val PendingCancelOrReplace = Value(3)
}

object OrdRejReason extends EnumTag[Integer](103) {
  val Tag = this
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

object HeartBtInt extends IntegerTag(108) {
  val Tag = this
}

object ClientID extends StringTag(109) {
  val Tag = this
}

object MinQty extends QtyTag(110) {
  val Tag = this
}

object MaxFloor extends QtyTag(111) {
  val Tag = this
}

object TestReqID extends StringTag(112) {
  val Tag = this
}

object LocateReqd extends BooleanTag(114) {
  val Tag = this
}

object OnBehalfOfCompID extends StringTag(115) {
  val Tag = this
}

object SettlCurrAmt extends AmtTag(119) {
  val Tag = this
}

object SettlCurrency extends CurrencyTag(120) {
  val Tag = this
}

object OrigSendingTime extends UtcTimestampTag(122) {
  val Tag = this
}

object GapFillFlag extends BooleanTag(123) {
  val Tag = this
}

object ExpireTime extends UtcTimestampTag(126) {
  val Tag = this
}

object DKReason extends EnumTag[Character](127) {
  val Tag = this
  val UnknownSymbol = Value('A')
  val WrongSide = Value('B')
  val QuantityExceedsOrder = Value('C')
  val NoMatchingOrder = Value('D')
  val PriceExceedsLimit = Value('E')
  val Other = Value('Z')
}

object DeliverToCompID extends StringTag(128) {
  val Tag = this
}

object DeliverToSubID extends StringTag(129) {
  val Tag = this
}

object ResetSeqNumFlag extends BooleanTag(141) {
  val Tag = this
}

object SenderLocationID extends StringTag(142) {
  val Tag = this
}

object ExecType extends EnumTag[Character](150) {
  val Tag = this
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

object LeavesQty extends QtyTag(151) {
  val Tag = this
}

object SecurityType extends StringTag(167) {
  val Tag = this
}

object EffectiveTime extends UtcTimestampTag(168) {
  val Tag = this
}

object OrderQty2 extends QtyTag(192) {
  val Tag = this
}

object SecondaryOrderID extends StringTag(198) {
  val Tag = this
}

object MaturityMonthYear extends StringTag(200) {
  val Tag = this
}

object PutOrCall extends IntegerTag(201) {
  val Tag = this
}

object StrikePrice extends PriceTag(202) {
  val Tag = this
}

object CustomerOrFirm extends EnumTag[Integer](204) {
  val Tag = this
  val Customer = Value(0)
  val Firm = Value(1)
}

object SecurityExchange extends ExchangeTag(207) {
  val Tag = this
}

object MaxShow extends QtyTag(210) {
  val Tag = this
}

object PegDifference extends PriceOffsetTag(211) {
  val Tag = this
}

object SubscriptionRequestType extends EnumTag[Character](263) {
  val Tag = this
  val Snapshot = Value('0')
  val SnapshotUpdate = Value('1')
  val Unsubscribe = Value('2')
}

object UnsolicitedIndicator extends BooleanTag(325) {
  val Tag = this
}

object TradSesReqID extends StringTag(335) {
  val Tag = this
}

object TradeSesReqID extends StringTag(335) {
  val Tag = this
}

object TradingSessionID extends StringTag(336) {
  val Tag = this
}

object TradSesMethod extends EnumTag[Integer](338) {
  val Tag = this
  val Electronic = Value(1)
  val OpenOutcry = Value(2)
  val TwoParty = Value(3)
}

object TradSesMode extends EnumTag[Integer](339) {
  val Tag = this
  val Testing = Value(1)
  val Simulated = Value(2)
  val Production = Value(3)
}

object RefTagId extends IntegerTag(371) {
  val Tag = this
}

object RefMsgType extends StringTag(372) {
  val Tag = this
}

object ContraBroker extends StringTag(375) {
  val Tag = this
}

object ComplianceID extends StringTag(376) {
  val Tag = this
}

object BusinessRejectReason extends EnumTag[Integer](380) {
  val Tag = this

  /** Other.  */
  val Other = Value(0)

  /** Unknown ID.  */
  val UnknownId = Value(1)

  /** Unknown Security.  */
  val UnknownSecurity = Value(2)

  /** Unknown Message Type.  */
  val UnknownMessageType = Value(3)

  /** Application not available.  */
  val ApplicationNotAvailable = Value(4)

  /** Conditionally Required Field Missing.  */
  val ConditionallyRequiredFieldMissing = Value(5)
}

object NoContraBrokers extends IntegerTag(382) {
  val Tag = this
}

object DiscretionInst extends EnumTag[Integer](388) {
  val Tag = this
  val RelatedToDisplayedPrice = Value(0)
  val RelatedToMarketPrice = Value(1)
  val RelatedToPrimaryPrice = Value(2)
  val RelatedToLocalPrimaryPrice = Value(3)
  val RelatedToMidpoint = Value(4)
  val RelatedToLastTradePRice = Value(5)
}

object DiscretionOffset extends PriceOffsetTag(389) {
  val Tag = this
}

object CxlRejResponseTo extends EnumTag[Character](434) {
  val Tag = this
  val OrderCancelRequest = Value('1')
  val OrderModificationRequest = Value('2')
}

object ClearingFirm extends StringTag(439) {
  val Tag = this
}

object ClearingAccount extends StringTag(440) {
  val Tag = this
}
