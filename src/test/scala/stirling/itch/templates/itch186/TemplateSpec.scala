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
package stirling.itch.templates.itch186

import java.nio.ByteBuffer
import stirling.itch.Spec
import stirling.itch.fields.itch186.Fields
import stirling.itch.messages.itch186.{Message, MessageParser}

abstract class TemplateSpec extends Spec with TemplateFixtures {
  "Template" when {
    "encoding" must {
      "produce correct output" in {
        message.encodeBytes must equal(encoded.toBytes)
      }
    }
    "transcoding" must {
      "produce correct result" in {
        MessageParser.parse((message.encodeBytes).toByteBuffer) must equal(message)
      }
    }
  }
}

trait TemplateFixtures {
  def encoded: String
  def fields = Fields
  def message: Message
  def newMessage(template: Template) = new Message(template)
}
