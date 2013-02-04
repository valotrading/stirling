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
package stirling.nasdaq.itch41

import java.nio.ByteBuffer
import stirling.io.{ByteBuffers, ByteString}

sealed trait Message

sealed trait MessageType {
  def parse(buffer: ByteBuffer): Message

  def size: Int
}

/*
 * Section 4.1
 */
case class Seconds(
  seconds: Int
) extends Message

object Seconds extends MessageType {
  def parse(buffer: ByteBuffer) = Seconds(
    seconds = buffer.getInt(1)
  )

  val size = 5
}

/*
 * Section 4.2
 */
case class SystemEvent(
  nanoseconds: Int,
  eventCode:   Byte
) extends Message

object SystemEvent extends MessageType {
  def parse(buffer: ByteBuffer) = SystemEvent(
    nanoseconds = buffer.getInt(1),
    eventCode   = buffer.get(5)
  )

  val size = 6
}

/*
 * Section 4.3.1
 */
case class StockDirectory(
  nanoseconds:              Int,
  stock:                    ByteString,
  marketCategory:           Byte,
  financialStatusIndicator: Byte,
  roundLotSize:             Int,
  roundLotsOnly:            Boolean
) extends Message

object StockDirectory extends MessageType {
  def parse(buffer: ByteBuffer) = StockDirectory(
    nanoseconds              = buffer.getInt(1),
    stock                    = ByteBuffers.slice(buffer, 5, 8),
    marketCategory           = buffer.get(13),
    financialStatusIndicator = buffer.get(14),
    roundLotSize             = buffer.getInt(15),
    roundLotsOnly            = buffer.get(19).toChar == 'Y'
  )

  val size = 20
}

/*
 * Section 4.3.2
 */
case class StockTradingAction(
  nanoseconds:  Int,
  stock:        ByteString,
  tradingState: Byte,
  reserved:     Byte,
  reason:       ByteString
) extends Message

object StockTradingAction extends MessageType {
  def parse(buffer: ByteBuffer) = StockTradingAction(
    nanoseconds  = buffer.getInt(1),
    stock        = ByteBuffers.slice(buffer, 5, 8),
    tradingState = buffer.get(13),
    reserved     = buffer.get(14),
    reason       = ByteBuffers.slice(buffer, 15, 4)
  )

  val size = 19
}

/*
 * Section 4.3.3
 */
case class RegSHORestriction(
  nanoseconds:  Int,
  stock:        ByteString,
  regShoAction: Byte
) extends Message

object RegSHORestriction extends MessageType {
  def parse(buffer: ByteBuffer) = RegSHORestriction(
    nanoseconds  = buffer.getInt(1),
    stock        = ByteBuffers.slice(buffer, 5, 8),
    regShoAction = buffer.get(13)
  )

  val size = 14
}

/*
 * Section 4.3.4
 */
case class MarketParticipantPosition(
  nanoseconds:            Int,
  mpid:                   ByteString,
  stock:                  ByteString,
  primaryMarketMaker:     Boolean,
  marketMakerMode:        Byte,
  marketParticipantState: Byte
) extends Message

object MarketParticipantPosition extends MessageType {
  def parse(buffer: ByteBuffer) = MarketParticipantPosition(
    nanoseconds            = buffer.getInt(1),
    mpid                   = ByteBuffers.slice(buffer, 5, 4),
    stock                  = ByteBuffers.slice(buffer, 9, 8),
    primaryMarketMaker     = buffer.get(17).toChar == 'Y',
    marketMakerMode        = buffer.get(18),
    marketParticipantState = buffer.get(19)
  )

  val size = 20
}

/*
 * Section 4.4.1
 */
case class AddOrder(
  nanoseconds:          Int,
  orderReferenceNumber: Long,
  buySellIndicator:     Byte,
  shares:               Int,
  stock:                ByteString,
  price:                Int
) extends Message

object AddOrder extends MessageType {
  def parse(buffer: ByteBuffer) = AddOrder(
    nanoseconds          = buffer.getInt(1),
    orderReferenceNumber = buffer.getLong(5),
    buySellIndicator     = buffer.get(13),
    shares               = buffer.getInt(14),
    stock                = ByteBuffers.slice(buffer, 18, 8),
    price                = buffer.getInt(26)
  )

  val size = 30
}

/*
 * Section 4.4.2
 */
case class AddOrderMPID(
  nanoseconds:          Int,
  orderReferenceNumber: Long,
  buySellIndicator:     Byte,
  shares:               Int,
  stock:                ByteString,
  price:                Int,
  attribution:          ByteString
) extends Message

object AddOrderMPID extends MessageType {
  def parse(buffer: ByteBuffer) = AddOrderMPID(
    nanoseconds          = buffer.getInt(1),
    orderReferenceNumber = buffer.getLong(5),
    buySellIndicator     = buffer.get(13),
    shares               = buffer.getInt(14),
    stock                = ByteBuffers.slice(buffer, 18, 8),
    price                = buffer.getInt(26),
    attribution          = ByteBuffers.slice(buffer, 30, 4)
  )

  val size = 34
}

/*
 * Section 4.5.1
 */
case class OrderExecuted(
  nanoseconds:          Int,
  orderReferenceNumber: Long,
  executedShares:       Int,
  matchNumber:          Long
) extends Message

