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
package stirling.oslo.itch20

import java.nio.ByteBuffer
import stirling.io.{ByteBuffers, ByteString}

sealed trait Message

sealed trait MessageType {
  def parse(buffer: ByteBuffer): Message
}

/*
 * Section 7.7.1
 */

case class Time(
  seconds: Int
) extends Message

object Time extends MessageType {
  def parse(buffer: ByteBuffer) = Time(
    seconds = buffer.getInt(4)
  )
}

/*
 * Section 7.7.2
 */

case class SystemEvent(
  nanosecond: Int,
  eventCode:  Byte
) extends Message

object SystemEvent extends MessageType {
  def parse(buffer: ByteBuffer) = SystemEvent(
    nanosecond = buffer.getInt(4),
    eventCode  = buffer.get()
  )

  object EventCode {
    val EndOfDay   = 'C'.toByte
    val StartOfDay = 'O'.toByte
  }
}

/*
 * Section 7.7.3
 */

case class SymbolDirectory(
  nanosecond:                      Int,
  instrumentID:                    Int,
  reserved1:                       ByteString,
  symbolStatus:                    Byte,
  isin:                            ByteString,
  priceBandTolerance:              Int,
  dynamicCircuitBreakerTolerances: Int,
  staticCircuitBreakerTolerances:  Int,
  segment:                         ByteString,
  reserved2:                       ByteString,
  currency:                        ByteString,
  reserved3:                       ByteString
) extends Message

object SymbolDirectory extends MessageType {
  def parse(buffer: ByteBuffer) = SymbolDirectory(
    nanosecond                      = buffer.getInt(4),
    instrumentID                    = buffer.getInt(4),
    reserved1                       = ByteBuffers.slice(buffer, 8, 2),
    symbolStatus                    = buffer.get(),
    isin                            = ByteBuffers.slice(buffer, 11, 12),
    priceBandTolerance              = buffer.getInt(4),
    dynamicCircuitBreakerTolerances = buffer.getInt(4),
    staticCircuitBreakerTolerances  = buffer.getInt(4),
    segment                         = ByteBuffers.slice(buffer, 35, 6),
    reserved2                       = ByteBuffers.slice(buffer, 41, 6),
    currency                        = ByteBuffers.slice(buffer, 47, 3),
    reserved3                       = ByteBuffers.slice(buffer, 50, 40)
  )

  object SymbolStatus {
    val Halted    = 'H'.toByte
    val Suspended = 'S'.toByte
    val Inactive  = 'a'.toByte
  }
}

/*
 * Section 7.7.4
 */

case class SymbolStatus(
  nanosecond:          Int,
  instrumentID:        Int,
  reserved1:           ByteString,
  tradingStatus:       Byte,
  reserved2:           ByteString,
  haltReason:          ByteString,
  sessionChangeReason: Byte,
  newEndTime:          Long,
  subBook:             Byte
) extends Message

object SymbolStatus extends MessageType {
  def parse(buffer: ByteBuffer) = SymbolStatus(
    nanosecond          = buffer.getInt(4),
    instrumentID        = buffer.getInt(4),
    reserved1           = ByteBuffers.slice(buffer, 8, 2),
    tradingStatus       = buffer.get(),
    reserved2           = ByteBuffers.slice(buffer, 11, 1),
    haltReason          = ByteBuffers.slice(buffer, 12, 4),
    sessionChangeReason = buffer.get(),
    newEndTime          = buffer.getLong(8),
    subBook             = buffer.get()
  )

  object TradingStatus {
    val Halt                    = 'H'.toByte
    val Regular                 = 'T'.toByte
    val OpeningAuctionCall      = 'a'.toByte
    val PostClose               = 'b'.toByte
    val MarketClosed            = 'c'.toByte
    val ClosingAuctionCall      = 'd'.toByte
    val ReOpeningAuctionCall    = 'e'.toByte
    val NoActiveSession         = 'w'.toByte
    val EndOfPostClose          = 'x'.toByte
    val PreTrading              = 'y'.toByte
    val ClosingPricePublication = 'z'.toByte
  }

