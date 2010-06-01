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
import fixengine.messages.HeartbeatMessage;
import fixengine.messages.LogonMessage;
import fixengine.messages.LogoutMessage;
import fixengine.messages.Message;
import fixengine.messages.RejectMessage;
import fixengine.messages.ResendRequestMessage;
import fixengine.messages.SequenceResetMessage;
import fixengine.messages.Session;
import fixengine.messages.TestRequestMessage;
import fixengine.session.InitiatorSession;

/**
 * @author Pekka Enberg
 */
public class ConnectionStatusTest extends ConcordionTestCase {
    private static final String BEGIN_STRING = Version.FIX_4_3.value();
    private static final String INITIATOR = "initiator";
    private static final String ACCEPTOR = "acceptor";

    private Message response;

    public void receiveTestRequest(String testReqId) {
        StubMessageStream server = new StubMessageStream();
        Session session = newSession(server);

        session.receive(newTestRequest(1, testReqId));

        response = server.read();
    }

    public String getResponse() {
        return toString(response);
    }

    public String getTestReqId() {
        if (response instanceof HeartbeatMessage) {
            HeartbeatMessage heartbeat = (HeartbeatMessage) response;
            return heartbeat.getTestReqId();
        }
        return null;
    }

    private String toString(Message message) {
        if (message instanceof HeartbeatMessage)
            return "Heartbeat";
        if (message instanceof TestRequestMessage)
            return "TestRequest";
        if (message instanceof ResendRequestMessage)
            return "ResendRequest";
        if (message instanceof RejectMessage)
            return "Reject";
        if (message instanceof SequenceResetMessage)
            return "SequenceReset";
        if (message instanceof LogoutMessage)
            return "Logout";
        if (message instanceof LogonMessage)
            return "Logon";
        throw new IllegalArgumentException(message.getClass().toString());
    }

    private Message newTestRequest(int seqNum, String testReqId) {
        TestRequestMessage result = new TestRequestMessage();
        result.setBeginString(BEGIN_STRING);
        result.setSenderCompId(INITIATOR);
        result.setTargetCompId(ACCEPTOR);
        result.setMsgSeqNum(seqNum);
        result.setSendingTime(new DateTime());
        result.setTestReqId(testReqId);
        return result;
    }

    private Session newSession(ObjectOutputStream<Message> stream) {
        Config config = new Config();
        config.setSenderCompId(INITIATOR);
        config.setTargetCompId(ACCEPTOR);
        config.setVersion(Version.FIX_4_3);
        return new InitiatorSession(stream, config);
    }
}
