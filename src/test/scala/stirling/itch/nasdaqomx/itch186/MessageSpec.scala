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

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import scala.language.implicitConversions
import stirling.io.ByteString

class MessageSpec extends WordSpec with MustMatchers {
  "Message" should {
    "decode Seconds" in {
      val message = Seconds("T12345")
      message.messageType must equal('T')
      message.second      must equal(12345)
    }
    "decode Milliseconds" in {
      val message = Milliseconds("M123")
      message.messageType must equal('M')
      message.millisecond must equal(123)
    }
    "decode SystemEvent" in {
      val message = SystemEvent("SO")
      message.messageType must equal('S')
      message.eventCode   must equal(SystemEventCode.StartOfMessages)
    }
    "decode MarketSegmentState" in {
      val message = MarketSegmentState("O123P")
      message.messageType     must equal('O')
      message.marketSegmentId must equal(123)
      message.eventCode       must equal(MarketSegmentStateCode.PreOpen)
    }
    "decode OrderBookDirectory" in {
      val message = OrderBookDirectory("R123456ABCDEFGHIJKLMNOPFI0000000000  1EURXHEL123       9     1000")
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
    "decode OrderBookTradingAction" in {
      val message = OrderBookTradingAction("H123456TX    ")
      message.messageType  must equal('H')
      message.orderBook    must equal(123456)
      message.tradingState must equal(TradingState.TradingOnNasdaqOmxNordic)
      message.reason       must equal(TradingActionReason.NotAvailable)
    }
    "decode AddOrder" in {
      val message = AddOrder("A123456789B    10000123456    215000")
      message.messageType          must equal('A')
      message.orderReferenceNumber must equal(123456789)
      message.buySellIndicator     must equal(BuySellIndicator.Buy)
      message.quantity             must equal(10000)
      message.orderBook            must equal(123456)
      message.price                must equal(215000)
    }
    "decode AddOrderMPID" in {
      val message = AddOrderMPID("F123456789B    10000123456    215000_MMO")
      message.messageType          must equal('F')
      message.orderReferenceNumber must equal(123456789)
      message.buySellIndicator     must equal(BuySellIndicator.Buy)
      message.quantity             must equal(10000)
      message.orderBook            must equal(123456)
      message.price                must equal(215000)
      message.attribution          must equal(Attribution.MarketMaker)
    }
    "decode OrderExecuted" in {
      val message = OrderExecuted("E123456789     1000123456789ACMEBAT ")
      message.messageType           must equal('E')
      message.orderReferenceNumber  must equal(123456789)
      message.executedQuantity      must equal(1000)
      message.matchNumber           must equal(123456789)
      message.owner.toString        must equal("ACME")
      message.counterparty.toString must equal("BAT ")
    }
    "decode OrderExecutedWithPrice" in {
      val message = OrderExecutedWithPrice("C123456789     1000123456789Y    332500ACMEBAT ")
      message.messageType           must equal('C')
      message.orderReferenceNumber  must equal(123456789)
      message.executedQuantity      must equal(1000)
      message.matchNumber           must equal(123456789)
      message.printable             must be(true)
      message.tradePrice            must equal(332500)
      message.owner.toString        must equal("ACME")
      message.counterparty.toString must equal("BAT ")
    }
    "decode OrderCancel" in {
      val message = OrderCancel("X123456789      500")
      message.messageType          must equal('X')
      message.orderReferenceNumber must equal(123456789)
      message.canceledQuantity     must equal(500)
    }
    "decode OrderDelete" in {
      val message = OrderDelete("D123456789")
      message.messageType          must equal('D')
      message.orderReferenceNumber must equal(123456789)
    }
    "decode Trade" in {
      val message = Trade("P123456789B     1000123456123456789    332500ACMEBAT ")
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
    "decode CrossTrade" in {
      val message = CrossTrade("Q123456789123456    122500123456789O      1000")
      message.messageType    must equal('Q')
      message.quantity       must equal(123456789)
      message.orderBook      must equal(123456)
      message.crossPrice     must equal(122500)
      message.matchNumber    must equal(123456789)
      message.crossType      must equal(CrossType.OpeningCross)
      message.numberOfTrades must equal(1000)
    }
    "decode BrokenTrade" in {
      val message = BrokenTrade("B123456789")
      message.messageType must equal('B')
      message.matchNumber must equal(123456789)
    }
    "decode NOII" in {
      val message = NOII("I     1000     2000B123456    125000C    122500      500    127500      250")
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

  implicit def stringToByteString(string: String): ByteString = new ByteString(string.getBytes)
}
