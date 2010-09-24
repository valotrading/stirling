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
import fixengine.tags.BeginSeqNo;
import fixengine.tags.EndSeqNo;

@RunWith(JDaveRunner.class) public class RecvResendRequestSpec extends InitiatorSpecification {
    public class InitializedSession {
        /* TODO: The current implementation does not support resending of
         * messages as specified in this test case, instead a SequenceReset is
         * issued. Once message recovery is implemented this test case shall be
         * rewritten. */
        /* Ref ID 8: Valid Resend Request */
        public void valid() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(MsgTypeValue.HEARTBEAT)
                        .msgSeqNum(2)
                    .build());
            server.respond(
                    new MessageBuilder(MsgTypeValue.RESEND_REQUEST)
                        .msgSeqNum(3)
                        .integer(BeginSeqNo.TAG, 1)
                        .integer(EndSeqNo.TAG, 0)
                    .build());
            server.expect(MsgTypeValue.SEQUENCE_RESET);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }
    }
}
