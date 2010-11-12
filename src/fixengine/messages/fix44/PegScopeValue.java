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
package fixengine.messages.fix44;

import fixengine.messages.Formattable;
import fixengine.messages.InvalidValueForTagException;

public enum PegScopeValue implements Formattable {
    LOCAL('1'),
    NATIONAL('2'),
    GLOBAL('3'),
    NATIONAL_EXCLUDING_LOCAL('4');

    private char value;

    PegScopeValue(char value) {
        this.value = value;
    }

    @Override public String value() {
        return Character.toString(value);
    }

    public static PegScopeValue parse(char value) {
        for (PegScopeValue type : PegScopeValue.values()) {
            if (type.value == value)
                return type;
        }
        throw new InvalidValueForTagException(Character.toString(value));
    }
}
