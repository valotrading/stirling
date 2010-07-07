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

import java.nio.ByteBuffer;

public class Parser {
    public interface Callback {
        void message(Message m);
        void invalidMessage(int msgSeqNum, SessionRejectReason reason, String text);
        void unsupportedMsgType(String msgType, int msgSeqNum);
        void invalidMsgType(String msgType, int msgSeqNum);
        void garbledMessage(String text);
    }

    public static void parse(silvertip.Message m, Callback callback) {
        parse(m.toByteBuffer(), callback);
    }

    private static void parse(ByteBuffer b, Callback callback) {
        MessageHeader header = null;
        try {
            header = parseHeaderBegin(b);
            if (header == null)
                return;
            int msgSeqNum = msgSeqNum(b, header);
            header.setMsgSeqNum(msgSeqNum);
            header.parse(b);
            Message msg = header.newMessage();
            msg.parse(b);
            trailer(b, header);
            callback.message(msg);
        } catch (InvalidMsgTypeException e) {
            callback.invalidMsgType(header.getMsgType(), header.getMsgSeqNum());
        } catch (UnsupportedMsgTypeException e) {
            callback.unsupportedMsgType(header.getMsgType(), header.getMsgSeqNum());
        } catch (GarbledMessageException e) {
            callback.garbledMessage(e.getMessage());
        } catch (ParseException e) {
            callback.invalidMessage(header.getMsgSeqNum(), e.getReason(), e.getMessage());
        }
    }

    private static MessageHeader parseHeaderBegin(ByteBuffer b) {
        MessageHeader header = new MessageHeader();
        String beginString = beginString(b);
        int bodyLength = bodyLength(b);
        int msgTypePosition = b.position();
        String msgTypeValue = msgType(b);
        header.setBeginString(beginString);
        header.setBodyLength(bodyLength);
        header.setMsgType(msgTypeValue);
        header.setMsgTypePosition(msgTypePosition);
        return header;
    }

    private static String beginString(ByteBuffer b) {
        if (!BeginStringField.TAG.equals(parseTag(b, new BeginStringField())))
            throw new BeginStringMissingException("BeginString(8): is missing");
        return parseValue(b, new BeginStringField());
    }

    private static int bodyLength(ByteBuffer b) {
        if (!BodyLengthField.TAG.equals(parseTag(b, new BeginStringField())))
            throw new BodyLengthMissingException("BodyLength(9): is missing");
        String value = parseValue(b, new BodyLengthField());
        if (value.isEmpty())
            throw new BodyLengthMissingException("BodyLength(9): Empty tag");
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new BodyLengthMissingException("BodyLength(9): Invalid value format");
        }
    }

    private static String msgType(ByteBuffer b) {
        if (!MsgTypeField.TAG.equals(parseTag(b, new BodyLengthField())))
            throw new MsgTypeMissingException("MsgType(35): is missing");
        return parseValue(b, new MsgTypeField());
    }

    private static int msgSeqNum(ByteBuffer b, MessageHeader header) {
        int result = -1;
        b.mark();
        Field previous = new MsgTypeField();
        for (;;) {
            Tag tag = parseTag(b, previous);
            Field field = header.lookup(tag);
            if (field == null)
                break;
            String value = parseValue(b, field);
            if (MsgSeqNumField.TAG.equals(tag)) {
                result = Integer.parseInt(value);
                break;
            }
            previous = field;
        }
        b.reset();
        return result;
    }

    private static void trailer(ByteBuffer b, MessageHeader header) {
        int pos = b.position() - header.getMsgTypePosition();
        int expected = checksum(b, b.position());
        Tag tag = parseTag(b, null);
        if (!CheckSumField.TAG.equals(tag)) {
            Field field = header.lookup(tag);
            if (field != null)
                throw new OutOfOrderTagException(field.prettyName() + ": Out of order tag");
            throw new InvalidTagException("Tag not defined for this message: " + tag.value());
        }
        if (pos != header.getBodyLength())
            throw new InvalidBodyLengthException("BodyLength(9): Expected: " + header.getBodyLength() + ", but was: " + pos);
        int checksum = Integer.parseInt(parseValue(b, new CheckSumField()));
        if (checksum != expected)
            throw new InvalidCheckSumException("CheckSum(10): Expected: " + expected + ", but was: " + checksum);
    }

    private static int checksum(ByteBuffer b, int end) {
        int checksum = 0;
        for (int i = 0; i < end; i++) {
            checksum += b.get(i);
        }
        return checksum % 256;
    }

    private static Tag parseTag(ByteBuffer b, Field previous) {
        StringBuilder result = new StringBuilder();
        for (;;) {
            int ch = b.get();
            if (ch == '=')
                break;
            result.append((char) ch);
        }
        String s = result.toString();
        if (s.contains("" + Field.DELIMITER))
            throw new NonDataValueIncludesFieldDelimiterException(previous.prettyName() + ": Non-data value includes field delimiter (SOH character)");
        Tag tag = new Tag(Integer.parseInt(s));
        if (tag.isUserDefined())
            throw new InvalidTagNumberException("Invalid tag number: " + tag.value());
        return tag;
    }

    private static String parseValue(ByteBuffer b, Field field) {
        StringBuilder result = new StringBuilder();
        for (;;) {
            int ch = b.get();
            if (ch == Field.DELIMITER)
                break;
            result.append((char) ch);
        }
        return result.toString();
    }
}
