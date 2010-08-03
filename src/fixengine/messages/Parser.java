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
            header = new MessageHeader();
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

    private static void trailer(ByteBuffer b, MessageHeader header) {
        int pos = b.position() - header.getMsgTypePosition();
        int expected = Checksums.checksum(b, b.position());
        StringField field;
        try {
            CheckSum.TAG.parse(b);
            field = MsgType.TAG.newField(Required.YES);
            field.parse(b);
        } catch (UnexpectedTagException e) {
            Field f = header.lookup(e.getTag());
            if (f != null)
                throw new OutOfOrderTagException(f.prettyName() + ": Out of order tag");
            throw new InvalidTagException("Tag not defined for this message: " + e.getTag());
        }
        if (pos != header.getBodyLength())
            throw new InvalidBodyLengthException("BodyLength(9): Expected: " + header.getBodyLength() + ", but was: " + pos);
        int checksum = Integer.parseInt(field.getValue());
        if (checksum != expected)
            throw new InvalidCheckSumException("CheckSum(10): Expected: " + expected + ", but was: " + checksum);
    }
}
