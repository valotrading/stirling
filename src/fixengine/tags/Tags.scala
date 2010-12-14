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
package fixengine.tags

import fixengine.messages.{EnumTag, Value}
import java.lang.{Integer, Character}


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

object CxlRejResponseTo extends EnumTag[Character](434) {
  val Tag = this
  val OrderCancelRequest = Value('1')
  val OrderModificationRequest = Value('2')
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

object HandlInst extends EnumTag[Character](21) {
  val Tag = this

  /** Automated execution order, private, no Broker intervention */
  val AutomatedOrderPrivate = Value('1')

  /** Automated execution order, public, Broker intervention OK */
  val AutomatedOrderPublic = Value('2')

  /** Manual order, best execution */
  val ManualOrder = Value('3')
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

object OrderCapacity extends EnumTag[Character](47) {
  val Tag = this
  val Agency = Value('A') /* Agency single order */
  val Principal = Value('P') /* Principal */
  val Riskless = Value('R') /* Riskless */
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
