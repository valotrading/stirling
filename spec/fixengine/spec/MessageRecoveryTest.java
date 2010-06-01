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
import fixengine.messages.AbstractMessage;
import fixengine.messages.HeartbeatMessage;
import fixengine.messages.Message;
import fixengine.messages.SequenceResetMessage;
import fixengine.messages.Session;
import fixengine.session.InitiatorSession;

/**
 * @author Pekka Enberg 
 */
public class MessageRecoveryTest extends ConcordionTestCase {
    private static final String BEGIN_STRING = Version.FIX_4_3.value();
    private static final String TARGET_COMP_ID = "targetCompId";
    private static final String SENDER_COMP_ID = "senderCompId";

    public String messagesAreResent() {
        StubMessageStream server = new StubMessageStream();
        Session session = newSession(server);

        // Out-of-order message received
        session.receive(newHeartbeatMessage(3));
        
        // The server resends the messages.
        session.receive(newHeartbeatMessage(1));
        session.receive(newHeartbeatMessage(2));
        session.receive(newHeartbeatMessage(3));

        return toString(session.isOutOfSync());
    }

    public String seqResetGapFillReceived(int outOfOrderSeqNum,
            int replyMsgSeqNum, int newSeqNo) {
        StubMessageStream server = new StubMessageStream();
        Session session = newSession(server);

        session.receive(newHeartbeatMessage(outOfOrderSeqNum));
        session.receive(newSequenceResetMessage(true, replyMsgSeqNum, newSeqNo));

        return toString(session.isOutOfSync());
    }

    public String seqResetResetReceived(int outOfOrderSeqNum,
            int replyMsgSeqNum, int newSeqNo) {
        StubMessageStream server = new StubMessageStream();
        Session session = newSession(server);

        session.receive(newHeartbeatMessage(outOfOrderSeqNum));
        session.receive(newSequenceResetMessage(false, replyMsgSeqNum, newSeqNo));

        return toString(session.isOutOfSync());
    }

    private AbstractMessage newHeartbeatMessage(int seqNum) {
        HeartbeatMessage msg = new HeartbeatMessage();
        msg.setBeginString(BEGIN_STRING);
        msg.setTargetCompId(TARGET_COMP_ID);
        msg.setSenderCompId(TARGET_COMP_ID);
        msg.setSendingTime(new DateTime());
        msg.setMsgSeqNum(seqNum);
        return msg;
    }

    private AbstractMessage newSequenceResetMessage(boolean gapFillFlag, int msgSeqNum, int newSeqNo) {
        SequenceResetMessage msg = new SequenceResetMessage();
        msg.setBeginString(BEGIN_STRING);
        msg.setTargetCompId(TARGET_COMP_ID);
        msg.setSenderCompId(TARGET_COMP_ID);
        msg.setSendingTime(new DateTime());
        msg.setMsgSeqNum(msgSeqNum);
        msg.setGapFillFlag(gapFillFlag);
        msg.setNewSeqNo(newSeqNo);
        return msg;
    }

    private String toString(boolean required) {
        if (required)
            return "required";
        return "not required";
    }

    private Session newSession(ObjectOutputStream<Message> stream) {
        Config config = new Config();
        config.setSenderCompId(SENDER_COMP_ID);
        config.setTargetCompId(TARGET_COMP_ID);
        config.setVersion(Version.FIX_4_3);
        return new InitiatorSession(stream, config);
    }
}