  object HaltReason {
    val PriceMovement                        = "1   "
    val ReceivedAnnouncement                 = "2   "
    val InAnticipationOfAnnouncement         = "3   "
    val SystemProblems                       = "4   "
    val Other                                = "5   "
    val ReferenceDataUpdate                  = "6   "
    val InstrumentLevelCircuitBreakerTripped = "101 "
    val MatchingPartitionSuspended           = "9998"
    val SystemSuspended                      = "9999"
    val ReasonNotAvailable                   = "    "
  }

  object SessionChangeReason {
    val ScheduledTransition           = '0'.toByte
    val ExtendedByMarketSurveillance  = '1'.toByte
    val ShortenedByMarketSurveillance = '2'.toByte
    val MarketOrderImbalance          = '3'.toByte
    val PriceOutsideRange             = '4'.toByte
    val CircuitBreakerTripped         = '5'.toByte
    val Unavailable                   = '9'.toByte
  }

  object SubBook {
    val Regular = '1'.toByte
    val OffBook = '2'.toByte
  }
}

/*
 * Section 7.7.5
 */

case class AddOrder(
  nanosecond:   Int,
  orderID:      Long,
  side:         Byte,
  quantity:     Long,
  instrumentID: Int,
  reserved1:    ByteString,
  price:        Long,
  flags:        Byte,
  reserved2:    ByteString
) extends Message

object AddOrder extends MessageType {
  def parse(buffer: ByteBuffer) = AddOrder(
    nanosecond   = buffer.getInt(4),
    orderID      = buffer.getLong(8),
    side         = buffer.get(),
    quantity     = buffer.getLong(8),
    instrumentID = buffer.getInt(4),
    reserved1    = ByteBuffers.slice(buffer, 25, 2),
    price        = buffer.getLong(8),
    flags        = buffer.get(),
    reserved2    = ByteBuffers.slice(buffer, 36, 8)
  )

  object Side {
    val BuyOrder  = 'B'.toByte
    val SellOrder = 'S'.toByte
  }

  object Flags {
    val MarketOrder = 1L << 4
  }
}

/*
 * Section 7.7.6
 */

case class OrderDeleted(
  nanosecond:   Int,
  orderID:      Long,
  reserved:     Byte,
  instrumentID: Int
) extends Message

object OrderDeleted extends MessageType {
  def parse(buffer: ByteBuffer) = OrderDeleted(
    nanosecond   = buffer.getInt(4),
    orderID      = buffer.getLong(8),
    reserved     = buffer.get(),
    instrumentID = buffer.getInt(4)
  )
}

/*
 * Section 7.7.7
 */

case class OrderModified(
  nanosecond:   Int,
  orderID:      Long,
  newQuantity:  Long,
  newPrice:     Long,
  flags:        Byte,
  instrumentID: Int,
  reserved:     ByteString
) extends Message

object OrderModified extends MessageType {
  def parse(buffer: ByteBuffer) = OrderModified(
    nanosecond   = buffer.getInt(4),
    orderID      = buffer.getLong(8),
    newQuantity  = buffer.getLong(8),
    newPrice     = buffer.getLong(8),
    flags        = buffer.get(),
    instrumentID = buffer.getInt(4),
    reserved     = ByteBuffers.slice(buffer, 33, 8)
  )

  object Flags {
    val PriorityFlag = 1L << 0
  }
}

/*
 * Section 7.7.8
 */

case class OrderBookClear(
  nanosecond:   Int,
  instrumentID: Int,
  reserved:     ByteString
) extends Message

object OrderBookClear extends MessageType {
  def parse(buffer: ByteBuffer) = OrderBookClear(
    nanosecond   = buffer.getInt(4),
    instrumentID = buffer.getInt(4),
    reserved     = ByteBuffers.slice(buffer, 8, 3)
  )
}

