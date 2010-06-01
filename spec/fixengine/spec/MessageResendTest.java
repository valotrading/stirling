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
import fixengine.messages.ResendRequestMessage;
import fixengine.messages.SequenceResetMessage;
import fixengine.messages.Session;
import fixengine.session.InitiatorSession;

/**
 * @author Pekka Enberg 
 */
public class MessageResendTest extends ConcordionTestCase {
    private static final String BEGIN_STRING = Version.FIX_4_3.value();
    private static final String TARGET_COMP_ID = "targetCompId";
    private static final String SENDER_COMP_ID = "senderCompId";

    private SequenceResetMessage response;

    public void respond(int beginSeqNo, int endSeqNo) {
        StubMessageStream server = new StubMessageStream();
        Session session = newSession(server);

        session.send(newHeartbeatMessage(1));
        server.read();

        session.send(newHeartbeatMessage(2));
        server.read();

        session.receive(newResendRequestMessage(3, beginSeqNo, endSeqNo));

        response = (SequenceResetMessage) server.read();
    }

    private Session newSession(ObjectOutputStream<Message> stream) {
        Config config = new Config();
        config.setSenderCompId(SENDER_COMP_ID);
        config.setTargetCompId(TARGET_COMP_ID);
        config.setVersion(Version.FIX_4_3);
        return new InitiatorSession(stream, config);
    }

    private AbstractMessage newHeartbeatMessage(int seqNum) {
        HeartbeatMessage msg = new HeartbeatMessage();
        msg.setBeginString(BEGIN_STRING);
        msg.setTargetCompId(TARGET_COMP_ID);
        msg.setSenderCompId(SENDER_COMP_ID);
        msg.setMsgSeqNum(seqNum);
        msg.setSendingTime(new DateTime());
        return msg;
    }

    private Message newResendRequestMessage(int msgSeqNum, int beginSeqNo, int endSeqNo) {
        ResendRequestMessage message = new ResendRequestMessage();
        message.setBeginString(BEGIN_STRING);
        message.setTargetCompId(TARGET_COMP_ID);
        message.setSenderCompId(SENDER_COMP_ID);
        message.setMsgSeqNum(msgSeqNum);
        message.setBeginSeqNo(beginSeqNo);
        message.setEndSeqNo(endSeqNo);
        message.setSendingTime(new DateTime());
        return message;
    }

    public String getPossDupFlag() {
        if (response.getPossDupFlag())
            return "Y";
        return "N";
    }

    public int getMsgSeqNo() {
        return response.getMsgSeqNum();
    }

    public int getNewSeqNo() {
        return response.getNewSeqNo();
    }
}
