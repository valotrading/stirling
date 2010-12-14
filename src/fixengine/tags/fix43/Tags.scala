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

import java.lang.{Character, Integer}
import fixengine.messages.{StringTag, EnumTag, Value}

object SecurityIDSource extends EnumTag[Character](22) {
  val Tag = this
  val ISIN = Value('4')
  val ExchangeSymbol = Value('8')
}

object PartyIDSource extends EnumTag[Character](447) {
  val Tag = this
  val Custom = Value('D')
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

object LegPositionEffect extends EnumTag[Character](564) {
  val Tag = this
  val Close = Value('C')
  val FIFO = Value('F')
  val Open = Value('O')
  val Rolled = Value('R')
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
