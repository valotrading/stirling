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
import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import xtch.types.Transcoding

class SignedInt8Spec extends WordSpec with MustMatchers with SignedInt8Transcoding {
  "SignedInt8" must {
    "encode correctly" in {
      encode(-127: java.lang.Integer) must equal(List(0x81).toByteArray)
    }
    "transcode successfully" in {
      encodeAndDecode(-128: java.lang.Integer) must equal(-128)
    }
  }
}

trait SignedInt8Transcoding extends Transcoding {
  def decode(buffer: ByteBuffer) = {
    SignedInt8.TYPE.decode(buffer, 1)
  }
  def encode(buffer: ByteBuffer, value: AnyRef) = {
    SignedInt8.TYPE.encode(buffer, value.asInstanceOf[Int], 1)
  }
}
