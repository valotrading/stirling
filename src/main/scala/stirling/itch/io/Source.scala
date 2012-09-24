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

import java.io.{Closeable, File, FileInputStream}
import java.nio.channels.{Channels, ReadableByteChannel}
import java.nio.{ByteOrder, ByteBuffer}
import java.util.zip.{GZIPInputStream, ZipFile}
import scala.collection.JavaConversions._
import silvertip.{MessageParser, PartialMessageException}

abstract class Source[Message] extends Iterator[Message] with Closeable

object Source {
  def fromFile[Message](file: File, parser: MessageParser[Message], readBufferSize: Int = 65535): Source[Message] = {
    new MessageIterator(newChannel(file), parser, readBufferSize)
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
}
