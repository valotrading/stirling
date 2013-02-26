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

import java.util.Locale
import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import stirling.fix.tags.fix42.BidPx

class FloatFieldSpec extends WordSpec with MustMatchers {
  "FloatField" should {
    val field = new FloatField(BidPx.Tag)
    "parse a negative value" in {
      field.parse("-00023.23")
      field.doubleValue must equal(-23.23)
    }
    "parse leading zeroes" in {
      field.parse("00023.23")
      field.doubleValue must equal(23.23)
    }
    "parse trailing zeroes" in {
      field.parse("23.000")
      field.doubleValue must equal(23.0)
    }
    "fail to parse the scientific notation" in {
      intercept[InvalidValueFormatException] {
        field.parse("1.23E4")
      }.getMessage must equal("BidPx(132): Invalid value format")
    }
    "format to a decimal number" in {
      field.setValue(23.23)
      field.value() must equal("23.23")
    }
    "format a huge number to a decimal number" in {
      field.setValue(10000000.0)
      field.value() must equal("10000000.00")
    }
    "use a full stop as the decimal separator regardless of the default local" in {
      val locale = Locale.getDefault
      Locale.setDefault(Locale.FRANCE)
      field.setValue(23.23)
      field.value() must equal("23.23")
      Locale.setDefault(locale)
    }
  }
}
