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

import fixengine.Version;

public class Parser {
    public static Message parse(silvertip.Message m) {
        return parse(m.toByteBuffer());
    }

    private static Message parse(ByteBuffer b) {
        MessageHeader header = parseHeader(b);
        return parseMessage(b, header);
    }

    public static MessageHeader parseHeader(ByteBuffer b) {
        String beginString = beginString(b);
        int bodyLength = bodyLength(b);
        int msgTypePosition = b.position();
        String msgTypeValue = msgType(b);
        MessageHeader header = new MessageHeader();
        int msgSeqNum = msgSeqNum(b, header);
        header.setBeginString(beginString);
        header.setBodyLength(bodyLength);
        header.setMsgType(msgTypeValue);
        header.setMsgSeqNum(msgSeqNum);
        header.setMsgTypePosition(msgTypePosition);
        MsgType msgType = MsgType.parse(msgTypeValue);
        if (msgType == null)
            return header;
        Field previous = new MsgTypeField();
        for (;;) {
            b.mark();
            Tag tag = parseTag(b, previous);
            Field field = header.lookup(tag);
            if (field == null)
                break;
            if (field.isParsed())
                throw new TagMultipleTimesException(field.prettyName() + ": Tag multiple times", header.getMsgSeqNum());
            String value = parseValue(b, field);
            field.parseValue(value);
            if (!field.isFormatValid())
                throw new InvalidValueFormatException(field.prettyName() + ": Invalid value format", header.getMsgSeqNum());
            if (!field.isValueValid())
                throw new InvalidValueException(field.prettyName() + ": Invalid value", header.getMsgSeqNum());
            previous = field;
        }
        b.reset();
        return header;
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

    public static Message parseMessage(ByteBuffer b, MessageHeader header) {
        if (MsgType.parse(header.getMsgType()) == null)
            return new UnknownMessage(header);
        Message msg = body(b, header);
        trailer(b, header);
        return msg;
    }

    private static String beginString(ByteBuffer b) {
        if (!BeginStringField.TAG.equals(parseTag(b, new BeginStringField())))
            throw new AssertionError();
        String beginString = parseValue(b, new BeginStringField());
        if (!Version.supports(beginString))
            throw new InvalidBeginStringException("BeginString(8): '" + beginString + "' is not supported");
        return beginString;
    }

    private static int bodyLength(ByteBuffer b) {
        if (!BodyLengthField.TAG.equals(parseTag(b, new BeginStringField())))
            throw new AssertionError();
        return Integer.parseInt(parseValue(b, new BodyLengthField()));
    }

    private static String msgType(ByteBuffer b) {
        if (!MsgTypeField.TAG.equals(parseTag(b, new BodyLengthField())))
            throw new AssertionError();
        return parseValue(b, new MsgTypeField());
    }

    private static Message body(ByteBuffer b, MessageHeader header) {
        Message msg = MsgType.parse(header.getMsgType()).newMessage(header);
        msg.setBeginString(header.getBeginString());
        Field previous = new MsgTypeField();
        for (;;) {
            b.mark();
            Tag tag = parseTag(b, previous);
            if (CheckSumField.TAG.equals(tag))
                break;
            Field field = msg.lookup(tag);
            if (field.isParsed())
                throw new TagMultipleTimesException(field.prettyName() + ": Tag multiple times", header.getMsgSeqNum());
            String value = parseValue(b, field);
            field.parseValue(value);
            if (!field.isFormatValid())
                throw new InvalidValueFormatException(field.prettyName() + ": Invalid value format", header.getMsgSeqNum());
            if (!field.isValueValid())
                throw new InvalidValueException(field.prettyName() + ": Invalid value", header.getMsgSeqNum());
            previous = field;
        }
        b.reset();
        return msg;
    }

    private static void trailer(ByteBuffer b, MessageHeader header) {
        int pos = b.position() - header.getMsgTypePosition();
        if (pos != header.getBodyLength())
            throw new InvalidBodyLengthException("Expected: " + header.getBodyLength() + ", but was: " + pos);
        int expected = checksum(b, b.position());
        if (!CheckSumField.TAG.equals(parseTag(b, null)))
            throw new AssertionError();
        int checksum = Integer.parseInt(parseValue(b, new CheckSumField()));
        if (checksum != expected)
            throw new InvalidCheckSumException("Invalid checksum: Expected: " + expected + ", but was: " + checksum);
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
        return new Tag(Integer.parseInt(s));
    }

    private static String parseValue(ByteBuffer b, Field field) {
        StringBuilder result = new StringBuilder();
        for (;;) {
            int ch = b.get();
            if (ch == Field.DELIMITER)
                break;
            result.append((char) ch);
        }
        if (result.length() == 0)
            throw new EmptyTagException(field.prettyName() + ": Empty tag");
        return result.toString();
    }
}
