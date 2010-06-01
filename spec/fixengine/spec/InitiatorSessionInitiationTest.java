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
import fixengine.session.InitiatorSession;

/**
 * @author Pekka Enberg 
 */
public class InitiatorSessionInitiationTest extends SessionInitiationTestCase {
    private final StubMessageStream server = new StubMessageStream();

    public String logon(String response) {
        Session session = newSession();
        session.receive(newResponse(response));
        if (!session.isOutOfSync()) {
            session.logon();
        }
        return state(session);
    }

    private Message newResponse(String action) {
        if (action.equals("Valid Logon")) {
            LogonMessage logon = new LogonMessage();
            logon.setTargetCompId(SENDER_COMP_ID);
            logon.setSenderCompId(TARGET_COMP_ID);
            logon.setBeginString(BEGIN_STRING);
            logon.setMsgSeqNum(1);
            logon.setSendingTime(new DateTime());
            return logon;
        }
        if (action.equals("Logon with MsgSeqNum too high")) {
            LogonMessage logon = new LogonMessage();
            logon.setTargetCompId(SENDER_COMP_ID);
            logon.setSenderCompId(TARGET_COMP_ID);
            logon.setBeginString(BEGIN_STRING);
            logon.setMsgSeqNum(2);
            logon.setSendingTime(new DateTime());
            return logon;
        }
        if (action.equals("Invalid Logon")) {
            LogonMessage logon = new LogonMessage();
            logon.setTargetCompId("invalid-initiator");
            logon.setSenderCompId(TARGET_COMP_ID);
            logon.setBeginString(BEGIN_STRING);
            logon.setMsgSeqNum(1);
            logon.setSendingTime(new DateTime());
            return logon;
        }
        return newMessage(action);
    }

    private Session newSession() {
        Config config = new Config();
        config.setSenderCompId(SENDER_COMP_ID);
        config.setTargetCompId(TARGET_COMP_ID);
        config.setVersion(Version.FIX_4_3);
        return new InitiatorSession(server, config);
    }
}
