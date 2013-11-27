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

import stirling.fix.CompIdValidator;
import stirling.fix.messages.fix42.MsgTypeValue;

@RunWith(JDaveRunner.class) public class SupportThirdPartyAddressingSpec extends InitiatorSpecification {
    public class InitializedSession {
        /* Ref ID 18: a. Receive messages with OnBehalfOfCompId values expected
         * as specified in testing profile and with correct usage. */
        public void msgWithValidBehalfOfCompId() throws Exception {
            setOnBehalfOfCompIdValidator(new CompIdValidator() {
                @Override
                public boolean validate(String onBehalfOfCompId, boolean exists, String msgType) {
                    if (msgType.equals(MsgTypeValue.HEARTBEAT))
                        return "behalfOf".equals(onBehalfOfCompId);
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
            checking(expectLogSevere("Invalid OnBehalfOfCompID(115): invalidOnBehalfOfCompId"));
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
            setDeliverToCompIdValidator(new CompIdValidator() {
                @Override
                public boolean validate(String deliverToCompId, boolean exists, String msgType) {
                    if (msgType.equals(MsgTypeValue.HEARTBEAT))
                        return "deliverTo".equals(deliverToCompId);
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
            checking(expectLogSevere("Invalid DeliverToCompID(128): invalidDeliverTo"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 3);
        }
    }
}
