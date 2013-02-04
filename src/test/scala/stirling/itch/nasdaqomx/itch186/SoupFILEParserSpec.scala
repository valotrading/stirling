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
package stirling.itch.nasdaqomx.itch186

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import stirling.io.Source

class SoupFILEParserSpec extends WordSpec with MustMatchers {
  "SoupFILEParser" must {
    "parse messages with read buffer underflow inside message" in {
      val messageTypes = "TMSORHAFECXDPQBI-"
      source(128).map(_.messageType.toChar).mkString must equal(messageTypes)
    }
  }

  private def source(readBufferSize: Int): Source[Message] = {
    Source.fromInputStream[Message](
      stream         = getClass.getResourceAsStream("/itch-v186.txt"),
      parser         = new SoupFILEParser,
      readBufferSize = readBufferSize
    )
  }
}