/*
 * Section 7.7.9
 */

case class OrderExecuted(
  nanosecond:       Int,
  orderID:          Long,
  executedQuantity: Long,
  tradeMatchID:     Long,
  instrumentID:     Int
) extends Message

object OrderExecuted extends MessageType {
  def parse(buffer: ByteBuffer) = OrderExecuted(
    nanosecond       = buffer.getInt(4),
    orderID          = buffer.getLong(8),
    executedQuantity = buffer.getLong(8),
    tradeMatchID     = buffer.getLong(8),
    instrumentID     = buffer.getInt(4)
  )
}

/*
 * Section 7.7.10
 */

case class OrderExecutedWithPriceAndSize(
  nanosecond:       Int,
  orderID:          Long,
  executedQuantity: Long,
  displayQuantity:  Long,
  tradeMatchID:     Long,
  printable:        Byte,
  price:            Long,
  instrumentID:     Int,
  reserved:         ByteString
) extends Message

object OrderExecutedWithPriceAndSize extends MessageType {
  def parse(buffer: ByteBuffer) = OrderExecutedWithPriceAndSize(
    nanosecond       = buffer.getInt(4),
    orderID          = buffer.getLong(8),
    executedQuantity = buffer.getLong(8),
    displayQuantity  = buffer.getLong(8),
    tradeMatchID     = buffer.getLong(8),
    printable        = buffer.get(),
    price            = buffer.getLong(8),
    instrumentID     = buffer.getInt(4),
    reserved         = ByteBuffers.slice(buffer, 49, 8)
  )

  object Printable {
    val NonPrintable = 'N'.toByte
    val Printable    = 'Y'.toByte
  }
}

/*
 * Section 7.7.11
 */

case class Trade(
  nanosecond:       Int,
  executedQuantity: Long,
  instrumentID:     Int,
  reserved1:        ByteString,
  price:            Long,
  tradeMatchID:     Long,
  reserved2:        ByteString
) extends Message

object Trade extends MessageType {
  def parse(buffer: ByteBuffer) = Trade(
    nanosecond       = buffer.getInt(4),
    executedQuantity = buffer.getLong(8),
    instrumentID     = buffer.getInt(4),
    reserved1        = ByteBuffers.slice(buffer, 16, 2),
    price            = buffer.getLong(8),
    tradeMatchID     = buffer.getLong(8),
    reserved2        = ByteBuffers.slice(buffer, 34, 30)
  )
}

/*
 * Section 7.7.12
 */

case class OffBookTrade(
  nanosecond:       Int,
  executedQuantity: Long,
  instrumentID:     Int,
  reserved1:        ByteString,
  price:            Long,
  tradeMatchID:     Long,
  tradeType:        ByteString,
  tradeTime:        ByteString,
  tradeDate:        ByteString,
  tradedCurrency:   ByteString,
  originalPrice:    Long,
  executionVenue:   ByteString,
  flags:            Byte,
  isin:             ByteString,
  reserved2:        ByteString
) extends Message

object OffBookTrade extends MessageType {
  def parse(buffer: ByteBuffer) = OffBookTrade(
    nanosecond       = buffer.getInt(4),
    executedQuantity = buffer.getLong(8),
    instrumentID     = buffer.getInt(4),
    reserved1        = ByteBuffers.slice(buffer, 16, 2),
    price            = buffer.getLong(8),
    tradeMatchID     = buffer.getLong(8),
    tradeType        = ByteBuffers.slice(buffer, 34, 4),
    tradeTime        = ByteBuffers.slice(buffer, 38, 8),
    tradeDate        = ByteBuffers.slice(buffer, 46, 8),
    tradedCurrency   = ByteBuffers.slice(buffer, 54, 4),
    originalPrice    = buffer.getLong(8),
    executionVenue   = ByteBuffers.slice(buffer, 66, 5),
    flags            = buffer.get(),
    isin             = ByteBuffers.slice(buffer, 72, 12),
    reserved2        = ByteBuffers.slice(buffer, 84, 13)
  )

