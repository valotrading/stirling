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

import fixengine.messages.{EnumTag, Value}
import java.lang.{Character, Integer}

object ExecInst extends EnumTag[Character](18) {
  val Tag = this
  val Midpoint = Value('M')
  val MarketPeg = Value('P')
  val PrimaryPeg = Value('R')
  val CancelOnSysFail = Value('Q')
}

object DisplayMethod extends EnumTag[Character](1084) {
  val Tag = this
  val Random = Value('3')
}

object OrderRestrictions extends EnumTag[Character](529) {
  val Tag = this
  val IssuerHolding = Value('B')
  val IssuePriceStabilization = Value('C')
  val ActingAsMarketMaker = Value('5')
}

object PartyRole extends EnumTag[Integer](452) {
  val Tag = this
  val ExecutingFirm = Value(1)
  val ClearingFirm = Value(4)
  val EnteringFirm = Value(7)
  val ContraFirm = Value(17)
  val ContraClearingFirmOrCcp = Value(18)
  val ClearingOrganization = Value(21)
  val EnteringTrader = Value(36)
}
