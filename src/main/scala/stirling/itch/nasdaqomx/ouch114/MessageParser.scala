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
package stirling.itch.nasdaqomx.ouch114

import java.nio.{BufferUnderflowException, ByteBuffer, ByteOrder}
import scala.annotation.switch
import silvertip.{GarbledMessageException, PartialMessageException}
import stirling.itch.ByteBuffers

class MessageParser extends silvertip.MessageParser[Message] {
  def parse(buffer: ByteBuffer) = {
    try {
      parseMessage(buffer)
    } catch {
      case _: BufferUnderflowException => throw new PartialMessageException
    }
  }

  protected def parseMessage(buffer: ByteBuffer) = {
    if (buffer.position + Message.messageTypeOffset >= buffer.limit)
      throw new PartialMessageException

    val msgTypeValue = buffer.get(buffer.position + Message.messageTypeOffset)
    val msgType      = messageType(msgTypeValue)
    val msg          = msgType.apply(ByteBuffers.slice(buffer, buffer.position, msgType.size(msgTypeValue)))

    msg
  }

  protected def messageType(messageType: Byte) = (messageType: @switch) match {
    case 'S' => SystemEvent
    case 'A' => OrderAccepted
    case 'R' => OrderAccepted
    case 'C' => CanceledOrder
    case 'E' => ExecutedOrder
    case 'B' => BrokenTrade
    case 'J' => RejectedOrder
    case 'P' => CancelPending
    case 'W' => MMORefreshRequest
    case  x  => throw new GarbledMessageException("Unknown message type: " + x)
  }
}
