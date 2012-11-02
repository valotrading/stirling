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
package stirling.fix.messages.fix42;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.lang.CharUtils;

import stirling.fix.messages.Heartbeat;
import stirling.fix.messages.InvalidMsgTypeException;
import stirling.fix.messages.Logout;
import stirling.fix.messages.Message;
import stirling.fix.messages.MessageFactory;
import stirling.fix.messages.MessageHeader;
import stirling.fix.messages.ResendRequest;
import stirling.fix.messages.SequenceReset;
import stirling.fix.messages.Tag;
import stirling.fix.messages.TestRequest;
import stirling.fix.messages.UnsupportedMsgTypeException;
import stirling.fix.messages.fix42.BusinessMessageReject;
import stirling.fix.messages.fix42.MsgTypeValue;
import stirling.fix.messages.fix42.Reject;

import java.lang.reflect.Constructor;

public class DefaultMessageFactory implements MessageFactory {
    private Map<String, Class<? extends Message>> messageTypes = new HashMap<String, Class<? extends Message>>();

    public DefaultMessageFactory() {
        message(MsgTypeValue.LOGON, Logon.class);
        message(MsgTypeValue.LOGOUT, Logout.class);
        message(MsgTypeValue.HEARTBEAT, Heartbeat.class);
        message(MsgTypeValue.RESEND_REQUEST, ResendRequest.class);
        message(MsgTypeValue.SEQUENCE_RESET, SequenceReset.class);
        message(MsgTypeValue.TEST_REQUEST, TestRequest.class);
        message(MsgTypeValue.REJECT, Reject.class);
        message(MsgTypeValue.BUSINESS_MESSAGE_REJECT, BusinessMessageReject.class);
        message(MsgTypeValue.EXECUTION_REPORT, ExecutionReport.class);
        message(MsgTypeValue.ORDER_CANCEL_REJECT, OrderCancelReject.class);
        message(MsgTypeValue.NEW_ORDER_SINGLE, NewOrderSingle.class);
        message(MsgTypeValue.ORDER_CANCEL_REQUEST, OrderCancelRequest.class);
        message(MsgTypeValue.ORDER_STATUS_REQUEST, OrderStatusRequest.class);
        message(MsgTypeValue.ALLOCATION_INSTRUCTION, Allocation.class);
    }

    @Override public Message create(String msgType) {
        MessageHeader header = createHeader();
        header.setMsgType(msgType);
        return create(msgType, header);
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

    @Override public MessageHeader createHeader() {
        return new DefaultMessageHeader();
    }

    @Override public Tag<?> createTag(String tagName) {
        try {
            return (Tag<?>) tagClass(tagName).getMethod("Tag").invoke(null);
        } catch (Exception e) {
            throw new IllegalArgumentException("Tag not found: " + tagName);
        }
    }

    @Override public String getProfile() {
        return "default";
    }

    @SuppressWarnings("unchecked") private Class<Tag<?>> tagClass(String tagName) {
        for (String tagsPackage : getTagsPackages()) {
            try {
                return (Class<Tag<?>>) Class.forName(tagsPackage + "." + tagName);
            } catch (ClassNotFoundException e) {
            }
        }
        throw new RuntimeException("Tag not found");
    }

    protected String getTagsPackage() {
        return getClass().getPackage().getName().replaceAll("^stirling.fix.messages", "stirling.fix.tags");
    }

    protected void message(String msgType, Class<? extends Message> clazz) {
        messageTypes.put(msgType, clazz);
    }

    private List<String> getTagsPackages() {
        List<String> packages = new ArrayList<String>();
        packages.add(getTagsPackage());
        packages.add("stirling.fix.tags.fix42");
        packages.add("stirling.fix.tags.fix43");
        packages.add("stirling.fix.tags.fix44");
        packages.add("stirling.fix.tags");
        return packages;
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

    protected boolean isSupported(String msgType) {
        return messageTypes.containsKey(msgType);
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
