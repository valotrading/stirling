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
package stirling.netstring

import java.nio.charset.Charset
import java.nio.{BufferUnderflowException, ByteBuffer}
import scala.annotation.tailrec
import silvertip.{GarbledMessageException, MessageParser, PartialMessageException}

object Netstring extends MessageParser[Array[Byte]] {
  private val ASCII = Charset.forName("US-ASCII")

  private val Colon = 0x3A.toByte
  private val Comma = 0x2C.toByte

  def format(content: Array[Byte]): Array[Byte] = {
    val length  = content.length.toString.getBytes(ASCII)
    val message = new Array[Byte](length.length + 1 + content.length + 1)

    System.arraycopy(length, 0, message, 0, length.length)
    message(length.length) = Colon

    System.arraycopy(content, 0, message, length.length + 1, content.length)
    message(message.length - 1) = Comma

    message
  }

  override def parse(buffer: ByteBuffer): Array[Byte] = {
    readContent(buffer, readLength(buffer))
  }

  private def readLength(buffer: ByteBuffer): Int = {
    val length = new String(readUntil(buffer, Colon), ASCII)

    try {
      length.toInt
    } catch {
      case _: NumberFormatException =>
        throw new GarbledMessageException("Expected integer, got '%s'".format(length))
    }
  }

  @tailrec
  private def readUntil(buffer: ByteBuffer, byte: Byte, bytes: Seq[Byte] = Seq()): Array[Byte] = {
    if (buffer.remaining == 0)
      throw new PartialMessageException

    val next = buffer.get()
    if (next == byte)
      bytes.toArray
    else
      readUntil(buffer, byte, bytes :+ next)
  }

  private def readContent(buffer: ByteBuffer, length: Int): Array[Byte] = {
    if (buffer.remaining < length + 1)
      throw new PartialMessageException

    val bytes = new Array[Byte](length)
    buffer.get(bytes)

    val comma = buffer.get()
    if (comma != Comma)
      throw new GarbledMessageException("Expected 0x%02X, got 0x%02X".format(Comma, comma))

    bytes
  }

  object Text extends MessageParser[String] {
    private val UTF8 = Charset.forName("UTF-8")

    def format(content: String): Array[Byte] = {
      Netstring.format(content.getBytes(UTF8))
    }

    override def parse(buffer: ByteBuffer): String = {
      new String(Netstring.parse(buffer), UTF8)
    }
  }
}
