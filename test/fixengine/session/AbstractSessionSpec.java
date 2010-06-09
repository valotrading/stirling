/*
 * Copyright 2008 the original author or authors.
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

import static fixengine.messages.Field.DELIMITER;
import jdave.Block;
import jdave.Specification;
import jdave.junit4.JDaveRunner;
import lang.TimeSource;

import org.jmock.Expectations;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.runner.RunWith;

import fixengine.Config;
import fixengine.Version;
import fixengine.io.ObjectOutputStream;
import fixengine.messages.ExecutionReportMessage;
import fixengine.messages.GarbledMessage;
import fixengine.messages.HeartbeatMessage;
import fixengine.messages.LogoutMessage;
import fixengine.messages.Message;
import fixengine.messages.MessageVisitor;
import fixengine.messages.NullMessage;
import fixengine.messages.OrderQtyField;
import fixengine.messages.RejectMessage;
import fixengine.messages.ResendRequestMessage;
import fixengine.messages.SequenceResetMessage;
import fixengine.messages.TestReqIdField;
import fixengine.messages.TimeInForceField;
import fixengine.messages.TokenStream;
import fixengine.session.store.SessionStore;

/**
 * @author Pekka Enberg
 */
@RunWith(JDaveRunner.class)
public class AbstractSessionSpec extends Specification<AbstractSession> {
    private static final String BEGIN_STRING = Version.FIX_4_3.value();
    private static final String INITIATOR = "initiator";
    private static final String ACCEPTOR = "acceptor";
    private static final Config config = new Config().setSenderCompId(
            INITIATOR).setTargetCompId(ACCEPTOR).setVersion(
            Version.FIX_4_3);

    @SuppressWarnings("unchecked")
    private final ObjectOutputStream<Message> stream = mock(ObjectOutputStream.class);
    private final SessionStore store = mock(SessionStore.class);
    private final AbstractSession session = new AbstractSession(stream, config, store) {
        @Override
        public void logon() {
            authenticated = true;
        }
    };

    public class NewSession {
        public AbstractSession create() {
            return session;
        }

        public void isNotAuthenticated() {
            specify(session.isAuthenticated(), must.equal(false));
        }

        public void isNotOutOfSync() {
            specify(session.isOutOfSync(), must.equal(false));
        }
    }

    public class SessionThatReceivesLogoutForLogout {
        public AbstractSession create() {
            final LogoutMessage logoutMsg = new LogoutMessage();
            logoutMsg.setBeginString(BEGIN_STRING);
            logoutMsg.setSenderCompId(ACCEPTOR);
            logoutMsg.setTargetCompId(INITIATOR);
            logoutMsg.setMsgSeqNum(1);
            logoutMsg.setSendingTime(new DateTime());
            checking(new Expectations() {{
                allowing(stream).isClosed(); will(returnValue(false));
                one(stream).writeObject(with(any(LogoutMessage.class)));
                one(stream).close();
            }});
            session.logon();
            session.receive(logoutMsg);
            session.logout();
            return session;
        }

        public void isNotAuthenticated() {
            specify(session.isAuthenticated(), must.equal(false));
        }
    }

    public class SessionThatIsTerminatedByTheOtherEndForLogout {
        public AbstractSession create() {
            checking(new Expectations() {{
                one(stream).writeObject(with(any(LogoutMessage.class)));
                one(stream).close();
                one(stream).isClosed(); will(returnValue(true));
                one(stream).close();
            }});
            session.logon();
            session.receive(new NullMessage());
            session.logout();
            return session;
        }

        public void isNotAuthenticated() {
            specify(session.isAuthenticated(), must.equal(false));
        }
    }

    public class SessionThatIsReceivesBogusMessageForLogout {
        public AbstractSession create() {
            final HeartbeatMessage bogusMsg = new HeartbeatMessage();
            bogusMsg.setBeginString(BEGIN_STRING);
            bogusMsg.setSenderCompId(ACCEPTOR);
            bogusMsg.setTargetCompId(INITIATOR);
            bogusMsg.setMsgSeqNum(1);
            bogusMsg.setSendingTime(new DateTime());
            checking(new Expectations() {{
                allowing(stream).isClosed(); will(returnValue(false));
                one(stream).writeObject(with(any(LogoutMessage.class)));
                one(stream).writeObject(with(any(RejectMessage.class)));
                one(stream).close();
            }});
            session.logon();
            session.receive(bogusMsg);
            session.logout();
            return session;
        }

        public void isNotAuthenticated() {
            specify(session.isAuthenticated(), must.equal(false));
        }
    }

