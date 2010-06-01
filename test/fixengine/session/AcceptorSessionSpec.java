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
import fixengine.messages.NullMessage;

/**
 * @author Pekka Enberg 
 */
@RunWith(JDaveRunner.class)
public class AcceptorSessionSpec extends Specification<AcceptorSession> {
    private static final String BEGIN_STRING = Version.FIX_4_3.value(); 
    private static final String INITIATOR = "initiator";
    private static final String ACCEPTOR= "initiator";
    private static final Config config = new Config().setSenderCompId(ACCEPTOR)
            .setTargetCompId(INITIATOR).setVersion(Version.FIX_4_3);

    private final AuthenticationManager am = mock(AuthenticationManager.class);
    private final ConnectionManager cm = mock(ConnectionManager.class);
    @SuppressWarnings("unchecked")
    private final ObjectOutputStream<Message> stream = mock(ObjectOutputStream.class);
    private AcceptorSession session = new AcceptorSession(stream, config, am, cm);

    public class AcceptorThatReceivesLogonMessageFromKnownInitiator {
        public AcceptorSession create() {
            final LogonMessage logonMsg = new LogonMessage();
            logonMsg.setBeginString(BEGIN_STRING);
            logonMsg.setMsgSeqNum(1);
            logonMsg.setTargetCompId(ACCEPTOR);
            logonMsg.setSenderCompId(INITIATOR);
            logonMsg.setSendingTime(new DateTime());
            checking(new Expectations() {{
                allowing(stream).isClosed(); will(returnValue(false));
                one(cm).connect(INITIATOR); will(returnValue(true));
                one(am).authenticate(INITIATOR); will(returnValue(true));
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

    public class AcceptorThatReceivesLogonMessageFromUnknownInitiator {
        public AcceptorSession create() {
            final LogonMessage logonMsg = new LogonMessage();
            logonMsg.setBeginString(BEGIN_STRING);
            logonMsg.setMsgSeqNum(1);
            logonMsg.setTargetCompId(ACCEPTOR);
            logonMsg.setSenderCompId(INITIATOR);
            logonMsg.setSendingTime(new DateTime());
            checking(new Expectations() {{
                allowing(stream).isClosed(); will(returnValue(false));
                one(cm).connect(INITIATOR); will(returnValue(true));
                one(am).authenticate(INITIATOR); will(returnValue(false));
                one(cm).disconnect(INITIATOR);
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

    public class AcceptorThatReceivesLogonMessageFromInitiatorThatIsAlreadyConnected {
        public AcceptorSession create() {
            final LogonMessage logonMsg = new LogonMessage();
            logonMsg.setBeginString(BEGIN_STRING);
            logonMsg.setMsgSeqNum(1);
            logonMsg.setTargetCompId(ACCEPTOR);
            logonMsg.setSenderCompId(INITIATOR);
            logonMsg.setSendingTime(new DateTime());
            checking(new Expectations() {{
                allowing(stream).isClosed(); will(returnValue(false));
                one(cm).connect(INITIATOR); will(returnValue(false));
                one(cm).disconnect(INITIATOR);
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

    public class AcceptorThatReceivesSomeOtherMessageThanLogonFromInitiator {
        public AcceptorSession create() {
            final HeartbeatMessage heartbeatMsg = new HeartbeatMessage();
            heartbeatMsg.setBeginString(BEGIN_STRING);
            heartbeatMsg.setTargetCompId(ACCEPTOR);
            heartbeatMsg.setSenderCompId(INITIATOR);
            heartbeatMsg.setMsgSeqNum(1);
            heartbeatMsg.setSendingTime(new DateTime());
            checking(new Expectations() {{
                allowing(stream).isClosed(); will(returnValue(false));
                one(stream).writeObject(with(any(LogoutMessage.class)));
                one(cm).disconnect(INITIATOR);
                one(stream).close();
            }});
            session.receive(heartbeatMsg);
            session.logon();
            return session;
        }

        public void isNotAuthenticated() {
            specify(session.isAuthenticated(), must.equal(false));
        }
    }

    public class AcceptorThatHasInitiatorTerminateConnectionBeforeSendingLogon {
        public AcceptorSession create() {
            checking(new Expectations() {{
                one(stream).close();
                allowing(stream).isClosed(); will(returnValue(true));
                one(cm).disconnect(INITIATOR);
                one(stream).close();
            }});
            session.receive(new NullMessage());
            session.logon();
            return session;
        }

        public void isNotAuthenticated() {
            specify(session.isAuthenticated(), must.equal(false));
        }
    }
}
