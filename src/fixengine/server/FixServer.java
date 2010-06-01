/*
 * Copyright 2009 the original author or authors.
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
package fixengine.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fixengine.io.IoException;
import fixengine.io.ProtocolHandler;
import fixengine.io.ProtocolHandlerFactory;

/**
 * @author Pekka Enberg 
 */
public class FixServer {
    private final ExecutorService executor = Executors.newCachedThreadPool();

    private final ProtocolHandlerFactory factory;
    private final InetAddress host;
    private final int port;

    private ServerSocketChannel serverChannel;

    public FixServer(InetAddress host, int port, ProtocolHandlerFactory factory) {
        this.host = host;
        this.port = port;
        this.factory = factory;
    }

    public void start() throws Exception {
        serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        InetSocketAddress socketAddress = new InetSocketAddress(host, port);
        serverChannel.socket().bind(socketAddress);

        try {
            select();
        } catch (IOException e) {
            throw new IoException(e);
        }
    }

    private void select() throws IOException {
        Selector selector = selector();

        while (selector.select() > 0) {
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> i = readyKeys.iterator();

            while (i.hasNext()) {
                final SelectionKey sk = i.next();
                i.remove();

                if (sk.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) sk.channel();
                    SocketChannel client = server.accept();
                    client.configureBlocking(false);
                    registerClient(selector, client);
                }
                if (sk.isReadable()) {
                    SocketChannel sc = (SocketChannel) sk.channel();
                    if (sc.isOpen()) {
                        Runnable runnable = (Runnable) sk.attachment();
                        runnable.run();
                    } else {
                        sk.attach(null);
                        sk.cancel();
                    }
                }
            }
        }
    }

    private Selector selector() throws IOException {
        Selector selector = SelectorProvider.provider().openSelector();
        SelectionKey sk = serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        sk.attach(this);
        return selector;
    }

    private void registerClient(Selector selector, final SocketChannel client) throws IOException {
        final SelectionKey sk = client.register(selector, SelectionKey.OP_READ);
        final ProtocolHandler handler = factory.newProtocolHandler();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                handler.start(sk, client);
            }
        });
    }
}