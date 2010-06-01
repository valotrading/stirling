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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

/**
 * @author Pekka Enberg 
 */
public class ByteStream implements Stream {
    private final ByteChannel channel;

    public ByteStream(ByteChannel channel) {
        this.channel = channel;
    }

    public int read(ByteBuffer buffer) {
        try {
            synchronized (channel) {
                int ret, result = 0;

                if (!channel.isOpen()) {
                    return -1;
                }
                while ((ret = channel.read(buffer)) > 0) {
                    result += ret;
                }
                buffer.flip();
                /*
                 * If at least one of the read() operations succeeded, return
                 * the number of bytes read to the user.
                 */
                if (result > 0) {
                    return result;
                }
                return ret;
            }
        } catch (IOException e) {
            throw new IoException(e);
        }
    }

    public void write(ByteBuffer buffer) {
        try {
            synchronized (channel) {
                channel.write(buffer);
            }
        } catch (IOException e) {
            throw new IoException(e);
        }
    }

    public void close() {
        try {
            synchronized (channel) {
                channel.close();
            }
        } catch (IOException e) {
            // Ignore.
        }
    }

    public boolean isClosed() {
        synchronized (channel) {
            return !channel.isOpen();
        }
    }
}