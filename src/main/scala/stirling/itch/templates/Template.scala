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
package stirling.itch.templates

import java.nio.ByteBuffer
import stirling.itch.fields.Field
import stirling.itch.messages.FieldContainer

abstract class Template[T <: FieldContainer] {
  def decode(buffer: ByteBuffer): T = {
    val container = newFieldContainer
    fields.foreach { field =>
      container.set(field, field.decode(buffer))
    }
    container
  }
  def encode(buffer: ByteBuffer, container: T) {
    fields.foreach { field =>
      container.get(field) match {
        case Some(value) => field.encode(buffer, value.asInstanceOf[AnyRef])
        case None => throw new IllegalArgumentException("Field %s missing".format(field.name))
      }
    }
  }
  def fields: List[Field[_]]
  def length = fields.map(_.length).sum
  def newFieldContainer: T
}
