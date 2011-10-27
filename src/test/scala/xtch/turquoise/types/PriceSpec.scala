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

class PriceSpec extends Spec with PriceTranscoding {
  "Price" must {
    "encode correctly" in {
      val value = -1200012345678L
      encode(value: java.lang.Long) must equal(List(0x20, 0xd1, 0xff, 0xff, 0xb2, 0x9e, 0x43, 0xff).toByteArray)
    }
    "transcode successfully" in {
      val value = -1200012345670L
      encodeAndDecode(value: java.lang.Long) must equal(value)
    }
  }
}

trait PriceTranscoding extends Transcoding {
  def decode(buffer: ByteBuffer) = {
    Price.TYPE.decode(buffer, 8)
  }
  def encode(buffer: ByteBuffer, value: AnyRef) = {
    Price.TYPE.encode(buffer, value.asInstanceOf[Long], 8)
  }
}
