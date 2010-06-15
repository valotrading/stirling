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
import com.mongodb.DBObject;
import com.mongodb.Mongo;

import fixengine.messages.Session;
import fixengine.session.Sequence;

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
        if (doc != null) session.setOutgoingSeq(outgoingSeq(doc));
    }

    public void delete() {
        sessions().drop();
    }

    private DBCollection sessions() {
        return db.getCollection("sessions");
    }

    private BasicDBObject sessionQuery(Session session) {
        BasicDBObject query = new BasicDBObject();
        query.put("senderCompId", session.getConfig().getSenderCompId());
        query.put("targetCompId", session.getConfig().getTargetCompId());
        return query;
    }

    private BasicDBObject sessionDoc(Session session) {
        BasicDBObject doc = new BasicDBObject();
        doc.put("outgoingSeq", session.getOutgoingSeq().peek());
        doc.put("senderCompId", session.getConfig().getSenderCompId());
        doc.put("targetCompId", session.getConfig().getTargetCompId());
        return doc;
    }

    private Sequence outgoingSeq(BasicDBObject sessionDoc) {
        Sequence seq = new Sequence();
        seq.reset(sessionDoc.getInt("outgoingSeq"));
        return seq;
    }
}
