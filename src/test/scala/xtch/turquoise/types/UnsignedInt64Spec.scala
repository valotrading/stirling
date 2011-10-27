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

import java.math.BigInteger
import java.nio.ByteBuffer
import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import xtch.types.Transcoding

class UnsignedInt64Spec extends WordSpec with MustMatchers with UnsignedInt64Transcoding {
  "UnsignedInt64" must {
    "encode correctly" in {
      val expected = List(0xfe, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff).toByteArray
      encode(new BigInteger(expected)) must equal(expected)
    }
    "transcode successfully" in {
      val bytes = List(0xfa, 0xa7, 0xb8, 0x07, 0xf2, 0xff, 0xff, 0xff).toByteArray
      encodeAndDecode(new BigInteger(bytes))
    }
  }
}

trait UnsignedInt64Transcoding extends Transcoding {
  def decode(buffer: ByteBuffer) = {
    UnsignedInt64.TYPE.decode(buffer, 8)
  }
  def encode(buffer: ByteBuffer, value: AnyRef) {
    UnsignedInt64.TYPE.encode(buffer, value.asInstanceOf[BigInteger], 8)
  }
}
