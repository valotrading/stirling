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

import stirling.itch.Spec
import stirling.itch.io.Source
import java.io.File
import java.nio.ByteBuffer
import stirling.itch.messages.itch41

class MessageParserSpec extends Spec with MessageParserFixtures {
  "MessageParser" must {
    "parse messages from binary file" in {
      val file = new File("src/test/resources/itch-v41.txt")
      val source = Source.fromFile[Message](file, new SoupBinTCPFileParser)
      expectedMessages must equal (source.toList)
    }
  }
}

trait MessageParserFixtures {
  val expectedMessages = List(
      Seconds(
        seconds = 24136
      ),
      SystemEvent(
        nanoSeconds       = 800734287,
        systemMessageType = SystemMessageType.StartOfMessages
      ),
      Seconds(
        seconds           = 24611
      ),
      StockDirectory(
        nanoSeconds              = 650506166,
        stock                    = ITCH.toAscii("QQQ     "),
        marketCategory           = MarketCategory.Nyse,
        financialStatusIndicator = FinancialStatusIndicator.NasdaqCompliant,
        roundLotSize             = 100,
        roundLotsOnly            = false
      ),
      MarketParticipantPosition(
        nanoSeconds            = 661054256,
        mpid                   = ITCH.toAscii("ABCD"),
        stock                  = ITCH.toAscii("SPY     "),
        isPrimary              = false,
        mode                   = MarketMakerMode.Normal,
        status                 = MarketParticipantState.Active
      ),
      StockTradingAction(
        nanoSeconds  = 841159237,
        stock        = ITCH.toAscii("SPY     "),
        tradingState = TradingState.Halted,
        reserved     = ' '.toByte,
        reason       = ITCH.toAscii("IPO1")
      ),
      RegSHOShortSalePriceTestRestrictedIndicator(
        nanoSeconds = 1412785731,
        stock       = ITCH.toAscii("SPY     "),
        shoAction   = RegSHOAction.NoPriceTest
      ),
      AddOrder(
        nanoSeconds      = 1935833240,
        referenceNumber  = 4096,
        buySellIndicator = BuySellIndicator.Buy,
        shares           = 1376271,
        stock            = ITCH.toAscii("SPY     "),
        price            = 65535
      ),
      AddOrder(
        nanoSeconds      = 825374775,
        referenceNumber  = 4096,
        buySellIndicator = BuySellIndicator.Buy,
        shares           = 458767,
        stock            = ITCH.toAscii("QQQ     "),
        price            = 65535,
        attribution      = Some(ITCH.toAscii("ATTR"))
      ),
      OrderExecuted(
        nanoSeconds     = 880306004,
        referenceNumber = 16L,
        executedShares  = 5,
        matchNumber     = 256L),
      OrderExecuted(
        nanoSeconds     = 1161242641,
        referenceNumber = 16L,
        executedShares  = 5,
        matchNumber     = 256L,
        printable       = Some(true),
        price           = Some(256)
      ),
      OrderCancel(
        nanoSeconds     = 275075413,
        referenceNumber = 1L,
        canceledShares  = 16
      ),
      OrderDelete(
        nanoSeconds     = 2016946456,
        referenceNumber = 2L
      ),
      OrderReplace(
        nanoSeconds             = 909260338,
        originalReferenceNumber = 1L,
        newReferenceNumber      = 2L,
        shares                  = 1,
        price                   = 2
      ),
      Trade(
        nanoSeconds          = 286331153,
        orderReferenceNumber = 1L,
        buySellIndicator     = BuySellIndicator.Sell,
        shares               = 2,
        stock                = ITCH.toAscii("SPY     "),
        price                = 4521985,
        matchNumber          = 2L
      ),
      CrossTrade(
        nanoSeconds = 50529027,
        shares      = 2L,
        stock       = ITCH.toAscii("QQQ     "),
        crossPrice  = 1179648,
        matchNumber = 2048,
        crossType   = CrossType.NasdaqClosing
      ),
      BrokenTrade(
        nanoSeconds = 1717986918,
        matchNumber = 2L
      ),
      NetOrderImbalanceIndicator(
        nanoSeconds            = 892482614,
        pairedShares           = 258L,
        imbalance              = 259L,
        imbalanceDirection     = ImbalanceDirection.Buy,
        stock                  = ITCH.toAscii("QQQ     "),
        farPrice               = 1048577,
        nearPrice              = 1052672,
        currentReferencePrice  = 1048592,
        crossType              = CrossType.NasdaqOpening,
        priceVarianceIndicator = PriceVarianceIndicator.LessThanOnePercent
      )
  )
}
