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
package fixengine.tags.fix42.chix.europe

import fixengine.messages.{EnumTag, Value}
import java.lang.{String, Character}

object ExecInst extends EnumTag[Character](18) {
  val Tag = this
  val MarketPeg = Value('P')
  val PrimaryPeg = Value('R')
  val Midpoint = Value('M')
}

object IDSource extends EnumTag[Character](22) {
  val Tag = this
  val ISIN = Value('4')
  val RIC = Value('5')
}

object TradeLiquidityIndicator extends EnumTag[String](9730) {
  val Tag = this
  val AddedLiquidity = Value("A")           /* Order added liquidity */
  val RemovedLiquidity = Value("R")         /* Order removed liquidity */
  val AddedLiquidityToDark = Value("D")     /* Dark trade */
  val RemovedLiquidityFromDark = Value("X") /* Onward routed trade */
}
