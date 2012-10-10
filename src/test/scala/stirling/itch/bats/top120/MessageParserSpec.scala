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

import stirling.itch.Spec
import stirling.itch.io.Source

class MessageParserSpec extends Spec {
  "MessageParser" must {
    "parse messages with read buffer overflow inside message" in {
      val messageTypes = "CJsDHTMEBbeAaFUufVvt"
      source(128).map(_.messageType.toChar).mkString must equal(messageTypes)
    }
  }

  private def source(readBufferSize: Int): Source[Message] = {
    Source.fromInputStream[Message](
      stream         = getClass.getResourceAsStream("/top-v120.txt"),
      parser         = new MessageParser,
      readBufferSize = readBufferSize
    )
  }
}
