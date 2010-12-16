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

import org.joda.time.DateTime;
import java.nio.ByteBuffer;

import fixengine.tags.MsgSeqNum;
import fixengine.messages.FixMessage;
import fixengine.messages.fix42.DefaultMessageFactory;

public class Parser {
    public interface Callback {
        void message(Message m);
        void invalidMessage(int msgSeqNum, Value<Integer> reason, String text);
        void unsupportedMsgType(String msgType, int msgSeqNum);
        void invalidMsgType(String msgType, int msgSeqNum);
    }

    public static void parse(FixMessage m, Callback callback) {
        parse(new DefaultMessageFactory(), m, callback);
    }

    public static void parse(MessageFactory messageFactory, FixMessage m, Callback callback) {
        parse(messageFactory, m.toByteBuffer(), callback, m.getReceiveTime());
    }

    private static void parse(MessageFactory messageFactory, ByteBuffer b, Callback callback, DateTime receiveTime) {
        MessageHeader header = null;
        try {
            header = new MessageHeader();
            header.parse(b);
            header.validate();
            Message msg = header.newMessage(messageFactory);
            msg.parse(b);
            msg.validate();
            msg.setReceiveTime(receiveTime);
            callback.message(msg);
        } catch (InvalidMsgTypeException e) {
            callback.invalidMsgType(header.getMsgType(), header.getInteger(MsgSeqNum.Tag()));
        } catch (UnsupportedMsgTypeException e) {
            callback.unsupportedMsgType(header.getMsgType(), header.getInteger(MsgSeqNum.Tag()));
        } catch (ParseException e) {
            callback.invalidMessage(header.getInteger(MsgSeqNum.Tag()), e.getReason(), e.getMessage());
        }
    }

    public static int parseMsgSeqNum(FixMessage message) {
        String value = parseField(message, MsgSeqNum.Tag());
        IntegerField field = new IntegerField(MsgSeqNum.Tag());
        field.parse(value);
        if (!field.isFormatValid())
            throw new InvalidValueFormatException(MsgSeqNum.Tag().prettyName() + " has invalid value format: " + value);
        return field.intValue();
    }

    private static String parseField(FixMessage message, Tag tagToFind) {
        ByteBuffer b = message.toByteBuffer();
        while (b.hasRemaining()) {
            try {
                int tag = Tag.parseTag(b);
                String value = AbstractField.parseValue(b);
                if (tag == tagToFind.value())
                  return value;
            } catch (NonDataValueIncludesFieldDelimiterException e) {
                /* Ignore tag that cannot be parsed due to delimiter in tag */
            }
        }
        throw new ParseException(tagToFind.prettyName() + " is missing");
    }
}
