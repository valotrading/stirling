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
import fixengine.tags.TestReqID;

@RunWith(JDaveRunner.class) public class SendTestRequestSpec extends InitiatorSpecification {
    public class InitializedSession {
        /* Ref ID 6: No data received during preset heartbeat interval
         * (HeatbeatInt field) + "some reasonable amount of time" (use 20% of
         * HeartBeatInt field) */
        public void noDataReceivedDuringPresetHeartbeatInterval() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(MsgTypeValue.TEST_REQUEST)
                        .msgSeqNum(2)
                        .string(TestReqID.TAG, "12345678")
                    .build());
            // TODO: Verify that TestReqID of Heartbeat matches
            server.expect(MsgTypeValue.HEARTBEAT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }
    }
}
