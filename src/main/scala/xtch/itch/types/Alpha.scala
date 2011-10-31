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

object Alpha {
  def apply(length: Int) = new Alpha(length)
}

class Alpha(val length: Int) extends AbstractDataType[String] {
  def decode(buffer: ByteBuffer) = read(buffer)
  def encode(buffer: ByteBuffer, value: String) {
    write(buffer, value.padTo(length, ' '))
  }
}
