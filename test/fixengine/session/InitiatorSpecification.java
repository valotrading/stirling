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

import static org.joda.time.DateTimeZone.UTC;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import jdave.Specification;
import lang.DefaultTimeSource;
import lang.Predicate;

import org.jmock.Expectations;
import org.jmock.internal.ExpectationBuilder;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import silvertip.Connection;
import silvertip.Events;
import fixengine.Config;
import fixengine.Version;
import fixengine.messages.BooleanField;
import fixengine.messages.DefaultMessageComparator;
import fixengine.messages.DefaultMessageVisitor;
import fixengine.messages.EnumField;
import fixengine.messages.Field;
import fixengine.messages.FixMessageParser;
import fixengine.messages.FloatField;
import fixengine.messages.IntegerField;
import fixengine.messages.Message;
import fixengine.messages.MessageHeader;
import fixengine.messages.MessageVisitor;
import fixengine.messages.MsgTypeValue;
import fixengine.messages.Parser;
import fixengine.messages.RawMessageBuilder;
import fixengine.messages.StringField;
import fixengine.messages.Tag;
import fixengine.messages.Value;
import fixengine.messages.fix42.DefaultMessageFactory;
import fixengine.session.store.InMemorySessionStore;
import fixengine.session.store.MongoSessionStore;
import fixengine.session.store.SessionStore;
import fixengine.tags.fix42.BeginSeqNo;
import fixengine.tags.fix42.BeginString;
import fixengine.tags.fix42.BodyLength;
import fixengine.tags.fix42.EncryptMethod;
import fixengine.tags.fix42.EndSeqNo;
import fixengine.tags.fix42.HeartBtInt;
import fixengine.tags.fix42.MsgType;
import fixengine.tags.fix42.OrigSendingTime;
import fixengine.tags.fix42.SenderCompID;
import fixengine.tags.fix42.SendingTime;
import fixengine.tags.fix42.TargetCompID;

public class InitiatorSpecification extends Specification<Session> {
    private static final String dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss.000'Z'";
    private static final DateTimeFormatter fmt = DateTimeFormat.forPattern(dateTimeFormat);

    protected static final long LOGOUT_RESPONSE_TIMEOUT_MSEC = 1000;
    private static final Version VERSION = Version.FIX_4_2;
    protected static final String INITIATOR = "initiator";
    protected static final String ACCEPTOR = "OPENFIX";

    private static final Random generator = new Random();

    protected static final long HEARTBEAT_INTERVAL_MSEC = 1000;
    protected final SimpleAcceptor server = new SimpleAcceptor(1024 + generator.nextInt(1024));
    protected Connection connection;
    protected TestSession session;
    private final Logger logger = mock(Logger.class);
    private long sessionTimeShift;
    private long heartBeatIntervalMSec = 1000;

    private Config config = new Config() {
        {
            setSenderCompId(INITIATOR);
            setTargetCompId(ACCEPTOR);
            setVersion(VERSION);
        }
    };

    protected ExpectationBuilder expectLogSevere(final String message) {
        return new Expectations() {{
            one(logger).severe(message);
        }};
    }

    protected ExpectationBuilder expectLogWarning(final String message) {
        return new Expectations() {{
            one(logger).warning(message);
        }};
    }

    protected void setHeartBeatInterval(long intervalInMSec) {
        heartBeatIntervalMSec = intervalInMSec;
    }

    protected long getHearbeatIntervalInMillis() {
        return heartBeatIntervalMSec;
    }

    protected int getHeartbeatIntervalInSeconds() {
        return (int) (heartBeatIntervalMSec/1000);
    }

    protected void shiftSessionTimeBy(long millis) {
        sessionTimeShift += millis;
    }

    protected void runInClient(Runnable command) throws Exception {
        runInClient(command, false);
    }

    protected void runInClient(Runnable command, boolean keepAlive) throws Exception {
        runInClient(command, keepAlive, 1000);
    }

    protected void runInClient(Runnable command, boolean keepAlive, long eventsIdleMSec) throws Exception {
        runInClient(command, new DefaultMessageVisitor(), keepAlive, 1000);
    }

    protected void runInClient(Runnable command, MessageVisitor visitor, boolean keepAlive, long eventsIdleMSec) throws Exception {
        runInClient(new Runnable() { @Override public void run() { } }, command, visitor, keepAlive, eventsIdleMSec);
    }

    protected void runInClient(Runnable beforeConnecting, Runnable afterConnected, MessageVisitor visitor, boolean keepAlive, long eventsIdleMSec) throws Exception {
        Thread serverThread = new Thread(server);
        serverThread.start();
        server.awaitForStart();
        Events events = Events.open(eventsIdleMSec);
        session = new TestSession();
        beforeConnecting.run();
        connection = openConnection(session, visitor, server.getPort(), keepAlive);
        events.register(connection);
        afterConnected.run();
        events.dispatch();
        server.stop();
        specify(server.passed());
    }

