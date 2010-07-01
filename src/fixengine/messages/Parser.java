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
        Message result = header(b);
        body(b, result);
        trailer(b);
        return result;
    }

    private static Message header(ByteBuffer b) throws AssertionError {
        String beginString = beginString(b);
        bodyLength(b);
        MsgType type = msgType(b);
        Message msg = type.newMessage(new MessageHeader(type));
        msg.setBeginString(beginString);
        return msg;
    }

    private static String beginString(ByteBuffer b) throws AssertionError {
        if (!BeginStringField.TAG.equals(parseTag(b, new BeginStringField())))
            throw new AssertionError();
        String beginString = parseValue(b, new BeginStringField());
        if (!Version.supports(beginString))
            throw new InvalidBeginStringException("BeginString(8): '" + beginString + "' is not supported");
        return beginString;
    }

    private static void bodyLength(ByteBuffer b) throws AssertionError {
        if (!BodyLengthField.TAG.equals(parseTag(b, new BeginStringField())))
            throw new AssertionError();
        parseValue(b, new BodyLengthField());
    }

    private static MsgType msgType(ByteBuffer b) throws AssertionError {
        if (!MsgTypeField.TAG.equals(parseTag(b, new BodyLengthField())))
            throw new AssertionError();
        String s = parseValue(b, new MsgTypeField());
        MsgType type = MsgType.parse(s);
        if (type == null)
            throw new InvalidMsgTypeException("MsgType(35): '" + s + "' is not supported");
        return type;
    }

    private static void body(ByteBuffer b, Message msg) {
        Field previous = new MsgTypeField();
        for (;;) {
            b.mark();
            Tag tag = parseTag(b, previous);
            if (CheckSumField.TAG.equals(tag))
                break;
            Field field = msg.lookup(tag);
            if (field.isParsed())
                throw new TagMultipleTimesException(field.prettyName() + ": Tag multiple times");
            String value = parseValue(b, field);
            field.parseValue(value);
            if (!field.isFormatValid())
                throw new InvalidValueFormatException(field.prettyName() + ": Invalid value format");
            if (!field.isValueValid())
                throw new InvalidValueException(field.prettyName() + ": Invalid value");
            previous = field;
        }
        b.reset();
    }

    private static void trailer(ByteBuffer b) throws AssertionError {
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