    public class SessionThatReceivesLogoutInitiatedByTheOtherSide {
        public AbstractSession create() {
            final LogoutMessage logoutMsg = new LogoutMessage();
            logoutMsg.setBeginString(BEGIN_STRING);
            logoutMsg.setSenderCompId(ACCEPTOR);
            logoutMsg.setTargetCompId(INITIATOR);
            logoutMsg.setMsgSeqNum(1);
            logoutMsg.setSendingTime(new DateTime());
            checking(new Expectations() {{
                allowing(stream).isClosed(); will(returnValue(false));
                one(stream).writeObject(with(any(LogoutMessage.class)));
                one(stream).close();
            }});
            session.logon();
            session.receive(logoutMsg);
            session.processMessage(dummy(MessageVisitor.class));
            return session;
        }

        public void isNotAuthenticated() {
            specify(session.isAuthenticated(), must.equal(false));
        }
    }

    public class SessionThatReceivesThirdPartyMessage {
        private HeartbeatMessage message = new HeartbeatMessage();

        public AbstractSession create() {
            message.setBeginString(BEGIN_STRING);
            message.setSenderCompId(ACCEPTOR);
            message.setTargetCompId(INITIATOR);
            message.setOnBehalfOfCompId("A");
            message.setPossDupFlag(false);
            message.setMsgSeqNum(1);
            message.setSendingTime(new DateTime());
            session.logon();
            return session;
        }

        public void rejectsTheMessage() {
            checking(new Expectations() {{
                allowing(stream).isClosed(); will(returnValue(false));
                one(stream).writeObject(with(any(RejectMessage.class)));
            }});
            session.receive(message);
        }
    }

    public class SessionThatReceivesMessageWithTooLowSequenceNumber {
        private HeartbeatMessage message1 = new HeartbeatMessage();
        private HeartbeatMessage message2 = new HeartbeatMessage();

        public AbstractSession create() {
            message1.setBeginString(BEGIN_STRING);
            message1.setSenderCompId(ACCEPTOR);
            message1.setTargetCompId(INITIATOR);
            message1.setMsgSeqNum(1);
            message1.setSendingTime(new DateTime());
            message2.setBeginString(BEGIN_STRING);
            message2.setSenderCompId(ACCEPTOR);
            message2.setTargetCompId(INITIATOR);
            message2.setMsgSeqNum(1);
            message2.setSendingTime(new DateTime());
            checking(new Expectations() {{
                one(stream).isClosed(); will(returnValue(false));
            }});
            session.receive(message1);
            return session;
        }

        public void terminatesTheConnection() {
            checking(new Expectations() {{
                one(stream).writeObject(with(any(LogoutMessage.class)));
                one(stream).close();
                one(stream).isClosed(); will(returnValue(true));
            }});
            session.receive(message2);
        }
    }

    public class SessionThatReceivesMessageWithTooLowSequenceNumberWithPossDupFlagSet {
        private HeartbeatMessage message1 = new HeartbeatMessage();
        private HeartbeatMessage message2 = new HeartbeatMessage();

        public AbstractSession create() {
            message1.setBeginString(BEGIN_STRING);
            message1.setSenderCompId(ACCEPTOR);
            message1.setTargetCompId(INITIATOR);
            message1.setMsgSeqNum(1);
            message1.setSendingTime(new DateTime(1));
            message2.setBeginString(BEGIN_STRING);
            message2.setSenderCompId(ACCEPTOR);
            message2.setTargetCompId(INITIATOR);
            message2.setMsgSeqNum(1);
            message2.setPossDupFlag(true);
            message2.setOrigSendingTime(new DateTime(2));
            message2.setSendingTime(new DateTime(3));
            checking(new Expectations() {{
                allowing(stream).isClosed(); will(returnValue(false));
            }});
            session.receive(message1);
            return session;
        }

        public void acceptsTheMessage() {
            session.receive(message2);
        }
    }

    public class SessionThatReceivesMessageWithPossDupFlagWithoutOrigSendingTime {
        private HeartbeatMessage message = new HeartbeatMessage();

        public AbstractSession create() {
            message.setBeginString(BEGIN_STRING);
            message.setSenderCompId(ACCEPTOR);
            message.setTargetCompId(INITIATOR);
            message.setPossDupFlag(true);
            message.setOrigSendingTime(null);
            message.setSendingTime(new DateTime());
            message.setMsgSeqNum(1);
            session.logon();
            return session;
        }

        public void rejectsTheMessage() {
            checking(new Expectations() {{
                allowing(stream).isClosed(); will(returnValue(false));
                one(stream).writeObject(with(any(RejectMessage.class)));
            }});
            session.receive(message);
        }
    }

    public class SessionThatReceivesMessageWithPossDupFlagAndInaccurateSendingTime {
        private HeartbeatMessage message = new HeartbeatMessage();

        public AbstractSession create() {
            message.setBeginString(BEGIN_STRING);
            message.setSenderCompId(ACCEPTOR);
            message.setTargetCompId(INITIATOR);
            message.setSendingTime(new DateTime(1));
            message.setOrigSendingTime(new DateTime(2));
            message.setPossDupFlag(true);
            message.setMsgSeqNum(1);
            session.logon();
            return session;
        }

