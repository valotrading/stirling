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
package stirling.itch.nasdaqomx.itch186

import stirling.io.ByteString

/*
 * Section 4.2.1
 */
object SystemEventCode {
  val StartOfMessages = 'O'.toByte
  val EndOfMessages   = 'C'.toByte
}

/*
 * Section 4.2.2
 */
object MarketSegmentStateCode {
  val PreOpen           = 'P'.toByte
  val OpeningAuction    = 'O'.toByte
  val ContinuousTrading = 'T'.toByte
  val ClosingAuction    = 'L'.toByte
  val PostTrade         = 'S'.toByte
  val Closed            = 'C'.toByte
}

/*
 * Section 4.3.1
 */
object FinancialProduct {
  val Stock                =  1
  val EquityWarrant        =  2
  val EquityRight          =  3
  val Bond                 =  4
  val LotteryBondSeries    =  6
  val Convertible          =  7
  val Warrant              =  8
  val UnitTrustCertificate = 11
  val IndexFundUnit        = 12
}

/*
 * Section 4.3.1
 */
object NoteCode {
  val NM =      1 // New Market Company
  val XR =      2 // Excluding Participating in Rights
  val SP =      4 // Excluding Participating in Split
  val PO =      8 // Public Offer
  val UD =     16 // Under Drawing
  val SR =     32 // Excluding Combined Split and Issue Rights
  val UL =     64 // Unlisted
  val WI =    128 // When Issued
  val BR =    256 // Company Bankruptcy
  val SU =    512 // Suspension
  val RL =   1024 // Removal from Listing Process
  val SL =   2048 // Other Surveillance List Reason
  val TO =   4096 // Significant Reverse Takeover Pending
  val CS =   8192 // Cent Shares
  val RS =  16384 // Reversed Split
  val BS =  32768 // Excluding Combined Bonus and Split
  val SS =  65536 // Excluding Combined Split and Redemption Share
  val FN = 131072 // First North Company
  val OB = 262144 // On the Surveillance List
  val XD = 524288 // Excluding Dividend
}

/*
 * Section 4.3.2
 */
object TradingState {
  val Halted                   = 'H'.toByte
  val TradingOnNasdaqOmxNordic = 'T'.toByte
  val AuctionPeriod            = 'Q'.toByte
}

/*
 * Section 4.3.2
 */
object TradingActionReason {
  val TradingHalt           = new ByteString("TH  ".getBytes)
  val RegulatoryHalt        = new ByteString("RH  ".getBytes)
  val MatchingHalt          = new ByteString("MH  ".getBytes)
  val TechnicalStop         = new ByteString("TS  ".getBytes)
  val VolatilityHaltDynamic = new ByteString("VHD ".getBytes)
  val VolatilityHaltStatic  = new ByteString("VHS ".getBytes)
  val NotAvailable          = new ByteString("    ".getBytes)
}

/*
 * Section 4.4.1
 */
object BuySellIndicator {
  val Buy  = 'B'.toByte
  val Sell = 'S'.toByte
}

/*
 * Section 4.4.2
 */
object Attribution {
  val MarketMaker = new ByteString("_MMO".getBytes)
}

/*
 * Section 4.6.1
 */
object TradeType {
  val MainBook    = 'B'.toByte
  val NordicAtMid = 'S'.toByte
}

/*
 * Section 4.6.2
 */
object CrossType {
  val OpeningCross             = 'O'.toByte
  val ClosingCross             = 'C'.toByte
  val CrossForHaltedSecurities = 'H'.toByte
}

/*
 * Section 4.8
 */
object ImbalanceDirection {
  val BuyImbalance                  = 'B'.toByte
  val SellImbalance                 = 'S'.toByte
  val NoImbalance                   = 'N'.toByte
  val InsufficientOrdersToCalculate = 'O'.toByte
}
