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
package stirling.itch.messages.itch41

import java.nio.{BufferUnderflowException, ByteBuffer, ByteOrder}
import silvertip.{PartialMessageException, GarbledMessageException}
import stirling.itch.types.Alpha

class MessageParser extends silvertip.MessageParser[Message] {
  import MessageParser._
  def parse(buffer: ByteBuffer) = {
    try {
      decode(buffer)
    } catch {
      case _: BufferUnderflowException => throw new PartialMessageException
    }
  }

  protected def decode(buffer: ByteBuffer) = {
    decodeMessage(buffer, decodeMessageType(buffer))
  }

  protected def decodeMessageType(buffer: ByteBuffer) = buffer.get.toChar

  protected def decodeMessage(buf: ByteBuffer, messageType: Char) = messageType match {
    case MessageType.Seconds                   => seconds(buf)
    case MessageType.SystemEvent               => systemEvent(buf)
    case MessageType.StockDirectory            => stockDirectory(buf)
    case MessageType.StockTradingAction        => stockTradingAction(buf)
    case MessageType.RegSHORestriction         => regSHOShortSalePriceTestRestrictedIndicator(buf)
    case MessageType.MarketParticipantPosition => marketParticipantPosition(buf)
    case MessageType.AddOrder                  => addOrder(buf)
    case MessageType.AddOrderMPID              => addOrderWithMPID(buf)
    case MessageType.OrderExecuted             => orderExecuted(buf)
    case MessageType.OrderExecutedWithPrice    => orderExecutedWithPrice(buf)
    case MessageType.OrderCancel               => orderCancel(buf)
    case MessageType.OrderDelete               => orderDelete(buf)
    case MessageType.OrderReplace              => orderReplace(buf)
    case MessageType.Trade                     => trade(buf)
    case MessageType.CrossTrade                => crossTrade(buf)
    case MessageType.BrokenTrade               => brokenTrade(buf)
    case MessageType.NOII                      => netOrderImbalanceIndicator(buf)
    case unknown                               => throw new GarbledMessageException("Unknown message type: " + unknown)
  }

  private def seconds(buf: ByteBuffer) = {
    Seconds(
      seconds = buf.getInt
    )
  }

  private def systemEvent(buf: ByteBuffer) = {
    SystemEvent(
      nanoSeconds       = buf.getInt,
      systemMessageType = buf.get
    )
  }

  private def stockDirectory(buf: ByteBuffer) = {
    StockDirectory(
      nanoSeconds              = buf.getInt,
      stock                    = readBytes8(buf),
      marketCategory           = buf.get,
      financialStatusIndicator = buf.get,
      roundLotSize             = buf.getInt,
      roundLotsOnly            = readChar(buf) == 'Y'
    )
  }

  private def stockTradingAction(buf: ByteBuffer) = {
    StockTradingAction(
      nanoSeconds  = buf.getInt,
      stock        = readBytes8(buf),
      tradingState = buf.get,
      reserved     = buf.get,
      reason       = readBytes4(buf)
    )
  }

  private def regSHOShortSalePriceTestRestrictedIndicator(buf: ByteBuffer) = {
    RegSHOShortSalePriceTestRestrictedIndicator(
      nanoSeconds = buf.getInt,
      stock       = readBytes8(buf),
      shoAction   = buf.get
    )
  }

  private def marketParticipantPosition(buf: ByteBuffer) = {
    MarketParticipantPosition(
      nanoSeconds = buf.getInt,
      mpid        = readBytes4(buf),
      stock       = readBytes8(buf),
      isPrimary   = readChar(buf) == 'Y',
      mode        = buf.get,
      status      = buf.get
    )
  }

  private def addOrder(buf: ByteBuffer) = {
    AddOrder(
      nanoSeconds      = buf.getInt,
      referenceNumber  = buf.getLong,
      buySellIndicator = buf.get,
      shares           = buf.getInt,
      stock            = readBytes8(buf),
      price            = buf.getInt
    )
  }

