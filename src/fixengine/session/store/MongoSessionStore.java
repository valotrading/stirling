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
package fixengine.session.store;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;

import fixengine.Config;
import fixengine.messages.FixMessage;
import fixengine.messages.Message;
import fixengine.messages.Parser;
import fixengine.messages.SessionRejectReasonValue;
import fixengine.session.Sequence;
import fixengine.session.Session;

/**
 * @author Karim Osman
 */
public class MongoSessionStore implements SessionStore {
    private DB db;

    public MongoSessionStore(String address, int port) throws Exception {
        db = new Mongo(address, port).getDB("fixengine");
    }

    public void save(Session session) {
        sessions().update(sessionQuery(session), sessionDoc(session), true, false);
    }

    @Override public void saveOutgoingMessage(Session session, Message message) {
        save(session);
        outgoingMessages().insert(messageDoc(message));
    }

    public void load(Session session) {
        BasicDBObject doc = (BasicDBObject)sessions().findOne(sessionQuery(session));
        if (doc != null) {
            session.setOutgoingSeq(outgoingSeq(doc));
            session.setIncomingSeq(incomingSeq(doc));
        }
    }

    @Override public List<Message> load(Session session, int beginSeqNo, int endSeqNo) {
        final List<Message> messages = new ArrayList<Message>();
        DBCursor cursor = outgoingMessages().find(messageQuery(session));
        while (cursor.hasNext()) {
            BasicDBObject doc = (BasicDBObject) cursor.next();
            int msgSeqNum = doc.getInt("msgSeqNum");
            if (msgSeqNum < beginSeqNo)
                continue;
            if (endSeqNo > 0 && msgSeqNum > endSeqNo)
                continue;
            Parser.parse(session.getMessageFactory(), message(doc), new Parser.Callback() {
                @Override public void message(Message message) {
                    messages.add(message);
                }

                @Override public void invalidMessage(int msgSeqNum, SessionRejectReasonValue reason, String text) {
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
        return messages;
    }

    public void resetOutgoingSeq(String senderCompId, String targetCompId, Sequence incomingSeq, Sequence outgoingSeq) {
        BasicDBObject query = sessionQuery(senderCompId, targetCompId);
        BasicDBObject doc = sessionDoc(senderCompId, targetCompId, incomingSeq, outgoingSeq);
        sessions().update(query, doc, true, false);
    }

    @Override public void clear(String senderCompId, String targetCompId) {
        DBCursor cursor = outgoingMessages().find(messageQuery(senderCompId, targetCompId));
        while (cursor.hasNext()) {
            BasicDBObject doc = (BasicDBObject) cursor.next();
            outgoingMessages().remove(doc);
        }
        cursor = sessions().find(sessionQuery(senderCompId, targetCompId));
        while (cursor.hasNext()) {
            BasicDBObject doc = (BasicDBObject) cursor.next();
            sessions().remove(doc);
        }
    }

    private DBCollection sessions() {
        return db.getCollection("sessions");
    }

    private DBCollection outgoingMessages() {
        return db.getCollection("outgoingMessages");
    }

    private BasicDBObject sessionQuery(Session session) {
        Config config = session.getConfig();
        return sessionQuery(config.getSenderCompId(), config.getTargetCompId());
    }

    private BasicDBObject sessionQuery(String senderCompId, String targetCompId) {
        BasicDBObject query = new BasicDBObject();
        query.put("senderCompId", senderCompId);
        query.put("targetCompId", targetCompId);
        return query;
    }

    private BasicDBObject sessionDoc(Session session) {
        Config config = session.getConfig();
        return sessionDoc(config.getSenderCompId(), config.getTargetCompId(), session.getIncomingSeq(), session.getOutgoingSeq());
    }

    private BasicDBObject sessionDoc(String senderCompId, String targetCompId, Sequence incomingSeq, Sequence outgoingSeq) {
        BasicDBObject doc = new BasicDBObject();
        doc.put("outgoingSeq", outgoingSeq.peek());
        doc.put("incomingSeq", incomingSeq.peek());
        doc.put("senderCompId", senderCompId);
        doc.put("targetCompId", targetCompId);
        return doc;
    }

    private BasicDBObject messageDoc(Message message) {
        BasicDBObject doc = new BasicDBObject();
        doc.put("senderCompId", message.getSenderCompId());
        doc.put("targetCompId", message.getTargetCompId());
        doc.put("msgType", message.getMsgType());
        doc.put("msgSeqNum", message.getMsgSeqNum());
        doc.put("data", message.format());
        return doc;
    }

    private BasicDBObject messageQuery(Session session) {
        return messageQuery(session.getConfig().getSenderCompId(), session.getConfig().getTargetCompId());
    }

    private BasicDBObject messageQuery(String senderCompId, String targetCompId) {
        BasicDBObject doc = new BasicDBObject();
        doc.put("senderCompId", senderCompId);
        doc.put("targetCompId", targetCompId);
        return doc;
    }

    private FixMessage message(BasicDBObject doc) {
        return FixMessage.fromString(doc.getString("data"), doc.getString("msgType"));
    }

    private Sequence outgoingSeq(BasicDBObject sessionDoc) {
        Sequence seq = new Sequence();
        seq.reset(sessionDoc.getInt("outgoingSeq"));
        return seq;
    }

    private Sequence incomingSeq(BasicDBObject sessionDoc) {
        Sequence seq = new Sequence();
        seq.reset(sessionDoc.getInt("incomingSeq"));
        return seq;
    }
}
