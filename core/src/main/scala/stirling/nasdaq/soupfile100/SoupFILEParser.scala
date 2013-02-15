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
package stirling.nasdaq.soupfile100

import java.nio.ByteBuffer
import silvertip.{GarbledMessageException, MessageParser, PartialMessageException}

class SoupFILEParser[Message](messageParser: MessageParser[Message]) extends MessageParser[Packet[Message]] {
  import SoupFILEParser._

  override def parse(buffer: ByteBuffer): Packet[Message] = {
    if (buffer.remaining < 2)
      throw new PartialMessageException

    val packet = buffer.get(buffer.position) match {
      case CR => EndOfSession: Packet[Message]
      case _  => SequencedData(messageParser.parse(buffer))
    }

    if (buffer.remaining < 2)
      throw new PartialMessageException

    if (buffer.get() != CR)
      throw new GarbledMessageException("Expected CR")

    if (buffer.get() != LF)
      throw new GarbledMessageException("Expected LF")

    packet
  }
}

object SoupFILEParser {
  val CR = 13
  val LF = 10
}
