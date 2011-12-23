/*
 * Copyright 2010 the original author or authors.
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
package stirling.itch.messages

import silvertip.{GarbledMessageException, PartialMessageException}
import stirling.itch.Spec

class ITCHFileParserSpec extends Spec {
  "ITCHFileParser" when {
    val parser = ITCHFileParser
    "parsing" must {
      "silently ignore superfluous terminators" in {
        intercept[PartialMessageException] {
          parser.parse(ITCHMessage.terminator.toByteBuffer)
        }
      }
      "throw an exception on an unknown message type" in {
        intercept[GarbledMessageException] {
          parser.parse("?".toByteBuffer)
        }
      }
    }
  }
}
