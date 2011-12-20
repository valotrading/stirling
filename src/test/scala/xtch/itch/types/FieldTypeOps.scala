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

class FieldTypeOps[T](val fieldType: FieldType[T]) {
  def decodeBytes(bytes: List[Byte]): T = {
    val buffer = ByteBuffer.allocate(fieldType.length)
    buffer.put(Array[Byte](bytes: _*))
    buffer.flip
    fieldType.decode(buffer)
  }
  def encodeBytes(value: T): List[Byte] = {
    val buffer = ByteBuffer.allocate(fieldType.length)
    fieldType.encode(buffer, value)
    buffer.flip
    val bytes = new Array[Byte](fieldType.length)
    buffer.get(bytes)
    List(bytes: _*)
  }
}
