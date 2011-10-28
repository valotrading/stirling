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
package xtch.itch.types

import java.nio.ByteBuffer
import xtch.Spec

class AlphaSpec extends Spec {
  "Alpha" when {
    val alpha = Alpha(5)
    "encoding" must {
      "produce correct output" in {
        alpha.encode("hello") must equal(List(0x68, 0x65, 0x6c, 0x6c, 0x6f).toBytes)
      }
      "pad shorter input to type length" in {
        alpha.encode("foo") must equal("foo  ".toBytes)
      }
      "throw an exception on too long input" in {
        intercept[IllegalArgumentException] {
          alpha.encode("excess")
        }
      }
    }
    "transcoding" must {
      "produce correct result" in {
        val value = "foo"
        alpha.decode(alpha.encode(value)) must equal(value)
      }
    }
  }
}
