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
package stirling.nasdaqomx.ouch201

import java.nio.ByteBuffer
import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import scala.language.implicitConversions
import stirling.io.ByteString

class MessageSpec extends WordSpec with MustMatchers {
  "Message" should {
    "parse SystemEvent" in {
      val message = SystemEvent("12345678SS")
      message.timestamp   must equal(12345678)
      message.messageType must equal('S')
      message.eventCode   must equal(SystemEventCode.StartOfDay)
    }
    "parse OrderAccepted" in {
      val message = OrderAccepted("12345678AABCDEFGHIJKLMNB000000400123456000125000012345ABC W1234567893DonaldX              ABCDEFGHIJXXXXABCDEFGHIJKL000000000 ")
      message.timestamp                must equal(12345678)
      message.messageType              must equal('A')
      message.orderToken.toString      must equal("ABCDEFGHIJKLMN")
      message.buySellIndicator         must equal(BuySellIndicator.Buy)
      message.quantity                 must equal(400)
      message.orderBook                must equal(123456)
      message.price                    must equal(1250000)
      message.timeInForce              must equal(12345)
      message.firm.toString            must equal("ABC ")
      message.display                  must equal(Display.MarketMakerOrder)
      message.orderReferenceNumber     must equal(123456789)
      message.capacity                 must equal(Capacity.MarketMaker)
      message.user.toString            must equal("Donald")
      message.clientReference.toString must equal("X              ")
      message.orderReference.toString  must equal("ABCDEFGHIJ")
      message.clearingFirm.toString    must equal("XXXX")
      message.clearingAccount.toString must equal("ABCDEFGHIJKL")
      message.minimumQuantity          must equal(0)
    }
    "parse OrderAccepted for cross order" in {
      val message = OrderAccepted("12345678RABCDEFGHIJKLMNB000000400123456000125000012345ABC N1234567892DonaldX              ABCDEFGHIJXXXXABCDEFGHIJKL000000200C")
      message.timestamp                must equal(12345678)
      message.messageType              must equal('R')
      message.orderToken.toString      must equal("ABCDEFGHIJKLMN")
      message.buySellIndicator         must equal(BuySellIndicator.Buy)
      message.quantity                 must equal(400)
      message.orderBook                must equal(123456)
      message.price                    must equal(1250000)
      message.timeInForce              must equal(12345)
      message.firm.toString            must equal("ABC ")
      message.display                  must equal(Display.NonDisplay)
      message.orderReferenceNumber     must equal(123456789)
      message.capacity                 must equal(Capacity.OwnAccount)
      message.user.toString            must equal("Donald")
      message.clientReference.toString must equal("X              ")
      message.orderReference.toString  must equal("ABCDEFGHIJ")
      message.clearingFirm.toString    must equal("XXXX")
      message.clearingAccount.toString must equal("ABCDEFGHIJKL")
      message.minimumQuantity          must equal(200)
      message.crossType                must equal(CrossType.ClosingCross)
    }
    "parse OrderReplaced for cross order" in {
      val message = OrderReplaced("12345678UABCDEFGHIJKLMNUNMLKJIHGFEDCBAB000000400123456000125000012345ABC Y1234567892DonaldX              ABCDEFGHIJXXXXABCDEFGHIJKL000000200C")
      message.timestamp                must equal(12345678)
      message.messageType              must equal('U')
      message.oldOrderToken.toString   must equal("ABCDEFGHIJKLMN")
      message.reason                   must equal(ReplacedOrderReason.User)
      message.orderToken.toString      must equal("NMLKJIHGFEDCBA")
      message.buySellIndicator         must equal(BuySellIndicator.Buy)
      message.quantity                 must equal(400)
      message.orderBook                must equal(123456)
      message.price                    must equal(1250000)
      message.timeInForce              must equal(12345)
      message.firm.toString            must equal("ABC ")
      message.display                  must equal(Display.Display)
      message.orderReferenceNumber     must equal(123456789)
      message.capacity                 must equal(Capacity.OwnAccount)
      message.user.toString            must equal("Donald")
      message.clientReference.toString must equal("X              ")
      message.orderReference.toString  must equal("ABCDEFGHIJ")
      message.clearingFirm.toString    must equal("XXXX")
      message.clearingAccount.toString must equal("ABCDEFGHIJKL")
      message.minimumQuantity          must equal(200)
      message.crossType                must equal(CrossType.ClosingCross)
    }
    "parse CanceledOrder" in {
      val message = CanceledOrder("12345678CABCDEFGHIJKLMN000000200T")
      message.timestamp           must equal(12345678)
      message.messageType         must equal('C')
      message.orderToken.toString must equal("ABCDEFGHIJKLMN")
      message.decrementQuantity   must equal(200)
      message.reason              must equal(CanceledOrderReason.Timeout)
    }
    "parse ExecutedOrder" in {
      val message = ExecutedOrder("12345678EABCDEFGHIJKLMN0000040000001250000A123456789FOO ")
      message.timestamp           must equal(12345678)
      message.messageType         must equal('E')
      message.orderToken.toString must equal("ABCDEFGHIJKLMN")
      message.executedQuantity    must equal(4000)
      message.executionPrice      must equal(1250000)
      message.liquidityFlag       must equal(LiquidityFlag.AddedLiquidity)
      message.matchNumber         must equal(123456789)
      message.contraFirm.toString must equal("FOO ")
    }
    "parse BrokenTrade" in {
      val message = BrokenTrade("12345678BABCDEFGHIJKLMN123456789E")
      message.timestamp           must equal(12345678)
      message.messageType         must equal('B')
      message.orderToken.toString must equal("ABCDEFGHIJKLMN")
      message.matchNumber         must equal(123456789)
      message.reason              must equal(BrokenTradeReason.Erroneous)
    }
    "parse RejectedOrder" in {
      val message = RejectedOrder("12345678JABCDEFGHIJKLMNT")
      message.timestamp           must equal(12345678)
      message.messageType         must equal('J')
      message.orderToken.toString must equal("ABCDEFGHIJKLMN")
      message.reason              must equal(RejectedOrderReason.TestMode)
    }
    "format EnterOrder" in {
      val buffer = ByteBuffer.allocate(EnterOrder.size)

      EnterOrder.format(
        buffer,
        messageType      = 'O',
        orderToken       = "ABCDEFGHIJKLMN",
        buySellIndicator = BuySellIndicator.Buy,
        quantity         = 200,
        orderBook        = 123456,
        price            = 1250000,
        timeInForce      = TimeInForce.MarketHours,
        firm             = "ABC",
        display          = Display.Display,
        capacity         = Capacity.OwnAccount,
        user             = "Donald",
        clientReference  = "X",
        orderReference   = "ABCDEFGHIJ",
        clearingFirm     = "XXXX",
        clearingAccount  = "ABCDEFGHIJKL",
        minimumQuantity  = 0,
        crossType        = CrossType.ClosingCross
      )

      asString(buffer) must equal("OABCDEFGHIJKLMNB000000200123456000125000099998ABC Y2DonaldX              ABCDEFGHIJXXXXABCDEFGHIJKL000000000C")
    }
    "format CancelOrder" in {
      val buffer = ByteBuffer.allocate(CancelOrder.size)

      CancelOrder.format(
        buffer,
        orderToken = "ABCDEFGHIJKLMN",
        quantity   = 100,
        user       = "Donald"
      )

      asString(buffer) must equal("XABCDEFGHIJKLMN000000100Donald")
    }
  }

  def asString(buffer: ByteBuffer): String = {
    buffer.flip

    val bytes = new Array[Byte](buffer.remaining)
    buffer.get(bytes)

    new String(bytes, "US-ASCII")
  }

  implicit def stringToByteString(string: String): ByteString = new ByteString(string.getBytes)
}
