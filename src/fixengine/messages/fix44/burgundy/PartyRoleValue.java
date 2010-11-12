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
package fixengine.messages.fix44.burgundy;

import fixengine.messages.Formattable;
import fixengine.messages.InvalidValueForTagException;

public enum PartyRoleValue implements Formattable {
    EXECUTING_FIRM(1),
    CLEARING_FIRM(4),
    ENTERING_FIRM(7),
    CONTRA_FIRM(17),
    CONTRA_CLEARING_FIRM_OR_CCP(18),
    CLEARING_ORGANIZATION(21),
    ENTERING_TRADER(36);

    private int value;

    PartyRoleValue(int value) {
        this.value = value;
    }

    @Override public String value() {
        return Integer.toString(value);
    }

    public static PartyRoleValue parse(int value) {
        for (PartyRoleValue type : PartyRoleValue.values()) {
            if (type.value == value)
                return type;
        }
        throw new InvalidValueForTagException(Integer.toString(value));
    }
}
