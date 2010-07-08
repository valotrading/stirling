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
public enum CxlRejReason implements Formattable<CxlRejReason> {
    /** Too late to cancel.  */
    TOO_LATE_TO_CANCEL(0),

    /** Unknown order.  */
    UNKNOWN_ORDER(1),

    /** Broker/Exchange Option.  */
    BROKER_EXCHANGE_OPTION(2),

    /** Order already in Pending Cancel or Pending Replace status.  */
    PENDING_CANCEL_OR_REPLACE(3),
    
    /** Unable to process Order Mass Cancel Request.  */
    UNABLE_TO_PROCESS_REQUEST(4),

    /** OrigOrdModTime did not match last TransactTime of order. */
    TIME_MISMATCH(5),             

    /** Duplicate ClOrdID received.  */
    DUPLICATE_CL_ORD_ID(6);       

    private int value;
    
    CxlRejReason(int value) {
        this.value = value;
    }

    @Override public String value() {
        return Integer.toString(value);
    }

    public static CxlRejReason parse(int value) {
        for (CxlRejReason type : CxlRejReason.values()) {
            if (type.value == value)
                return type;
        }
        throw new InvalidValueForTagException(Integer.toString(value));
    }
}