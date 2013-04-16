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

import java.nio.{BufferUnderflowException, ByteBuffer, ByteOrder}
import scala.annotation.switch
import silvertip.{GarbledMessageException, PartialMessageException}

class MessageParser extends silvertip.MessageParser[Message] {
  def parse(buffer: ByteBuffer) = {
    try {
      parseMessage(buffer)
    } catch {
      case _: BufferUnderflowException => throw new PartialMessageException
    }
  }

  protected def parseMessage(buffer: ByteBuffer) = {
    if (buffer.position == buffer.limit)
      throw new PartialMessageException

    val length  = buffer.get(buffer.position)
    val msgType = messageType(buffer.get(buffer.position + 1))

    if (buffer.limit < buffer.position + length)
      throw new PartialMessageException

    val msg = msgType.parse(buffer.slice)

    buffer.position(buffer.position + length)

    msg
  }

  protected def messageType(messageType: Byte) = (messageType: @switch) match {
    case 0x54 => Time
    case 0x53 => SystemEvent
    case 0x52 => SymbolDirectory
    case 0x48 => SymbolStatus
    case 0x41 => AddOrder
    case 0x44 => OrderDeleted
    case 0x55 => OrderModified
    case 0x79 => OrderBookClear
    case 0x45 => OrderExecuted
    case 0x43 => OrderExecutedWithPriceAndSize
    case 0x50 => Trade
    case 0x78 => OffBookTrade
    case 0x42 => TradeBreak
    case 0x49 => AuctionInfo
    case 0x77 => Statistics
    case 0x90 => EnhancedTrade
    case 0x76 => RecoveryTrade
    case  x   => throw new GarbledMessageException("Unknown message type: " + x)
  }
}
