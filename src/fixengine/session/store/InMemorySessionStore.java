/**
 * 
 */
package fixengine.session.store;

import fixengine.session.Sequence;
import fixengine.session.Session;

public class InMemorySessionStore implements SessionStore {
    @Override public void load(Session session) {
    }

    @Override public void resetOutgoingSeq(String senderCompId, String targetCompId, Sequence incomingSeq, Sequence outgoingSeq) {
    }

    @Override public void save(Session session) {
    }
}