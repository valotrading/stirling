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
package fixengine.performance;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Random;

import silvertip.Connection;
import silvertip.Events;
import silvertip.Message;
import silvertip.Server;
import silvertip.Server.ConnectionFactory;
import silvertip.protocols.FixMessageParser;
import fixengine.Config;
import fixengine.Version;
import fixengine.messages.DefaultMessageVisitor;
import fixengine.messages.NewOrderSingleMessage;
import fixengine.session.HeartBtIntValue;
import fixengine.session.Session;
import fixengine.session.store.InMemorySessionStore;
import fixengine.session.store.SessionStore;

public class PerformanceTest implements Runnable {
    private static final Random generator = new Random();
    private static final int NUM_MESSAGES = 500000;
    private long rx[];
    private long tx[];

    public static void main(String[] args) {
        PerformanceTest perf = new PerformanceTest();
        perf.run();
    }

    @Override public void run() {
        System.out.println("Warming up...");
        benchmark(randomPort());
        System.out.println("Benchmarking...");
        benchmark(randomPort());
    }

    private static int randomPort() {
        return 1024 + generator.nextInt(1024);
    }

    private void benchmark(int port) {
        rx = new long[NUM_MESSAGES];
        tx = new long[NUM_MESSAGES];
        Thread client = new Thread(new PerfClient(port));
        Thread server = new Thread(new PerfServer(port));
        try {
            long start = System.nanoTime();
            server.start();
            Thread.sleep(1000); // XXX: This is unreliable.
            client.start();
            server.join();
            client.join();
            long[] diff = new long[NUM_MESSAGES];
            for (int i = 0; i < NUM_MESSAGES; i++) {
                diff[i] = rx[i] - tx[i];
            }
            long end = System.nanoTime();
            System.out.printf("Time  : %.2f sec\n" , nanosToSeconds(end - start));
            System.out.printf("Min   : %.2f msec\n", nanosToMillis((double) Stats.min(diff)));
            System.out.printf("Max   : %.2f msec\n", nanosToMillis((double) Stats.max(diff)));
            System.out.printf("Avg   : %.2f msec\n", nanosToMillis(Stats.mean(diff)));
            System.out.printf("Stddev: %.2f msec\n", nanosToMillis(Stats.stddev(diff)));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static double nanosToSeconds(double nano) {
        return nano / 1000000000.0;
    }

    private static double nanosToMillis(double nano) {
        return nano / 1000000.0;
    }

    public class PerfServer implements Runnable {
        private final int port;
        private int count;

        public PerfServer(int port) {
            this.port = port;
        }

        @Override public void run() {
            try {
                final Events events = Events.open(30000);
                Server server = Server.accept(port, new ConnectionFactory() {
                    @Override public Connection newConnection(SocketChannel channel) {
                        try {
                            channel.socket().setTcpNoDelay(true);
                        } catch (SocketException e) {
                            throw new RuntimeException(e);
                        }
                        return new Connection<Message>(channel, new FixMessageParser(), new Connection.Callback<Message>() {
                            @Override public void messages(Connection<Message> conn, Iterator<Message> messages) {
                                while (messages.hasNext()) {
                                    messages.next();
                                    rx[count++] = System.nanoTime();
                                    if (count == NUM_MESSAGES) {
                                        conn.close();
                                        break;
                                    }
                                }
                            }

                            @Override public void idle(Connection<Message> conn) {
                            }

                            @Override public void closed(Connection<Message> conn) {
                                events.stop();
                            }
                        });
                    }
                });
                events.register(server);
                events.dispatch();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public class PerfClient implements Runnable {
        private final int port;

        public PerfClient(int port) {
            this.port = port;
        }

        @Override public void run() {
            try {
                Events events = Events.open(30000);
                final Session session = new Session(getHeartBtIntValue(), getConfig(), getSessionStore());
                SocketChannel channel = SocketChannel.open();
                channel.connect(new InetSocketAddress("localhost", port));
                channel.configureBlocking(false);
                try {
                    channel.socket().setTcpNoDelay(true);
                } catch (SocketException e) {
                    throw new RuntimeException(e);
                }
                Connection conn = new Connection<Message>(channel, new FixMessageParser(),
                        new Connection.Callback<Message>() {
                            @Override public void messages(Connection<Message> conn, Iterator<Message> messages) {
                                while (messages.hasNext())
                                    session.receive(conn, messages.next(), new DefaultMessageVisitor());
                            }

                            @Override public void idle(Connection<Message> conn) {
                                session.keepAlive(conn);
                            }

                            @Override public void closed(Connection<Message> conn) {
                            }
                        });
                session.logon(conn);
                for (int i = 0; i < NUM_MESSAGES; i++) {
                    tx[i] = System.nanoTime();
                    NewOrderSingleMessage message = new NewOrderSingleMessage();
                    session.send(conn, message);
                }
                events.register(conn);
                events.dispatch();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected HeartBtIntValue getHeartBtIntValue() {
        return new HeartBtIntValue(30);
    }

    protected Config getConfig() {
        Config config = new Config();
        config.setSenderCompId("initiator");
        config.setTargetCompId("acceptor");
        config.setVersion(Version.FIX_4_2);
        return config;
    }

    protected SessionStore getSessionStore() {
        return new InMemorySessionStore();
    }
}
