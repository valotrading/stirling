/*
 * Copyright 2013 the original author or authors.
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
package stirling.io

import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.util.Arrays

object TextFormat {
  val ASCII = Charset.forName("US-ASCII")

  def alphaPadRight(value: String, fieldSize: Int, fillChar: Byte): Array[Byte] = {
    if (value.length > fieldSize)
      throw new IllegalArgumentException("Value length %d exceeds field size %d".format(value.length, fieldSize))

    val formatted = value.getBytes(ASCII)
    val formattedLength = formatted.length

    if (formattedLength == fieldSize) {
      formatted
    } else {
      val bytes = new Array[Byte](fieldSize)
      System.arraycopy(formatted, 0, bytes, 0, formattedLength)
      Arrays.fill(bytes, formattedLength, fieldSize, fillChar)

      bytes
    }
  }

  def numericPadLeft(value: Long, fieldSize: Int, fillChar: Byte): Array[Byte] = {
    if (value < 0)
      throw new IllegalArgumentException("Negative value %d".format(value))

    val formatted = value.toString.getBytes(ASCII)
    val formattedLength = formatted.length

    if (formattedLength > fieldSize)
      throw new IllegalArgumentException("Formatted length %d exceeds field size %d".format(formattedLength, fieldSize))

    val padTo = fieldSize - formattedLength

    val bytes = new Array[Byte](fieldSize)
    Arrays.fill(bytes, 0, padTo, fillChar)
    System.arraycopy(formatted, 0, bytes, padTo, formattedLength)

    bytes
  }
}
