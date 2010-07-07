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
public enum MessageType {
    HEARTBEAT("0") {
        @Override public Message newMessage(MessageHeader header) {
            return new HeartbeatMessage(header);
        }
    },
    TEST_REQUEST("1") {
        @Override public Message newMessage(MessageHeader header) {
            return new TestRequestMessage(header);
        }
    },
    RESEND_REQUEST("2") {
        @Override public Message newMessage(MessageHeader header) {
            return new ResendRequestMessage(header);
        }
    },
    REJECT("3") {
        @Override public Message newMessage(MessageHeader header) {
            return new RejectMessage(header);
        }
    },
    SEQUENCE_RESET("4") {
        @Override public Message newMessage(MessageHeader header) {
            return new SequenceResetMessage(header);
        }
    },
    LOGOUT("5") {
        @Override public Message newMessage(MessageHeader header) {
            return new LogoutMessage(header);
        }
    },
    EXECUTION_REPORT("8") {
        @Override public Message newMessage(MessageHeader header) {
            return new ExecutionReportMessage(header);
        }
    },
    ORDER_CANCEL_REJECT("9") {
        @Override public Message newMessage(MessageHeader header) {
            return new OrderCancelRejectMessage(header);
        }
    },
    LOGON("A") {
        @Override public Message newMessage(MessageHeader header) {
            return new LogonMessage(header);
        }
    },
    NEW_ORDER_SINGLE("D") {
        @Override public Message newMessage(MessageHeader header) {
            return new NewOrderSingleMessage(header);
        }
    },
    ORDER_CANCEL_REQUEST("F") {
        @Override public Message newMessage(MessageHeader header) {
            return new OrderCancelRequestMessage(header);
        }
    },
    ORDER_MODIFICATION_REQUEST("G") {
        @Override public Message newMessage(MessageHeader header) {
            return new OrderModificationRequestMessage(header);
        }
    },
    BUSINESS_MESSAGE_REJECT("j") {
        @Override public Message newMessage(MessageHeader header) {
            return new BusinessMessageRejectMessage(header);
        }
    };

    private String value;

    MessageType(String value) {
        this.value = value;
    }

    public abstract Message newMessage(MessageHeader header);

    public String value() {
        return value;
    }

    public static MessageType parse(String value) {
        if (!isValid(value))
            throw new InvalidMsgTypeException("MsgType(35): Invalid message type: " + value);
        for (MessageType type : MessageType.values()) {
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