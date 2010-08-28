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

import org.apache.commons.lang.CharUtils;

/**
 * @author Pekka Enberg
 */
public enum MsgTypeValue {
    HEARTBEAT("0"),
    TEST_REQUEST("1"),
    RESEND_REQUEST("2"),
    REJECT("3"),
    SEQUENCE_RESET("4"),
    LOGOUT("5"),
    EXECUTION_REPORT("8"),
    ORDER_CANCEL_REJECT("9"),
    LOGON("A"),
    NEW_ORDER_SINGLE("D"),
    ORDER_CANCEL_REQUEST("F"),
    ORDER_MODIFICATION_REQUEST("G"),
    BUSINESS_MESSAGE_REJECT("j"),
    ALLOCATION_INSTRUCTION("J");

    private String value;

    MsgTypeValue(String value) {
        this.value = value;
    }

    public Message newMessage(MessageFactory factory, MessageHeader header) {
        return factory.create(this, header);
    }

    public String value() {
        return value;
    }

    public static MsgTypeValue parse(String value) {
        if (!isValid(value))
            throw new InvalidMsgTypeException("MsgType(35): Invalid message type: " + value);
        for (MsgTypeValue type : MsgTypeValue.values()) {
            if (type.value.equals(value))
                return type;
        }
        throw new UnsupportedMsgTypeException("MsgType(35): Unknown message type: " + value);
    }

    private static boolean isValid(String msgType) {
        if (msgType.length() == 1) {
            return isValidSingle(msgType);
        } else if (msgType.length() == 2) {
            return isValidWide(msgType);
        }
        return false;
    }

    private static boolean isValidSingle(String msgType) {
        char first = msgType.charAt(0);
        return CharUtils.isAsciiAlphanumeric(first);
    }

    private static boolean isValidWide(String msgType) {
        char first = msgType.charAt(0);
        if (first != 'A')
            return false;

        char second = msgType.charAt(1);
        if (!CharUtils.isAsciiAlphaUpper(second))
            return false;

        return second >= 'A' && second <= 'I';
    }
}
