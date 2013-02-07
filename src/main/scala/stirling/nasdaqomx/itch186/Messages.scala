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
import stirling.io.{ByteString, TextFormat}

sealed trait Message {
  def payload: ByteString

  def messageType = payload.byteAt(0)

  override def toString = payload.toString
}

sealed trait MessageType {
  def apply(payload: ByteString): Message

  def size: Int

  protected def alpha(value: String, fieldSize: Int) = {
    TextFormat.alphaPadLeft(value, fieldSize, ' '.toByte)
  }

  protected def numeric(value: Long, fieldSize: Int) = {
    TextFormat.numericPadLeft(value, fieldSize, '0'.toByte)
  }
}

/*
 * Section 4.1.1
 */
class Seconds(val payload: ByteString) extends Message {
  def second:      Long = payload.slice(1, 5).toLong
}

object Seconds extends MessageType {
  def apply(payload: ByteString) = new Seconds(payload)

  val size = 6

  def format(
    buffer: ByteBuffer,
    second: Long
  ) {
    buffer.put('T'.toByte)
    buffer.put(numeric(second, 5))
  }
}

/*
 * Section 4.1.2
 */
class Milliseconds(val payload: ByteString) extends Message {
  def millisecond: Long = payload.slice(1, 3).toLong
}

object Milliseconds extends MessageType {
  def apply(payload: ByteString) = new Milliseconds(payload)

  val size = 4

  def format(
    buffer:      ByteBuffer,
    millisecond: Long
  ) {
    buffer.put('M'.toByte)
    buffer.put(numeric(millisecond, 3))
  }
}

/*
 * Section 4.2.1
 */
class SystemEvent(val payload: ByteString) extends Message {
  def eventCode:   Byte = payload.byteAt(1)
}

object SystemEvent extends MessageType {
  def apply(payload: ByteString) = new SystemEvent(payload)

  val size = 2

  def format(
    buffer:    ByteBuffer,
    eventCode: Byte
  ) {
    buffer.put('S'.toByte)
    buffer.put(eventCode)
  }
}

/*
 * Section 4.2.2
 */
class MarketSegmentState(val payload: ByteString) extends Message {
  def marketSegmentId: Long = payload.slice(1, 3).toLong
  def eventCode:       Byte = payload.byteAt(4)
}

object MarketSegmentState extends MessageType {
  def apply(payload: ByteString) = new MarketSegmentState(payload)

  val size = 5

  def format(
    buffer:          ByteBuffer,
    marketSegmentId: Long,
    eventCode:       Byte
  ) {
    buffer.put('O'.toByte)
    buffer.put(numeric(marketSegmentId, 3))
    buffer.put(eventCode)
  }
}

/*
 * Section 4.3.1
 */
class OrderBookDirectory(val payload: ByteString) extends Message {
  def orderBook:        Long       = payload.slice( 1,  6).toLong
  def symbol:           ByteString = payload.slice( 7, 16)
  def isin:             ByteString = payload.slice(23, 12)
  def financialProduct: Long       = payload.slice(35,  3).toLong
  def tradingCurrency:  ByteString = payload.slice(38,  3)
  def mic:              ByteString = payload.slice(41,  4)
  def marketSegmentId:  Long       = payload.slice(45,  3).toLong
  def noteCodes:        Long       = payload.slice(48,  8).toLong
  def roundLotSize:     Long       = payload.slice(56,  9).toLong
}

object OrderBookDirectory extends MessageType {
  def apply(payload: ByteString) = new OrderBookDirectory(payload)

  val size = 65

  def format(
    buffer:           ByteBuffer,
    orderBook:        Long,
    symbol:           String,
    isin:             String,
    financialProduct: Long,
    tradingCurrency:  String,
    mic:              String,
    marketSegmentId:  Long,
    noteCodes:        Long,
    roundLotSize:     Long
  ) {
    buffer.put('R'.toByte)
    buffer.put(numeric(orderBook, 6))
    buffer.put(alpha(symbol, 16))
    buffer.put(alpha(isin, 12))
    buffer.put(numeric(financialProduct, 3))
    buffer.put(alpha(tradingCurrency, 3))
    buffer.put(alpha(mic, 4))
    buffer.put(numeric(marketSegmentId, 3))
    buffer.put(numeric(noteCodes, 8))
    buffer.put(numeric(roundLotSize, 9))
  }
}

