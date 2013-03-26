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
package stirling.bats.pitch1122

import java.nio.{BufferUnderflowException, ByteBuffer}
import scala.annotation.switch
import silvertip.{GarbledMessageException, PartialMessageException}
import stirling.io.ByteBuffers

trait MessageParser[Message] extends silvertip.MessageParser[Message] {
  def parse(buffer: ByteBuffer) = try {
    if (buffer.position + Commons.messageTypeOffset >= buffer.limit)
      throw new PartialMessageException

    val msgType = messageType(buffer.get(buffer.position + Commons.messageTypeOffset))

    msgType.apply(ByteBuffers.slice(buffer, buffer.position, msgType.size))
  } catch {
    case _: BufferUnderflowException => throw new PartialMessageException
  }

  protected def messageType(messageType: Byte): MessageType[Message]
}

object MessageParser extends MessageParser[Message] {
  override protected def messageType(messageType: Byte) = (messageType: @switch) match {
    case 's' => SymbolClear
    case 'A' => AddOrderShort
    case 'd' => AddOrderLong
    case 'E' => OrderExecuted
    case 'X' => OrderCancel
    case 'P' => TradeShort
    case 'r' => TradeLong
    case 'B' => TradeBreak
    case 'H' => TradingStatus
    case 'I' => AuctionUpdate
    case 'J' => AuctionSummary
    case 'R' => RetailPriceImprovement
    case  x  => throw new GarbledMessageException("Unknown message type: " + x)
  }
}
