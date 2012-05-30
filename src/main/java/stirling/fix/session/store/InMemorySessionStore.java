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
package stirling.fix.session.store;

import java.util.LinkedList;
import java.util.List;

import stirling.fix.messages.FixMessage;
import stirling.fix.messages.Message;
import stirling.fix.messages.Parser;
import stirling.fix.messages.Value;
import stirling.fix.session.Sequence;
import stirling.fix.session.Session;

public class InMemorySessionStore implements SessionStore {
    private List<Message> outgoingMessages = new LinkedList<Message>();
    private List<Message> incomingMessages = new LinkedList<Message>();

    @Override public void load(Session session) {
    }

    @Override public List<Message> getOutgoingMessages(Session session, int beginSeqNo, int endSeqNo) {
        return getRange(session, outgoingMessages, beginSeqNo, endSeqNo);
    }

    @Override public List<Message> getIncomingMessages(Session session, int beginSeqNo, int endSeqNo) {
        return getRange(session, incomingMessages, beginSeqNo, endSeqNo);
    }

    private static List<Message> getRange(Session session, List<Message> messages, int beginSeqNo, int endSeqNo) {
        final List<Message> range = new LinkedList<Message>();
        for (Message message : messages) {
            if (message.getMsgSeqNum() < beginSeqNo)
                continue;
            if (endSeqNo > 0 && message.getMsgSeqNum() > endSeqNo)
                continue;
            Parser.parse(session.getMessageFactory(), FixMessage.fromString(message.format(), message.getMsgType()), new Parser.Callback() {
                @Override public void message(Message message) {
                    range.add(message);
                }

                @Override public void invalidMessage(int msgSeqNum, Value<Integer> reason, String text) {
                    throw new RuntimeException("Invalid message: " + text);
                }

                @Override public void unsupportedMsgType(String msgType, int msgSeqNum) {
                    throw new RuntimeException("Unsupported message type: " + msgType);
                }

                @Override public void invalidMsgType(String msgType, int msgSeqNum) {
                    throw new RuntimeException("Invalid message type: " + msgType);
                }
            });
        }
        return range;
    }

    @Override public void resetOutgoingSeq(Session session, Sequence incomingSeq, Sequence outgoingSeq) {
        session.setIncomingSeq(incomingSeq);
        session.setOutgoingSeq(outgoingSeq);
    }

    @Override public void save(Session session) {
    }

    @Override public void saveOutgoingMessage(Session session, Message message) {
        outgoingMessages.add(message);
    }

    @Override public void saveIncomingMessage(Session session, Message message) {
        incomingMessages.add(message);
    }

    @Override public void clear(String senderCompId, String targetCompId) {
        incomingMessages.clear();
        outgoingMessages.clear();
    }

    @Override public boolean isDuplicate(Session session, Message message) {
        for (Message storedMessage : incomingMessages) {
            if (session.getMessageComparator().equals(storedMessage, message))
                return true;
        }
        return false;
    }

    @Override public void close() {
    }
}
