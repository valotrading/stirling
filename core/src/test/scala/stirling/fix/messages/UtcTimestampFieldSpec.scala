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

import org.joda.time.DateTime
import org.joda.time.DateTimeZone._
import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import stirling.fix.tags.fix42.OrigTime

class UtcTimestampFieldSpec extends WordSpec with MustMatchers with UtcTimestampFieldFixtures {
  "UtcTimestampField" when {
    "parsing" should {
      val timestamp = new UtcTimestampField(OrigTime.Tag)
      "not parse non-timestamp strings" in {
        intercept[InvalidValueFormatException] {
          timestamp.parse("ZZ")
        }.getMessage must equal("OrigTime(42): Invalid value format: ZZ")
      }
    }
    "it has a value" should {
      val timestamp = new UtcTimestampField(OrigTime.Tag)
      timestamp.setValue(dateTime)
      "format to a timestamp string" in {
        timestamp.value() must equal(dateTimeAsString)
      }
    }
    "it does not have a value" should {
      val timestamp = new UtcTimestampField(OrigTime.Tag)
      "format to null" in {
        timestamp.value() must equal(null)
      }
    }
    "it has a parsed value without milliseconds" should {
      val timestamp = new UtcTimestampField(OrigTime.Tag)
      timestamp.parseValue(dateTimeAsString)
      "format to the same timestamp string" in {
        timestamp.value() must equal(dateTimeAsString)
      }
      "parse the timestamp string into a DateTime value" in {
        timestamp.dateValue must equal(dateTime)
      }
    }
    "it has a parsed value with milliseconds" should {
      val timestamp = new UtcTimestampField(OrigTime.Tag)
      timestamp.parseValue(dateTimeWithMillisecondsAsString)
      "format to the same timestamp string" in {
        timestamp.value() must equal(dateTimeWithMillisecondsAsString)
      }
      "parse the timestamp string into a DateTime value" in {
        timestamp.dateValue must equal(dateTimeWithMilliseconds)
      }
    }
  }
}

trait UtcTimestampFieldFixtures {
  def dateTime= new DateTime(2008, 9, 11, 1, 2, 3, 0, UTC)
  def dateTimeAsString = "20080911-01:02:03"
  def dateTimeWithMilliseconds = new DateTime(2008, 9, 11, 1, 2, 3, 456, UTC)
  def dateTimeWithMillisecondsAsString = "20080911-01:02:03.456"
}
