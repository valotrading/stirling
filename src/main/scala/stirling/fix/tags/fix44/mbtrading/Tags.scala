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
package stirling.fix.tags.fix44.mbtrading

import stirling.fix.messages.{
  CharValue,
  EnumTag,
  FloatTag,
  IntegerTag,
  IntegerValue,
  QtyTag,
  StringTag
}
import java.lang.{
  Character,
  Integer
}

object TimeInForce extends EnumTag[Integer](59) {
  val Day = IntegerValue(0)
  val GoodTillCancel = IntegerValue(1)
  val AtTheOpening = IntegerValue(2)
  val ImmediateOrCancel = IntegerValue(3)
  val FillOrKill = IntegerValue(4)
  val GoodTillDate = IntegerValue(6)
  val DayPlus = IntegerValue(9)
}
object ExecType extends EnumTag[Character](150) {
  val New = CharValue('0')
  val PartialFill = CharValue('1')
  val Fill = CharValue('2')
  val DoneForDay = CharValue('3')
  val Canceled = CharValue('4')
  val Replace = CharValue('5')
  val PendingCancel = CharValue('6')
  val Rejected = CharValue('8')
  val Suspended = CharValue('9')
  val PendingNew = CharValue('A')
  val Restated = CharValue('D')
  val PendingReplace = CharValue('E')
  val Trade = CharValue('F')
  val Break = CharValue('H')
  val Status = CharValue('I')
  val Resumed = CharValue('R')
}
object PosReqType extends EnumTag[Integer](724) {
  val PositionsCurrent = IntegerValue(0)
  val PositionsBOD = IntegerValue(9)
}
object PosPendBuy extends QtyTag(10000)
object PosPendSell extends QtyTag(10001)
object MorningBuyingPower extends StringTag(10002)
object PosBuyPowerUsed extends FloatTag(10003)
object PosRealizedPNL extends FloatTag(10004)
object MBTAccountType extends EnumTag[Integer](10005) {
  val Cash = IntegerValue(1)
  val USMArgin = IntegerValue(2)
  val DVP = IntegerValue(6)
  val Forex = IntegerValue(20)
}
object LiquidityTag extends StringTag(9730)
object OvernightBuyingPower extends StringTag(10006)
object RealizedPnL extends StringTag(10008)
object MorningAccountValue extends StringTag(10009)
object MorningExcessEquity extends StringTag(10010)
object PosEquityUsed extends FloatTag(10015)
object MBTInternalOrderId extends StringTag(10017)
object AccountCredit extends StringTag(10018)
object MBTXAggressive extends IntegerTag(10022)
object AccountBasedPerms extends StringTag(10024)
object MorningExcessEquity2 extends StringTag(10040)
object OptionStrategyCode extends StringTag(10047)
object OvernightExcess extends StringTag(10048)
object BODOOvernightExcessEq extends StringTag(10050)
object OrderGroupID1 extends StringTag(10055)
object UserQuotePerms extends StringTag(10059)
object MultiSymbol extends StringTag(10065)
object MultiPrice extends StringTag(10066)
object MBTMultifunction extends StringTag(10068)
object TodayRealizedPNL2 extends StringTag(10069)
object UserRoutePerm extends StringTag(10071)
object TriggerFromOrderID extends StringTag(10073)
object CompanyID extends StringTag(10076)
object AccountBank extends StringTag(10077)
object AccountBranch extends StringTag(10078)
object FLID extends StringTag(10401)
object UserSessionID extends StringTag(10402)
