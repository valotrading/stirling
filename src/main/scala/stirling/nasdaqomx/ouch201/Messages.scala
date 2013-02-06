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
import java.nio.charset.Charset
import java.util.Arrays
import scala.annotation.switch
import stirling.io.{ByteString, TextFormat}

sealed trait Message {
  def payload: ByteString

  def timestamp   = payload.slice(0, 8).toLong
  def messageType = payload.byteAt(8)

  override def toString = payload.toString
}

object Message {
  val ASCII = Charset.forName("US-ASCII")

  val messageTypeOffset = 8

  def alpha(value: String, fieldSize: Int) = TextFormat.alpha(value, fieldSize, ' '.toByte)

  def numeric(value: Long, fieldSize: Int) = TextFormat.numeric(value, fieldSize, '0'.toByte)
}

sealed trait MessageType {
  def apply(payload: ByteString): Message

  def size: Int
}

/*
 * Section 2.1
 */
class SystemEvent(val payload: ByteString) extends Message {
  def eventCode: Byte = payload.byteAt(9)
}

object SystemEvent extends MessageType {
  def apply(payload: ByteString) = new SystemEvent(payload)

  def size = 10
}

/*
 * Section 2.2.1
 */
class OrderAccepted(val payload: ByteString) extends Message {
  def orderToken:           ByteString = payload.slice(  9, 14)
  def buySellIndicator:     Byte       = payload.byteAt(23)
  def quantity:             Long       = payload.slice( 24,  9).toLong
  def orderBook:            Long       = payload.slice( 33,  6).toLong
  def price:                Long       = payload.slice( 39, 10).toLong
  def timeInForce:          Long       = payload.slice( 49,  5).toLong
  def firm:                 ByteString = payload.slice( 54,  4)
  def display:              Byte       = payload.byteAt(58)
  def orderReferenceNumber: Long       = payload.slice( 59,  9).toLong
  def capacity:             Byte       = payload.byteAt(68)
  def user:                 ByteString = payload.slice( 69,  6)
  def clientReference:      ByteString = payload.slice( 75, 15)
  def orderReference:       ByteString = payload.slice( 90, 10)
  def clearingFirm:         ByteString = payload.slice(100,  4)
  def clearingAccount:      ByteString = payload.slice(104, 12)
  def minimumQuantity:      Long       = payload.slice(116,  9).toLong
  def crossType:            Byte       = payload.byteAt(125)
}

object OrderAccepted extends MessageType {
  def apply(payload: ByteString) = new OrderAccepted(payload)

  def size = 126
}

/*
 * Section 2.2.2
 */
class OrderReplaced(val payload: ByteString) extends Message {
  def oldOrderToken:        ByteString = payload.slice(  9, 14)
  def reason:               Byte       = payload.byteAt(23)
  def orderToken:           ByteString = payload.slice( 24, 14)
  def buySellIndicator:     Byte       = payload.byteAt(38)
  def quantity:             Long       = payload.slice( 39,  9).toLong
  def orderBook:            Long       = payload.slice( 48,  6).toLong
  def price:                Long       = payload.slice( 54, 10).toLong
  def timeInForce:          Long       = payload.slice( 64,  5).toLong
  def firm:                 ByteString = payload.slice( 69,  4)
  def display:              Byte       = payload.byteAt(73)
  def orderReferenceNumber: Long       = payload.slice( 74,  9).toLong
  def capacity:             Byte       = payload.byteAt(83)
  def user:                 ByteString = payload.slice( 84,  6)
  def clientReference:      ByteString = payload.slice( 90, 15)
  def orderReference:       ByteString = payload.slice(105, 10)
  def clearingFirm:         ByteString = payload.slice(115,  4)
  def clearingAccount:      ByteString = payload.slice(119, 12)
  def minimumQuantity:      Long       = payload.slice(131,  9).toLong
  def crossType:            Byte       = payload.byteAt(140)
}

object OrderReplaced extends MessageType {
  def apply(payload: ByteString) = new OrderReplaced(payload)

  def size = 141
}

/*
 * Section 2.2.3
 */
