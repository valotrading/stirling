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

import fixengine.tags.MsgSeqNum;

public class Parser {
    public interface Callback {
        void message(Message m);
        void invalidMessage(int msgSeqNum, SessionRejectReasonValue reason, String text);
        void unsupportedMsgType(String msgType, int msgSeqNum);
        void invalidMsgType(String msgType, int msgSeqNum);
        void garbledMessage(String text);
        void msgSeqNumMissing(String text);
    }

    public static void parse(silvertip.Message m, Callback callback) {
        parse(new DefaultMessageFactory(), m, callback);
    }

    private static void parse(ByteBuffer b, Callback callback) {
        parse(new DefaultMessageFactory(), b, callback);
    }

    public static void parse(MessageFactory messageFactory, silvertip.Message m, Callback callback) {
        parse(messageFactory, m.toByteBuffer(), callback);
    }

    private static void parse(MessageFactory messageFactory, ByteBuffer b, Callback callback) {
        MessageHeader header = null;
        try {
            header = new MessageHeader();
            header.parse(b);
            header.validate();
            Message msg = header.newMessage(messageFactory);
            msg.parse(b);
            msg.validate();
            callback.message(msg);
        } catch (MsgSeqNumMissingException e) {
            callback.msgSeqNumMissing(e.getMessage());
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
}
