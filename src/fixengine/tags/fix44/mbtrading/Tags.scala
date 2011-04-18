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
package fixengine.tags.fix44.mbtrading

import fixengine.messages.{
  FloatTag,
  IntegerTag,
  EnumTag,
  QtyTag,
  StringTag
}
import java.lang.Integer

object PosPendBuy extends QtyTag(10000)

object PosPendSell extends QtyTag(10001)

object MorningBuyingPower extends StringTag(10002)

object PosBuyPowerUsed extends FloatTag(10003)

object PosRealizedPNL extends FloatTag(10004)

object MBTAccountType extends EnumTag[Integer](10005) {
  val Cash = 1
  val USMArgin = 2
  val DVP = 6
}

object OvernightBuyingPower extends StringTag(10006)

object RealizedPnL extends StringTag(10008)

object MorningExcessEquity extends StringTag(10009)

object MorningExcessEquity2 extends StringTag(10010)

object PosEquityUsed extends FloatTag(10015)

object AccountCredit extends StringTag(10018)

object MBTXAggressive extends IntegerTag(10022)

object OvernightExcess extends StringTag(10048)

object BODOOvernightExcessEq extends StringTag(10050)

object OrderGroupID1 extends StringTag(10055)

object LiquidityTag extends StringTag(9730)

object MBTInternalOrderId extends StringTag(10017)

object UserQuotePerms extends StringTag(10059)
