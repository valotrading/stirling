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

import static fixengine.messages.MsgTypeValue.HEARTBEAT;
import static fixengine.messages.MsgTypeValue.LOGON;
import static fixengine.messages.MsgTypeValue.LOGOUT;
import static fixengine.messages.MsgTypeValue.RESEND_REQUEST;
import static fixengine.messages.MsgTypeValue.TEST_REQUEST;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import jdave.Specification;
import jdave.junit4.JDaveRunner;
import lang.DefaultTimeSource;

import org.joda.time.DateTime;
import org.junit.runner.RunWith;

import silvertip.Connection;
import silvertip.Events;
import silvertip.protocols.FixMessageParser;
import fixengine.Config;
import fixengine.Version;
import fixengine.messages.BooleanField;
import fixengine.messages.DefaultMessageVisitor;
import fixengine.messages.EncryptMethodValue;
import fixengine.messages.EnumField;
import fixengine.messages.Field;
import fixengine.messages.Formattable;
import fixengine.messages.IntegerField;
import fixengine.messages.Message;
import fixengine.messages.MessageHeader;
import fixengine.messages.MsgTypeValue;
import fixengine.messages.Parser;
import fixengine.messages.SessionRejectReasonValue;
import fixengine.messages.StringField;
import fixengine.messages.Tag;
import fixengine.session.store.SessionStore;
import fixengine.tags.EncryptMethod;
import fixengine.tags.HeartBtInt;
import fixengine.tags.SenderCompID;
import fixengine.tags.SendingTime;
import fixengine.tags.TargetCompID;
import fixengine.tags.TestReqID;

@RunWith(JDaveRunner.class) public class InitiatorSpec extends Specification<Void> {
    private static final Version VERSION = Version.FIX_4_2;
    private static final String INITIATOR = "initiator";
    private static final String ACCEPTOR = "OPENFIX";
    private static final int HEARTBEAT_INTERVAL = 30;
    private static final int PORT = 7001;

    private final SimpleAcceptor server = new SimpleAcceptor(PORT);
    private Connection connection;
    private Session session;

    public class Logon_1B {
        public void valid() throws Exception {
            server.expect(LOGON);
            server.respondLogon();
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }

        public void validButMsgSeqNumIsTooHigh() throws Exception {
            server.expect(LOGON);
            server.respond(
                    new MessageBuilder(LOGON)
                        .msgSeqNum(2)
                        .integer(HeartBtInt.TAG, HEARTBEAT_INTERVAL)
                        .enumeration(EncryptMethod.TAG, EncryptMethodValue.NONE)
                    .build());
            server.expect(RESEND_REQUEST);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }

