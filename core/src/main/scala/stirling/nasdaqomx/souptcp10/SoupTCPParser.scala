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
package stirling.nasdaqomx.souptcp10

import java.nio.{BufferUnderflowException, ByteBuffer}
import scala.annotation.{switch, tailrec}
import silvertip.{GarbledMessageException, MessageParser, PartialMessageException}

object SoupTCPParser extends MessageParser[Packet] {
  private val LF: Byte = 0x0A

  override def parse(buffer: ByteBuffer): Packet = {
    if (buffer.remaining < 1)
      throw new PartialMessageException

    packetType(buffer.get()).parse(slice(buffer))
  }


  @tailrec
  private def slice(buffer: ByteBuffer, offset: Int = 0): ByteBuffer = {
    if (buffer.position + offset == buffer.limit)
      throw new PartialMessageException
    if (buffer.get(buffer.position + offset) == LF) {
      val slice = buffer.slice
      slice.limit(offset)
      buffer.position(buffer.position + offset + 1)
      slice
    } else {
      slice(buffer, offset + 1)
    }
  }

  private def packetType(packetType: Byte) = (packetType: @switch) match {
    case '+' => Debug
    case 'A' => LoginAccepted
    case 'J' => LoginRejected
    case 'S' => SequencedData
    case 'H' => ServerHeartbeat
    case 'L' => LoginRequest
    case 'U' => UnsequencedData
    case 'R' => ClientHeartbeat
    case 'O' => LogoutRequest
    case  x  => throw new GarbledMessageException("Unknown packet type: " + x)
  }
}
