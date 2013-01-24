/*
 * Copyright 2013 the original author or authors.
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
package stirling.itch.nasdaqomx.ouch114

import stirling.itch.ByteString

/*
 * Section 2.1
 */
object SystemEventCode {
  val StartOfDay = 'S'.toByte
  val EndOfDay   = 'E'.toByte
}

/*
 * Section 2.2.1
 */
object BuySellIndicator {
  val Buy  = 'B'.toByte
  val Sell = 'S'.toByte
}

/*
 * Section 2.2.2
 */
object CanceledOrderReason {
  val UserRequested         = 'U'.toByte
  val ImmediateOrCancel     = 'I'.toByte
  val Timeout               = 'T'.toByte
  val Supervisory           = 'S'.toByte
  val RegulatoryRestriction = 'D'.toByte
  val SelfMatchPrevention   = 'Q'.toByte
}

/*
 * Section 2.2.3
 */
object LiquidityFlag {
  val AddedLiquidity                 = 'A'.toByte
  val RemovedLiquidity               = 'R'.toByte
  val InternalizedInContinuousMarket = 'X'.toByte
  val ExecutedInAuction              = 'C'.toByte
  val InternalizedInAuction          = 'Y'.toByte
}

/*
 * Section 2.2.4
 */
object BrokenTradeReason {
  val Erroneous   = 'E'.toByte
  val Consent     = 'C'.toByte
  val Supervisory = 'S'.toByte
  val External    = 'X'.toByte
}

/*
 * Section 2.2.5
 */
object RejectedOrderReason {
  val TestMode                           = 'T'.toByte
  val Halted                             = 'H'.toByte
  val QuantityExceeded                   = 'Z'.toByte
  val InvalidOrderBookIdentity           = 'S'.toByte
  val InvalidDisplayType                 = 'D'.toByte
  val Closed                             = 'C'.toByte
  val FirmNotAuthorized                  = 'L'.toByte
  val NotPermittedForClearingType        = 'M'.toByte
  val StockOrTimeRestriction             = 'R'.toByte
  val InvalidPrice                       = 'X'.toByte
  val InvalidMinimumQuantity             = 'N'.toByte
  val InvalidUserID                      = 'U'.toByte
  val InvalidData                        = '4'.toByte
  val UnspecifiedError                   = 'O'.toByte

  val PrmOrderEntryDisabled              = 'a'.toByte
  val PrmInvalidSymbol                   = 'b'.toByte
  val PrmRestrictedSymbol                = 'c'.toByte
  val PrmFailsPriceCheck                 = 'd'.toByte
  val PrmMarketOrdersNotAllowed          = 'e'.toByte
  val PrmSurpassesMaxOrderShareThreshold = 'f'.toByte
  val PrmSurpassesMaxOrderValueThreshold = 'g'.toByte
  val PrmSurpassesNotionalValueThreshold = 'h'.toByte
  val PrmOverTotalRiskValue              = 'i'.toByte
  val PrmOverDailyTradeOneSidedValue     = 'j'.toByte
  val PrmOverDailyTradeTotalValue        = 'k'.toByte
  val PrmOverDailyOpenOrderOneSidedValue = 'l'.toByte
  val PrmOverDailyOpenOrderTotalValue    = 'm'.toByte
}

/*
 * Section 3.1
 */
object Display {
  val Display                                 = 'Y'.toByte
  val NonDisplay                              = 'N'.toByte
  val ImbalanceOnly                           = 'I'.toByte
  val MarketMakerOrder                        = 'W'.toByte
  val MarketMakerOrderRefresh                 = 'U'.toByte
  val DisplayAndOverridePostTradeAnonymity    = 'D'.toByte
  val NonDisplayAndOverridePostTradeAnonymity = 'R'.toByte
}

/*
 * Section 3.1
 */
object Capacity {
  val Client                = '1'.toByte
  val OwnAccount            = '2'.toByte
  val MarketMaker           = '3'.toByte
  val IssuerHolding         = '4'.toByte
  val IssuePriceStabilizing = '5'.toByte
  val RisklessPrincipal     = '6'.toByte
}

/*
 * Section 3.1
 */
object CrossType {
  val ClosingCross = 'C'.toByte
  val OpeningCross = 'O'.toByte
  val HaltCross    = 'H'.toByte
}
