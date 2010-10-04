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

import fixengine.messages.MsgTypeValue;

@RunWith(JDaveRunner.class) public class SupportThirdPartyAddressingSpec extends InitiatorSpecification {
    public class InitializedSession {
        /* Ref ID 18: a. Receive messages with OnBehalfOfCompID and
         * DeliverToCompID values expected as specified in testing profile and
         * with correct usage. */
        public void valid() throws Exception {
            respondWithNonPointToPointMsg();
        }

        /* Ref ID 18: b. Receive messages with OnBehalfOfCompID or
         * DeliverToCompID values not specifed in testing profile or incorrect
         * usage. */
        public void invalid() throws Exception {
            respondWithNonPointToPointMsg();
        }

        private void respondWithNonPointToPointMsg() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(MsgTypeValue.HEARTBEAT)
                        .setOnBehalfOfCompId("behalfOf")
                        .setDeliverToCompId("deliverTo")
                        .msgSeqNum(2)
                    .build());
            server.expect(MsgTypeValue.REJECT);
            checking(expectLogSevere("Third-party message routing is not supported"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 3);
        }
    }
}
