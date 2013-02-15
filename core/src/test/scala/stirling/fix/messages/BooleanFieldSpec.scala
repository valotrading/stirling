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
import stirling.fix.tags.fix42.PossDupFlag

class BooleanFieldSpec extends WordSpec with MustMatchers {
  "BooleanField" when {
    val field = new BooleanField(PossDupFlag)
    "parsing" should {
      "parse true string value" in {
        field.parse("Y")
        field.booleanValue must equal(true)
      }
      "parse false string value" in {
        field.parse("N")
        field.booleanValue must equal(false)
      }
      "raise an exception if attempting to parse a bogus value" in {
        intercept[IllegalArgumentException] {
          field.parse("123")
        }
      }
    }
    "it has false value" should {
      "format to false string value" in {
        field.value() must equal("N")
      }
    }
    "it has true value" should {
      "format to true string value" in {
        field.setValue(true)
        field.value() must equal("Y")
      }
    }
  }
}
