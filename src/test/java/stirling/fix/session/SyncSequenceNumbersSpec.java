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

import stirling.fix.messages.fix42.MsgTypeValue;

@RunWith(JDaveRunner.class) public class SyncSequenceNumbersSpec extends InitiatorSpecification {
    public class InitializedSession {
        /* Ref ID 9: Application failure */
        public void applicationFailure() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.expect(MsgTypeValue.HEARTBEAT);
            server.expect(MsgTypeValue.SEQUENCE_RESET);
            server.respondLogout(2);
            server.expect(MsgTypeValue.LOGOUT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                    session.heartbeat(connection);
                    session.sequenceReset(connection, new Sequence());
                }
            });
            specify(session.getIncomingSeq().peek(), 3);
            specify(session.getOutgoingSeq().peek(), 3);
        }
    }
}
