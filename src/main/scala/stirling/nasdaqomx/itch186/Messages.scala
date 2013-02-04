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

import stirling.io.ByteString

sealed trait Message {
  def payload: ByteString

  def messageType = payload.byteAt(0)

  override def toString = payload.toString
}

sealed trait MessageType {
  def apply(payload: ByteString): Message

  def size: Int
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
}
