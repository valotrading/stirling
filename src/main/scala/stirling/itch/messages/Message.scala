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

import java.nio.ByteBuffer
import stirling.itch.fields.FieldContainer
import stirling.itch.templates.MessageTemplate

trait Message extends FieldContainer {
  def encode(buffer: ByteBuffer) {
    buffer.put(messageType)
  }
  def length = 1 + template.length
  def template: MessageTemplate[_]
  def messageType = template.messageType.toByte
}