    protected void logonHeartbeat() throws Exception {
        server.expect(MsgTypeValue.LOGON);
        server.respondLogon();
        server.respond(
                new MessageBuilder(MsgTypeValue.HEARTBEAT)
                    .msgSeqNum(2)
                .build());
        runInClient(new Runnable() {
            @Override public void run() {
                session.logon(connection);
            }
        });
    }

    protected RawMessageBuilder message() {
        return new RawMessageBuilder();
    }

    protected RawMessageBuilder message(String bodyLength, String msgType) {
        return message()
                .field(BeginString.Tag(), "FIX.4.2")
                .field(BodyLength.Tag(), bodyLength)
                .field(MsgType.Tag(), msgType)
                .field(SenderCompID.Tag(), ACCEPTOR)
                .field(TargetCompID.Tag(), INITIATOR);
    }

    protected String formatDateTime(DateTime dateTime) {
        return fmt.withZone(UTC).print(dateTime);
    }

    protected class MessageBuilder {
        private final Message message;

        public MessageBuilder(String type) {
            MessageHeader header = new MessageHeader(type);
            header.setBeginString(VERSION.value());
            header.setString(SenderCompID.Tag(), ACCEPTOR);
            header.setString(TargetCompID.Tag(), INITIATOR);
            header.setDateTime(SendingTime.Tag(), new DefaultTimeSource().currentTime());
            if (MsgTypeValue.SEQUENCE_RESET.equals(type)) {
                header.setDateTime(OrigSendingTime.Tag(), header.getDateTime(SendingTime.Tag()));
            }
            this.message = new DefaultMessageFactory().create(type, header);
        }

        public MessageBuilder setOnBehalfOfCompId(String onBehalfOfCompId) {
            message.setOnBehalfOfCompId(onBehalfOfCompId);
            return this;
        }

        public MessageBuilder setDeliverToCompId(String deliverToCompId) {
            message.setDeliverToCompId(deliverToCompId);
            return this;
        }

        public MessageBuilder setBeginString(String beginString) {
            message.setBeginString(beginString);
            return this;
        }

        public MessageBuilder setSendingTime(DateTime sendingTime) {
            message.setSendingTime(sendingTime);
            return this;
        }

        public MessageBuilder setSenderCompID(String senderCompID) {
            message.setSenderCompId(senderCompID);
            return this;
        }

        public MessageBuilder setTargetCompID(String targetCompID) {
            message.setTargetCompId(targetCompID);
            return this;
        }

        public MessageBuilder msgSeqNum(int msgSeqNum) {
            message.setMsgSeqNum(msgSeqNum);
            return this;
        }

        public MessageBuilder setPossDupFlag(boolean possDupFlag) {
            message.setPossDupFlag(possDupFlag);
            return this;
        }

        public MessageBuilder setOrigSendingTime(DateTime origSendingTime) {
            message.setOrigSendingTime(origSendingTime);
            return this;
        }

        public MessageBuilder setPossResend(boolean possResend) {
            message.setPossResend(possResend);
            return this;
        }

        public MessageBuilder string(Tag<StringField> tag, String value) {
            message.setString(tag, value);
            return this;
        }

        public MessageBuilder integer(Tag<IntegerField> tag, Integer value) {
            message.setInteger(tag, value);
            return this;
        }

        public MessageBuilder float0(Tag<? extends FloatField> tag, Double value) {
            message.setFloat(tag, value);
            return this;
        }

        public MessageBuilder bool(Tag<BooleanField> tag, boolean value) {
            message.setBoolean(tag, value);
            return this;
        }

        <T> MessageBuilder enumeration(Tag<? extends EnumField<Value<T>>> tag, Value<T> value) {
            message.setEnum(tag, value);
            return this;
        }

        public MessageBuilder integer(int value) {
            return this;
        }

        public Message build() {
            return message;
        }
    }

    protected class SimpleAcceptor implements Runnable {
        private final CountDownLatch serverStopped = new CountDownLatch(1);
        private final CountDownLatch serverStarted = new CountDownLatch(1);
        private final List<Runnable> commands = new ArrayList<Runnable>();
        private Socket clientSocket;
        private int successCount;
        private int failureCount;
        private final int port;

        private SimpleAcceptor(int port) {
            this.port = port;
        }

        public int getPort() {
            return port;
        }

        public boolean passed() {
            return failureCount == 0 && successCount == commands.size();
        }

        public void respondLogon() {
            respond(
                    new MessageBuilder(MsgTypeValue.LOGON)
                        .msgSeqNum(1)
                        .integer(HeartBtInt.Tag(), getHeartbeatIntervalInSeconds())
                        .enumeration(EncryptMethod.Tag(), EncryptMethod.None())
                    .build());
        }

        public void respondLogout(int msgSeqNum) {
            respond(
                    new MessageBuilder(MsgTypeValue.LOGOUT)
                        .msgSeqNum(msgSeqNum)
                    .build());
        }

