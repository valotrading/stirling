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
package stirling.io

import java.io.{File, FileOutputStream}
import java.util.zip.{GZIPOutputStream, ZipEntry, ZipOutputStream}
import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import stirling.itch.nasdaqomx.itch186.{Message, SoupFILEParser}

abstract class SourceSpec extends WordSpec with MustMatchers {
  "Source" when {
    "reading a message stream" should {
      "yield messages" in {
        source("T    1\r\nM  1\r\n").map(_.messageType.toChar).mkString must equal("TM")
      }
    }
  }
  def source(data: String): Source[Message] = {
    val bytes = data.getBytes("US-ASCII")
    Source.fromFile(file(bytes), new SoupFILEParser)
  }

  protected def file(data: Array[Byte]): File
}

class ZipCompressedSourceSpec extends SourceSpec {
  def file(data: Array[Byte]): File = {
    val file   = File.createTempFile("SourceSpec", ".zip")
    val output = new ZipOutputStream(new FileOutputStream(file))
    val entry  = new ZipEntry("SourceSpec.txt")

    output.putNextEntry(entry)
    output.write(data)
    output.close()

    file
  }
}

class GZipCompressedSourceSpec extends SourceSpec {
  def file(data: Array[Byte]): File = {
    val file   = File.createTempFile("SourceSpec", ".gz")
    val output = new GZIPOutputStream(new FileOutputStream(file))

    output.write(data)
    output.close()

    file
  }
}

class UncompressedSourceSpec extends SourceSpec {
  def file(data: Array[Byte]): File = {
    val file   = File.createTempFile("SourceSpec", ".txt")
    val output = new FileOutputStream(file)

    output.write(data)
    output.close()

    file
  }
}
