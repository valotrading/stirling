/*
 * Copyright 2008 the original author or authors.
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

/**
 * @author Pekka Enberg 
 */
public enum OrdRejReasonValue implements Formattable {
    BROKER_EXCHANGE_OPTION(0),
    UNKNOWN_SYMBOL(1),
    EXCHANGE_CLOSED(2),
    ORDER_EXCEEDS_LIMIT(3),
    TOO_LATE_TO_ENTER(4),
    UNKNOWN_ORDER(5),
    DUPLICATE_ORDER(6),
    DUPLICATE_OF_VERBALLY_COMMUNICATED_ORDER(7),
    STALE_ORDER(8),
    TRADE_ALONG_REQUIRED(9),
    INVALID_INVESTOR_ID(10),
    UNSUPPORTED_ORDER_CHARACTERISTIC(11),
    SURVEILLANCE_OPTION(12);

    private int value;
    
    OrdRejReasonValue(int value) {
        this.value = value;
    }

    @Override public String value() {
        return Integer.toString(value);
    }

    public static OrdRejReasonValue parse(int value) {
        for (OrdRejReasonValue type : OrdRejReasonValue.values()) {
            if (type.value == value)
                return type;
        }
        throw new InvalidValueForTagException(Integer.toString(value));
    }
}
