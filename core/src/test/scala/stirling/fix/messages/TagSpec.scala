/*
 * Copyright 2008 the original author or authors.
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
package stirling.fix.messages

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers

class TagSpec extends WordSpec with MustMatchers {
  "Tag" when {
    "in normal range" should {
      "not be user defined" in {
        Tag.isUserDefined(4999) must equal(false)
      }
    }
    "in user defined range" should {
      "be user defined" in {
        Tag.isUserDefined(5000) must equal(true)
      }
    }
    "in reserved range" should {
      "be user defined" in {
        Tag.isUserDefined(10000) must equal(true)
      }
    }
  }
}
