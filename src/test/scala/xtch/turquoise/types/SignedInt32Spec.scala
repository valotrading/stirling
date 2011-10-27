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

class SignedInt32Spec extends Spec with SignedInt32Transcoding {
  "SignedInt32" must {
    "encode correctly" in {
      encode(-2147483647: java.lang.Integer) must equal(List(0x01, 0x00, 0x00, 0x80).toByteArray)
    }
    "transcode successfully" in {
      val value = -2147483648
      encodeAndDecode(value: java.lang.Integer) must equal(value)
    }
  }
}

trait SignedInt32Transcoding extends Transcoding {
  def decode(buffer: ByteBuffer) = {
    SignedInt32.TYPE.decode(buffer, 4)
  }
  def encode(buffer: ByteBuffer, value: AnyRef) {
    SignedInt32.TYPE.encode(buffer, value.asInstanceOf[Int], 4)
  }
}
