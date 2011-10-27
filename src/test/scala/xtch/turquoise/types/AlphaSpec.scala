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

class AlphaSpec extends WordSpec with MustMatchers with AlphaTranscoding {
  "Alpha" must {
    "transcode successfully" in {
      val value = '+'
      encodeAndDecode(value: java.lang.Character) must equal(value)
    }
  }
}

trait AlphaTranscoding extends Transcoding {
  def decode(buffer: ByteBuffer) = {
    Alpha.TYPE.decode(buffer, 1)
  }
  def encode(buffer: ByteBuffer, value: AnyRef) {
    Alpha.TYPE.encode(buffer, value.asInstanceOf[Char], 1);
  }
}
