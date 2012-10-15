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
package stirling.fix.tags.fix42.lime

import stirling.fix.messages.{
  BooleanTag,
  CharValue,
  EnumTag,
  FloatTag,
  IntegerTag,
  PriceTag,
  StringTag
}
import java.lang.Character

object Side extends EnumTag[Character](54) {
  val Buy        = CharValue('1')
  val Sell       = CharValue('2')
  val SellShort  = CharValue('5')
  val BuyToCover = CharValue('9')
}

object TimeInForce extends EnumTag[Character](59) {
  val Day               = CharValue('0')
  val AtTheOpening      = CharValue('2')
  val ImmediateOrCancel = CharValue('3')
  val ExtendedDay       = CharValue('5')
  val GoodTillDate      = CharValue('6')
  val AtTheClose        = CharValue('7')
  val TimeInMarket      = CharValue('8')
}

object CancelAllOnDisconnect extends BooleanTag(7001)

object Liquidity extends IntegerTag(8001)

object Position extends IntegerTag(8004)

object BuyingPower extends FloatTag(8005)

object Invisible extends BooleanTag(9003)

object PostOnly extends BooleanTag(9004)

object ShortSaleAffirm extends BooleanTag(9009)

object LongSaleAffirm extends BooleanTag(9010)

object AllowRouting extends BooleanTag(9011)

object AlternateExDestination extends StringTag(9012)

object RouteToNYSE extends BooleanTag(9014)

object ISO extends BooleanTag(9017)

object CancelAllOpen extends BooleanTag(9020)

object CancelPairs extends StringTag(9021)

object MarketConfirmPrices extends PriceTag(9028)

object PegType extends EnumTag[Character](9034) {
  val Primary               = CharValue('1')
  val Market                = CharValue('2')
  val MidPoint              = CharValue('3')
  val AlternateMidPoint     = CharValue('4') // BATS/BYX only
  val PriceImprovedPrimary  = CharValue('5') // LAVA only
  val PriceImprovedMarket   = CharValue('6') // LAVA only
  val PriceImprovedMidPoint = CharValue('7') // LAVA only
}

object ClientOrderData extends StringTag(9050)

object LockedOrCrossedAction extends EnumTag[Character](9064) {
  val Reject                   = CharValue('R')
  val PriceAdjust              = CharValue('P')
  val HideNotSlide             = CharValue('H')
  val PriceAdjustRejectCrossed = CharValue('L')
  val SingleReprice            = CharValue('S')
  val Blind                    = CharValue('B')
}

object RegularSessionOnly extends BooleanTag(9066)

object ShortSaleAffirmLongQuantity extends IntegerTag(9067)

object ExternalClOrdId extends StringTag(9508)
