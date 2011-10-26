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
package xtch.types

import java.nio.ByteBuffer

trait Transcoding {
  val BufferSize = 1024
  def decode(buffer: ByteBuffer): AnyRef
  def encode(buffer: ByteBuffer, value: AnyRef)
  def encode(value: AnyRef): Array[Byte] = {
    val buffer = ByteBuffer.allocate(BufferSize)
    encode(buffer, value)
    buffer.flip
    val bytes = new Array[Byte](buffer.limit)
    buffer.get(bytes, 0, bytes.length)
    bytes
  }
  def encodeAndDecode(value: AnyRef) = {
    decode(ByteBuffer.wrap(encode(value)))
  }
}