  object Flags {
    val LateTrade                 = 1L << 0
    val BargainConditionIndicator = 1L << 5
  }
}

/*
 * Section 7.7.13
 */

case class TradeBreak(
  nanosecond:   Int,
  tradeMatchID: Long,
  tradeType:    Byte,
  instrumentID: Int,
  isin:         ByteString
) extends Message

object TradeBreak extends MessageType {
  def parse(buffer: ByteBuffer) = TradeBreak(
    nanosecond   = buffer.getInt(4),
    tradeMatchID = buffer.getLong(8),
    tradeType    = buffer.get(),
    instrumentID = buffer.getInt(4),
    isin         = ByteBuffers.slice(buffer, 17, 12)
  )

  object TradeType {
    val OnBookTrade  = 'T'.toByte
    val OffBookTrade = 'N'.toByte
  }
}

/*
 * Section 7.7.14
 */

case class AuctionInfo(
  nanosecond:     Int,
  pairedQuantity: Long,
  reserved1:      ByteString,
  instrumentID:   Int,
  reserved2:      ByteString,
  price:          Long,
  auctionType:    Byte
) extends Message

object AuctionInfo extends MessageType {
  def parse(buffer: ByteBuffer) = AuctionInfo(
    nanosecond     = buffer.getInt(4),
    pairedQuantity = buffer.getLong(8),
    reserved1      = ByteBuffers.slice(buffer, 12, 9),
    instrumentID   = buffer.getInt(4),
    reserved2      = ByteBuffers.slice(buffer, 25, 2),
    price          = buffer.getLong(8),
    auctionType    = buffer.get()
  )

  object AuctionType {
    val ReOpeningAuction = 'A'.toByte
    val ClosingAuction   = 'C'.toByte
    val OpeningAuction   = 'O'.toByte
  }
}

/*
 * Section 7.7.15
 */

case class Statistics(
  nanosecond:              Int,
  instrumentID:            Int,
  reserved1:               ByteString,
  statisticType:           Byte,
  price:                   Long,
  openClosePriceIndicator: Byte,
  reserved2:               ByteString
) extends Message

object Statistics extends MessageType {
  def parse(buffer: ByteBuffer) = Statistics(
    nanosecond              = buffer.getInt(4),
    instrumentID            = buffer.getInt(4),
    reserved1               = ByteBuffers.slice(buffer, 8, 2),
    statisticType           = buffer.get(),
    price                   = buffer.getLong(8),
    openClosePriceIndicator = buffer.get(),
    reserved2               = ByteBuffers.slice(buffer, 20, 1)
  )

  object StatisticsType {
    val OpeningPrice         = 'O'.toByte
    val ClosingPrice         = 'C'.toByte
    val PreviousClosingPrice = 'P'.toByte
  }

  object OpenClosePriceIndicator {
    val UT               = 'A'.toByte
    val AT               = 'B'.toByte
    val MidOfBBO         = 'C'.toByte
    val LastAT           = 'D'.toByte
    val LastUT           = 'E'.toByte
    val Manual           = 'F'.toByte
    val PreviousClose    = 'I'.toByte
    val BestBid          = 'U'.toByte
    val BestOffer        = 'V'.toByte
    val None             = 'W'.toByte
    val PriceUnavailable = 'Z'.toByte
  }
}

/*
 * Section 7.7.16
 */

case class EnhancedTrade(
  nanosecond:       Int,
  executedQuantity: Long,
  instrumentID:     Int,
  reserved1:        ByteString,
  price:            Long,
  reserved2:        ByteString,
  auctionType:      Byte,
  offBookTradeType: ByteString,
  tradeTime:        ByteString,
  tradeDate:        ByteString,
  actionType:       Byte,
  tradedCurrency:   ByteString,
  originalPrice:    Long,
  executionVenue:   ByteString,
  flags:            Byte,
  reserved3:        ByteString,
  buyerId:          ByteString,
  sellerId:         ByteString
) extends Message

