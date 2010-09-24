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
import org.jmock.Expectations;
import org.junit.runner.RunWith;

import fixengine.messages.MsgTypeValue;
import fixengine.tags.NewSeqNo;

@RunWith(JDaveRunner.class) public class RecvSequenceResetWithoutGapFillSpec extends InitiatorSpecification {
    public class InitializedSession {
        /* Ref ID 11: a. Receive Sequence Reset (reset) message with NewSeqNo >
         * than expected sequence number */
        public void newSeqNoGreaterThanExpectedMsgSeqNum() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(MsgTypeValue.SEQUENCE_RESET)
                        .msgSeqNum(2)
                        .integer(NewSeqNo.TAG, 5)
                    .build());
            server.respondLogout(5);
            server.expect(MsgTypeValue.LOGOUT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 6);
        }

        /* Ref ID 11: b. Receive Sequence Reset (reset) message with NewSeqNo =
         * to expected sequence number */
        public void newSeqNoEqualToMsgSeqNum() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(MsgTypeValue.SEQUENCE_RESET)
                        .msgSeqNum(2)
                        .integer(NewSeqNo.TAG, 2)
                    .build());
            server.respondLogout(2);
            server.expect(MsgTypeValue.LOGOUT);
            checking(new Expectations() {{
                one(logger).warning("NewSeqNo(36)=2 is equal to expected MsgSeqNum(34)=2");
            }});
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 3);
        }

        /* Ref ID 11: c. Receive Sequence Reset (reset) message with NewSeqNo <
         * than expected sequence number */
        public void newSeqNoSmallerThanMsgSeqNum() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(MsgTypeValue.HEARTBEAT)
                        .msgSeqNum(2)
                    .build());
            server.respond(
                    new MessageBuilder(MsgTypeValue.SEQUENCE_RESET)
                        .msgSeqNum(3)
                        .integer(NewSeqNo.TAG, 2)
                    .build());
            server.expect(MsgTypeValue.REJECT);
            checking(new Expectations() {{
                one(logger).warning("Value is incorrect (out of range) for this tag, NewSeqNum(36)=2");
            }});
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 3);
        }
    }
}