object OrderExecuted extends MessageType {
  def parse(buffer: ByteBuffer) = OrderExecuted(
    nanoseconds          = buffer.getInt(1),
    orderReferenceNumber = buffer.getLong(5),
    executedShares       = buffer.getInt(13),
    matchNumber          = buffer.getLong(17)
  )

  val size = 25
}

/*
 * Section 4.5.2
 */
case class OrderExecutedWithPrice(
  nanoseconds:          Int,
  orderReferenceNumber: Long,
  executedShares:       Int,
  matchNumber:          Long,
  printable:            Boolean,
  price:                Int
) extends Message

object OrderExecutedWithPrice extends MessageType {
  def parse(buffer: ByteBuffer) = OrderExecutedWithPrice(
    nanoseconds          = buffer.getInt(1),
    orderReferenceNumber = buffer.getLong(5),
    executedShares       = buffer.getInt(13),
    matchNumber          = buffer.getLong(17),
    printable            = buffer.get(25).toChar == 'Y',
    price                = buffer.getInt(26)
  )

  val size = 30
}


/*
 * Section 4.5.3
 */
case class OrderCancel(
  nanoseconds:          Int,
  orderReferenceNumber: Long,
  canceledShares:       Int
) extends Message

object OrderCancel extends MessageType {
  def parse(buffer: ByteBuffer) = OrderCancel(
    nanoseconds          = buffer.getInt(1),
    orderReferenceNumber = buffer.getLong(5),
    canceledShares       = buffer.getInt(13)
  )

  val size = 17
}

/*
 * Section 4.5.4
 */
case class OrderDelete(
  nanoseconds:          Int,
  orderReferenceNumber: Long
) extends Message

object OrderDelete extends MessageType {
  def parse(buffer: ByteBuffer) = OrderDelete(
    nanoseconds          = buffer.getInt(1),
    orderReferenceNumber = buffer.getLong(5)
  )

  val size = 13
}

/*
 * Section 4.5.5
 */
case class OrderReplace(
  nanoseconds:                  Int,
  originalOrderReferenceNumber: Long,
  newOrderReferenceNumber:      Long,
  shares:                       Int,
  price:                        Int
) extends Message

object OrderReplace extends MessageType {
  def parse(buffer: ByteBuffer) = OrderReplace(
    nanoseconds                  = buffer.getInt(1),
    originalOrderReferenceNumber = buffer.getLong(5),
    newOrderReferenceNumber      = buffer.getLong(13),
    shares                       = buffer.getInt(21),
    price                        = buffer.getInt(25)
  )

  val size = 29
}

/*
 * Section 4.6.1
 */
case class Trade(
  nanoseconds:          Int,
  orderReferenceNumber: Long,
  buySellIndicator:     Byte,
  shares:               Int,
  stock:                ByteString,
  price:                Int,
  matchNumber:          Long
) extends Message

object Trade extends MessageType {
  def parse(buffer: ByteBuffer) = Trade(
    nanoseconds          = buffer.getInt(1),
    orderReferenceNumber = buffer.getLong(5),
    buySellIndicator     = buffer.get(13),
    shares               = buffer.getInt(14),
    stock                = ByteBuffers.slice(buffer, 18, 8),
    price                = buffer.getInt(26),
    matchNumber          = buffer.getLong(30)
  )

  val size = 38
}

/*
 * Section 4.6.2
 */
case class CrossTrade(
  nanoseconds: Int,
  shares:      Long,
  stock:       ByteString,
  crossPrice:  Int,
  matchNumber: Long,
  crossType:   Byte
) extends Message

object CrossTrade extends MessageType {
  def parse(buffer: ByteBuffer) = CrossTrade(
    nanoseconds = buffer.getInt(1),
    shares      = buffer.getLong(5),
    stock       = ByteBuffers.slice(buffer, 13, 8),
    crossPrice  = buffer.getInt(21),
    matchNumber = buffer.getLong(25),
    crossType   = buffer.get(33)
  )

  val size = 34
}

/*
 * Section 4.6.3
 */
case class BrokenTrade(
  nanoseconds: Int,
  matchNumber: Long
) extends Message

object BrokenTrade extends MessageType {
  def parse(buffer: ByteBuffer) = BrokenTrade(
    nanoseconds = buffer.getInt(1),
    matchNumber = buffer.getLong(5)
  )

  val size = 13
}

/*
 * Section 4.7
 */
case class NOII(
  nanoseconds:            Int,
  pairedShares:           Long,
  imbalance:              Long,
  imbalanceDirection:     Byte,
  stock:                  ByteString,
  farPrice:               Int,
  nearPrice:              Int,
  currentReferencePrice:  Int,
  crossType:              Byte,
  priceVarianceIndicator: Byte
) extends Message

object NOII extends MessageType {
  def parse(buffer: ByteBuffer) = NOII(
    nanoseconds            = buffer.getInt(1),
    pairedShares           = buffer.getLong(5),
    imbalance              = buffer.getLong(13),
    imbalanceDirection     = buffer.get(21),
    stock                  = ByteBuffers.slice(buffer, 22, 8),
    farPrice               = buffer.getInt(30),
    nearPrice              = buffer.getInt(34),
    currentReferencePrice  = buffer.getInt(38),
    crossType              = buffer.get(42),
    priceVarianceIndicator = buffer.get(43)
  )

  val size = 44
}
