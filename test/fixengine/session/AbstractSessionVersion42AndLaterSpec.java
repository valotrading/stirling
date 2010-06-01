/*
 * Copyright 2009 the original author or authors.
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
package fixengine.session;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.jmock.Expectations;
import org.junit.runner.RunWith;

import fixengine.Config;
import fixengine.Version;
import fixengine.io.ObjectOutputStream;
import fixengine.messages.BusinessMessageRejectMessage;
import fixengine.messages.Message;
import fixengine.messages.RejectMessage;
import fixengine.messages.UnknownMessage;

/**
 * @author Pekka Enberg
 */
@RunWith(JDaveRunner.class)
public class AbstractSessionVersion42AndLaterSpec extends Specification<AbstractSession> {
    private static final String INITIATOR = "initiator";
    private static final String ACCEPTOR = "acceptor";
    private static final Config config = new Config().setSenderCompId(
            INITIATOR).setTargetCompId(ACCEPTOR).setVersion(
            Version.FIX_4_1);

    @SuppressWarnings("unchecked")
    private final ObjectOutputStream<Message> stream = mock(ObjectOutputStream.class);
    private final AbstractSession session = new AbstractSession(stream, config) {
        @Override
        public void logon() {
            authenticated = true;
        }
    };

    public class SessionThatReceivesUnknownMessage {
        private UnknownMessage unknown;

        public AbstractSession create() {
            unknown = new UnknownMessage("ZZ");
            unknown.setMsgSeqNum(1);
            session.logon();
            return session;
        }

        public void rejectsTheMessage() {
            checking(new Expectations() {{
                one(stream).writeObject(with(any(RejectMessage.class)));
            }});
            session.receive(unknown);
        }
    }

    public class SessionThatReceivesUnsupportedMessage {
        private UnknownMessage unknown;

        public AbstractSession create() {
            unknown = new UnknownMessage("AA");
            unknown.setMsgSeqNum(1);
            session.logon();
            return session;
        }

        public void rejectsTheMessage() {
            checking(new Expectations() {{
                one(stream).writeObject(with(any(BusinessMessageRejectMessage.class)));
            }});
            session.receive(unknown);
        }
    }
}
