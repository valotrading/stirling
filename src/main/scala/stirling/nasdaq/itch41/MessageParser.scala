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

    val msgType = messageType(buffer.get(buffer.position))

    if (buffer.limit < buffer.position + msgType.size)
      throw new PartialMessageException

    val msg = msgType.parse(buffer.slice)

    buffer.position(buffer.position + msgType.size)

    msg
  }

  protected def messageType(messageType: Byte) = (messageType: @switch) match {
    case 'T' => Seconds
    case 'S' => SystemEvent
    case 'R' => StockDirectory
    case 'H' => StockTradingAction
    case 'Y' => RegSHORestriction
    case 'L' => MarketParticipantPosition
    case 'A' => AddOrder
    case 'F' => AddOrderMPID
    case 'E' => OrderExecuted
    case 'C' => OrderExecutedWithPrice
    case 'X' => OrderCancel
    case 'D' => OrderDelete
    case 'U' => OrderReplace
    case 'P' => Trade
    case 'Q' => CrossTrade
    case 'B' => BrokenTrade
    case 'I' => NOII
    case  x  => throw new GarbledMessageException("Unknown message type: " + x)
  }
}