object EnhancedTrade extends MessageType {
  def parse(buffer: ByteBuffer) = EnhancedTrade(
    nanosecond       = buffer.getInt(4),
    executedQuantity = buffer.getLong(8),
    instrumentID     = buffer.getInt(4),
    reserved1        = ByteBuffers.slice(buffer, 16, 2),
    price            = buffer.getLong(8),
    reserved2        = ByteBuffers.slice(buffer, 26, 8),
    auctionType      = buffer.get(),
    offBookTradeType = ByteBuffers.slice(buffer, 35, 4),
    tradeTime        = ByteBuffers.slice(buffer, 39, 8),
    tradeDate        = ByteBuffers.slice(buffer, 47, 8),
    actionType       = buffer.get(),
    tradedCurrency   = ByteBuffers.slice(buffer, 56, 4),
    originalPrice    = buffer.getLong(8),
    executionVenue   = ByteBuffers.slice(buffer, 68, 5),
    flags            = buffer.get(),
    reserved3        = ByteBuffers.slice(buffer, 74, 29),
    buyerId          = ByteBuffers.slice(buffer, 103, 11),
    sellerId         = ByteBuffers.slice(buffer, 114, 11)
  )

  object AuctionType {
    val ReOpeningAuction = 'A'.toByte
    val ClosingAuction   = 'C'.toByte
    val OpeningAuction   = 'O'.toByte
  }

  object ActionType {
    val CancelledTrade = 'C'.toByte
    val Trade          = 'N'.toByte
  }

  object Flags {
    val LateTrade                 = 1L << 0
    val BargainConditionIndicator = 1L << 5
  }
}

/*
 * Section 7.7.17
 */

case class RecoveryTrade(
  nanosecond:       Int,
  executedQuantity: Long,
  instrumentID:     Int,
  reserved1:        ByteString,
  price:            Long,
  tradeId:          Long,
  auctionType:      Byte,
  offBookTradeType: ByteString,
  tradeTime:        ByteString,
  tradeDate:        ByteString,
  actionType:       Byte,
  tradedCurrency:   ByteString,
  originalPrice:    Long,
  executionVenue:   ByteString,
  flags:            Byte,
  reserved2:        ByteString,
  buyerId:          ByteString,
  sellerId:         ByteString
) extends Message

object RecoveryTrade extends MessageType {
  def parse(buffer: ByteBuffer) = RecoveryTrade(
    nanosecond       = buffer.getInt(4),
    executedQuantity = buffer.getLong(8),
    instrumentID     = buffer.getInt(4),
    reserved1        = ByteBuffers.slice(buffer, 16, 2),
    price            = buffer.getLong(8),
    tradeId          = buffer.getLong(8),
    auctionType      = buffer.get(),
    offBookTradeType = ByteBuffers.slice(buffer, 35, 4),
    tradeTime        = ByteBuffers.slice(buffer, 39, 8),
    tradeDate        = ByteBuffers.slice(buffer, 47, 8),
    actionType       = buffer.get(),
    tradedCurrency   = ByteBuffers.slice(buffer, 56, 4),
    originalPrice    = buffer.getLong(8),
    executionVenue   = ByteBuffers.slice(buffer, 68, 5),
    flags            = buffer.get(),
    reserved2        = ByteBuffers.slice(buffer, 74, 29),
    buyerId          = ByteBuffers.slice(buffer, 103, 11),
    sellerId         = ByteBuffers.slice(buffer, 114, 11)
  )

  object AuctionType {
    val ReOpeningAuction = 'A'.toByte
    val ClosingAuction   = 'C'.toByte
    val OpeningAuction   = 'O'.toByte
    val NA               = '\0'.toByte
  }

  object ActionType {
    val CancelledTrade = 'C'.toByte
    val Trade          = 'N'.toByte
  }

  object Flags {
    val LateTrade                 = 1L << 0
    val BargainConditionIndicator = 1L << 5
  }
}
