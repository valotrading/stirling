/*
 * Copyright 2008 the original author or authors.
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
import org.joda.time.DateTime;
import org.junit.runner.RunWith;

import fixengine.Config;
import fixengine.Version;
import fixengine.io.ObjectOutputStream;
import fixengine.messages.HeartbeatMessage;
import fixengine.messages.LogonMessage;
import fixengine.messages.LogoutMessage;
import fixengine.messages.Message;
import fixengine.session.store.SessionStore;
import fixengine.messages.Session;

/**
 * @author Pekka Enberg
 */
@RunWith(JDaveRunner.class)
public class InitiatorSessionSpec extends Specification<InitiatorSession> {
    private static final String BEGIN_STRING = Version.FIX_4_3.value();
    private static final String INITIATOR = "initiator";
    private static final String ACCEPTOR = "acceptor";

    private final Config config = new Config().setSenderCompId(INITIATOR).setTargetCompId(ACCEPTOR).setVersion(Version.FIX_4_3);
    @SuppressWarnings("unchecked")
    private final ObjectOutputStream<Message> stream = mock(ObjectOutputStream.class);
    private final SessionStore store = mock(SessionStore.class);
    private final InitiatorSession session = new InitiatorSession(stream, config, store);

    public class InitiatorThatReceivesValidLogonForLogon {
        public InitiatorSession create() {
            final LogonMessage logonMsg = new LogonMessage();
            logonMsg.setBeginString(BEGIN_STRING);
            logonMsg.setMsgSeqNum(1);
            logonMsg.setSenderCompId(ACCEPTOR);
            logonMsg.setTargetCompId(INITIATOR);
            logonMsg.setSendingTime(new DateTime());
            checking(new Expectations() {{
                one(store).load(with(any(Session.class)));
                allowing(stream).isClosed(); will(returnValue(false));
                one(stream).writeObject(with(any(LogonMessage.class)));
            }});
            session.receive(logonMsg);
            session.logon();
            return session;
        }

        public void isAuthenticated() {
            specify(session.isAuthenticated(), must.equal(true));
        }
    }

    public class InitiatorThatReceivesInvalidLogonForLogon {
        public InitiatorSession create() {
            final LogonMessage logonMsg = new LogonMessage();
            logonMsg.setBeginString(BEGIN_STRING);
            logonMsg.setMsgSeqNum(1);
            logonMsg.setBeginString(BEGIN_STRING);
            logonMsg.setSenderCompId(ACCEPTOR);
            logonMsg.setTargetCompId("some-other-initiator");
            logonMsg.setSendingTime(new DateTime());
            checking(new Expectations() {{
                one(store).load(with(any(Session.class)));
                allowing(stream).isClosed(); will(returnValue(false));
                one(stream).writeObject(with(any(LogonMessage.class)));
                one(stream).writeObject(with(any(LogoutMessage.class)));
                one(stream).close();
            }});
            session.receive(logonMsg);
            session.logon();
            return session;
        }

        public void isNotAuthenticated() {
            specify(session.isAuthenticated(), must.equal(false));
        }
    }

    public class InitiatorThatReceivesSomeOtherMessageThanLogonForLogon {
        public InitiatorSession create() {
            checking(new Expectations() {{
                one(store).load(with(any(Session.class)));
                allowing(stream).isClosed(); will(returnValue(false));
                one(stream).writeObject(with(any(LogonMessage.class)));
                one(stream).close();
            }});
            HeartbeatMessage message = new HeartbeatMessage();
            message.setBeginString(BEGIN_STRING);
            message.setTargetCompId(INITIATOR);
            message.setSenderCompId(ACCEPTOR);
            message.setMsgSeqNum(1);
            message.setSendingTime(new DateTime());
            session.receive(message);
            session.logon();
            return session;
        }

        public void isNotAuthenticated() {
            specify(session.isAuthenticated(), must.equal(false));
        }
    }
}