/*
 * Section 4.3.2
 */
class OrderBookTradingAction(val payload: ByteString) extends Message {
  def orderBook:    Long       = payload.slice(1, 6).toLong
  def tradingState: Byte       = payload.byteAt(7)
  def reserved:     Byte       = payload.byteAt(8)
  def reason:       ByteString = payload.slice(9, 4)
}

object OrderBookTradingAction extends MessageType {
  def apply(payload: ByteString) = new OrderBookTradingAction(payload)

  val size = 13

  def format(
    buffer:       ByteBuffer,
    orderBook:    Long,
    tradingState: Byte,
    reserved:     Byte,
    reason:       String
  ) {
    buffer.put('H'.toByte)
    buffer.put(numeric(orderBook, 6))
    buffer.put(tradingState)
    buffer.put(reserved)
    buffer.put(alpha(reason, 4))
  }
}

/*
 * Section 4.4.1
 */
class AddOrder(val payload: ByteString) extends Message {
  def orderReferenceNumber: Long = payload.slice( 1,  9).toLong
  def buySellIndicator:     Byte = payload.byteAt(10)
  def quantity:             Long = payload.slice(11,  9).toLong
  def orderBook:            Long = payload.slice(20,  6).toLong
  def price:                Long = payload.slice(26, 10).toLong
}

object AddOrder extends MessageType {
  def apply(payload: ByteString) = new AddOrder(payload)

  val size = 36

  def format(
    buffer:               ByteBuffer,
    orderReferenceNumber: Long,
    buySellIndicator:     Byte,
    quantity:             Long,
    orderBook:            Long,
    price:                Long
  ) {
    buffer.put('A'.toByte)
    buffer.put(numeric(orderReferenceNumber, 9))
    buffer.put(buySellIndicator)
    buffer.put(numeric(quantity, 9))
    buffer.put(numeric(orderBook, 6))
    buffer.put(numeric(price, 10))
  }
}

/*
 * Section 4.4.2
 */
class AddOrderMPID(val payload: ByteString) extends Message {
  def orderReferenceNumber: Long       = payload.slice( 1,  9).toLong
  def buySellIndicator:     Byte       = payload.byteAt(10)
  def quantity:             Long       = payload.slice(11,  9).toLong
  def orderBook:            Long       = payload.slice(20,  6).toLong
  def price:                Long       = payload.slice(26, 10).toLong
  def attribution:          ByteString = payload.slice(36, 4)
}

object AddOrderMPID extends MessageType {
  def apply(payload: ByteString) = new AddOrderMPID(payload)

  val size = 40

  def format(
    buffer:               ByteBuffer,
    orderReferenceNumber: Long,
    buySellIndicator:     Byte,
    quantity:             Long,
    orderBook:            Long,
    price:                Long,
    attribution:          String
  ) {
    buffer.put('F'.toByte)
    buffer.put(numeric(orderReferenceNumber, 9))
    buffer.put(buySellIndicator)
    buffer.put(numeric(quantity, 9))
    buffer.put(numeric(orderBook, 6))
    buffer.put(numeric(price, 10))
    buffer.put(alpha(attribution, 4))
  }
}

/*
 * Section 4.5.1
 */
class OrderExecuted(val payload: ByteString) extends Message {
  def orderReferenceNumber: Long       = payload.slice( 1, 9).toLong
  def executedQuantity:     Long       = payload.slice(10, 9).toLong
  def matchNumber:          Long       = payload.slice(19, 9).toLong
  def owner:                ByteString = payload.slice(28, 4)
  def counterparty:         ByteString = payload.slice(32, 4)
}

object OrderExecuted extends MessageType {
  def apply(payload: ByteString) = new OrderExecuted(payload)

  val size = 36

