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

import stirling.lang.Predicate;

import stirling.fix.messages.Field;
import stirling.fix.messages.Message;
import stirling.fix.messages.fix42.MsgTypeValue;
import stirling.fix.messages.Reject;
import stirling.fix.messages.Required;
import stirling.fix.messages.SequenceReset;
import stirling.fix.tags.fix42.BeginSeqNo;
import stirling.fix.tags.fix42.EndSeqNo;
import stirling.fix.tags.fix42.GapFillFlag;
import stirling.fix.tags.fix42.NewSeqNo;
import stirling.fix.tags.fix42.RefSeqNo;

@RunWith(JDaveRunner.class) public class RecvResendRequestSpec extends InitiatorSpecification {
    public class InitializedSession {
        /* Ref ID 8: Valid Resend Request */
        public void resendSingleMessage() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(MsgTypeValue.HEARTBEAT)
                        .msgSeqNum(2)
                    .build());
            server.expect(MsgTypeValue.HEARTBEAT);
            server.expect(MsgTypeValue.REJECT);

            int msgNum = 2;

            server.respondResendRequest(++msgNum, 2, 2);
            server.expect(MsgTypeValue.SEQUENCE_RESET, new GapFillValidator(2, 3));

            server.respondResendRequest(++msgNum, 3, 3);
            server.expect(MsgTypeValue.REJECT, new ResendValidator(3));

            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                    session.heartbeat(connection);
                    session.send(connection, normalMessage());
                }
            });
        }

        /* Ref ID 8: Valid Resend Request */
        public void resendOldMessages() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(MsgTypeValue.HEARTBEAT)
                        .msgSeqNum(2)
                    .build());
            server.expect(MsgTypeValue.HEARTBEAT);
            server.expect(MsgTypeValue.HEARTBEAT);
            server.expect(MsgTypeValue.REJECT);
            server.expect(MsgTypeValue.REJECT);
            server.expect(MsgTypeValue.HEARTBEAT);
            server.expect(MsgTypeValue.HEARTBEAT);

            int msgNum = 2;

            server.respondResendRequest(++msgNum, 1, 4);
            server.expect(MsgTypeValue.SEQUENCE_RESET, new GapFillValidator(1, 4));
            server.expect(MsgTypeValue.REJECT, new ResendValidator(4));

            server.respondResendRequest(++msgNum, 1, 5);
            server.expect(MsgTypeValue.SEQUENCE_RESET, new GapFillValidator(1, 4));
            server.expect(MsgTypeValue.REJECT, new ResendValidator(4));
            server.expect(MsgTypeValue.REJECT, new ResendValidator(5));

            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                    session.heartbeat(connection);
                    session.heartbeat(connection);
                    session.send(connection, normalMessage());
                    session.send(connection, normalMessage());
                    session.heartbeat(connection);
                    session.heartbeat(connection);
                }
            });
        }

        /* Ref ID 8: Valid Resend Request */
        public void resendLastMessages() throws Exception {
            server.expect(MsgTypeValue.LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(MsgTypeValue.HEARTBEAT)
                        .msgSeqNum(2)
                    .build());
            server.expect(MsgTypeValue.HEARTBEAT);
            server.expect(MsgTypeValue.HEARTBEAT);
            server.expect(MsgTypeValue.HEARTBEAT);
            server.expect(MsgTypeValue.HEARTBEAT);
            server.expect(MsgTypeValue.HEARTBEAT);
            server.expect(MsgTypeValue.REJECT);
            server.expect(MsgTypeValue.REJECT);
            server.expect(MsgTypeValue.HEARTBEAT);
            server.expect(MsgTypeValue.HEARTBEAT);
            server.expect(MsgTypeValue.REJECT);
            server.expect(MsgTypeValue.HEARTBEAT);

            int msgNum = 2;

            server.respondResendRequest(++msgNum, 1, 12);
            server.expect(MsgTypeValue.SEQUENCE_RESET, new GapFillValidator(1, 7));
            server.expect(MsgTypeValue.REJECT, new ResendValidator(7));
            server.expect(MsgTypeValue.REJECT, new ResendValidator(8));
            server.expect(MsgTypeValue.SEQUENCE_RESET, new GapFillValidator(9, 11));
            server.expect(MsgTypeValue.REJECT, new ResendValidator(11));
            server.expect(MsgTypeValue.SEQUENCE_RESET, new GapFillValidator(12, 13));

            server.respondResendRequest(++msgNum, 2, 12);
            server.expect(MsgTypeValue.SEQUENCE_RESET, new GapFillValidator(2, 7));
            server.expect(MsgTypeValue.REJECT, new ResendValidator(7));
            server.expect(MsgTypeValue.REJECT, new ResendValidator(8));
            server.expect(MsgTypeValue.SEQUENCE_RESET, new GapFillValidator(9, 11));
            server.expect(MsgTypeValue.REJECT, new ResendValidator(11));
            server.expect(MsgTypeValue.SEQUENCE_RESET, new GapFillValidator(12, 13));

            server.respondResendRequest(++msgNum, 6, 0);
            server.expect(MsgTypeValue.SEQUENCE_RESET, new GapFillValidator(6, 7));
            server.expect(MsgTypeValue.REJECT, new ResendValidator(7));
            server.expect(MsgTypeValue.REJECT, new ResendValidator(8));
            server.expect(MsgTypeValue.SEQUENCE_RESET, new GapFillValidator(9, 11));
            server.expect(MsgTypeValue.REJECT, new ResendValidator(11));
            server.expect(MsgTypeValue.SEQUENCE_RESET, new GapFillValidator(12, 13));

            server.respondResendRequest(++msgNum, 8, 0);
            server.expect(MsgTypeValue.REJECT, new ResendValidator(8));
            server.expect(MsgTypeValue.SEQUENCE_RESET, new GapFillValidator(9, 11));
            server.expect(MsgTypeValue.REJECT, new ResendValidator(11));
            server.expect(MsgTypeValue.SEQUENCE_RESET, new GapFillValidator(12, 13));

            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                    session.heartbeat(connection);
                    session.heartbeat(connection);
                    session.heartbeat(connection);
                    session.heartbeat(connection);
                    session.heartbeat(connection);
                    session.send(connection, normalMessage());
                    session.send(connection, normalMessage());
                    session.heartbeat(connection);
                    session.heartbeat(connection);
                    session.send(connection, normalMessage());
                    session.heartbeat(connection);
                }
            });
        }
    }

    private Message normalMessage() {
        return new MessageBuilder(MsgTypeValue.REJECT)
                .integer(RefSeqNo.Tag(), 2)
                .build();
    }

    private class GapFillValidator implements Predicate<Message> {
        private final int expectedMsgSeqNum;
        private final int expectedNewSeqNo;
        GapFillValidator(int msgSeqNum, int newSeqNo) {
            expectedMsgSeqNum = msgSeqNum;
            expectedNewSeqNo = newSeqNo;
        }
        @Override public boolean apply(Message msg) {
            return msg.getMsgType().equals(MsgTypeValue.SEQUENCE_RESET) &&
                    expectedMsgSeqNum == msg.getMsgSeqNum() &&
                    msg.getBoolean(GapFillFlag.Tag()) &&
                    msg.getInteger(NewSeqNo.Tag()).intValue() == expectedNewSeqNo;
        }
    }

    private class ResendValidator implements Predicate<Message> {
        private final int expectedMsgSeqNum;
        ResendValidator(int msgSeqNum) {
            expectedMsgSeqNum = msgSeqNum;
        }
        @Override public boolean apply(Message msg) {
            return msg.getMsgType().equals(MsgTypeValue.REJECT) &&
                    expectedMsgSeqNum == msg.getMsgSeqNum() &&
                    msg.getPossDupFlag();
        }
    }
}
