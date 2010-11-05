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
    private Map<String, Class<? extends Message>> messageTypes = new HashMap<String, Class<? extends Message>>();

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

    @Override public Message create(String msgType) {
        return create(msgType, new MessageHeader(msgType));
    }

    @Override public Message create(String msgType, MessageHeader header) {
        if (!isValid(msgType))
            throw new InvalidMsgTypeException("MsgType(35): Invalid message type: " + msgType);
        if (!messageTypes.containsKey(msgType))
            throw new UnsupportedMsgTypeException("MsgType(35): Unknown message type: " + msgType);
        try {
            return constructor(msgType).newInstance(header);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override public Tag<?> createTag(String tagName) {
        try {
            return tagClass(tagName).newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Tag not found: " + tagName);
        }
    }

    @SuppressWarnings("unchecked") private Class<Tag<?>> tagClass(String tagName) {
        try {
            try {
                return (Class<Tag<?>>) Class.forName(getTagsPackage() + "." + tagName);
            } catch (ClassNotFoundException e) {
                return (Class<Tag<?>>) Class.forName(getDefaultTagsPackage() + "." + tagName);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String getTagsPackage() {
      return getDefaultTagsPackage();
    }

    private String getDefaultTagsPackage() {
      return "fixengine.tags";
    }

    protected void message(String msgType, Class<? extends Message> clazz) {
        messageTypes.put(msgType, clazz);
    }

    private Constructor<? extends Message> constructor(String msgType) throws NoSuchMethodException {
        return messageClass(msgType).getDeclaredConstructor(MessageHeader.class);
    }

    private Class<? extends Message> messageClass(String msgType) {
        return messageTypes.get(msgType);
    }

    protected boolean isValid(String msgType) {
        if (msgType.length() == 1) {
            return isValidSingle(msgType);
        } else if (msgType.length() == 2) {
            return isValidWide(msgType);
        }
        return false;
    }

    private boolean isValidSingle(String msgType) {
        char first = msgType.charAt(0);
        return CharUtils.isAsciiAlphanumeric(first);
    }

    private boolean isValidWide(String msgType) {
        char first = msgType.charAt(0);
        if (first != 'A')
            return false;

        char second = msgType.charAt(1);
        if (!CharUtils.isAsciiAlphaUpper(second))
            return false;

        return second >= 'A' && second <= 'I';
    }
}
