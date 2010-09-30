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

@RunWith(JDaveRunner.class) public class InitiateLogoutProcessSpec extends InitiatorSpecification {
    public class InitializedSession {
        /* Ref ID 12: Initiate logout */
        public void initiateLogoutAndCounterpartyResponded() throws Exception {
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
            shiftSessionTimeBy(LOGOUT_RESPONSE_TIMEOUT_MSEC+1);
            session.processInitiatedLogout(connection);
        }

        /* Ref ID 12: Initiate logout */
        public void initiateLogoutAndCounterpartyDidNotRespond() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.expect(MsgTypeValue.LOGOUT);
            checking(new Expectations() {{
                one(logger).warning("Response to logout not received in "+LOGOUT_RESPONSE_TIMEOUT_MSEC/1000+" second(s), disconnecting");
            }});
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                    session.logout(connection);
                }
            });
            shiftSessionTimeBy(LOGOUT_RESPONSE_TIMEOUT_MSEC+1);
            session.processInitiatedLogout(connection);
        }
    }
}
