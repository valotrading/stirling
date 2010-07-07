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

import fixengine.tags.BeginString;
import fixengine.tags.BodyLength;
import fixengine.tags.MsgType;

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
            header.parse(b);
            header.validate();
            Message msg = header.newMessage();
            msg.parse(b);
            msg.validate();
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
        StringField field = BeginString.TAG.parse(b, BeginString.TAG);
        return field.getValue();
    }

    private static int bodyLength(ByteBuffer b) {
        IntegerField field = BodyLength.TAG.parse(b, BeginString.TAG);
        if (!field.isFormatValid())
            throw new BodyLengthMissingException("BodyLength(9): Invalid value format");
        if (field.isEmpty())
            throw new BodyLengthMissingException("BodyLength(9): Empty tag");
        return field.getValue();
    }

    private static String msgType(ByteBuffer b) {
        StringField field = MsgType.TAG.parse(b, BodyLength.TAG);
        if (field.isEmpty())
            throw new MsgTypeMissingException("MsgType(35): is missing");
        return field.getValue();
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
