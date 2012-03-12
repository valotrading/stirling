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

import stirling.fix.messages.Message;
import stirling.fix.messages.fix42.MsgTypeValue;
import stirling.fix.tags.fix42.EncryptMethod;
import stirling.fix.tags.fix42.GapFillFlag;
import stirling.fix.tags.fix42.HeartBtInt;
import stirling.fix.tags.fix42.NewSeqNo;
import stirling.lang.Predicate;

@RunWith(JDaveRunner.class) public class LogonSpec extends InitiatorSpecification {
    public class InitializedSession {
        /* Ref ID 1B: b. Send Logon message */
        public void valid() throws Exception {
            server.expect(MsgTypeValue.LOGON, new Predicate<Message>() {
                @Override public boolean apply(Message message) {
                    return message.getInteger(HeartBtInt.Tag()).equals(getHeartbeatIntervalInSeconds());
                }
            });
            server.respondLogon();
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }

        /* Ref ID 1B: c. Valid Logon message as response is received */
        public void validButMsgSeqNumIsTooHigh() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respond(
                    new MessageBuilder(MsgTypeValue.LOGON)
                        .msgSeqNum(2)
                        .integer(HeartBtInt.Tag(), getHeartbeatIntervalInSeconds())
                        .enumeration(EncryptMethod.Tag(), EncryptMethod.None())
                    .build());
            server.expect(MsgTypeValue.RESEND_REQUEST);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }

        /* Ref ID 1B: d. Invalid Logon message is received */
        public void invalid() throws Exception {
            // TODO: Invalid MsgType
            server.expect(MsgTypeValue.LOGON);
            server.respond(
                    new MessageBuilder(MsgTypeValue.LOGON)
                        .msgSeqNum(1)
                        .integer(HeartBtInt.Tag(), getHeartbeatIntervalInSeconds())
                        /* EncryptMethod(98) missing */
                    .build());
            server.expect(MsgTypeValue.LOGOUT);
            checking(expectLogSevere("EncryptMethod(98): Tag missing"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }

        /* Ref ID 1B: e. Receive any message other than a Logon message. */
        public void otherMessageThanLogon() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respond(
                    new MessageBuilder(MsgTypeValue.HEARTBEAT)
                        .msgSeqNum(1)
                    .build());
            server.expect(MsgTypeValue.LOGOUT);
            checking(expectLogSevere("first message is not a logon"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }

        public void validWithMsgSeqNumGreaterThanExpected() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respond(
                    new MessageBuilder(MsgTypeValue.LOGON)
                        .msgSeqNum(2)
                        .integer(HeartBtInt.Tag(), getHeartbeatIntervalInSeconds())
                        .enumeration(EncryptMethod.Tag(), EncryptMethod.None())
                    .build());
            server.expect(MsgTypeValue.RESEND_REQUEST);
            server.respond(
                    new MessageBuilder(MsgTypeValue.SEQUENCE_RESET)
                        .msgSeqNum(1)
                        .bool(GapFillFlag.Tag(), true)
                        .integer(NewSeqNo.Tag(), 4)
                    .build());
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }
    }
}
