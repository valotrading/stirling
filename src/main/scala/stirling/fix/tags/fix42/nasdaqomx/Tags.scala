/*
 * Copyright 2012 the original author or authors.
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
package stirling.fix.tags.fix42.nasdaqomx

import stirling.fix.messages.{
  BooleanTag,
  CharValue,
  FloatTag,
  EnumTag,
  IntegerTag,
  PriceTag,
  StringTag
}
import java.lang.Character

object OrderQty extends IntegerTag(38)

object OrdType extends EnumTag[Character](40) {
  val Market = CharValue('1')
  val Limit  = CharValue('2')
  val Pegged = CharValue('P')
}

object OrderRestrictions extends EnumTag[Character](529) {
  val ActingAsMarketMaker     = CharValue('5')
  val IssuerHolding           = CharValue('B')
  val IssuePriceStabilization = CharValue('C')
}

object SubMktID extends IntegerTag(5815)

object ClRefID extends StringTag(6209)

object DisplayInst extends EnumTag[Character](9140) {
  val ImbalanceOnly = CharValue('I')
  val NordicAtMid   = CharValue('M')
  val NonDisplay    = CharValue('N')
  val Display       = CharValue('Y')
}

object CrossTradeFlag extends EnumTag[Character](9355) {
  val ClosingCross = CharValue('C')
  val HaltCross    = CharValue('H')
  val OpeningCross = CharValue('O')
}

object BrSeqNbr extends StringTag(9861)

object TradeID extends StringTag(1003)

object LiquidityFlag extends EnumTag[Character](9882) {
  val NordicAddedLiquidity           = CharValue('A')
  val NordicRemovedLiquidity         = CharValue('R')
  val InternalizedInContinuousMarket = CharValue('X')
  val ExecutedInAuction              = CharValue('C')
  val InternalizedInAuction          = CharValue('Y')
  val NordicAtMid                    = CharValue('M')
  val InternalizedByNordicAtMid      = CharValue('Z')
  val ExecutionCancel                = CharValue('E')
  val UnspecifedDestination          = CharValue('0')
  val ChiX                           = CharValue('1')
  val Turquoise                      = CharValue('2')
  val BATS                           = CharValue('3')
  val Burgundy                       = CharValue('4')
  val XOSL                           = CharValue('5')
  val MarketAccess                   = CharValue('6')
}
