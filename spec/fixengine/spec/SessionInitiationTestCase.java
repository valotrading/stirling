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

import fixengine.Version;
import fixengine.messages.HeartbeatMessage;
import fixengine.messages.LogoutMessage;
import fixengine.messages.Message;
import fixengine.messages.RejectMessage;
import fixengine.messages.ResendRequestMessage;
import fixengine.messages.SequenceResetMessage;
import fixengine.messages.Session;
import fixengine.messages.TestRequestMessage;

/**
 * @author Pekka Enberg 
 */
public abstract class SessionInitiationTestCase extends ConcordionTestCase {
    protected static final String BEGIN_STRING = Version.FIX_4_3.value();
    protected static final String TARGET_COMP_ID = "initiator";
    protected static final String SENDER_COMP_ID = "acceptor";
    
    protected Message newMessage(String type) {
        Message result = newMessage0(type);
        result.setBeginString(BEGIN_STRING);
        result.setTargetCompId(SENDER_COMP_ID);
        result.setSenderCompId(TARGET_COMP_ID);
        result.setMsgSeqNum(1);
        result.setSendingTime(new DateTime());
        return result;
    }

    private Message newMessage0(String type) {
        if (type.equals("Heartbeat")) {
            return new HeartbeatMessage();
        }
        if (type.equals("Logout")) {
            return new LogoutMessage();
        }
        if (type.equals("Reject")) {
            RejectMessage reject = new RejectMessage();
            reject.setRefSeqNo(1);
            return reject;
        }
        if (type.equals("Resend Request")) {
            ResendRequestMessage resend = new ResendRequestMessage();
            resend.setBeginSeqNo(1);
            resend.setEndSeqNo(0);
            return resend;
        }
        if (type.equals("Sequence Reset")) {
            SequenceResetMessage reset = new SequenceResetMessage();
            reset.setGapFillFlag(true);
            reset.setNewSeqNo(1);
            return reset;
        }
        if (type.equals("Test Request")) {
            TestRequestMessage testRequest = new TestRequestMessage();
            testRequest.setTestReqId("test-req");
            return testRequest;
        }
        throw new IllegalArgumentException(type);
    }

    protected String state(Session session) {
        if (session.isDisconnected()) {
            return "Disconnected";
        }
        if (session.isOutOfSync()) {
            return "Out-of-sync";
        }
        if (session.isAuthenticated()) {
            return "Authenticated";
        }
        return "Connected";
    }
}
