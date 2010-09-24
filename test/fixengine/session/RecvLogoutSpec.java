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

@RunWith(JDaveRunner.class) public class RecvLogoutSpec extends InitiatorSpecification {
    public class InitializedSession {
        /* Ref ID 13: a. Receive valid Logout message in response to a
         * solicited logout process */
        public void receiveValidLogoutMsgSolicited() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respondLogout(2);
            server.expect(MsgTypeValue.LOGOUT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }

        /* Ref ID 13: b. Receive valid Logout message unsolicited */
        public void receiveValidLogoutMsgUnsolicited() throws Exception {
            /* TODO: Wait for counterparty to disconnect up to 10 seconds. If
             * max exceeded, disconnect and genereate an "error" condition in
             * test output. */
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.expect(MsgTypeValue.LOGOUT);
            server.respondLogout(2);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                    session.logout(connection);
                }
            });
        }
    }
}