  private def addOrderWithMPID(buf: ByteBuffer) = {
    AddOrder(
      nanoSeconds      = buf.getInt,
      referenceNumber  = buf.getLong,
      buySellIndicator = buf.get,
      shares           = buf.getInt,
      stock            = readBytes8(buf),
      price            = buf.getInt,
      attribution      = Some(readBytes4(buf))
    )
  }

  private def orderExecuted(buf: ByteBuffer) = {
    OrderExecuted(
      nanoSeconds     = buf.getInt,
      referenceNumber = buf.getLong,
      executedShares  = buf.getInt,
      matchNumber     = buf.getLong
    )
  }

  private def orderExecutedWithPrice(buf: ByteBuffer) = {
    OrderExecuted(
      nanoSeconds     = buf.getInt,
      referenceNumber = buf.getLong,
      executedShares  = buf.getInt,
      matchNumber     = buf.getLong,
      printable       = Some(buf.get == 'Y'),
      price           = Some(buf.getInt)
    )
  }

  private def orderCancel(buf: ByteBuffer) = {
    OrderCancel(
      nanoSeconds     = buf.getInt,
      referenceNumber = buf.getLong,
      canceledShares  = buf.getInt
    )
  }

  private def orderDelete(buf: ByteBuffer) = {
    OrderDelete(
      nanoSeconds     = buf.getInt,
      referenceNumber = buf.getLong
    )
  }

  private def orderReplace(buf: ByteBuffer) = {
    OrderReplace(
      nanoSeconds             = buf.getInt,
      originalReferenceNumber = buf.getLong,
      newReferenceNumber      = buf.getLong,
      shares                  = buf.getInt,
      price                   = buf.getInt
    )
  }

  private def trade(buf: ByteBuffer) = {
    Trade(
      nanoSeconds          = buf.getInt,
      orderReferenceNumber = buf.getLong,
      buySellIndicator     = buf.get,
      shares               = buf.getInt,
      stock                = readBytes8(buf),
      price                = buf.getInt,
      matchNumber          = buf.getLong
    )
  }

  private def crossTrade(buf: ByteBuffer) = {
    CrossTrade(
      nanoSeconds = buf.getInt,
      shares      = buf.getLong,
      stock       = readBytes8(buf),
      crossPrice  = buf.getInt,
      matchNumber = buf.getLong,
      crossType   = buf.get
    )
  }

  private def brokenTrade(buf: ByteBuffer) = {
    BrokenTrade(
      nanoSeconds = buf.getInt,
      matchNumber = buf.getLong
    )
  }

  private def netOrderImbalanceIndicator(buf: ByteBuffer) = {
    NetOrderImbalanceIndicator(
      nanoSeconds            = buf.getInt,
      pairedShares           = buf.getLong,
      imbalance              = buf.getLong,
      imbalanceDirection     = buf.get,
      stock                  = readBytes8(buf),
      farPrice               = buf.getInt,
      nearPrice              = buf.getInt,
      currentReferencePrice  = buf.getInt,
      crossType              = buf.get,
      priceVarianceIndicator = buf.get
    )
  }
}

object MessageParser {
  def readBytes(buf: ByteBuffer, length: Int): ByteBuffer = {
    val origLimit = buf.limit
    buf.limit(buf.position + length)

    val targetBuffer = ByteBuffer.allocate(length)
    targetBuffer.order(ByteOrder.BIG_ENDIAN)
    targetBuffer.put(buf)
    targetBuffer.flip

    buf.limit(origLimit)
    targetBuffer
  }

  def readBytes4(buf: ByteBuffer): ByteBuffer = readBytes(buf, 4)
  def readBytes8(buf: ByteBuffer): ByteBuffer = readBytes(buf, 8)
  def readChar(buf: ByteBuffer): Char = buf.get.toChar
}
