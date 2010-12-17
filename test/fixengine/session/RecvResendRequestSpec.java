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
import fixengine.tags.fix42.BeginSeqNo;
import fixengine.tags.fix42.EndSeqNo;

@RunWith(JDaveRunner.class) public class RecvResendRequestSpec extends InitiatorSpecification {
    public class InitializedSession {
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
                        .integer(BeginSeqNo.Tag(), 2)
                        .integer(EndSeqNo.Tag(), 0)
                    .build());
            server.expect(MsgTypeValue.HEARTBEAT);
            server.expect(MsgTypeValue.HEARTBEAT);
            server.expect(MsgTypeValue.HEARTBEAT);
            server.expect(MsgTypeValue.HEARTBEAT);
            server.respond(
                    new MessageBuilder(MsgTypeValue.RESEND_REQUEST)
                        .msgSeqNum(4)
                        .integer(BeginSeqNo.Tag(), 2)
                        .integer(EndSeqNo.Tag(), 2)
                    .build());
            server.expect(MsgTypeValue.HEARTBEAT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                    session.heartbeat(connection);
                    session.heartbeat(connection);
                }
            });
        }
    }
}
