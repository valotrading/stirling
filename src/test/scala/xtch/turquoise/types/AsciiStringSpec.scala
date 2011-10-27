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
package xtch.turquoise.types

import java.nio.ByteBuffer
import xtch.Spec
import xtch.types.Transcoding

class AsciiStringSpec extends Spec with AsciiStringTranscoding {
  "AsciiString" must {
    "encode value of maximum length correctly" in {
      encode("0123456789") must equal(List(48, 49, 50, 51, 52, 53, 54, 55, 56, 57).toByteArray)
    }
    "encode value shorter than maximum length correctly" in {
      encode("01234567") must equal(List(48, 49, 50, 51, 52, 53, 54, 55, 0x00).toByteArray)
    }
    "transcode successfully" in {
      encodeAndDecode("foobar") must equal("foobar")
    }
  }
}

trait AsciiStringTranscoding extends Transcoding {
  def decode(buffer: ByteBuffer) = {
    AsciiString.TYPE.decode(buffer, 10)
  }
  def encode(buffer: ByteBuffer, value: AnyRef) = {
    AsciiString.TYPE.encode(buffer, value.asInstanceOf[String], 10)
  }
}