        public void rejectsTheMessage() {
            checking(new Expectations() {{
                one(stream).writeObject(with(any(RejectMessage.class)));
                one(stream).writeObject(with(any(LogoutMessage.class)));
                one(stream).close();
                one(stream).isClosed(); will(returnValue(true));
            }});
            session.receive(message);
        }
    }

    public class SessionThatReceivesMessageWithInvalidBeginString {
        private Message message = new HeartbeatMessage();

        public AbstractSession create() {
            message.setMsgSeqNum(1);
            message.setBeginString(Version.FIX_4_0.value());
            session.logon();
            return session;
        }

        public void terminatesTheConnection() {
            checking(new Expectations() {{
                one(stream).writeObject(with(any(LogoutMessage.class)));
                one(stream).close();
                one(stream).isClosed(); will(returnValue(true));
            }});
            session.receive(message);
        }
    }

    public class SessionThatReceivesMessageWithSenderCompId {
        private Message message = new HeartbeatMessage();

        public AbstractSession create() {
            message.setBeginString(BEGIN_STRING);
            message.setSenderCompId("X");
            message.setMsgSeqNum(1);
            session.logon();
            return session;
        }

        public void rejectsMessageAndTerminatesTheConnection() {
            checking(new Expectations() {{
                one(stream).writeObject(with(any(RejectMessage.class)));
                one(stream).writeObject(with(any(LogoutMessage.class)));
                one(stream).close();
                one(stream).isClosed(); will(returnValue(true));
            }});
            session.receive(message);
        }
    }

    public class SessionThatHasReceivesMessagesOutOfOrder {
        private HeartbeatMessage message1 = new HeartbeatMessage();
        private HeartbeatMessage message2 = new HeartbeatMessage();
        private HeartbeatMessage message3 = new HeartbeatMessage();

        public AbstractSession create() {
            checking(new Expectations() {{
                allowing(stream).isClosed(); will(returnValue(false));
                one(stream).writeObject(with(any(ResendRequestMessage.class)));
            }});
            message1.setBeginString(BEGIN_STRING);
            message1.setSenderCompId(ACCEPTOR);
            message1.setTargetCompId(INITIATOR);
            message1.setMsgSeqNum(1);
            message1.setSendingTime(new DateTime());
            message2.setBeginString(BEGIN_STRING);
            message2.setSenderCompId(ACCEPTOR);
            message2.setTargetCompId(INITIATOR);
            message2.setMsgSeqNum(2);
            message2.setSendingTime(new DateTime());
            message3.setBeginString(BEGIN_STRING);
            message3.setSenderCompId(ACCEPTOR);
            message3.setTargetCompId(INITIATOR);
            message3.setMsgSeqNum(3);
            message3.setSendingTime(new DateTime());
            session.receive(message3);
            return session;
        }

        public void isOutOfSync() {
            specify(session.isOutOfSync(), must.equal(true));
        }

        public void isNotOutOfSyncIfTheOtherEndResendsMissingMessages() {
            session.receive(message1);
            session.receive(message2);
            session.receive(message3);
            specify(session.isOutOfSync(), must.equal(false));
        }

        public void isNotOutOfSyncIfTheOtherFillsTheGap() {
            final SequenceResetMessage seqReset = new SequenceResetMessage();
            seqReset.setBeginString(BEGIN_STRING);
            seqReset.setSenderCompId(ACCEPTOR);
            seqReset.setTargetCompId(INITIATOR);
            seqReset.setMsgSeqNum(1);
            seqReset.setGapFillFlag(true);
            seqReset.setNewSeqNo(3);
            seqReset.setSendingTime(new DateTime());
            session.receive(seqReset);
            specify(session.isOutOfSync(), must.equal(false));
        }

        public void isNotOutOfSyncIfTheOtherEndForcesSequenceReset() {
            final SequenceResetMessage seqReset = new SequenceResetMessage();
            seqReset.setBeginString(BEGIN_STRING);
            seqReset.setSenderCompId(ACCEPTOR);
            seqReset.setTargetCompId(INITIATOR);
            seqReset.setMsgSeqNum(3);
            seqReset.setGapFillFlag(false);
            seqReset.setNewSeqNo(4);
            seqReset.setSendingTime(new DateTime());
            session.receive(seqReset);
            specify(session.isOutOfSync(), must.equal(false));
        }

