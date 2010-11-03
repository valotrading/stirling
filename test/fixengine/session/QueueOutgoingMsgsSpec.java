/*
 * Copyright 2010 the original author or authors.
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

import jdave.junit4.JDaveRunner;
import org.junit.runner.RunWith;

import fixengine.messages.DefaultMessageVisitor;
import fixengine.messages.MessageHeader;
import fixengine.messages.MsgTypeValue;
import fixengine.messages.NewOrderSingleMessage;

import silvertip.Connection;

@RunWith(JDaveRunner.class) public class QueueOutgoingMsgsSpec extends InitiatorSpecification {
    public class InitializedSession {
        /* Ref ID 16: Message to send/queue while disconnected. */
        public void messageToSendQueuedWhileDisconnected() throws Exception {
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            MessageHeader header = new MessageHeader(MsgTypeValue.NEW_ORDER_SINGLE);
            NewOrderSingleMessage message = new NewOrderSingleMessage(header);
            specify(session.getOutgoingMsgQueue().isEmpty(), true);
            session.send(connection, message);
            specify(session.getOutgoingMsgQueue().isEmpty(), false);
        }

        /* Ref ID 16: Re-connect with queued messages. */
        public void reconnectWithQueuedMessages() throws Exception {
        }
    }
}
