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
package stirling.itch.io

import java.io.{Closeable, File, FileInputStream, IOException}
import java.nio.channels.{Channels, ReadableByteChannel}
import scala.collection.JavaConversions._
import stirling.itch.messages.itch186.{FileParser, Message}
import silvertip.{MessageParser, PartialMessageException}
import java.util.zip.{GZIPInputStream, ZipFile}
import java.nio.{ByteOrder, ByteBuffer}

trait Source[T] extends Iterator[T] with Closeable

object Source {
  def fromFile[T](file: File, parser: MessageParser[T]): Source[T] = {
    new FileSource[T](newChannel(file), parser)
  }

  private def newChannel(file: File) = {
    if (file.getName.endsWith(".zip"))
      newZipChannel(file)
    else if (file.getName.endsWith(".gz"))
      newGZipChannel(file)
    else
      newUncompressedChannel(file)
  }
  private def newZipChannel(file: File) = {
    val stream = {
      val zipFile = new ZipFile(file)
      zipFile.getInputStream(zipFile.entries.toSeq.head)
    }
    Channels.newChannel(stream)
  }
  private def newGZipChannel(file: File) = {
    Channels.newChannel(new GZIPInputStream(new FileInputStream(file)))
  }
  private def newUncompressedChannel(file: File) = {
    new FileInputStream(file).getChannel
  }

  private class FileSource[T](channel: ReadableByteChannel, parser: MessageParser[T]) extends Source[T] {
    private val buffer = ByteBuffer.allocate(4096)
    buffer.order(ByteOrder.BIG_ENDIAN)

    private val iterator = Iterator.continually(read).takeWhile(!_.isEmpty).map(_.get)

    def close() = channel.close()
    def next() = iterator.next()
    def hasNext = iterator.hasNext

    private def read(): Option[T] = {
      try {
        buffer.mark()
        Some(parser.parse(buffer))
      } catch {
        case _: PartialMessageException =>
          buffer.reset()
          buffer.compact()
          if (refill() <= 0) None else read()
      }
    }
    private def refill() = {
      val bytes = channel.read(buffer)
      buffer.flip()
      bytes
    }

    refill()
  }
}