        public void raisesExceptionIfTheOtherEndTriesToFillGapWithOutOfOrderSequenceNumber() {
            final SequenceResetMessage seqReset = new SequenceResetMessage();
            seqReset.setBeginString(BEGIN_STRING);
            seqReset.setSenderCompId(ACCEPTOR);
            seqReset.setTargetCompId(INITIATOR);
            seqReset.setMsgSeqNum(3);
            seqReset.setGapFillFlag(true);
            seqReset.setNewSeqNo(3);
            seqReset.setSendingTime(new DateTime());
            specify(new Block() {
                @Override
                public void run() throws Throwable {
                    session.receive(seqReset);
                }
            }, must.raise(InvalidSequenceResetException.class));
        }
    }

    public class SessionThatReceivesGarbledMessage {
        public AbstractSession create() {
            session.logon();
            return session;
        }

        public void ignoresTheMessage() {
            session.receive(new GarbledMessage());
        }
    }

    public class SessionThatReceivesMessageWithSendingTimeThatIsNotWithinReasonableTime {
        private DateTime currentTime = new DateTime(DateTimeZone.UTC);
        private TimeSource timeSource = mock(TimeSource.class);
        private HeartbeatMessage message = new HeartbeatMessage();

        public AbstractSession create() {
            message.setBeginString(BEGIN_STRING);
            message.setSenderCompId(ACCEPTOR);
            message.setTargetCompId(INITIATOR);
            message.setSendingTime(currentTime.plusMinutes(3));
            message.setMsgSeqNum(1);
            session.setTimeSource(timeSource);
            session.logon();
            return session;
        }

        public void rejectsTheMessageAndTerminatesConnection() {
            checking(new Expectations() {{
                allowing(timeSource).currentTime(); will(returnValue(currentTime));
                allowing(stream).isClosed(); will(returnValue(false));
                one(stream).writeObject(with(any(RejectMessage.class)));
                one(stream).writeObject(with(any(LogoutMessage.class)));
                one(stream).close();
            }});
            session.receive(message);
        }
    }

    public class SessionThatReceivesMessageWithEmptyField {
        private HeartbeatMessage message = new HeartbeatMessage();

        public AbstractSession create() {
            message.setBeginString(BEGIN_STRING);
            message.setSenderCompId(ACCEPTOR);
            message.setTargetCompId(INITIATOR);
            message.setMsgSeqNum(1);
            message.setSendingTime(new DateTime());
            message.parseField(TestReqIdField.TAG, new TokenStream("=" + DELIMITER));
            session.logon();
            return session;
        }

        public void rejectsTheMessageAndTerminatesConnection() {
            checking(new Expectations() {{
                allowing(stream).isClosed(); will(returnValue(false));
                one(stream).writeObject(with(any(RejectMessage.class)));
            }});
            session.receive(message);
        }
    }

    public class SessionThatReceivesMessageWithInvalidFieldFormat {
        private ExecutionReportMessage message = new ExecutionReportMessage();

        public AbstractSession create() {
            message.setBeginString(BEGIN_STRING);
            message.setSenderCompId(ACCEPTOR);
            message.setTargetCompId(INITIATOR);
            message.setMsgSeqNum(1);
            message.setSendingTime(new DateTime());
            message.parseField(OrderQtyField.TAG, new TokenStream("=Z" + DELIMITER));
            session.logon();
            return session;
        }

        public void rejectsTheMessageAndTerminatesConnection() {
            checking(new Expectations() {{
                allowing(stream).isClosed(); will(returnValue(false));
                one(stream).writeObject(with(any(RejectMessage.class)));
            }});
            session.receive(message);
        }
    }

    public class SessionThatReceivesMessageWithInvalidFieldValue {
        private ExecutionReportMessage message = new ExecutionReportMessage();

        public AbstractSession create() {
            message.setBeginString(BEGIN_STRING);
            message.setSenderCompId(ACCEPTOR);
            message.setTargetCompId(INITIATOR);
            message.setMsgSeqNum(1);
            message.setSendingTime(new DateTime());
            message.parseField(TimeInForceField.TAG, new TokenStream("=Z" + DELIMITER));
            session.logon();
            return session;
        }

        public void rejectsTheMessageAndTerminatesConnection() {
            checking(new Expectations() {{
                allowing(stream).isClosed(); will(returnValue(false));
                one(stream).writeObject(with(any(RejectMessage.class)));
            }});
            session.receive(message);
        }
    }

    public class SessionThatReceivesMessageWithoutAllMandatoryFields {
        private ExecutionReportMessage message = new ExecutionReportMessage();

        public AbstractSession create() {
            message.setBeginString(BEGIN_STRING);
            message.setSenderCompId(ACCEPTOR);
            message.setTargetCompId(INITIATOR);
            message.setMsgSeqNum(1);
            message.setSendingTime(new DateTime());
            session.logon();
            return session;
        }

        public void rejectsTheMessageAndTerminatesConnection() {
            checking(new Expectations() {{
                allowing(stream).isClosed(); will(returnValue(false));
                one(stream).writeObject(with(any(RejectMessage.class)));
            }});
            session.receive(message);
        }
    }}
