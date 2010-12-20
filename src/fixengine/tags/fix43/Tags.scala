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
  EnumTag,
  FloatTag,
  IntegerTag,
  MonthYearTag,
  NumInGroupTag,
  PriceTag,
  PriceOffsetTag,
  StringTag,
  Value
}
import java.lang.{
  Character,
  Integer
}

object SecurityIDSource extends EnumTag[Character](22) {
  val Tag = this
  val ISIN = Value('4')
  val ExchangeSymbol = Value('8')
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

  /** Unable to process Order Mass Cancel Request.  */
  val UnableToProcessRequest = Value(4)

  /** OrigOrdModTime did not match last TransactTime of order. */
  val TimeMismatch = Value(5)

  /** Duplicate ClOrdID received.  */
  val DuplicateClOrdId = Value(6)
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
  val TradeAlongRequired = Value(9)
  val InvalidInvestorId = Value(10)
  val UnsupportedOrderCharacteristic = Value(11)
  val SurveillanceOption = Value(12)
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
  val Trade = Value('F')
  val TradeCorrect = Value('G')
  val TradeCancel = Value('H')
  val OrderStatus = Value('I')
}

object PegOffsetValue extends PriceOffsetTag(211) {
  val Tag = this
}

object TradSesStatus extends EnumTag(340) {
  val Tag = this
  val Unknown = Value(0)
  val Halted = Value(1)
  val Open = Value(2)
  val Closed = Value(3)
  val PreOpen = Value(4)
  val PreClose = Value(5)
  val RequestRejected = Value(6)
}

object SessionRejectReason extends EnumTag[Integer](373) {
  val Tag = this

  /** Invalid tag number.  */
  val InvalidTagNumber = Value(0)

  /** Required tag missing.  */
  val TagMissing = Value(1)

  /** Tag not defined for this message type.  */
  val InvalidTag = Value(2)

  /** Undefined tag.  */
  val UndefinedTag = Value(3)

  /** Tag specified without value.  */
  val EmptyTag = Value(4)

  /** Value is incorrect (out of range) for this tag.  */
  val InvalidValue = Value(5)

  /** Incorrect data format for value.  */
  val InvalidValueFormat = Value(6)

  /** Decryption problem.  */
  val DecryptionProblem = Value(7)

  /** Signature problem.  */
  val SignatureProblem = Value(8)

  /** CompId problem.  */
  val CompIdProblem = Value(9)

  /** SendingTime accuracy problem.  */
  val SendingTimeAccuracyProblem = Value(10)

  /** Invalid MsgType.  */
  val InvalidMsgType = Value(11)

  /** XML validation error.  */
  val XmlValidationError = Value(12)

  /** Tag appears more than once.  */
  val TagMultipleTimes = Value(13)

  /** Tag specified out of required order.  */
  val OutOfOrderTag = Value(14)

  /** Repeating group fields out of order.  */
  val OutOfOrderGroupField = Value(15)

  /** Incorrect NumInGroup count for repeating group.  */
  val NumInGroupMismatch = Value(16)

  /** Non "data" value includes field delimiter (SOH character).  */
  val FieldDelimiterInValue = Value(17)
}

object ExecRestatementReason extends EnumTag[Integer](378) {
  val Tag = this
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

  /** Not authorized.  */
  val NotAuthorized = Value(6)

  /** DeliverTo firm not available at this time.  */
  val DeliverToFirmNotAvailable = Value(7)
}

object PartyIDSource extends EnumTag[Character](447) {
  val Tag = this
  val Custom = Value('D')
}

object PartyID extends StringTag(448) {
  val Tag = this
}

object NoPartyIDs extends IntegerTag(453) {
  val Tag = this
}

object SecondaryClOrdID extends StringTag(526) {
  val Tag = this
}

object SecondaryExecID extends StringTag(527) {
  val Tag = this
}

object OrderCapacity extends EnumTag[Character](528) {
  val Tag = this
  val Agency = Value('A')    /* Agency single order */
  val Principal = Value('P') /* Principal */
  val Riskless = Value('R')  /* Riskless */
}

object OrderRestrictions extends EnumTag[Character](529) {
  val Tag = this
  val ProgramTrade = Value(1)
  val IndexArbitrage = Value(2)
  val NonIndexArbitrage = Value(3)
  val CompetingMarketMaker = Value(4)
}

object NoLegs extends NumInGroupTag(555) {
  val Tag = this
}
object LegPositionEffect extends EnumTag[Character](564) {
  val Tag = this
  val Close = Value('C')
  val FIFO = Value('F')
  val Open = Value('O')
  val Rolled = Value('R')
}

object LegPrice extends PriceTag(566) {
  val Tag = this
}

object TradeRequestID extends StringTag(568) {
  val Tag = this
}

object TradeRequestType extends EnumTag[Integer](569) {
  val Tag = this
  val AllTrades = Value(0)
  val MatchedTrades = Value(1)
  val UnmatchedTrades = Value(2)
  val UnreportedTrades = Value(3)
  val AdvisoriesMatch = Value(4)
}

object NoDates extends IntegerTag(580) {
  val Tag = this
}

object AccountType extends IntegerTag(581) {
  val Tag = this
}

object MassStatusReqID extends StringTag(584) {
  val Tag = this
}

object MassStatusReqType extends EnumTag[Integer](585) {
  val Tag = this
  val StatusSecurity = Value(1)
  val StatusUnderlyingSecurity = Value(2)
  val StatusProduct = Value(3)
  val StatusCFICode = Value(4)
  val StatusSecurityType = Value(5)
  val StatusTrdSession = Value(6)
  val StatusAllOrders = Value(7)
  val StatusPartyID = Value(8)
}

object LegSymbol extends StringTag(600) {
  val Tag = this
}

object LegCFICode extends StringTag(608) {
  val Tag = this
}

object LegMaturityMonthYear extends MonthYearTag(610) {
  val Tag = this
}
object LegStrikePrice extends PriceTag(612) {
  val Tag = this
}

object LegRatioQty extends FloatTag(623) {
  val Tag = this
}

object LegSide extends EnumTag[Character](624) {
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

object TradingSessionSubID extends StringTag(625) {
  val Tag = this
}

object Price2 extends PriceTag(640) {
  val Tag = this
}

object LegRefID extends StringTag(654) {
  val Tag = this
}
