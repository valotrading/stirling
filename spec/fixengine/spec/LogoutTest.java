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

import org.concordion.integration.junit3.ConcordionTestCase;
import org.joda.time.DateTime;

import fixengine.Config;
import fixengine.Version;
import fixengine.io.ObjectOutputStream;
import fixengine.messages.LogonMessage;
import fixengine.messages.LogoutMessage;
import fixengine.messages.Message;
import fixengine.messages.NullMessage;
import fixengine.messages.ResendRequestMessage;
import fixengine.messages.Session;
import fixengine.session.InitiatorSession;

/**
 * @author Pekka Enberg 
 */
public class LogoutTest extends ConcordionTestCase {
    private static final String BEGIN_STRING = Version.FIX_4_3.value();
    private static final String TARGET_COMP_ID = "targetCompId";
    private static final String SENDER_COMP_ID = "senderCompId";

    public String issuesLogout() {
        StubMessageStream server = new StubMessageStream();
        Session session = newSession(server);

        session.receive(newLogonMessage(1));
        session.logon();

        session.receive(newLogoutMessage(2));
        session.logout();

        return toString(session.isAuthenticated());
    }

    public String terminates() {
        StubMessageStream server = new StubMessageStream();
        Session session = newSession(server);

        session.receive(newLogonMessage(1));
        session.logon();
        session.receive(new NullMessage());
        session.logout();

        return toString(session.isAuthenticated());
    }

    public String recoversMessagesBeforeLoggingOut() {
        StubMessageStream server = new StubMessageStream();
        Session session = newSession(server);

        session.receive(newLogonMessage(1));
        session.logon();

        session.receive(newResendRequestMessage(2));
        session.receive(newLogoutMessage(3));
        session.logout();

        return toString(session.isAuthenticated());
    }

    private Message newLogonMessage(int seqNum) {
        LogonMessage result = new LogonMessage();
        result.setBeginString(BEGIN_STRING);
        result.setSenderCompId(SENDER_COMP_ID);
        result.setTargetCompId(SENDER_COMP_ID);
        result.setMsgSeqNum(seqNum);
        result.setSendingTime(new DateTime());
        return result;
    }

    private Message newLogoutMessage(int seqNum) {
        Message result = new LogoutMessage();
        result.setBeginString(BEGIN_STRING);
        result.setSenderCompId(SENDER_COMP_ID);
        result.setTargetCompId(SENDER_COMP_ID);
        result.setMsgSeqNum(seqNum);
        result.setSendingTime(new DateTime());
        return result;
    }

    private Message newResendRequestMessage(int seqNum) {
        ResendRequestMessage result = new ResendRequestMessage();
        result.setBeginString(BEGIN_STRING);
        result.setSenderCompId(SENDER_COMP_ID);
        result.setTargetCompId(SENDER_COMP_ID);
        result.setMsgSeqNum(seqNum);
        result.setSendingTime(new DateTime());
        result.setBeginSeqNo(1);
        result.setEndSeqNo(0);
        return result;
    }

    private String toString(boolean open) {
        if (open)
            return "open";
        return "closed";
    }

    private Session newSession(ObjectOutputStream<Message> stream) {
        Config config = new Config();
        config.setSenderCompId(SENDER_COMP_ID);
        config.setTargetCompId(TARGET_COMP_ID);
        config.setVersion(Version.FIX_4_3);
        return new InitiatorSession(stream, config);
    }
}
