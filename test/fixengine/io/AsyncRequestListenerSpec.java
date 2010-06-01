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

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.jmock.Expectations;
import org.junit.runner.RunWith;

import concurrent.DefaultExecutorService;

/**
 * @author Pekka Enberg 
 */
@RunWith(JDaveRunner.class)
public class AsyncRequestListenerSpec extends Specification<AsyncRequestListener> {
    private RequestListener delegatee = mock(RequestListener.class);
    private ExecutorService executor = new DefaultExecutorService() {
        @Override
        public void execute(Runnable command) {
            command.run();
        }
    };

    public class Any {
        private AsyncRequestListener listener = new AsyncRequestListener(executor, delegatee);

        public void onDisconnectedIsExecutedAsynchronously() {
            checking(new Expectations() {{
                one(delegatee).onEndOfStream();
            }});
            listener.onEndOfStream();
        }

        public void onReceiveIsExecutedAsynchronously() {
            final ByteBuffer buffer = dummy(ByteBuffer.class);
            checking(new Expectations() {{
                one(delegatee).onReceive(buffer);
            }});
            listener.onReceive(buffer);
        }
    }
}
