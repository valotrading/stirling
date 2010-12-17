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
  UtcTimestampTag,
  Value
}
import java.lang.{
  Character,
  Integer,
  String
}

object Account extends StringTag(1) {
  val Tag = this
}

object Commission extends AmtTag(12) {
  val Tag = this
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

object ExecTransType extends EnumTag[Integer](40) {
  val Tag = this
  val New = Value(0)
  val Cancel = Value(1)
  val Correct = Value(2)
  val Status = Value(3)
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

object AllocTransType extends EnumTag[Integer](71) {
  val Tag = this
  val New = Value(0)
  val Replace = Value(1)
  val Cancel = Value(2)
  val Preliminary = Value(3)                 /* (without MiscFees and NetMoney) */
  val Calculated = Value(4)                  /* (includes MiscFees and NetMoney) */
  val CalculatedWithoutPrelminary = Value(5) /* Calculated without Preliminary (sent unsolicited by broker, includes MiscFees and NetMoney) */
}

object TradeDate extends LocalMktDateTag(75) {
  val Tag = this
}

object AllocShares extends QtyTag(80) {
  val Tag = this
}

object StopPx extends PriceTag(99) {
  val Tag = this
}

object ExDestination extends ExchangeTag(100) {
  val Tag = this
}

object ClientID extends StringTag(109) {
  val Tag = this
}

object LocateReqd extends BooleanTag(114) {
  val Tag = this
}

object EffectiveTime extends UtcTimestampTag(168) {
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

object TradSesMethod extends StringTag(338) {
  val Tag = this
}

object TradSesMode extends StringTag(339) {
  val Tag = this
}

object ComplianceID extends StringTag(376) {
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
object ClearingFirm extends StringTag(439) {
  val Tag = this
}

object ClearingAccount extends StringTag(440) {
  val Tag = this
}
