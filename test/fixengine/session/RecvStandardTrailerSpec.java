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
import fixengine.tags.BeginString;
import fixengine.tags.BodyLength;
import fixengine.tags.CheckSum;
import fixengine.tags.MsgSeqNum;
import fixengine.tags.MsgType;
import fixengine.tags.SenderCompID;
import fixengine.tags.SendingTime;
import fixengine.tags.TargetCompID;

@RunWith(JDaveRunner.class) public class RecvStandardTrailerSpec extends InitiatorSpecification {
    public class InitializedSession {
        /* Ref ID 3: a. Valid CheckSum */
        public void valid() throws Exception {
            logonHeartbeat();
            specify(session.getIncomingSeq().peek(), 3);
        }

        /* Ref ID 3: b. Invalid CheckSum */
        public void invalid() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    message()
                    .field(BeginString.Tag(), "FIX.4.2")
                    .field(BodyLength.TAG, "55")
                    .field(MsgType.Tag(), "0")
                    .field(SenderCompID.Tag(), "OPENFIX")
                    .field(TargetCompID.Tag(), "initiator")
                    .field(MsgSeqNum.TAG, "2")
                    .field(SendingTime.TAG, "20100810-07:25:02")
                    .field(CheckSum.Tag(), "100")
                    .toString());
            checking(expectLogWarning("CheckSum(10) mismatch, expected=239, actual=100"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 2);
        }

        /* Ref ID 3: c. Garbled message */
        public void garbledMessage() throws Exception {
            missingCheckSumField();
        }

        /* Ref ID 3: d. CheckSum is last field of message, value has length of
         * 3, and is delimited by <SOH>. */
        public void checkSumIsLastFieldOfMsgEtc() throws Exception {
            logonHeartbeat();
            specify(session.getIncomingSeq().peek(), 3);
        }

        /* Ref ID 3: e. CheckSum is not the last field of message. */
        public void checkSumIsNotTheLastFieldOfMsg() throws Exception {
            missingCheckSumField();
        }

        /* Ref ID 3: e. CheckSum value does not have length of 3. */
        public void checkSumValueDoesNotHaveLengthOfThree() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    message()
                    .field(BeginString.Tag(), "FIX.4.2")
                    .field(BodyLength.TAG, "56")
                    .field(MsgType.Tag(), "0")
                    .field(SenderCompID.Tag(), "acceptor")
                    .field(TargetCompID.Tag(), "initiator")
                    .field(MsgSeqNum.TAG, "2")
                    .field(SendingTime.TAG, "20100810-07:58:22")
                    .field(CheckSum.Tag(), "48")
                    .toString());
            checking(expectLogWarning("CheckSum(10) has invalid length: 48"));
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 2);
        }

        /* Ref ID 3: e. CheckSum is not delimited by <SOH>. */
        private void checkSumIsNotDelimitedBySOH() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            String msg = message()
                .field(BeginString.Tag(), "FIX.4.2")
                .field(BodyLength.TAG, "55")
                .field(MsgType.Tag(), "0")
                .field(SenderCompID.Tag(), "OPENFIX")
                .field(TargetCompID.Tag(), "initiator")
                .field(MsgSeqNum.TAG, "2")
                .field(SendingTime.TAG, "20100810-07:25:02")
                .field(CheckSum.Tag(), "239").toString();
            server.respond(msg.substring(0, msg.length() - 1));
            /* FIXME: Currently this error condition is never reported to
             * Session. */
            //checking(new Expectations() {{
            //    one(logger).severe("CheckSum(10): "));
            //}});
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
            specify(session.getIncomingSeq().peek(), 2);
        }

        private void missingCheckSumField() throws Exception {
            /* TODO: Implement me. */
        }
    }
}
