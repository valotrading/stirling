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
package stirling.itch.nasdaq.itch41

import java.nio.{BufferUnderflowException, ByteBuffer, ByteOrder}

object ByteBuffers {
  def copy(buffer: ByteBuffer, length: Int): ByteBuffer = {
    val original = buffer.limit
    val limit    = buffer.position + length

    if (limit > buffer.capacity)
      throw new BufferUnderflowException()

    buffer.limit(limit)

    val copy = ByteBuffer.allocate(length)
    copy.order(ByteOrder.BIG_ENDIAN)
    copy.put(buffer)
    copy.flip()

    buffer.position(limit)
    buffer.limit(original)

    copy
  }

  def slice(buffer: ByteBuffer, index: Int, length: Int): ByteString = {
    buffer.position(index)

    val slice = new Array[Byte](length)
    buffer.get(slice)

    ByteString(slice)
  }
}
