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

/**
 * @author Pekka Enberg 
 */
public class ChannelRequestProcessor implements Runnable {
    private final RequestListener listener;
    private final Stream stream;
    private final ByteBuffer buffer;

    public ChannelRequestProcessor(Stream stream, ByteBuffer buffer, RequestListener listener) {
        this.listener = listener;
        this.stream = stream;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        process();
    }

    private void process() {
        synchronized (buffer) {
            buffer.clear();
            int nr = stream.read(buffer);
            if (nr > 0) {
                listener.onReceive(buffer);
            } else if (nr < 0) {
                listener.onEndOfStream();
            } else {
                /* nothing to do */
            }
        }
    }
}