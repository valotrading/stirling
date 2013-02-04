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

import java.io.File
import java.nio.ByteBuffer
import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import scala.language.implicitConversions
import stirling.io.ByteString
import stirling.itch.io.Source

class BinaryFILEParserSpec extends WordSpec with MustMatchers with MessageParserFixtures {
  "BinaryFILEParser" must {
    "parse messages with read buffer underflow inside message" in {
      source(64).toList must equal(expectedMessages)
    }
    "parse messages with read buffer underflow on message boundary" in {
      source(46).toList must equal(expectedMessages)
    }
  }

  private def source(readBufferSize: Int): Source[Message] = {
    Source.fromInputStream[Message](
      stream         = getClass.getResourceAsStream("/itch-v41.txt"),
      parser         = new BinaryFILEParser,
      readBufferSize = readBufferSize
    )
  }
}

trait MessageParserFixtures {
  val expectedMessages = List(
    Seconds(
      seconds = 24136
    ),
    SystemEvent(
      nanoseconds = 800734287,
      eventCode   = SystemEventCode.StartOfMessages
    ),
    Seconds(
      seconds           = 24611
    ),
    StockDirectory(
      nanoseconds              = 650506166,
      stock                    = "QQQ     ",
      marketCategory           = MarketCategory.Nyse,
      financialStatusIndicator = FinancialStatusIndicator.CompliantOnNasdaqOrUnknown,
      roundLotSize             = 100,
      roundLotsOnly            = false
    ),
    MarketParticipantPosition(
      nanoseconds            = 661054256,
      mpid                   = "ABCD",
      stock                  = "SPY     ",
      primaryMarketMaker     = false,
      marketMakerMode        = MarketMakerMode.Normal,
      marketParticipantState = MarketParticipantState.Active
    ),
    StockTradingAction(
      nanoseconds  = 841159237,
      stock        = "SPY     ",
      tradingState = TradingState.Halted,
      reserved     = ' '.toByte,
      reason       = "IPO1"
    ),
    RegSHORestriction(
      nanoseconds  = 1412785731,
      stock        = "SPY     ",
      regShoAction = RegSHOAction.NoPriceTest
    ),
    AddOrder(
      nanoseconds          = 1935833240,
      orderReferenceNumber = 4096,
      buySellIndicator     = BuySellIndicator.Buy,
      shares               = 1376271,
      stock                = "SPY     ",
      price                = 65535
    ),
    AddOrderMPID(
      nanoseconds          = 825374775,
      orderReferenceNumber = 4096,
      buySellIndicator     = BuySellIndicator.Buy,
      shares               = 458767,
      stock                = "QQQ     ",
      price                = 65535,
      attribution          = "ATTR"
    ),
    OrderExecuted(
      nanoseconds          = 880306004,
      orderReferenceNumber = 16L,
      executedShares       = 5,
      matchNumber          = 256L
    ),
    OrderExecutedWithPrice(
      nanoseconds          = 1161242641,
      orderReferenceNumber = 16L,
      executedShares       = 5,
      matchNumber          = 256L,
      printable            = true,
      price                = 256
    ),
    OrderCancel(
      nanoseconds          = 275075413,
      orderReferenceNumber = 1L,
      canceledShares       = 16
    ),
    OrderDelete(
      nanoseconds          = 2016946456,
      orderReferenceNumber = 2L
    ),
    OrderReplace(
      nanoseconds                  = 909260338,
      originalOrderReferenceNumber = 1L,
      newOrderReferenceNumber      = 2L,
      shares                       = 1,
      price                        = 2
    ),
    Trade(
      nanoseconds          = 286331153,
      orderReferenceNumber = 1L,
      buySellIndicator     = BuySellIndicator.Sell,
      shares               = 2,
      stock                = "SPY     ",
      price                = 4521985,
      matchNumber          = 2L
    ),
    CrossTrade(
      nanoseconds = 50529027,
      shares      = 2L,
      stock       = "QQQ     ",
      crossPrice  = 1179648,
      matchNumber = 2048,
      crossType   = CrossType.NasdaqClosing
    ),
    BrokenTrade(
      nanoseconds = 1717986918,
      matchNumber = 2L
    ),
    NOII(
      nanoseconds            = 892482614,
      pairedShares           = 258L,
      imbalance              = 259L,
      imbalanceDirection     = ImbalanceDirection.BuyImbalance,
      stock                  = "QQQ     ",
      farPrice               = 1048577,
      nearPrice              = 1052672,
      currentReferencePrice  = 1048592,
      crossType              = CrossType.NasdaqOpening,
      priceVarianceIndicator = PriceVarianceIndicator.LessThanOnePercent
    )
  )

  implicit def stringToByteString(string: String): ByteString = new ByteString(string.getBytes)
}
