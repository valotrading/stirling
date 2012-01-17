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
import stirling.fix.tags.fix42.BeginSeqNo

class IntegerFieldSpec extends WordSpec with MustMatchers {
  "IntegerField" should {
    val field = new IntegerField(BeginSeqNo.Tag)
    "parse a negative value" in {
      field.parse("-23")
      field.intValue must equal(-23)
    }
    "parse leading zeroes" in {
      field.parse("00023")
      field.intValue must equal(23)
    }
    "fail to parse a decimal number" in {
      field.parse("1.23")
      field.isFormatValid must equal(false)
    }
    "fail to parse a non-numeric string" in {
      field.parse("ZZ")
      field.isFormatValid must equal(false)
    }
  }
}
