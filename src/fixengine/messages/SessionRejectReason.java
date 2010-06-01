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
public enum SessionRejectReason {
    /** Invalid tag number.  */
    INVALID_TAG_NUMBER(0),

    /** Required tag missing.  */
    TAG_MISSING(1),

    /** Tag not defined for this message type.  */
    INVALID_TAG(2),

    /** Undefined tag.  */
    UNDEFINED_TAG(3),

    /** Tag specified without value.  */
    EMPTY_TAG(4),
    
    /** Value is incorrect (out of range) for this tag.  */
    INVALID_VALUE(5),

    /** Incorrect data format for value.  */
    INVALID_VALUE_FORMAT(6),
    
    /** Decryption problem.  */
    DECRYPTION_PROBLEM(7),
    
    /** Signature problem.  */
    SIGNATURE_PROBLEM(8),
    
    /** CompId problem.  */
    COMP_ID_PROBLEM(9),

    /** SendingTime accuracy problem.  */
    SENDING_TIME_ACCURACY_PROBLEM(10),
    
    /** Invalid MsgType.  */
    INVALID_MSG_TYPE(11),
    
    /** XML validation error.  */
    XML_VALIDATION_ERROR(12),
    
    /** Tag appears more than once.  */
    TAG_MULTIPLE_TIMES(13),
    
    /** Tag specified out of required order.  */
    OUT_OF_ORDER_TAG(14),
    
    /** Repeating group fields out of order.  */
    OUT_OF_ORDER_GROUP_FIELD(15),
    
    /** Incorrect NumInGroup count for repeating group.  */
    NUM_IN_GROUP_MISMATCH(16),
    
    /** Non "data" value includes field delimiter (SOH character).  */
    FIELD_DELIMITER_IN_VALUE(17);

    private final int value;

    SessionRejectReason(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static SessionRejectReason parse(int value) {
        for (SessionRejectReason type : SessionRejectReason.values()) {
            if (type.value == value)
                return type;
        }
        throw new InvalidValueForTagException(Integer.toString(value));
    }
}