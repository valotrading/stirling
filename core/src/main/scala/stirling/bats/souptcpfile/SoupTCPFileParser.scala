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
package stirling.bats.souptcpfile

import java.nio.ByteBuffer
import silvertip.{GarbledMessageException, MessageParser, PartialMessageException}

class SoupTCPFileParser[Message](messageParser: MessageParser[Message]) extends MessageParser[Message] {
  import SoupTCPFileParser._

  override def parse(buffer: ByteBuffer): Message = {
    if (buffer.remaining < 1)
      throw new PartialMessageException

    if (buffer.get() != SequencedMessage)
      throw new GarbledMessageException("Expected Sequenced Message")

    val message = messageParser.parse(buffer)

    if (buffer.remaining < 1)
      throw new PartialMessageException

    if (buffer.get() != LF)
      throw new GarbledMessageException("Expected LF")

    message
  }
}

object SoupTCPFileParser {
  val LF:               Byte = 0x0A
  val SequencedMessage: Byte = 0x53
}