        public void respondResendRequest(int msgSeqNum, int beginSeqNo, int endSeqNo) {
            respond(
                    new MessageBuilder(MsgTypeValue.RESEND_REQUEST)
                        .msgSeqNum(msgSeqNum)
                        .integer(BeginSeqNo.Tag(), beginSeqNo)
                        .integer(EndSeqNo.Tag(), endSeqNo)
                    .build());
        }

        protected void respond(final Message message) {
            respond(message.format());
        }

        protected void respond(final Message message, final long delaySendingByMilliseconds) {
            respond(message.format(), delaySendingByMilliseconds);
        }

        protected void respond(final String raw, final long delaySendingByMilliseconds) {
            this.commands.add(new Runnable() {
                @Override public void run() {
                    try {
                        Thread.sleep(delaySendingByMilliseconds);
                        clientSocket.getOutputStream().write(raw.getBytes());
                        successCount++;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        protected void respond(final String raw) {
            this.commands.add(new Runnable() {
                @Override public void run() {
                    try {
                        clientSocket.getOutputStream().write(raw.getBytes());
                        successCount++;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        public void expect(final String type) {
            expect(type, new Predicate<Message>() {
                @Override public boolean apply(Message msg) {
                    return msg.getMsgType().equals(type);
                }
            });
        }

        public void expect(final String type, final Predicate<Message> validator) {
            this.commands.add(new Runnable() {
                @Override public void run() {
                    StringBuilder raw = parse();
                    if (raw.length() == 0)
                        return;
                    Parser.parse(new fixengine.messages.FixMessage(raw.toString().getBytes(), type), new Parser.Callback() {
                        @Override public void message(Message m) {
                            if (validator.apply(m))
                                successCount++;
                            else
                                failureCount++;
                        }

                        @Override public void unsupportedMsgType(String msgType, int msgSeqNum) {
                        }

                        @Override public void invalidMsgType(String msgType, int msgSeqNum) {
                        }

                        @Override public void invalidMessage(int msgSeqNum, Value<Integer> reason, String text) {
                        }
                    });
                }

                private StringBuilder parse() {
                    StringBuilder raw = new StringBuilder();
                    InputStream reader;
                    try {
                        reader = clientSocket.getInputStream();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    for (;;) {
                        int c = -1;
                        try {
                            c = reader.read();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        if (c == -1)
                            break;
                        raw.append((char) c);
                        if (raw.toString().contains("10=")) {
                            do {
                                try {
                                    c = reader.read();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                raw.append((char) c);
                            } while (c != Field.DELIMITER);
                            break;
                        }
                    }
                    return raw;
                }
            });
        }

        public void stop() {
            try {
                serverStopped.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public void awaitForStart() throws InterruptedException {
            serverStarted.await();
        }

        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = newSocket();
                serverStarted.countDown();
                clientSocket = serverSocket.accept();
                for (Runnable c : commands) {
                    c.run();
                }
                clientSocket.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    } catch (IOException ex) {
                    }
                }
                serverStopped.countDown();
            }
        }

        private ServerSocket newSocket() throws IOException {
            try {
                return new ServerSocket(port);
            } finally {
                serverStarted.countDown();
            }
        }
    }

    protected Connection openConnection(final Session session, final MessageVisitor messageVisitor, int port, final boolean keepAlive) throws IOException {
        return Connection.connect(new InetSocketAddress("localhost", port), new FixMessageParser(), new Connection.Callback<fixengine.messages.FixMessage>() {
            @Override public void messages(Connection<fixengine.messages.FixMessage> conn, Iterator<fixengine.messages.FixMessage> messages) {
                while (messages.hasNext())
                    session.receive(conn, messages.next(), messageVisitor);
            }

            @Override public void idle(Connection<fixengine.messages.FixMessage> conn) {
                if (keepAlive)
                    session.keepAlive(conn);
                else
                    conn.close();
            }

            @Override public void closed(Connection<fixengine.messages.FixMessage> conn) {
            }

            @Override public void garbledMessage(String message, byte[] data) {
                logger.warning(message);
            }
        });
    }

    private SessionStore getSessionStore() throws Exception {
        String value = System.getProperty("fixengine.session.store");
        if ("mongo".equals(value)) {
            SessionStore store = new MongoSessionStore("localhost", 27017);
            store.clear(INITIATOR, ACCEPTOR);
            return store;
        }
        return new InMemorySessionStore();
    }

    protected class TestSession extends Session {
        protected TestSession() throws Exception {
            super(HeartBtIntValue.milliseconds(getHearbeatIntervalInMillis()), InitiatorSpecification.this.config,
                getSessionStore(), new DefaultMessageFactory(), new DefaultMessageComparator());
        }

        protected MessageQueue<Message> getOutgoingMsgQueue() {
            return outgoingQueue;
        }

        @Override protected long getLogoutResponseTimeoutMsec() {
            return LOGOUT_RESPONSE_TIMEOUT_MSEC;
        }

        @Override protected Logger getLogger() {
            return logger;
        }

        @Override public DateTime currentTime() {
            return super.currentTime().plus(sessionTimeShift);
        }
    }
}
