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

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

import fixengine.Config;
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

    public void load(Session session) {
        BasicDBObject doc = (BasicDBObject)sessions().findOne(sessionQuery(session));
        if (doc != null) {
          session.setOutgoingSeq(outgoingSeq(doc));
          session.setIncomingSeq(incomingSeq(doc));
        }
    }

    public void resetOutgoingSeq(String senderCompId, String targetCompId, Sequence incomingSeq, Sequence outgoingSeq) {
        BasicDBObject query = sessionQuery(senderCompId, targetCompId);
        BasicDBObject doc = sessionDoc(senderCompId, targetCompId, incomingSeq, outgoingSeq);
        sessions().update(query, doc, true, false);
    }

    private DBCollection sessions() {
        return db.getCollection("sessions");
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
