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
package stirling.itch.bats.top120

import scala.language.implicitConversions
import stirling.io.ByteString
import stirling.itch.Spec

class MessageSpec extends Spec {
  "Message" should {
    "decode LogonAccepted" in {
      val message = LogonAccepted("C\n")
      message.messageType must equal('C')
    }
    "decode LogonRejected" in {
      val message = LogonRejected("JA\n")
      message.messageType  must equal('J')
      message.rejectReason must equal(RejectReason.AuthenticationOrAuthorizationProblem)
    }
    "decode ExpandedSpin" in {
      val message = ExpandedSpin("s34348112TESTA   00001234000002000000123500001000343470000000123400000100000120100T0??\n")
      message.messageType      must equal('s')
      message.timestamp        must equal(34348112)
      message.symbol.toString  must equal("TESTA   ")
      message.bidPrice         must equal(123400)
      message.bidQuantity      must equal(200)
      message.askPrice         must equal(123500)
      message.askQuantity      must equal(1000)
      message.lastTradeTime    must equal(34347000)
      message.lastTradePrice   must equal(123400)
      message.lastTradeSize    must equal(100)
      message.cumulativeVolume must equal(120100)
      message.haltStatus       must equal(HaltStatus.Trading)
      message.regShoAction     must equal(RegSHOAction.NoPriceTestInEffect)
    }
    "decode SpinDone" in {
      val message = SpinDone("D\n")
      message.messageType must equal('D')
    }
    "decode ServerHeartbeat" in {
      val message = ServerHeartbeat("H\n")
      message.messageType must equal('H')
    }
    "decode Seconds" in {
      val message = Seconds("T34348\n")
      message.messageType must equal('T')
      message.seconds     must equal(34348)
    }
    "decode Milliseconds" in {
      val message = Milliseconds("M110\n")
      message.messageType  must equal('M')
      message.milliseconds must equal(110)
    }
    "decode ExpandedBidUpdate" in {
      val message = ExpandedBidUpdate("EZVZZT   0000123400001100\n")
      message.messageType     must equal('E')
      message.symbol.toString must equal("ZVZZT   ")
      message.bidPrice        must equal(123400)
      message.bidQuantity     must equal(1100)
    }
    "decode LongBidUpdate" in {
      val message = LongBidUpdate("BZVZZT 0000123400001100\n")
      message.messageType     must equal('B')
      message.symbol.toString must equal("ZVZZT ")
      message.bidPrice        must equal(123400)
      message.bidQuantity     must equal(1100)
    }
    "decode ShortBidUpdate" in {
      val message = ShortBidUpdate("bRIMM1312200100\n")
      message.messageType     must equal('b')
      message.symbol.toString must equal("RIMM")
      message.bidPrice        must equal(13122)
      message.bidQuantity     must equal(100)
    }
    "decode ExpandedAskUpdate" in {
      val message = ExpandedAskUpdate("eQQQQ    0000123400001100\n")
      message.messageType     must equal('e')
      message.symbol.toString must equal("QQQQ    ")
      message.askPrice        must equal(123400)
      message.askQuantity     must equal(1100)
    }
    "decode LongAskUpdate" in {
      val message = LongAskUpdate("AQQQQ  0000123400001100\n")
      message.messageType     must equal('A')
      message.symbol.toString must equal("QQQQ  ")
      message.askPrice        must equal(123400)
      message.askQuantity     must equal(1100)
    }
    "decode ShortAskUpdate" in {
      val message = ShortAskUpdate("aQID 0394500200\n")
      message.messageType     must equal('a')
      message.symbol.toString must equal("QID ")
      message.askPrice        must equal(3945)
      message.askQuantity     must equal(200)
    }
    "decode ExpandedTwoSidedUpdate" in {
      val message = ExpandedTwoSidedUpdate("FQQQQ    00004870002402000000487100000200\n")
      message.messageType     must equal('F')
      message.symbol.toString must equal("QQQQ    ")
      message.bidPrice        must equal(487000)
      message.bidQuantity     must equal(240200)
      message.askPrice        must equal(487100)
      message.askQuantity     must equal(200)
    }
    "decode LongTwoSidedUpdate" in {
      val message = LongTwoSidedUpdate("UQQQQ  00004870002402000000487100000200\n")
      message.messageType     must equal('U')
      message.symbol.toString must equal("QQQQ  ")
      message.bidPrice        must equal(487000)
      message.bidQuantity     must equal(240200)
      message.askPrice        must equal(487100)
      message.askQuantity     must equal(200)
    }
    "decode ShortTwoSidedUpdate" in {
      val message = ShortTwoSidedUpdate("uQID 03944120000394500300\n")
      message.messageType     must equal('u')
      message.symbol.toString must equal("QID ")
      message.bidPrice        must equal(3944)
      message.bidQuantity     must equal(12000)
      message.askPrice        must equal(3945)
      message.askQuantity     must equal(300)
    }
    "decode ExpandedTrade" in {
      val message = ExpandedTrade("fSPY     0001379800000100024250601\n")
      message.messageType      must equal('f')
      message.symbol.toString  must equal("SPY     ")
      message.lastPrice        must equal(1379800)
      message.lastQuantity     must equal(100)
      message.cumulativeVolume must equal(24250601)
    }
    "decode LongTrade" in {
      val message = LongTrade("VSPY   0001379800000100024250601\n")
      message.messageType      must equal('V')
      message.symbol.toString  must equal("SPY   ")
      message.lastPrice        must equal(1379800)
      message.lastQuantity     must equal(100)
      message.cumulativeVolume must equal(24250601)
    }
    "decode ShortTrade" in {
      val message = ShortTrade("vRIMM13122003001200400\n")
      message.messageType      must equal('v')
      message.symbol.toString  must equal("RIMM")
      message.lastPrice        must equal(13122)
      message.lastQuantity     must equal(300)
      message.cumulativeVolume must equal(1200400)
    }
    "decode TradingStatus" in {
      val message = TradingStatus("tQQQQ    H1??\n")
      message.messageType     must equal('t')
      message.symbol.toString must equal("QQQQ    ")
      message.haltStatus      must equal(HaltStatus.Halted)
      message.regShoAction    must equal(RegSHOAction.RegSHOPriceTestRestrictionInEffect)
    }
  }

  implicit def stringToByteString(string: String): ByteString = new ByteString(string.getBytes)
}
