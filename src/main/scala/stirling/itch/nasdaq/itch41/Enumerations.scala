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
package stirling.itch.nasdaq.itch41

/*
 * Section 4.2
 */
object SystemEventCode {
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

/*
 * Section 4.3.1
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

/*
 * Section 4.3.1
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
  val CompliantOnNasdaqOrUnknown  = ' '.toByte
}

/*
 * Section 4.3.2
 */
object TradingState {
  val Halted            = 'H'.toByte
  val HaltedOnNasdaq    = 'V'.toByte
  val Quotation         = 'Q'.toByte
  val QuotationOnNasdaq = 'R'.toByte
  val TradingOnNasdaq   = 'T'.toByte
}

/*
 * Section 4.3.3
 */
object RegSHOAction {
  val NoPriceTest                     = '0'.toByte
  val PriceTestDueToIntraDayPriceDrop = '1'.toByte
  val PriceTestRemains                = '2'.toByte
}

/*
 * Section 4.3.4
 */
object MarketMakerMode {
  val Normal       = 'N'.toByte
  val Passive      = 'P'.toByte
  val Syndicate    = 'S'.toByte
  val PreSyndicate = 'R'.toByte
  val Penalty      = 'L'.toByte
}

/*
 * Section 4.3.4
 */
object MarketParticipantState {
  val Active             = 'A'.toByte
  val ExcusedOrWithdrawn = 'E'.toByte
  val Withdrawn          = 'W'.toByte
  val Suspended          = 'S'.toByte
  val Deleted            = 'D'.toByte
}

/*
 * Section 4.6.1
 */
object BuySellIndicator {
  val Buy  = 'B'.toByte
  val Sell = 'S'.toByte
}

/*
 * Section 4.6.2
 */
object CrossType {
  val NasdaqOpening             = 'O'.toByte
  val NasdaqClosing             = 'C'.toByte
  val IpoOrHaltedOrPaused       = 'H'.toByte
  val NasdaqIntradayOrPostClose = 'I'.toByte
}

/*
 * Section 4.7
 */
object ImbalanceDirection {
  val BuyImbalance                  = 'B'.toByte
  val SellImbalance                 = 'S'.toByte
  val NoImbalance                   = 'N'.toByte
  val InsufficientOrdersToCalculate = 'O'.toByte
}

/*
 * Section 4.7
 */
object PriceVarianceIndicator {
  val LessThanOnePercent     = 'L'.toByte
  val LessThanTwoPercent     = '1'.toByte //  1% to  1.99%
  val LessThanThreePercent   = '2'.toByte //  2% to  2.99%
  val LessThanFourPercent    = '3'.toByte //  3% to  3.99%
  val LessThanFivePercent    = '4'.toByte //  4% to  4.99%
  val LessThanSixPercent     = '5'.toByte //  5% to  5.99%
  val LessThanSevenPercent   = '6'.toByte //  6% to  6.99%
  val LessThanEightPercent   = '7'.toByte //  7% to  7.99%
  val LessThanNinePercent    = '8'.toByte //  8% to  8.99%
  val LessThanTenPercent     = '9'.toByte //  9% to  9.99%
  val LessThanTwentyPercent  = 'A'.toByte // 10% to 19.99%
  val LessThanThirtyPercent  = 'B'.toByte // 20% to 29.99%
  val ThirtyPercentOrGreater = 'C'.toByte
  val CannotBeCalculated     = ' '.toByte
}
