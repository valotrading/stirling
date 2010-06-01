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
public class IdleDetectingByteStream implements Stream {
    private final Stream channel;
    private final Timer rxTimer;
    private final Timer txTimer;

    public IdleDetectingByteStream(Stream channel, Timeout rxTimeout, Timeout txTimeout, final IdleListener listener) {
        this.channel = channel;
        this.rxTimer = Timer.schedule(rxTimeout, new Runnable() {
            @Override
            public void run() {
                listener.onReceiveIdle();
            }
        });
        this.txTimer = Timer.schedule(txTimeout, new Runnable() {
            @Override
            public void run() {
                listener.onTransmitIdle();
            }
        });
    }

    @Override
    public int read(ByteBuffer buffer) {
        try {
            rxTimer.cancel();
            return channel.read(buffer);
        } finally {
            rxTimer.schedule();
        }
    }

    @Override
    public void write(ByteBuffer buffer) {
        try {
            txTimer.cancel();
            channel.write(buffer);
        } finally {
            txTimer.schedule();
        }
    }

    @Override
    public void close() {
        rxTimer.cancel();
        txTimer.cancel();
        channel.close();
    }

    @Override
    public boolean isClosed() {
        return channel.isClosed();
    }
}
