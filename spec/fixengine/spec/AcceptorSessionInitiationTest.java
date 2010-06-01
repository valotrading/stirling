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
package fixengine.spec;

import org.joda.time.DateTime;

import fixengine.Config;
import fixengine.Version;
import fixengine.messages.LogonMessage;
import fixengine.messages.Message;
import fixengine.messages.Session;
import fixengine.session.AcceptorSession;

/**
 * @author Pekka Enberg 
 */
public class AcceptorSessionInitiationTest extends SessionInitiationTestCase {
    private static final String BEGIN_STRING = Version.FIX_4_3.value();
    private static final String INITIATOR = "initiator";
    private static final String ACCEPTOR = "acceptor";
    
    private final StubAuthenticationManager am = new StubAuthenticationManager();
    private final StubConnectionManager cm = new StubConnectionManager();
    private final StubMessageStream client = new StubMessageStream();

    public String logon(String request) {
        Session session = newSession();
        am.add(INITIATOR);
        session.receive(newRequest(request));
        session.logon();
        cm.reset();
        return state(session);
    }

    private Message newRequest(String action) {
        if (action.equals("Valid Logon")) {
            LogonMessage logon = new LogonMessage();
            logon.setSenderCompId(INITIATOR);
            logon.setTargetCompId(ACCEPTOR);
            logon.setBeginString(BEGIN_STRING);
            logon.setMsgSeqNum(1);
            logon.setSendingTime(new DateTime());
            return logon;
        }
        if (action.equals("Logon with MsgSeqNum too high")) {
            LogonMessage logon = new LogonMessage();
            logon.setSenderCompId(INITIATOR);
            logon.setTargetCompId(ACCEPTOR);
            logon.setBeginString(BEGIN_STRING);
            logon.setMsgSeqNum(2);
            logon.setSendingTime(new DateTime());
            return logon;
        }
        if (action.equals("Logon message with duplicate identity")) {
            cm.connect(INITIATOR);
            LogonMessage logon = new LogonMessage();
            logon.setSenderCompId(INITIATOR);
            logon.setTargetCompId(ACCEPTOR);
            logon.setBeginString(BEGIN_STRING);
            logon.setMsgSeqNum(1);
            logon.setSendingTime(new DateTime());
            return logon;
        }
        if (action.equals("Logon message with unknown identity")) {
            LogonMessage logon = new LogonMessage();
            logon.setSenderCompId("unknown-initiator");
            logon.setTargetCompId(ACCEPTOR);
            logon.setBeginString(BEGIN_STRING);
            logon.setMsgSeqNum(1);
            logon.setSendingTime(new DateTime());
            return logon;
        }
        if (action.equals("Invalid Logon")) {
            LogonMessage logon = new LogonMessage();
            logon.setSenderCompId(INITIATOR);
            logon.setTargetCompId("invalid-initiator");
            logon.setBeginString(BEGIN_STRING);
            logon.setMsgSeqNum(1);
            logon.setSendingTime(new DateTime());
            return logon;
        }
        return newMessage(action);
    }

    private Session newSession() {
        Config config = new Config();
        config.setSenderCompId(ACCEPTOR);
        config.setVersion(Version.FIX_4_3);
        return new AcceptorSession(client, config, am, cm);
    }
}
