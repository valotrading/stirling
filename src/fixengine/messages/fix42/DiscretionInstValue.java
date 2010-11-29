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
package fixengine.messages.fix42;

import fixengine.messages.Formattable;
import fixengine.messages.InvalidValueForTagException;

public enum DiscretionInstValue implements Formattable {
    RELATED_TO_DISPLAYED_PRICE('0'),
    RELATED_TO_MARKET_PRICE('1'),
    RELATED_TO_PRIMARY_PRICE('2'),
    RELATED_TO_LOCAL_PRIMARY_PRICE('3'),
    RELATED_TO_MIDPOINT('4'),
    RELATED_TO_LAST_TRADE_PRICE('5');

    private char value;

    DiscretionInstValue(char value) {
        this.value = value;
    }

    public String value() {
        return Character.toString(value);
    }

    public static DiscretionInstValue parse(char value) {
        for (DiscretionInstValue type : DiscretionInstValue.values()) {
            if (type.value == value)
                return type;
        }
        throw new InvalidValueForTagException(Character.toString(value));
    }
}
