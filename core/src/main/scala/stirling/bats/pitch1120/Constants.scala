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
package stirling.bats.pitch1120

/*
 * Section 4.3
 */
object SideIndicator {
  val Buy  = 'B'.toByte
  val Sell = 'S'.toByte
}

/*
 * Section 4.7
 */
object HaltStatus {
  val Halted    = 'H'.toByte
  val QuoteOnly = 'Q'.toByte
  val Trading   = 'T'.toByte
}

/*
 * Section 4.7
 */
object RegSHOAction {
  val NoPriceTestInEffect     = '0'.toByte
  val RegSHOPriceTestInEffect = '1'.toByte
}

/*
 * Section 4.8
 */
object AuctionType {
  val OpeningAuction = 'O'.toByte
  val ClosingAuction = 'C'.toByte
  val HaltAuction    = 'H'.toByte
  val IPOAction      = 'I'.toByte
}

/*
 * Section 4.10
 */
object RPI {
  val BuySideRPI    = 'B'.toByte
  val SellSideRPI   = 'S'.toByte
  val BuyAndSellRPI = 'A'.toByte
  val NoRPI         = 'N'.toByte
}
