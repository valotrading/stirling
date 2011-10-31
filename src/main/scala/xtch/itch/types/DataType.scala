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

trait DataType[T] {
  def decode(buffer: ByteBuffer): T
  def decode(bytes: List[Byte]): T = {
    val buffer = ByteBuffer.allocate(length)
    buffer.put(Array[Byte](bytes: _*))
    buffer.flip
    decode(buffer)
  }
  def encode(buffer: ByteBuffer, value: T)
  def encode(value: T): List[Byte] = {
    val buffer = ByteBuffer.allocate(length)
    encode(buffer, value)
    buffer.flip
    val bytes = new Array[Byte](length)
    buffer.get(bytes)
    List(bytes: _*)
  }
  def length: Int
}
