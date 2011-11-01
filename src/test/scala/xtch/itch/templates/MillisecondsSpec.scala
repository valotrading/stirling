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
package xtch.itch.templates

import java.nio.ByteBuffer
import xtch.itch.Spec
import xtch.itch.elements.Fields
import xtch.itch.messages.ITCHMessage

class MillisecondsSpec extends Spec with MillisecondsFixtures {
  "Milliseconds" when {
    "encoding" must {
      "produce correct output" in {
        message.encodeBytes must equal(encoded.toBytes)
      }
    }
  }
}

trait MillisecondsFixtures {
  def encoded = "M 12\r\n"
  def message = {
    val message = ITCHMessage(Templates.Milliseconds)
    message.set(Fields.Millisecond, millisecond)
    message
  }
  def millisecond = 12L
}
