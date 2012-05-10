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

import java.io.Closeable;
import java.util.List;

import stirling.fix.messages.Message;
import stirling.fix.session.Sequence;
import stirling.fix.session.Session;

/**
 * @author Karim Osman
 */
public interface SessionStore extends Closeable {
    void save(Session session);
    void saveOutgoingMessage(Session session, Message message);
    void saveIncomingMessage(Session session, Message message);
    void load(Session session);
    List<Message> getOutgoingMessages(Session session, int beginSeqNo, int endSeqNo);
    List<Message> getIncomingMessages(Session session, int beginSeqNo, int endSeqNo);
    void resetOutgoingSeq(Session session, Sequence incomingSeq, Sequence outgoingSeq);
    void clear(String senderCompId, String targetCompId);
    boolean isDuplicate(Session session, Message message);
}
