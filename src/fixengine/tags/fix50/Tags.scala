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
package fixengine.tags.fix50

import fixengine.messages.{
  EnumTag,
  Value,
  QtyTag
}
import java.lang.{
  Character,
  String
}

object ExecInst extends EnumTag[Character](18) {
  val Midpoint = Value('M')
  val MarketPeg = Value('P')
  val PrimaryPeg = Value('R')
  val CancelOnSysFail = Value('Q')
}
object MatchType extends EnumTag[String](574) {
  val TwoPartyTradeReport = Value("2")
  val AutoMatch = Value("4")
  val CallAuction = Value("7")
}
object DisplayMethod extends EnumTag[Character](1084) {
  val Random = Value('3')
}
object DisplayLowQty extends QtyTag(1085)
object DisplayHighQty extends QtyTag(1086)
object DisplayMinIncr extends QtyTag(1087)