  def format(
    buffer:               ByteBuffer,
    orderReferenceNumber: Long,
    executedQuantity:     Long,
    matchNumber:          Long,
    owner:                String,
    counterparty:         String
  ) {
    buffer.put('E'.toByte)
    buffer.put(numeric(orderReferenceNumber, 9))
    buffer.put(numeric(executedQuantity, 9))
    buffer.put(numeric(matchNumber, 9))
    buffer.put(alpha(owner, 4))
    buffer.put(alpha(counterparty, 4))
  }
}

/*
 * Section 4.5.2
 */
class OrderExecutedWithPrice(val payload: ByteString) extends Message {
  def orderReferenceNumber: Long       = payload.slice( 1,  9).toLong
  def executedQuantity:     Long       = payload.slice(10,  9).toLong
  def matchNumber:          Long       = payload.slice(19,  9).toLong
  def printable:            Boolean    = payload.byteAt(28) == 'Y'
  def tradePrice:           Long       = payload.slice(29, 10).toLong
  def owner:                ByteString = payload.slice(39,  4)
  def counterparty:         ByteString = payload.slice(43,  4)
}

object OrderExecutedWithPrice extends MessageType {
  def apply(payload: ByteString) = new OrderExecutedWithPrice(payload)

  val size = 47

  def format(
    buffer:               ByteBuffer,
    orderReferenceNumber: Long,
    executedQuantity:     Long,
    matchNumber:          Long,
    printable:            Boolean,
    tradePrice:           Long,
    owner:                String,
    counterparty:         String
  ) {
    buffer.put('C'.toByte)
    buffer.put(numeric(orderReferenceNumber, 9))
    buffer.put(numeric(executedQuantity, 9))
    buffer.put(numeric(matchNumber, 9))
    buffer.put((if (printable) 'Y' else 'N').toByte)
    buffer.put(numeric(tradePrice, 10))
    buffer.put(alpha(owner, 4))
    buffer.put(alpha(counterparty, 4))
  }
}

/*
 * Section 4.5.3
 */
class OrderCancel(val payload: ByteString) extends Message {
  def orderReferenceNumber: Long = payload.slice( 1, 9).toLong
  def canceledQuantity:     Long = payload.slice(10, 9).toLong
}

object OrderCancel extends MessageType {
  def apply(payload: ByteString) = new OrderCancel(payload)

  val size = 19

  def format(
    buffer:               ByteBuffer,
    orderReferenceNumber: Long,
    canceledQuantity:     Long
  ) {
    buffer.put('X'.toByte)
    buffer.put(numeric(orderReferenceNumber, 9))
    buffer.put(numeric(canceledQuantity, 9))
  }
}

/*
 * Section 4.5.4
 */
class OrderDelete(val payload: ByteString) extends Message {
  def orderReferenceNumber: Long = payload.slice(1, 9).toLong
}

object OrderDelete extends MessageType {
  def apply(payload: ByteString) = new OrderDelete(payload)

  val size = 10

  def format(
    buffer:               ByteBuffer,
    orderReferenceNumber: Long
  ) {
    buffer.put('D'.toByte)
    buffer.put(numeric(orderReferenceNumber, 9))
  }
}

/*
 * Section 4.6.1
 */
class Trade(val payload: ByteString) extends Message {
  def orderReferenceNumber: Long       = payload.slice( 1,  9).toLong
  def tradeType:            Byte       = payload.byteAt(10)
  def quantity:             Long       = payload.slice(11,  9).toLong
  def orderBook:            Long       = payload.slice(20,  6).toLong
  def matchNumber:          Long       = payload.slice(26,  9).toLong
  def tradePrice:           Long       = payload.slice(35, 10).toLong
  def buyer:                ByteString = payload.slice(45,  4)
  def seller:               ByteString = payload.slice(49,  4)
}

object Trade extends MessageType {
  def apply(payload: ByteString) = new Trade(payload)

  val size = 53

