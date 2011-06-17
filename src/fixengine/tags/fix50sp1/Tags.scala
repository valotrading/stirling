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
package fixengine.tags.fix50sp1

import fixengine.messages.{
  ExchangeTag,
  StringTag,
  EnumTag,
  CharValue,
  IntegerValue
}
import java.lang.{
  Character,
  Integer
}

object SettlMethod extends EnumTag[Character](1193) {
  val Cash = CharValue('C')
  val Physical = CharValue('P')
}
object ExerciseStyle extends EnumTag[Integer](1194) {
  val European = IntegerValue(0)
  val American = IntegerValue(1)
  val Bermuda = IntegerValue(2)
}
object MarketSegmentID extends StringTag(1300)
object MarketID extends ExchangeTag(1301)
