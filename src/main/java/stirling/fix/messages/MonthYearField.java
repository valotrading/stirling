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
package stirling.fix.messages;

import static org.joda.time.DateTimeZone.UTC;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class MonthYearField extends AbstractField<DateTime> {
    /* TODO: Add support for: YYYYMMDD, YYYYMMWW */
    private static final String FORMAT = "yyyyMM";

    public MonthYearField(Tag<MonthYearField> tag) {
        this(tag, Required.YES);
    }

    public MonthYearField(Tag<MonthYearField> tag, DateTime dateTime) {
        super(tag, dateTime, Required.YES);
    }

    public MonthYearField(Tag<MonthYearField> tag, Required required) {
        super(tag, null, required);
    }

    @Override public void parse(String value) {
        DateTimeFormatter fmt = getFormat();
        try {
            this.value = fmt.withZone(UTC).parseDateTime(value);
        } catch (Exception e) {
            validFormat = false;
        }
    }

    public DateTime dateValue() {
        return value;
    }

    @Override protected String value() {
        if (!hasValue()) {
            return null;
        }
        DateTimeFormatter fmt = getFormat();
        return fmt.withZone(UTC).print(value);
    }

    private DateTimeFormatter getFormat() {
        return DateTimeFormat.forPattern(FORMAT);
    }
}