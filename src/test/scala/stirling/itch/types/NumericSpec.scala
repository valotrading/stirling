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
package stirling.itch.types

import java.nio.ByteBuffer
import stirling.itch.Spec

class NumericSpec extends Spec {
  "Numeric" when {
    "encoding" must {
      val numeric = Numeric(3)
      "produce correct output" in {
        numeric.encodeBytes(303L) must equal(List(0x33, 0x30, 0x33).toBytes)
      }
      "pad shorter input to type length" in {
        numeric.encodeBytes(20L) must equal(" 20".toBytes)
      }
      "throw an exception on too large input" in {
        intercept[IllegalArgumentException] {
          numeric.encodeBytes(1000L)
        }
      }
    }
    "transcoding" must {
      val numeric = Numeric(8)
      "produce correct result" in {
        val value = 31137L
        numeric.decodeBytes(numeric.encodeBytes(value)) must equal(value)
      }
    }
  }
}
