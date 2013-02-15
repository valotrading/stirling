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
package stirling.nasdaqomx.itch186

import java.nio.ByteBuffer
import org.scalatest.matchers.MustMatchers
import org.scalatest.{OneInstancePerTest, WordSpec}
import scala.language.implicitConversions
import stirling.io.ByteString

class MessageSpec extends WordSpec with MustMatchers with OneInstancePerTest {
  "Message" should {
    val payload = ByteBuffer.allocate(1024)

    "format and parse Seconds" in {
      Seconds.format(
        buffer = payload,
        second = 12345
      )
      val message = Seconds(payload)
      message.messageType must equal('T')
      message.second      must equal(12345)
    }
    "format and parse Milliseconds" in {
      Milliseconds.format(
        buffer      = payload,
        millisecond = 123
      )
      val message = Milliseconds(payload)
      message.messageType must equal('M')
      message.millisecond must equal(123)
    }
    "format and parse SystemEvent" in {
      SystemEvent.format(
        buffer    = payload,
        eventCode = 'O'.toByte
      )
      val message = SystemEvent(payload)
      message.messageType must equal('S')
      message.eventCode   must equal(SystemEventCode.StartOfMessages)
    }
    "format and parse MarketSegmentState" in {
      MarketSegmentState.format(
        buffer          = payload,
        marketSegmentId = 123,
        eventCode       = 'P'.toByte
      )
      val message = MarketSegmentState(payload)
      message.messageType     must equal('O')
      message.marketSegmentId must equal(123)
      message.eventCode       must equal(MarketSegmentStateCode.PreOpen)
    }
    "format and parse OrderBookDirectory" in {
      OrderBookDirectory.format(
        buffer           = payload,
        orderBook        = 123456,
        symbol           = "ABCDEFGHIJKLMNOP",
        isin             = "FI0000000000",
        financialProduct = 1,
        tradingCurrency  = "EUR",
        mic              = "XHEL",
        marketSegmentId  = 123,
        noteCodes        = 9,
        roundLotSize     = 1000
      )
      val message = OrderBookDirectory(payload)
      message.messageType              must equal('R')
      message.orderBook                must equal(123456)
      message.symbol.toString          must equal("ABCDEFGHIJKLMNOP")
      message.isin.toString            must equal("FI0000000000")
      message.financialProduct         must equal(FinancialProduct.Stock)
      message.tradingCurrency.toString must equal("EUR")
      message.mic.toString             must equal("XHEL")
      message.marketSegmentId          must equal(123)
      message.noteCodes                must equal(NoteCode.NM | NoteCode.PO)
      message.roundLotSize             must equal(1000)
    }
    "format and parse OrderBookTradingAction" in {
      OrderBookTradingAction.format(
        buffer       = payload,
        orderBook    = 123456,
        tradingState = 'T'.toByte,
        reserved     = 'X'.toByte,
        reason       = " "
      )
      val message = OrderBookTradingAction(payload)
      message.messageType  must equal('H')
      message.orderBook    must equal(123456)
      message.tradingState must equal(TradingState.TradingOnNasdaqOmxNordic)
      message.reason       must equal(TradingActionReason.NotAvailable)
    }
    "format and parse AddOrder" in {
      AddOrder.format(
        buffer               = payload,
        orderReferenceNumber = 123456789,
        buySellIndicator     = 'B'.toByte,
        quantity             = 10000,
        orderBook            = 123456,
        price                = 215000
      )
      val message = AddOrder(payload)
      message.messageType          must equal('A')
      message.orderReferenceNumber must equal(123456789)
      message.buySellIndicator     must equal(BuySellIndicator.Buy)
      message.quantity             must equal(10000)
      message.orderBook            must equal(123456)
      message.price                must equal(215000)
    }
    "format and parse AddOrderMPID" in {
      AddOrderMPID.format(
        buffer               = payload,
        orderReferenceNumber = 123456789,
        buySellIndicator     = 'B'.toByte,
        quantity             = 10000,
        orderBook            = 123456,
        price                = 215000,
        attribution          = "_MMO"
      )
      val message = AddOrderMPID(payload)
      message.messageType          must equal('F')
      message.orderReferenceNumber must equal(123456789)
      message.buySellIndicator     must equal(BuySellIndicator.Buy)
      message.quantity             must equal(10000)
      message.orderBook            must equal(123456)
      message.price                must equal(215000)
      message.attribution          must equal(Attribution.MarketMaker)
    }
    "format and parse OrderExecuted" in {
      OrderExecuted.format(
        buffer               = payload,
        orderReferenceNumber = 123456789,
        executedQuantity     = 1000,
        matchNumber          = 123456789,
        owner                = "ACME",
        counterparty         = "BAT "
      )
      val message = OrderExecuted(payload)
      message.messageType           must equal('E')
      message.orderReferenceNumber  must equal(123456789)
      message.executedQuantity      must equal(1000)
      message.matchNumber           must equal(123456789)
      message.owner.toString        must equal("ACME")
      message.counterparty.toString must equal("BAT ")
    }
    "format and parse OrderExecutedWithPrice" in {
      OrderExecutedWithPrice.format(
        buffer               = payload,
        orderReferenceNumber = 123456789,
        executedQuantity     = 1000,
        matchNumber          = 123456789,
        printable            = true,
        tradePrice           = 332500,
        owner                = "ACME",
        counterparty         = "BAT"
      )
      val message = OrderExecutedWithPrice(payload)
      message.messageType           must equal('C')
      message.orderReferenceNumber  must equal(123456789)
      message.executedQuantity      must equal(1000)
      message.matchNumber           must equal(123456789)
      message.printable             must be(true)
      message.tradePrice            must equal(332500)
      message.owner.toString        must equal("ACME")
      message.counterparty.toString must equal("BAT ")
    }
    "format and parse OrderCancel" in {
      OrderCancel.format(
        buffer               = payload,
        orderReferenceNumber = 123456789,
        canceledQuantity     = 500
      )
      val message = OrderCancel(payload)
      message.messageType          must equal('X')
      message.orderReferenceNumber must equal(123456789)
      message.canceledQuantity     must equal(500)
    }
    "format and parse OrderDelete" in {
      OrderDelete.format(
        buffer               = payload,
        orderReferenceNumber = 123456789
      )
      val message = OrderDelete(payload)
      message.messageType          must equal('D')
      message.orderReferenceNumber must equal(123456789)
    }
    "format and parse Trade" in {
      Trade.format(
        buffer               = payload,
        orderReferenceNumber = 123456789,
        tradeType            = 'B'.toByte,
        quantity             = 1000,
        orderBook            = 123456,
        matchNumber          = 123456789,
        tradePrice           = 332500,
        buyer                = "ACME",
        seller               = "BAT"
      )
      val message = Trade(payload)
      message.messageType          must equal('P')
      message.orderReferenceNumber must equal(123456789)
      message.tradeType            must equal(TradeType.MainBook)
      message.quantity             must equal(1000)
      message.orderBook            must equal(123456)
      message.matchNumber          must equal(123456789)
      message.tradePrice           must equal(332500)
      message.buyer.toString       must equal("ACME")
      message.seller.toString      must equal("BAT ")
    }
    "format and parse CrossTrade" in {
      CrossTrade.format(
        buffer         = payload,
        quantity       = 123456789,
        orderBook      = 123456,
        crossPrice     = 122500,
        matchNumber    = 123456789,
        crossType      = 'O'.toByte,
        numberOfTrades = 1000
      )
      val message = CrossTrade(payload)
      message.messageType    must equal('Q')
      message.quantity       must equal(123456789)
      message.orderBook      must equal(123456)
      message.crossPrice     must equal(122500)
      message.matchNumber    must equal(123456789)
      message.crossType      must equal(CrossType.OpeningCross)
      message.numberOfTrades must equal(1000)
    }
    "format and parse BrokenTrade" in {
      BrokenTrade.format(
        buffer      = payload,
        matchNumber = 123456789
      )
      val message = BrokenTrade(payload)
      message.messageType must equal('B')
      message.matchNumber must equal(123456789)
    }
    "format and parse NOII" in {
      NOII.format(
        buffer             = payload,
        pairedQuantity     = 1000,
        imbalanceQuantity  = 2000,
        imbalanceDirection = 'B'.toByte,
        orderBook          = 123456,
        equilibriumPrice   = 125000,
        crossType          = 'C'.toByte,
        bestBidPrice       = 122500,
        bestBidQuantity    = 500,
        bestAskPrice       = 127500,
        bestAskQuantity    = 250
      )
      val message = NOII(payload)
      message.messageType        must equal('I')
      message.pairedQuantity     must equal(1000)
      message.imbalanceQuantity  must equal(2000)
      message.imbalanceDirection must equal(ImbalanceDirection.BuyImbalance)
      message.orderBook          must equal(123456)
      message.equilibriumPrice   must equal(125000)
      message.crossType          must equal(CrossType.ClosingCross)
      message.bestBidPrice       must equal(122500)
      message.bestBidQuantity    must equal(500)
      message.bestAskPrice       must equal(127500)
      message.bestAskQuantity    must equal(250)
    }
  }

  implicit def byteBufferToByteString(buffer: ByteBuffer): ByteString = {
    buffer.flip

    val bytes = new Array[Byte](buffer.remaining)
    buffer.get(bytes)

    new ByteString(bytes)
  }
}
