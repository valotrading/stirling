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
import java.util.concurrent.ExecutorService;

/**
 * @author Pekka Enberg 
 */
public class AsyncRequestListener implements RequestListener {
    private final RequestListener listener;
    private final ExecutorService executor;

    public AsyncRequestListener(ExecutorService executor, RequestListener listener) {
        this.executor = executor;
        this.listener = listener;
    }

    @Override
    public void onEndOfStream() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                listener.onEndOfStream();
            }
        });
    }

    @Override
    public void onReceive(final ByteBuffer buffer) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                listener.onReceive(buffer);
            }
        });
    }
}
