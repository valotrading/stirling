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

import stirling.fix.messages.DefaultMessageValidator;
import stirling.fix.messages.AbstractMessageValidator;
import stirling.fix.messages.Message;
import stirling.fix.messages.Validator;
import stirling.fix.messages.fix42.MsgTypeValue;

import stirling.fix.messages.Validator.ErrorHandler;
import stirling.fix.session.Session;
import stirling.fix.tags.fix43.SessionRejectReason;

@RunWith(JDaveRunner.class) public class SupportThirdPartyAddressingSpec extends InitiatorSpecification {
    public class InitializedSession {
        /* Ref ID 18: a. Receive messages with OnBehalfOfCompId values expected
         * as specified in testing profile and with correct usage. */
        public void msgWithValidBehalfOfCompId() throws Exception {
            setMessageValidator(new DefaultMessageValidator() {
                @Override
                protected boolean isOnBehalfOfCompIdValid(Session session, Message message) {
                    if (message.getMsgType().equals(MsgTypeValue.HEARTBEAT))
                        return "behalfOf".equals(message.getOnBehalfOfCompId());
                    return true;
                }
            });
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(MsgTypeValue.HEARTBEAT)
                        .setOnBehalfOfCompId("behalfOf")
                        .msgSeqNum(2)
                    .build());
            server.respondLogout(3);
            server.expect(MsgTypeValue.LOGOUT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 4);
        }

        /* Ref ID 18: b. Receive messages with OnBehalfOfCompId values not
         * specifed in testing profile or incorrect usage. */
        public void msgWithInvalidOnBehalfOfCompId() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(MsgTypeValue.HEARTBEAT)
                        .setOnBehalfOfCompId("invalidOnBehalfOfCompId")
                        .msgSeqNum(2)
                    .build());
            server.expect(MsgTypeValue.REJECT);
            checking(expectLogSevere("OnBehalfOfCompID(115) not allowed: invalidOnBehalfOfCompId"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 3);
        }

        /* Ref ID 18: a. Receive messages with DeliverToCompID values expected
         * as specified in testing profile and with correct usage. */
        public void msgWithValidDeliverToCompId() throws Exception {
            setMessageValidator(new DefaultMessageValidator() {
                @Override
                protected boolean isDeliverToCompIdValid(Session session, Message message) {
                    if (message.getMsgType().equals(MsgTypeValue.HEARTBEAT))
                        return "deliverTo".equals(message.getDeliverToCompId());
                    return true;
                }
            });
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(MsgTypeValue.HEARTBEAT)
                        .setDeliverToCompId("deliverTo")
                        .msgSeqNum(2)
                    .build());
            server.respondLogout(3);
            server.expect(MsgTypeValue.LOGOUT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 4);
        }

        public void msgWithInvalidDeliverToCompId() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(MsgTypeValue.HEARTBEAT)
                        .setDeliverToCompId("invalidDeliverTo")
                        .msgSeqNum(2)
                    .build());
            server.expect(MsgTypeValue.REJECT);
            checking(expectLogSevere("DeliverToCompID(128) not allowed: invalidDeliverTo"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 3);
        }
    }
}
