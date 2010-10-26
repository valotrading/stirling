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

import fixengine.messages.Formattable;
import fixengine.messages.InvalidValueForTagException;

public enum LastCapacityValue implements Formattable {
    AGENCT('1'),
    CROSS_AS_AGENT('2'),
    CROSS_AS_PRINCIPAL('3'),
    PRINCIPAL('4');

    private char value;

    LastCapacityValue(char value) {
        this.value = value;
    }

    @Override public String value() {
        return Character.toString(value);
    }

    public static LastCapacityValue parse(char value) {
        for (LastCapacityValue type : LastCapacityValue.values()) {
            if (type.value == value)
                return type;
        }
        throw new InvalidValueForTagException(Character.toString(value));
    }
}
