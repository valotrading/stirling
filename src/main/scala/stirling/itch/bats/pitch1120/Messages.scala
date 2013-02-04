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

import java.nio.ByteBuffer
import stirling.io.ByteString
import stirling.itch.ByteBuffers

sealed trait Message {
  def payload: ByteString

  def timestamp   = payload.slice(0, 8).toLong
  def messageType = payload.byteAt(8)

  override def toString = payload.toString
}

object Message {
  val messageTypeOffset = 8
}

sealed trait MessageType {
  def apply(payload: ByteString): Message

  def size: Int
}

/*
 * Section 4.2
 */
class SymbolClear(val payload: ByteString) extends Message {
  def stockSymbol: ByteString = payload.slice(9, 6)
}

object SymbolClear extends MessageType {
  def apply(payload: ByteString) = new SymbolClear(payload)

  val size = 15
}

/*
 * Section 4.3
 */
class AddOrderShort(val payload: ByteString) extends Message {
  def orderId:       ByteString = payload.slice(9, 12)
  def sideIndicator: Byte       = payload.byteAt(21)
  def shares:        Long       = payload.slice(22, 6).toLong
  def stockSymbol:   ByteString = payload.slice(28, 6)
  def price:         Long       = payload.slice(34, 10).toLong
  def display:       Boolean    = payload.byteAt(44) == 'Y'
}

object AddOrderShort extends MessageType {
  def apply(payload: ByteString) = new AddOrderShort(payload)

  val size = 45
}

/*
 * Section 4.3
 */
class AddOrderLong(val payload: ByteString) extends Message {
  def orderId:       ByteString = payload.slice(9, 12)
  def sideIndicator: Byte       = payload.byteAt(21)
  def shares:        Long       = payload.slice(22, 6).toLong
  def stockSymbol:   ByteString = payload.slice(28, 8)
  def price:         Long       = payload.slice(36, 10).toLong
  def display:       Boolean    = payload.byteAt(46) == 'Y'
  def participantId: ByteString = payload.slice(47, 4)
}

object AddOrderLong extends MessageType {
  def apply(payload: ByteString) = new AddOrderLong(payload)

  val size = 51
}

/*
 * Section 4.4.1
 */
class OrderExecuted(val payload: ByteString) extends Message {
  def orderId:        ByteString = payload.slice(9, 12)
  def executedShares: Long       = payload.slice(21, 6).toLong
  def executionId:    ByteString = payload.slice(27, 12)
}

object OrderExecuted extends MessageType {
  def apply(payload: ByteString) = new OrderExecuted(payload)

  val size = 39
}

/*
 * Section 4.4.2
 */
class OrderCancel(val payload: ByteString) extends Message {
  def orderId:        ByteString = payload.slice(9, 12)
  def canceledShares: Long       = payload.slice(21, 6).toLong
}

object OrderCancel extends MessageType {
  def apply(payload: ByteString) = new OrderCancel(payload)

  val size = 27
}

/*
 * Section 4.5
 */
class TradeShort(val payload: ByteString) extends Message {
  def orderId:       ByteString = payload.slice(9, 12)
  def sideIndicator: Byte       = payload.byteAt(21)
  def shares:        Long       = payload.slice(22, 6).toLong
  def stockSymbol:   ByteString = payload.slice(28, 6)
  def price:         Long       = payload.slice(34, 10).toLong
  def executionId:   ByteString = payload.slice(44, 12)
}

object TradeShort extends MessageType {
  def apply(payload: ByteString) = new TradeShort(payload)

  val size = 56
}

/*
 * Section 4.5
 */
class TradeLong(val payload: ByteString) extends Message {
  def orderId:       ByteString = payload.slice(9, 12)
  def sideIndicator: Byte       = payload.byteAt(21)
  def shares:        Long       = payload.slice(22, 6).toLong
  def stockSymbol:   ByteString = payload.slice(28, 8)
  def price:         Long       = payload.slice(36, 10).toLong
  def executionId:   ByteString = payload.slice(46, 12)
}

object TradeLong extends MessageType {
  def apply(payload: ByteString) = new TradeLong(payload)

  val size = 58
}

/*
 * Section 4.6
 */
class TradeBreak(val payload: ByteString) extends Message {
  def executionId: ByteString = payload.slice(9, 12)
}

object TradeBreak extends MessageType {
  def apply(payload: ByteString) = new TradeBreak(payload)

  val size = 21
}

/*
 * Section 4.7
 */
class TradingStatus(val payload: ByteString) extends Message {
  def stockSymbol:  ByteString = payload.slice(9, 8)
  def haltStatus:   Byte       = payload.byteAt(17)
  def regShoAction: Byte       = payload.byteAt(18)
  def reserved1:    Byte       = payload.byteAt(19)
  def reserved2:    Byte       = payload.byteAt(20)
}

object TradingStatus extends MessageType {
  def apply(payload: ByteString) = new TradingStatus(payload)

  val size = 21
}

/*
 * Section 4.8
 */
class AuctionUpdate(val payload: ByteString) extends Message {
  def stockSymbol:      ByteString = payload.slice(9, 8)
  def auctionType:      Byte       = payload.byteAt(17)
  def referencePrice:   Long       = payload.slice(18, 10).toLong
  def buyShares:        Long       = payload.slice(28, 10).toLong
  def sellShares:       Long       = payload.slice(38, 10).toLong
  def indicativePrice:  Long       = payload.slice(48, 10).toLong
  def auctionOnlyPrice: Long       = payload.slice(58, 10).toLong
}

object AuctionUpdate extends MessageType {
  def apply(payload: ByteString) = new AuctionUpdate(payload)

  val size = 68
}

/*
 * Section 4.9
 */
class AuctionSummary(val payload: ByteString) extends Message {
  def stockSymbol: ByteString = payload.slice(9, 8)
  def auctionType: Byte       = payload.byteAt(17)
  def price:       Long       = payload.slice(18, 10).toLong
  def shares:      Long       = payload.slice(28, 10).toLong
}

object AuctionSummary extends MessageType {
  def apply(payload: ByteString) = new AuctionSummary(payload)

  val size = 38
}

/*
 * Section 4.10
 */
class RetailPriceImprovement(val payload: ByteString) extends Message {
  def stockSymbol:            ByteString = payload.slice(9, 8)
  def retailPriceImprovement: Byte       = payload.byteAt(17)
}

object RetailPriceImprovement extends MessageType {
  def apply(payload: ByteString) = new RetailPriceImprovement(payload)

  val size = 18
}
