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
package stirling.itch.bats.top120

import java.nio.{BufferUnderflowException, ByteBuffer, ByteOrder}
import scala.annotation.switch
import silvertip.{GarbledMessageException, PartialMessageException}
import stirling.io.ByteBuffers

class MessageParser extends silvertip.MessageParser[Message] {
  def parse(buffer: ByteBuffer) = {
    try {
      parseMessage(buffer)
    } catch {
      case _: BufferUnderflowException => throw new PartialMessageException
    }
  }

  protected def parseMessage(buffer: ByteBuffer) = {
    if (buffer.limit == buffer.position)
      throw new PartialMessageException

    val msgType = messageType(buffer.get(buffer.position))
    val msg     = msgType.apply(ByteBuffers.slice(buffer, buffer.position, msgType.size))

    msg
  }

  protected def messageType(messageType: Byte) = (messageType: @switch) match {
    case 'C' => LogonAccepted
    case 'J' => LogonRejected
    case 's' => ExpandedSpin
    case 'D' => SpinDone
    case 'H' => ServerHeartbeat
    case 'T' => Seconds
    case 'M' => Milliseconds
    case 'E' => ExpandedBidUpdate
    case 'B' => LongBidUpdate
    case 'b' => ShortBidUpdate
    case 'e' => ExpandedAskUpdate
    case 'A' => LongAskUpdate
    case 'a' => ShortAskUpdate
    case 'F' => ExpandedTwoSidedUpdate
    case 'U' => LongTwoSidedUpdate
    case 'u' => ShortTwoSidedUpdate
    case 'f' => ExpandedTrade
    case 'V' => LongTrade
    case 'v' => ShortTrade
    case 't' => TradingStatus
    case  x  => throw new GarbledMessageException("Unknown message type: " + x)
  }
}
