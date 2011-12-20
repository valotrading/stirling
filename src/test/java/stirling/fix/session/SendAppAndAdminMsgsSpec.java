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
import stirling.fix.tags.fix42.GapFillFlag;
import stirling.fix.tags.fix42.NewSeqNo;

@RunWith(JDaveRunner.class) public class SendAppAndAdminMsgsSpec extends InitiatorSpecification {
    public class InitializedSession {
        /* Ref ID 15: Send more than one message of the same type with header
         * and body fields ordered differently to verify acceptance. (Exclude
         * those which have restrictions regarding order). */
        public void reorderedHeaderFields() throws Exception {
            // TODO: Testing of reordering of header fields would require
            // formatting a raw message with an correct sending time.
        }

        private void reorderedBodyFields() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(MsgTypeValue.SEQUENCE_RESET)
                        .msgSeqNum(2)
                        .bool(GapFillFlag.Tag(), true)
                        .integer(NewSeqNo.Tag(), 2)
                    .build());
            server.respond(
                    new MessageBuilder(MsgTypeValue.SEQUENCE_RESET)
                        .msgSeqNum(2)
                        .integer(NewSeqNo.Tag(), 2)
                        .bool(GapFillFlag.Tag(), true)
                    .build());
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 2);
        }
    }
}
