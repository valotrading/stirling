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
package stirling.fix.session;

import jdave.junit4.JDaveRunner;

import org.junit.runner.RunWith;

import stirling.fix.messages.DefaultMessageVisitor;
import stirling.fix.messages.MessageHeader;
import stirling.fix.messages.fix42.MsgTypeValue;
import stirling.fix.messages.fix42.NewOrderSingle;
import stirling.fix.tags.fix42.BeginSeqNo;
import stirling.fix.tags.fix42.EndSeqNo;

@RunWith(JDaveRunner.class) public class QueueOutgoingMsgsSpec extends InitiatorSpecification {
    public class InitializedSession {
        /* Ref ID 16: a. Message to send/queue while disconnected. */
        public void messageToSendQueuedWhileDisconnected() throws Exception {
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            MessageHeader header = new MessageHeader(MsgTypeValue.NEW_ORDER_SINGLE);
            NewOrderSingle message = new NewOrderSingle(header);
            specify(session.getOutgoingMsgQueue().isEmpty(), true);
            session.send(connection, message);
            specify(session.getOutgoingMsgQueue().isEmpty(), false);
        }

        /* Ref ID 16: b. Re-connect with queued messages. Steps (2) and (3) are
         * not covered as messages are not expected to be lost. */
        public void reconnectWithQueuedMessages() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(MsgTypeValue.RESEND_REQUEST)
                        .msgSeqNum(2)
                        .integer(BeginSeqNo.Tag(), 1)
                        .integer(EndSeqNo.Tag(), 3)
                    .build());
            server.expect(MsgTypeValue.NEW_ORDER_SINGLE);
            server.expect(MsgTypeValue.NEW_ORDER_SINGLE);
            server.expect(MsgTypeValue.NEW_ORDER_SINGLE);
            runInClient(
                new Runnable() {
                    @Override public void run() {
                        session.send(connection, message(1));
                        session.send(connection, message(2));
                        session.send(connection, message(3));
                    }
                }, new Runnable() {
                    @Override public void run() {
                        session.logon(connection);
                    }
                }, new DefaultMessageVisitor(), false, 1000);
        }

        private NewOrderSingle message(int msgSeqNum) {
            NewOrderSingle message = new NewOrderSingle(new MessageHeader(MsgTypeValue.NEW_ORDER_SINGLE));
            message.setMsgSeqNum(msgSeqNum);
            return message;
        }
    }
}
