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
package fixengine.messages.fix42;

import fixengine.messages.Formattable;
import fixengine.messages.InvalidValueForTagException;

public enum AllocTransTypeValue implements Formattable {
    NEW('0'),
    REPLACE('1'),
    CANCEL('2'),
    PRELIMINARY('3'),                    /* (without MiscFees and NetMoney) */
    CALCULATED('4'),                     /* (includes MiscFees and NetMoney) */
    CALCULATED_WITHOUT_PRELIMINARY('5'); /* Calculated without Preliminary (sent unsolicited by broker, includes MiscFees and NetMoney) */

    private char value;
    
    AllocTransTypeValue(char value) {
        this.value = value;
    }

    @Override public String value() {
        return Character.toString(value);
    }

    public static AllocTransTypeValue parse(char value) {
        for (AllocTransTypeValue type : AllocTransTypeValue.values()) {
            if (type.value == value)
                return type;
        }
        throw new InvalidValueForTagException(Character.toString(value));
    }
}