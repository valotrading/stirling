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
package fixengine.messages;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.runner.RunWith;

/**
 * @author Pekka Enberg 
 */
@RunWith(JDaveRunner.class)
public class UtcTimestampFieldSpec extends Specification<UtcTimestampField> {
    private static final DateTimeZone zoneUTC = DateTimeZone.UTC;
    private static final DateTime DATE_TIME = new DateTime(2008, 9, 11, 1, 2, 3, 0, zoneUTC);
    private final Tag tag = dummy(Tag.class);
    private UtcTimestampField timestamp;

    public class TimestampFieldThatDoesHasValue {
        public UtcTimestampField create() {
            return timestamp = new UtcTimestampField(tag);
        }

        public void failsToParseNonTimestampStrings() {
            timestamp.parse("ZZ");
            specify(timestamp.isFormatValid(), must.equal(false));
        }

        public void formatsToStringTimestamp() {
            timestamp.setValue(new DateTime(2008, 9, 11, 1, 2, 3, 0, DateTimeZone.UTC));
            specify(timestamp.value(), must.equal("20080911-01:02:03"));
        }
    }

    public class TimestampFieldThatDoesNotHaveValue {
        public UtcTimestampField create() {
            return timestamp = new UtcTimestampField(tag);
        }

        public void formatsToEmptyTimestamp() {
            specify(timestamp.value(), must.equal(null));
        }
    }

    public class TimestampFieldThatHasParsedValue {
        public UtcTimestampField create() {
            timestamp = new UtcTimestampField(tag);
            timestamp.parseValue("20080911-01:02:03");
            return timestamp;
        }

        public void formatsToTheSameTimestamp() {
            specify(timestamp.value(), must.equal("20080911-01:02:03"));
        }

        public void parsesStringTimestampToDateTimeValue() {
            specify(timestamp.dateValue(), must.equal(DATE_TIME));
        }
    }}