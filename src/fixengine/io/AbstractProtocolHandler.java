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
package fixengine.io;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @author Pekka Enberg
 */
public abstract class AbstractProtocolHandler<T> implements ProtocolHandler, RequestListener, IdleListener {
    private static final int BUFFER_SIZE = 4096;

    protected final Converter<T> converter;
    private final Timeout rxTimeout;
    private final Timeout txTimeout;

    public AbstractProtocolHandler(Converter<T> converter, Timeout rxTimeout, Timeout txTimeout) {
        this.rxTimeout = rxTimeout;
        this.txTimeout = txTimeout;
        this.converter = converter;
    }

    @Override
    public void start(SelectionKey sk, SocketChannel channel) {
        CodedObjectOutputStream<T> output = open(sk, channel);
        init(output);
        onEstablished();
    }

    protected CodedObjectOutputStream<T> open(SelectionKey sk, SocketChannel channel) {
        Stream stream = newByteStream(channel);
        sk.attach(newRequestProcessor(stream));
        return newObjectOutputStream(stream);
    }

    protected Stream newByteStream(SocketChannel channel) {
        return new IdleDetectingByteStream(new ByteStream(channel), rxTimeout, txTimeout, this);
    }

    protected ChannelRequestProcessor newRequestProcessor(Stream stream) {
        return new ChannelRequestProcessor(stream, newBuffer(), getRequestListener());
    }

    private CodedObjectOutputStream<T> newObjectOutputStream(Stream stream) {
        return new CodedObjectOutputStream<T>(stream, newBuffer(), converter);
    }

    protected ByteBuffer newBuffer() {
        return ByteBuffer.allocateDirect(BUFFER_SIZE);
    }

    protected RequestListener getRequestListener() {
        return this;
    }

    protected abstract void init(CodedObjectOutputStream<T> output);

    protected void onEstablished() {
    }

    @Override
    public void onReceive(ByteBuffer buffer) {
        onMessageReceived(converter.convertToObject(buffer));
    }

    public abstract void onMessageReceived(T message);
}
