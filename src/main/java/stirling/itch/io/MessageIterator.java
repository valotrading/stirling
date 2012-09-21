/*
 * Copyright 2012 the original author or authors.
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
package stirling.itch.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.util.NoSuchElementException;
import silvertip.MessageParser;
import silvertip.PartialMessageException;

class MessageIterator<Message> extends Source<Message> {
    private ReadableByteChannel    channel;
    private MessageParser<Message> parser;
    private ByteBuffer             buffer;
    private Message                next;

    public MessageIterator(ReadableByteChannel channel, MessageParser<Message> parser, int readBufferSize) {
        this.channel = channel;
        this.parser  = parser;
        this.buffer  = ByteBuffer.allocate(readBufferSize);

        buffer.order(ByteOrder.BIG_ENDIAN);

        refill();

        next = read();
    }

    public void close() throws IOException {
        channel.close();
    }

    public boolean hasNext() {
        return next != null;
    }

    public Message next() {
        if (next == null)
            throw new NoSuchElementException();

        Message current = next;

        next = read();

        return current;
    }

    private Message read() {
        while (true) {
            try {
                buffer.mark();
                return parser.parse(buffer);
            } catch (PartialMessageException e) {
                buffer.reset();
                buffer.compact();

                if (refill() == -1)
                    return null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int refill() {
        try {
            int bytes = channel.read(buffer);
            buffer.flip();

            return bytes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
