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

import fixengine.messages.{EnumTag, Value}
import java.lang.Character
import java.lang.Integer

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

object CustomerOrFirm extends EnumTag[Integer](204) {
  val Tag = this
  val Customer = Value(0)
  val Firm = Value(1)
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
