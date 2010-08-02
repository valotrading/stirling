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
import fixengine.tags.CheckSum;
import fixengine.tags.MsgSeqNum;
import fixengine.tags.MsgType;

public class Parser {
    public interface Callback {
        void message(Message m);
        void invalidMessage(int msgSeqNum, SessionRejectReasonValue reason, String text);
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
            header.parse(b);
            header.validate();
            Message msg = header.newMessage();
            msg.parse(b);
            msg.validate();
            trailer(b, header);
            callback.message(msg);
        } catch (InvalidMsgTypeException e) {
            callback.invalidMsgType(header.getMsgType(), header.getInteger(MsgSeqNum.TAG));
        } catch (UnsupportedMsgTypeException e) {
            callback.unsupportedMsgType(header.getMsgType(), header.getInteger(MsgSeqNum.TAG));
        } catch (GarbledMessageException e) {
            callback.garbledMessage(e.getMessage());
        } catch (ParseException e) {
            callback.invalidMessage(header.getInteger(MsgSeqNum.TAG), e.getReason(), e.getMessage());
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
        StringField field;
        try {
            field = BeginString.TAG.parse(b, BeginString.TAG);
        } catch (UnexpectedTagException e) {
            throw new BeginStringMissingException(BeginString.TAG.prettyName() + ": is missing");
        }
        return field.getValue();
    }

    private static int bodyLength(ByteBuffer b) {
        IntegerField field;
        try {
            field = BodyLength.TAG.parse(b, BeginString.TAG);
        } catch (UnexpectedTagException e) {
            throw new MsgTypeMissingException(BodyLength.TAG.prettyName() + ": is missing");
        }
        if (!field.isFormatValid())
            throw new BodyLengthMissingException(BodyLength.TAG.prettyName() + ": Invalid value format");
        if (field.isEmpty())
            throw new BodyLengthMissingException(BodyLength.TAG.prettyName() + ": Empty tag");
        return field.getValue();
    }

    private static String msgType(ByteBuffer b) {
        StringField field;
        try {
            field = MsgType.TAG.parse(b, BodyLength.TAG);
        } catch (UnexpectedTagException e) {
            throw new MsgTypeMissingException(MsgType.TAG.prettyName() + ": is missing");
        }
        if (field.isEmpty())
            throw new MsgTypeMissingException(MsgType.TAG.prettyName() + ": Empty tag");
        return field.getValue();
    }

    private static void trailer(ByteBuffer b, MessageHeader header) {
        int pos = b.position() - header.getMsgTypePosition();
        int expected = Checksums.checksum(b, b.position());
        StringField field;
        try {
            field = CheckSum.TAG.parse(b, null);
        } catch (UnexpectedTagException e) {
            Field f = header.lookup(e.getTag());
            if (f != null)
                throw new OutOfOrderTagException(f.prettyName() + ": Out of order tag");
            throw new InvalidTagException("Tag not defined for this message: " + e.getTag().value());
        }
        if (pos != header.getBodyLength())
            throw new InvalidBodyLengthException("BodyLength(9): Expected: " + header.getBodyLength() + ", but was: " + pos);
        int checksum = Integer.parseInt(field.getValue());
        if (checksum != expected)
            throw new InvalidCheckSumException("CheckSum(10): Expected: " + expected + ", but was: " + checksum);
    }
}
