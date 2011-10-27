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
package xtch.fast.types

import java.nio.ByteBuffer
import xtch.Spec
import xtch.types.Transcoding

class AsciiStringSpec extends Spec with AsciiStringTranscoding {
  "AsciiString" must {
    "transcode successfully" in {
      val value = "Hello"
      encodeAndDecode(value) must equal(value)
    }
    "throw an exception when encoding a string that contains a stop bit" in {
      intercept[IllegalArgumentException] {
        val withStopBit = new String(Array[Byte](0, 0, AsciiString.TYPE.addStopBit(0), 0))
        AsciiString.TYPE.encode(ByteBuffer.allocate(BufferSize), withStopBit, Int.MaxValue)
      }
    }
  }
}

trait AsciiStringTranscoding extends Transcoding {
  def encode(buffer: ByteBuffer, value: AnyRef): Unit = {
    AsciiString.TYPE.encode(buffer, value.asInstanceOf[String], Int.MaxValue)
  }
  def decode(buffer: ByteBuffer) = {
    AsciiString.TYPE.decode(buffer, Int.MaxValue)
  }
}
