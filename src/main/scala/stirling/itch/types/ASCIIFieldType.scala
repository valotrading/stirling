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
package stirling.itch.types

import java.nio.ByteBuffer

trait ASCIIFieldType[T] extends FieldType[T] with ASCII {
  protected def read(buffer: ByteBuffer) = {
    val bytes = new Array[Byte](length)
    buffer.get(bytes)
    new String(bytes, charset).trim
  }
  protected def write(buffer: ByteBuffer, value: String) {
    if (value.length != length)
      throw new IllegalArgumentException("Value length = %d, type length = %d"
        .format(value.length, length))
    buffer.put(value.getBytes(charset))
  }
}
