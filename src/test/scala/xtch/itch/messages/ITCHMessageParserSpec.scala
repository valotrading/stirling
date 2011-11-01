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
package xtch.itch.messages

import java.nio.ByteBuffer
import silvertip.GarbledMessageException
import xtch.itch.Spec
import xtch.itch.elements.Fields
import xtch.itch.messages.ITCHMessageParser._
import xtch.itch.templates.Templates

class ITCHMessageParserSpec extends Spec {
  "ITCHMessageParser" when {
    "parsing" must {
      "parse a Seconds message" in {
        parse(Encoded.seconds.toByteBuffer) must equal(Decoded.seconds)
      }
      "parse a Milliseconds message" in {
        parse(Encoded.milliseconds.toByteBuffer) must equal(Decoded.milliseconds)
      }
      "throw an exception on an unknown message type" in {
        intercept[GarbledMessageException] {
          parse("S".toByteBuffer)
        }
      }
    }
  }
}

object Decoded {
  def milliseconds = {
    val message = new ITCHMessage(Templates.Milliseconds)
    message.set(Fields.Millisecond, 12L)
    message
  }
  def seconds = {
    val message = new ITCHMessage(Templates.Seconds)
    message.set(Fields.Second, 12345L)
    message
  }
}

object Encoded {
  def milliseconds = "M 12\r\n"
  def seconds = "T12345\r\n"
}
