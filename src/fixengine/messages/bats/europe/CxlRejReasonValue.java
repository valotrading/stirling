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

public enum CxlRejReasonValue implements Formattable {
    /** Too late to cancel.  */
    TOO_LATE_TO_CANCEL(0),

    /** Unknown order.  */
    UNKNOWN_ORDER(1),

    /** Order already in Pending Cancel or Pending Replace status.  */
    PENDING_CANCEL_OR_REPLACE(3);

    private int value;

    CxlRejReasonValue(int value) {
        this.value = value;
    }

    @Override public String value() {
        return Integer.toString(value);
    }

    public static CxlRejReasonValue parse(int value) {
        for (CxlRejReasonValue type : CxlRejReasonValue.values()) {
            if (type.value == value)
                return type;
        }
        throw new InvalidValueForTagException(Integer.toString(value));
    }
}
