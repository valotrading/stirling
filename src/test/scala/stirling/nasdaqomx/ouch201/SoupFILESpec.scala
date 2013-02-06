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
package stirling.nasdaqomx.ouch201

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import stirling.io.Source
import stirling.nasdaq.soupfile100.{EndOfSession, Packet, SequencedData, SoupFILEParser}

class SoupFILESpec extends WordSpec with MustMatchers {
  "SoupFILEParser" must {
    "parse messages with read buffer underflow inside message" in {
      val messageTypes = "SARUCEBJ"
      source(256).map(messageType).mkString must equal(messageTypes)
    }
  }

  private def messageType(packet: Packet[Message]): Char = packet match {
    case SequencedData(message) => message.messageType.toChar
    case EndOfSession           => '-'
  }

  private def source(readBufferSize: Int): Source[Packet[Message]] = {
    Source.fromInputStream(
      stream         = getClass.getResourceAsStream("/ouch-v201.txt"),
      parser         = new SoupFILEParser(new MessageParser),
      readBufferSize = readBufferSize
    )
  }
}
