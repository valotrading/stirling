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
package stirling.itch.bats.pitch1120

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import scala.language.implicitConversions
import stirling.io.ByteString

class MessageSpec extends WordSpec with MustMatchers {
  "Message" should {
    "decode SymbolClear" in {
      val message = SymbolClear("12345678uACME  ")
      message.timestamp            must equal(12345678)
      message.messageType          must equal('u')
      message.stockSymbol.toString must equal("ACME  ")
    }
    "decode AddOrderShort" in {
      val message = AddOrderShort("12345678AABCDEFGHIJKLB000500ACME  0000002500Y")
      message.timestamp            must equal(12345678)
      message.messageType          must equal('A')
      message.orderId.toString     must equal("ABCDEFGHIJKL")
      message.sideIndicator        must equal(SideIndicator.Buy)
      message.shares               must equal(500)
      message.stockSymbol.toString must equal("ACME  ")
      message.price                must equal(2500)
      message.display              must equal(true)
    }
    "decode AddOrderLong" in {
      val message = AddOrderLong("12345678dABCDEFGHIJKLS000500ACME    0000002500NRR  ")
      message.timestamp              must equal(12345678)
      message.messageType            must equal('d')
      message.orderId.toString       must equal("ABCDEFGHIJKL")
      message.sideIndicator          must equal(SideIndicator.Sell)
      message.shares                 must equal(500)
      message.stockSymbol.toString   must equal("ACME    ")
      message.price                  must equal(2500)
      message.display                must equal(false)
      message.participantId.toString must equal("RR  ")
    }
    "decode OrderExecuted" in {
      val message = OrderExecuted("12345678EABCDEFGHIJKL010000MNOPQRSTUVWX")
      message.timestamp            must equal(12345678)
      message.messageType          must equal('E')
      message.orderId.toString     must equal("ABCDEFGHIJKL")
      message.executedShares       must equal(10000)
      message.executionId.toString must equal("MNOPQRSTUVWX")
    }
    "decode OrderCancel" in {
      val message = OrderCancel("12345678XABCDEFGHIJKL005000")
      message.timestamp        must equal(12345678)
      message.messageType      must equal('X')
      message.orderId.toString must equal("ABCDEFGHIJKL")
      message.canceledShares   must equal(5000)
    }
    "decode TradeShort" in {
      val message = TradeShort("12345678PABCDEFGHIJKLB000500ACME  0000002500MNOPQRSTUVWX")
      message.timestamp            must equal(12345678)
      message.messageType          must equal('P')
      message.orderId.toString     must equal("ABCDEFGHIJKL")
      message.sideIndicator        must equal(SideIndicator.Buy)
      message.shares               must equal(500)
      message.stockSymbol.toString must equal("ACME  ")
      message.price                must equal(2500)
      message.executionId.toString must equal("MNOPQRSTUVWX")
    }
    "decode TradeLong" in {
      val message = TradeLong("12345678rABCDEFGHIJKLS000500ACME    0000002500MNOPQRSTUVWX")
      message.timestamp            must equal(12345678)
      message.messageType          must equal('r')
      message.orderId.toString     must equal("ABCDEFGHIJKL")
      message.sideIndicator        must equal(SideIndicator.Sell)
      message.shares               must equal(500)
      message.stockSymbol.toString must equal("ACME    ")
      message.price                must equal(2500)
      message.executionId.toString must equal("MNOPQRSTUVWX")
    }
    "decode TradeBreak" in {
      val message = TradeBreak("12345678BABCDEFGHIJKL")
      message.timestamp            must equal(12345678)
      message.messageType          must equal('B')
      message.executionId.toString must equal("ABCDEFGHIJKL")
    }
    "decode TradingStatus" in {
      val message = TradingStatus("12345678HACME    T0")
      message.timestamp            must equal(12345678)
      message.messageType          must equal('H')
      message.stockSymbol.toString must equal("ACME    ")
      message.haltStatus           must equal(HaltStatus.Trading)
      message.regShoAction         must equal(RegSHOAction.NoPriceTestInEffect)
    }
    "decode AuctionUpdate" in {
      val message = AuctionUpdate("12345678IACME    O00000010000000002000000000300000000040000000005000")
      message.timestamp            must equal(12345678)
      message.messageType          must equal('I')
      message.stockSymbol.toString must equal("ACME    ")
      message.auctionType          must equal(AuctionType.OpeningAuction)
      message.referencePrice       must equal(1000)
      message.buyShares            must equal(2000)
      message.sellShares           must equal(3000)
      message.indicativePrice      must equal(4000)
      message.auctionOnlyPrice     must equal(5000)
    }
    "decode AuctionSummary" in {
      val message = AuctionSummary("12345678JACME    C00000010000000002000")
      message.timestamp            must equal(12345678)
      message.messageType          must equal('J')
      message.stockSymbol.toString must equal("ACME    ")
      message.auctionType          must equal('C')
      message.price                must equal(1000)
      message.shares               must equal(2000)
    }
    "decode RetailPriceImprovement" in {
      val message = RetailPriceImprovement("12345678RACME    B")
      message.timestamp              must equal(12345678)
      message.messageType            must equal('R')
      message.stockSymbol.toString   must equal("ACME    ")
      message.retailPriceImprovement must equal(RPI.BuySideRPI)
    }
  }

  implicit def stringToByteString(string: String): ByteString = new ByteString(string.getBytes)
}
