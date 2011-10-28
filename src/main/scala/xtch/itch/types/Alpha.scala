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
package xtch.itch.types

import java.nio.ByteBuffer
import java.nio.charset.Charset._

object Alpha {
  def apply(length: Int) = new Alpha(length)
}

class Alpha(val length: Int) extends Type[String] {
  def decode(buffer: ByteBuffer) = {
    val bytes = new Array[Byte](length)
    buffer.get(bytes)
    new String(bytes, charset).trim
  }
  def encode(buffer: ByteBuffer, value: String) {
    if (value.length > length)
      throw new IllegalArgumentException("Value length = %d, maximum length = %d"
        .format(value.length, length))
    val bytes = value.padTo(length, ' ').getBytes(charset)
    buffer.put(bytes)
  }
  val charset = forName("US-ASCII")
}
