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
package fixengine.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

import fixengine.io.IoException;
import fixengine.io.ProtocolHandler;
import fixengine.messages.Session;

/**
 * @author Pekka Enberg
 */
public class FixClient {
    private final ProtocolHandler handler;
    private final InetAddress host;
    private final int port;

    public FixClient(InetAddress host, int port, ProtocolHandler handler) {
        this.host = host;
        this.port = port;
        this.handler = handler;
    }

    public void start() throws Exception {
        Selector selector = selector();
        SocketChannel channel = open();
        SelectionKey sk = channel.register(selector, SelectionKey.OP_READ);
        new Thread(new RequestProcessor(selector)).start();
        handler.start(sk, channel);
    }

    public Session getSession() {
        return handler.getSession();
    }

    private Selector selector() {
        try {
            return SelectorProvider.provider().openSelector();
        } catch (IOException e) {
            throw new IoException(e);
        }
    }

    private SocketChannel open() throws Exception {
        InetSocketAddress isa = new InetSocketAddress(host, port);
        SocketChannel channel = SocketChannel.open();
        channel.connect(isa);
        channel.configureBlocking(false);
        return channel;
    }

    public class RequestProcessor implements Runnable {
        private final Selector selector;

        public RequestProcessor(Selector selector) {
            this.selector = selector;
        }

        @Override
        public void run() {
            try {
                select();
            } catch (IOException e) {
                throw new IoException(e);
            }
        }

        private void select() throws IOException {
            while (selector.select() > 0) {
                process();
            }
        }

        private void process() throws IOException {
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = readyKeys.iterator();

            while (it.hasNext()) {
                SelectionKey sk = it.next();
                it.remove();

                if (!sk.isValid() || !sk.isReadable()) {
                    continue;
                }

                Runnable processor = (Runnable) sk.attachment();
                processor.run();
            }
        }
    }
}
