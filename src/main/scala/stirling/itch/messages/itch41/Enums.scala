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
package stirling.itch.messages.itch41

/**
 * System Event Codes as specified in section 4.2.
 */
object SystemMessageType {
  val StartOfMessages    = 'O'.toByte
  val StartOfSystemHours = 'S'.toByte
  val StartOfMarketHours = 'Q'.toByte
  val EndOfMarketHours   = 'M'.toByte
  val EndOfSystemHours   = 'E'.toByte
  val EndOfMessages      = 'C'.toByte
  val Halt               = 'A'.toByte
  val QuoteOnlyPeriod    = 'R'.toByte
  val Resumption         = 'B'.toByte
}

/**
 * Market Category as specified in section 4.3.1.
 */
object MarketCategory {
  val Nyse                     = 'N'.toByte
  val NyseAmex                 = 'A'.toByte
  val NyseArca                 = 'P'.toByte
  val NasdaqGlobalSelectMarket = 'Q'.toByte
  val NasdaqGlobalMarkget      = 'G'.toByte
  val NasdaqCapitalMarket      = 'S'.toByte
  val BatsBzxExchange          = 'Z'.toByte
}

/**
 * Financial Status Indicator as specified in section 4.3.1.
 */
object FinancialStatusIndicator {
  val Deficient                   = 'D'.toByte
  val Delinquent                  = 'E'.toByte
  val Bankrupt                    = 'Q'.toByte
  val Suspended                   = 'S'.toByte
  val DeficientAndBankrupt        = 'G'.toByte
  val DeficientAndDeliquent       = 'H'.toByte
  val DelinquentAndBankrupt       = 'J'.toByte
  val DeficientDelinquentBankrupt = 'K'.toByte
  val NasdaqCompliant             = ' '.toByte
}

/**
 * Trading State as specified in section 4.3.2.
 */
object TradingState {
  val Halted          = 'H'.toByte
  val HaltedInNasdaq  = 'V'.toByte
  val Quotation       = 'Q'.toByte
  val QuotationNasdaq = 'R'.toByte
  val TradingOnNasdaq = 'T'.toByte
}

/**
 * Reg SHO Action as specified in section 4.3.3.
 */
object RegSHOAction {
  val NoPriceTest                       = '0'.toByte
  val PriceTestRemainsIntraDayPriceDrop = '1'.toByte
  val PriceTestRemains                  = '2'.toByte
}

/**
 * Market Maker Mode as specified in section 4.3.4.
 */
object MarketMakerMode {
  val Normal       = 'N'.toByte
  val Passive      = 'P'.toByte
  val Syndicate    = 'S'.toByte
  val PreSyndicate = 'R'.toByte
  val Penalty      = 'L'.toByte
}

/**
 * Market Participant State as specified in section 4.3.4.
 */
object MarketParticipantState {
  val Active             = 'A'.toByte
  val ExcusedOrWithdrawn = 'E'.toByte
  val Withdrawn          = 'W'.toByte
  val Suspended          = 'S'.toByte
  val Deleted            = 'D'.toByte
}

/**
 * Buy/Sell Indicator as specified in section 4.6.1.
 */
object BuySellIndicator {
  val Buy  = 'B'.toByte
  val Sell = 'S'.toByte
}

/**
 * Imbalance Direction as specified in section 4.7.
 */
object ImbalanceDirection {
  val Buy                           = 'B'.toByte
  val Sell                          = 'S'.toByte
  val NoImbalance                   = 'N'.toByte
  val InsufficientOrdersToCalculate = 'O'.toByte
}

/**
 * Cross Type as specified in section 4.7.
 */
object CrossType {
  val NasdaqOpening              = 'O'.toByte
  val NasdaqClosing              = 'C'.toByte
  val IpoAndHaltedOrPaused       = 'H'.toByte
  val NasdaqIntradayAndPostClose = 'I'.toByte
}

/**
 * Price Variance Indicator as specified in Section 4.7.
 */
object PriceVarianceIndicator {
  val CannotBeCalculated              = ' '.toByte
  val LessThanOnePercent              = 'L'.toByte
  val LessThanTwoPercent              = '1'.toByte
  val LessThanThreePercent            = '2'.toByte
  val LessThanFourPercent             = '3'.toByte
  val LessThanFivePercent             = '4'.toByte
  val LessThanSixPercent              = '5'.toByte
  val LessThanSevenPercent            = '6'.toByte
  val LessThanEightPercent            = '7'.toByte
  val LessThanNinePercent             = '8'.toByte
  val LessThanTenPercent              = '9'.toByte
  val LessThanTwentyPercent           = 'A'.toByte
  val LessThanThirtyPercent           = 'B'.toByte
  val EqualOrGreaterThanThirtyPercent = 'C'.toByte
}
