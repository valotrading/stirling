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
package stirling.fix.tags.fix50

import stirling.fix.messages.{
  CharValue,
  EnumTag,
  IntegerValue,
  QtyTag,
  StringValue
}
import java.lang.{
  Character,
  String
}

object ExecInst extends EnumTag[Character](18) {
  val Midpoint = CharValue('M')
  val MarketPeg = CharValue('P')
  val PrimaryPeg = CharValue('R')
  val CancelOnSysFail = CharValue('Q')
}
object MatchType extends EnumTag[String](574) {
  val TwoPartyTradeReport = StringValue("2")
  val AutoMatch = StringValue("4")
  val CallAuction = StringValue("7")
}
object DisplayMethod extends EnumTag[Character](1084) {
  val Random = CharValue('3')
}
object DisplayLowQty extends QtyTag(1085)
object DisplayHighQty extends QtyTag(1086)
object DisplayMinIncr extends QtyTag(1087)
