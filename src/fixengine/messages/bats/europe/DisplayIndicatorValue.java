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
package fixengine.messages.bats.europe;

import fixengine.messages.Formattable;
import fixengine.messages.InvalidValueForTagException;

public enum DisplayIndicatorValue implements Formattable {
    DISPLAYED_ORDER('X'),
    INVISIBLE('I');

    private char value;

    DisplayIndicatorValue(char value) {
        this.value = value;
    }

    @Override public String value() {
        return Character.toString(value);
    }

    public static DisplayIndicatorValue parse(char value) {
        for (DisplayIndicatorValue type : DisplayIndicatorValue.values()) {
            if (type.value == value)
                return type;
        }
        throw new InvalidValueForTagException(Character.toString(value));
    }
}
