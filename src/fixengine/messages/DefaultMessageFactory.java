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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.CharUtils;
import java.lang.reflect.Constructor;

public class DefaultMessageFactory implements MessageFactory {
    private Map<MsgTypeValue, Class<? extends Message>> messageTypes = new HashMap<MsgTypeValue, Class<? extends Message>>();

    public DefaultMessageFactory() {
        message(MsgTypeValue.LOGON, LogonMessage.class);
        message(MsgTypeValue.LOGOUT, LogoutMessage.class);
        message(MsgTypeValue.HEARTBEAT, HeartbeatMessage.class);
        message(MsgTypeValue.RESEND_REQUEST, ResendRequestMessage.class);
        message(MsgTypeValue.SEQUENCE_RESET, SequenceResetMessage.class);
        message(MsgTypeValue.TEST_REQUEST, TestRequestMessage.class);
        message(MsgTypeValue.REJECT, RejectMessage.class);
        message(MsgTypeValue.BUSINESS_MESSAGE_REJECT, BusinessMessageRejectMessage.class);
        message(MsgTypeValue.EXECUTION_REPORT, ExecutionReportMessage.class);
        message(MsgTypeValue.ORDER_CANCEL_REJECT, OrderCancelRejectMessage.class);
        message(MsgTypeValue.NEW_ORDER_SINGLE, NewOrderSingleMessage.class);
        message(MsgTypeValue.ORDER_CANCEL_REQUEST, OrderCancelRequestMessage.class);
        message(MsgTypeValue.ORDER_MODIFICATION_REQUEST, OrderModificationRequestMessage.class);
        message(MsgTypeValue.ALLOCATION_INSTRUCTION, AllocationMessage.class);
    }

    @Override public Message create(MsgTypeValue type) {
        return create(type, new MessageHeader(type));
    }

    @Override public Message create(MsgTypeValue type, MessageHeader header) {
        if (!isValid(type.value()))
            throw new InvalidMsgTypeException("MsgType(35): Invalid message type: " + type.value());
        if (!messageTypes.containsKey(type))
            throw new UnsupportedMsgTypeException("MsgType(35): Unknown message type: " + type.value());
        try {
            return constructor(type).newInstance(header);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override public String getTagsPackage() {
      return "fixengine.tags";
    }

    protected void message(MsgTypeValue type, Class<? extends Message> clazz) {
        messageTypes.put(type, clazz);
    }

    private Constructor<? extends Message> constructor(MsgTypeValue type) throws NoSuchMethodException {
        return messageClass(type).getDeclaredConstructor(MessageHeader.class);
    }

    private Class<? extends Message> messageClass(MsgTypeValue type) {
        return messageTypes.get(type);
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
