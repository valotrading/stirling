/*
 * Copyright 2009 the original author or authors.
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
package fixengine.messages.fix42.bats.europe;

import fixengine.messages.Formattable;
import fixengine.messages.InvalidValueForTagException;

public enum TimeInForceValue implements Formattable {
    DAY('0'),
    GOOD_TILL_CANCEL('1'),
    IMMEDIATE_OR_CANCEL('3'),
    GOOD_TILL_DATE('6'),
    GOOD_THROUGH_CROSSING('8');

    private char value;

    TimeInForceValue(char value) {
        this.value = value;
    }

    public String value() {
        return Character.toString(value);
    }

    public static TimeInForceValue parse(char value) {
        for (TimeInForceValue type : TimeInForceValue.values()) {
            if (type.value == value)
                return type;
        }
        throw new InvalidValueForTagException(Character.toString(value));
    }
}