  def format(
    buffer:               ByteBuffer,
    orderReferenceNumber: Long,
    tradeType:            Byte,
    quantity:             Long,
    orderBook:            Long,
    matchNumber:          Long,
    tradePrice:           Long,
    buyer:                String,
    seller:               String
  ) {
    buffer.put('P'.toByte)
    buffer.put(numeric(orderReferenceNumber, 9))
    buffer.put(tradeType)
    buffer.put(numeric(quantity, 9))
    buffer.put(numeric(orderBook, 6))
    buffer.put(numeric(matchNumber, 9))
    buffer.put(numeric(tradePrice, 10))
    buffer.put(alpha(buyer, 4))
    buffer.put(alpha(seller, 4))
  }
}

/*
 * Section 4.6.2
 */
class CrossTrade(val payload: ByteString) extends Message {
  def quantity:       Long = payload.slice( 1,  9).toLong
  def orderBook:      Long = payload.slice(10,  6).toLong
  def crossPrice:     Long = payload.slice(16, 10).toLong
  def matchNumber:    Long = payload.slice(26,  9).toLong
  def crossType:      Byte = payload.byteAt(35)
  def numberOfTrades: Long = payload.slice(36, 10).toLong
}

object CrossTrade extends MessageType {
  def apply(payload: ByteString) = new CrossTrade(payload)

  val size = 46

  def format(
    buffer:         ByteBuffer,
    quantity:       Long,
    orderBook:      Long,
    crossPrice:     Long,
    matchNumber:    Long,
    crossType:      Byte,
    numberOfTrades: Long
  ) {
    buffer.put('Q'.toByte)
    buffer.put(numeric(quantity, 9))
    buffer.put(numeric(orderBook, 6))
    buffer.put(numeric(crossPrice, 10))
    buffer.put(numeric(matchNumber, 9))
    buffer.put(crossType)
    buffer.put(numeric(numberOfTrades, 10))
  }
}

/*
 * Section 4.7
 */
class BrokenTrade(val payload: ByteString) extends Message {
  def matchNumber: Long = payload.slice(1, 9).toLong
}

object BrokenTrade extends MessageType {
  def apply(payload: ByteString) = new BrokenTrade(payload)

  val size = 10

  def format(
    buffer:         ByteBuffer,
    matchNumber:    Long
  ) {
    buffer.put('B'.toByte)
    buffer.put(numeric(matchNumber, 9))
  }
}

/*
 * Section 4.8
 */
class NOII(val payload: ByteString) extends Message {
  def pairedQuantity:     Long = payload.slice( 1,  9).toLong
  def imbalanceQuantity:  Long = payload.slice(10,  9).toLong
  def imbalanceDirection: Byte = payload.byteAt(19)
  def orderBook:          Long = payload.slice(20,  6).toLong
  def equilibriumPrice:   Long = payload.slice(26, 10).toLong
  def crossType:          Byte = payload.byteAt(36)
  def bestBidPrice:       Long = payload.slice(37, 10).toLong
  def bestBidQuantity:    Long = payload.slice(47,  9).toLong
  def bestAskPrice:       Long = payload.slice(56, 10).toLong
  def bestAskQuantity:    Long = payload.slice(66,  9).toLong
}

object NOII extends MessageType {
  def apply(payload: ByteString) = new NOII(payload)

  val size = 75

  def format(
    buffer:             ByteBuffer,
    pairedQuantity:     Long,
    imbalanceQuantity:  Long,
    imbalanceDirection: Byte,
    orderBook:          Long,
    equilibriumPrice:   Long,
    crossType:          Byte,
    bestBidPrice:       Long,
    bestBidQuantity:    Long,
    bestAskPrice:       Long,
    bestAskQuantity:    Long
  ) {
    buffer.put('I'.toByte)
    buffer.put(numeric(pairedQuantity, 9))
    buffer.put(numeric(imbalanceQuantity, 9))
    buffer.put(imbalanceDirection)
    buffer.put(numeric(orderBook, 6))
    buffer.put(numeric(equilibriumPrice, 10))
    buffer.put(crossType)
    buffer.put(numeric(bestBidPrice, 10))
    buffer.put(numeric(bestBidQuantity, 9))
    buffer.put(numeric(bestAskPrice, 10))
    buffer.put(numeric(bestAskQuantity, 9))
  }
}