class CanceledOrder(val payload: ByteString) extends Message {
  def orderToken:        ByteString = payload.slice( 9, 14)
  def decrementQuantity: Long       = payload.slice(23,  9).toLong
  def reason:            Byte       = payload.byteAt(32) 
}

object CanceledOrder extends MessageType {
  def apply(payload: ByteString) = new CanceledOrder(payload)

  def size = 33
}

/*
 * Section 2.2.3
 */
class ExecutedOrder(val payload: ByteString) extends Message {
  def orderToken:       ByteString = payload.slice( 9, 14)
  def executedQuantity: Long       = payload.slice(23,  9).toLong
  def executionPrice:   Long       = payload.slice(32, 10).toLong
  def liquidityFlag:    Byte       = payload.byteAt(42)
  def matchNumber:      Long       = payload.slice(43,  9).toLong
  def contraFirm:       ByteString = payload.slice(52,  4)
}

object ExecutedOrder extends MessageType {
  def apply(payload: ByteString) = new ExecutedOrder(payload)

  def size = 56
}

/*
 * Section 2.2.4
 */
class BrokenTrade(val payload: ByteString) extends Message {
  def orderToken:  ByteString = payload.slice( 9, 14)
  def matchNumber: Long       = payload.slice(23,  9).toLong
  def reason:      Byte       = payload.byteAt(32)
}

object BrokenTrade extends MessageType {
  def apply(payload: ByteString) = new BrokenTrade(payload)

  def size = 33
}

/*
 * Section 2.2.5
 */
class RejectedOrder(val payload: ByteString) extends Message {
  def orderToken: ByteString = payload.slice(9, 14)
  def reason:     Byte       = payload.byteAt(23)
}

object RejectedOrder extends MessageType {
  def apply(payload: ByteString) = new RejectedOrder(payload)

  def size = 24
}

/*
 * Section 2.2.6
 */
class CancelPending(val payload: ByteString) extends Message {
  def orderToken: ByteString = payload.slice(9, 14)
}

object CancelPending extends MessageType {
  def apply(payload: ByteString) = new CancelPending(payload)

  def size = 23
}

/*
 * Section 2.2.7
 */
class MMORefreshRequest(val payload: ByteString) extends Message {
  def firm:       ByteString = payload.slice( 9, 4)
  def orderBook:  Long       = payload.slice(13, 6).toLong
}

object MMORefreshRequest extends MessageType {
  def apply(payload: ByteString) = new MMORefreshRequest(payload)

  def size = 19
}

/*
 * Section 3.1
 */
object EnterOrder {
  import Message._

  val size = 109

  def format(
    buffer:           ByteBuffer,
    messageType:      Byte,
    orderToken:       String,
    buySellIndicator: Byte,
    quantity:         Long,
    orderBook:        Long,
    price:            Long,
    timeInForce:      Long,
    firm:             String,
    display:          Byte,
    capacity:         Byte,
    user:             String,
    clientReference:  String,
    orderReference:   String,
    clearingFirm:     String,
    clearingAccount:  String,
    minimumQuantity:  Long,
    crossType:        Byte
  ) {
    buffer.put(messageType)
    buffer.put(alpha(orderToken, 14))
    buffer.put(buySellIndicator)
    buffer.put(numeric(quantity, 9))
    buffer.put(numeric(orderBook, 6))
    buffer.put(numeric(price, 10))
    buffer.put(numeric(timeInForce, 5))
    buffer.put(alpha(firm, 4))
    buffer.put(display)
    buffer.put(capacity)
    buffer.put(alpha(user, 6))
    buffer.put(alpha(clientReference, 15))
    buffer.put(alpha(orderReference, 10))
    buffer.put(alpha(clearingFirm, 4))
    buffer.put(alpha(clearingAccount, 12))
    buffer.put(numeric(minimumQuantity, 9))
    buffer.put(crossType)
  }
}

/*
 * Section 3.2
 */
object CancelOrder {
  import Message._

  val size = 30

  def format(
    buffer:     ByteBuffer,
    orderToken: String,
    quantity:   Long,
    user:       String
  ) {
    buffer.put('X'.toByte)
    buffer.put(alpha(orderToken, 14))
    buffer.put(numeric(quantity, 9))
    buffer.put(alpha(user, 6))
  }
}
