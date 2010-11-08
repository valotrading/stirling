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
package fixengine.messages;

public enum DKReasonValue implements Formattable {
    UNKNOWN_SYMBOL('A'),
    WRONG_SIDE('B'),
    QUANTITY_EXCEEDS_ORDER('C'),
    NO_MATCHING_ORDER('D'),
    PRICE_EXCEEDS_LIMIT('E'),
    OTHER('Z');

    private char value;

    DKReasonValue(char value) {
        this.value = value;
    }

    @Override public String value() {
        return Character.toString(value);
    }

    public static DKReasonValue parse(char value) {
        for (DKReasonValue type : DKReasonValue.values()) {
            if (type.value == value)
                return type;
        }
        throw new InvalidValueForTagException(Character.toString(value));
    }
}