        public void invalid() throws Exception {
            server.expect(LOGON);
            server.respond(
                    new MessageBuilder(LOGON)
                        .setBeginString("FIX.5.0")
                        .msgSeqNum(1)
                        .integer(HeartBtInt.TAG, HEARTBEAT_INTERVAL)
                        .enumeration(EncryptMethod.TAG, EncryptMethodValue.NONE)
                    .build());
            server.expect(LOGOUT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }

        public void otherMessageThanLogon() throws Exception {
            server.expect(LOGON);
            server.respond(
                    new MessageBuilder(HEARTBEAT)
                        .msgSeqNum(1)
                    .build());
            server.expect(LOGOUT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }
    }

    public class ReceiveMessageStandardHeader_2 {
        public void msgSeqNumReceivedAsExpected() throws Exception {
            server.expect(LOGON);
            server.respondLogon();
            server.respond(new MessageBuilder(HEARTBEAT).msgSeqNum(2).build());
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }

        public void msgSeqNumHigherThanExpected() throws Exception {
            server.expect(LOGON);
            server.respondLogon();
            server.respond(new MessageBuilder(HEARTBEAT).msgSeqNum(3).build());
            server.expect(RESEND_REQUEST);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }

        public void msgSeqNumLowerThanExpectedWithoutPossDupFlag() throws Exception {
            server.expect(LOGON);
            server.respondLogon();
            server.respond(new MessageBuilder(HEARTBEAT).msgSeqNum(1).build());
            server.expect(LOGOUT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }

        public void garbledMessageReceived() throws Exception {
            server.expect(LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(HEARTBEAT)
                        .setBeginString("")
                        .msgSeqNum(2)
                    .build());
            server.respond(
                    new MessageBuilder(TEST_REQUEST)
                        .msgSeqNum(2)
                        .string(TestReqID.TAG, "12345678")
                    .build());
            server.expect(HEARTBEAT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }

        public void possDupFlag() throws Exception {
            server.expect(LOGON);
            server.respondLogon();
            server.respond(
                    new MessageBuilder(HEARTBEAT)
                        .msgSeqNum(2)
                    .build());
            server.respond(
                    new MessageBuilder(HEARTBEAT)
                        .setPossDupFlag(true)
                        .setOrigSendingTime(new DateTime().minusMinutes(1))
                        .msgSeqNum(2)
                    .build());
            server.respondLogout(3);
            server.expect(LOGOUT);
            runInClient(new Runnable() {
                @Override public void run() {
                    session.logon(connection);
                }
            });
        }
    }

    private void runInClient(Runnable command) throws Exception {
        Thread serverThread = new Thread(server);
        serverThread.start();
        server.awaitForStart();
        Events events = Events.open(1000);
        session = newSession();
        connection = openConnection(session);
        events.register(connection);
        command.run();
        events.dispatch();
        server.stop();
        specify(server.passed());
    }

    private Session newSession() {
        return new Session(new HeartBtIntValue(HEARTBEAT_INTERVAL), getConfig(), new SessionStore() {
            @Override public void load(Session session) {
            }

            @Override public void resetOutgoingSeq(String senderCompId, String targetCompId, Sequence incomingSeq, Sequence outgoingSeq) {
            }

            @Override public void save(Session session) {
            }
        });
    }

    private Config getConfig() {
        Config config = new Config();
        config.setSenderCompId(INITIATOR);
        config.setTargetCompId(ACCEPTOR);
        config.setVersion(VERSION);
        return config;
    }

    private Connection openConnection(final Session session) throws IOException {
        return Connection.connect(new InetSocketAddress("localhost", PORT), new FixMessageParser(), new Connection.Callback() {
            public void messages(Connection conn, Iterator<silvertip.Message> messages) {
                while (messages.hasNext())
                    session.receive(conn, messages.next(), new DefaultMessageVisitor());
            }

            public void idle(Connection conn) {
                conn.close();
            }
        });
    }

    class MessageBuilder {
        private final Message message;

        public MessageBuilder(MsgTypeValue type) {
            MessageHeader header = new MessageHeader(type);
            header.setBeginString(VERSION.value());
            header.setString(SenderCompID.TAG, ACCEPTOR);
            header.setString(TargetCompID.TAG, INITIATOR);
            header.setDateTime(SendingTime.TAG, new DefaultTimeSource().currentTime());
            this.message = type.newMessage(header);
        }

        public MessageBuilder setBeginString(String beginString) {
            message.setBeginString(beginString);
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

        public MessageBuilder string(Tag<StringField> tag, String value) {
            message.setString(tag, value);
            return this;
        }

        public MessageBuilder integer(Tag<IntegerField> tag, Integer value) {
            message.setInteger(tag, value);
            return this;
        }

        public MessageBuilder bool(Tag<BooleanField> tag, boolean value) {
            message.setBoolean(tag, value);
            return this;
        }

        <T extends Formattable> MessageBuilder enumeration(Tag<? extends EnumField<T>> tag, T value) {
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

    class SimpleAcceptor implements Runnable {
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

        public boolean passed() {
            return failureCount == 0 && successCount == commands.size();
        }

        public void respondLogon() {
            server.respond(
                    new MessageBuilder(LOGON)
                        .msgSeqNum(1)
                        .integer(HeartBtInt.TAG, HEARTBEAT_INTERVAL)
                        .enumeration(EncryptMethod.TAG, EncryptMethodValue.NONE)
                    .build());
        }

        public void respondLogout(int msgSeqNum) {
            server.respond(
                    new MessageBuilder(LOGOUT)
                        .msgSeqNum(msgSeqNum)
                    .build());
        }

        public void respond(final Message message) {
            this.commands.add(new Runnable() {
                @Override public void run() {
                    try {
                        clientSocket.getOutputStream().write(message.format().getBytes());
                        successCount++;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        public void expect(final MsgTypeValue type) {
            this.commands.add(new Runnable() {
                @Override public void run() {
                    StringBuilder raw = parse();
                    Parser.parse(new silvertip.Message(raw.toString().getBytes()), new Parser.Callback() {
                        @Override public void message(Message m) {
                            if (m.getMsgType().equals(type.value()))
                                successCount++;
                            else
                                failureCount++;
                        }

                        @Override public void unsupportedMsgType(String msgType, int msgSeqNum) {
                        }

                        @Override public void invalidMsgType(String msgType, int msgSeqNum) {
                        }

                        @Override public void invalidMessage(int msgSeqNum, SessionRejectReasonValue reason, String text) {
                        }

                        @Override public void garbledMessage(String text) {
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
                            /* Ignore */
                        }
                        raw.append((char) c);
                        if (raw.toString().contains("10=")) {
                            do {
                                try {
                                    c = reader.read();
                                } catch (IOException e) {
                                    /* Ignore */
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

        @Override public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
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
    }
}