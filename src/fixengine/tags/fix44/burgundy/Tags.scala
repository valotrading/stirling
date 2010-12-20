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
package fixengine.tags.fix44.burgundy

import fixengine.messages.{
  EnumTag,
  Value
}
import java.lang.{
  Character,
  Integer
}

object OrderRestrictions extends EnumTag[Character](529) {
  val IssuerHolding = Value('B')
  val IssuePriceStabilization = Value('C')
  val ActingAsMarketMaker = Value('5')
}
object TrdType extends EnumTag[Integer](828) {
  val RegularTrade = Value(0)
  val DerivativeRelatedTransaction = Value(49)
  val PortfolioTrade = Value(50)
  val VolumeWeightedAverageTrade = Value(51)
  val MarketplaceGrantedTrade = Value(52)
}
