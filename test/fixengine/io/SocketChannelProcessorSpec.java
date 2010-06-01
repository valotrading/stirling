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

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.jmock.Expectations;
import org.junit.runner.RunWith;

/**
 * @author Pekka Enberg 
 */
@RunWith(JDaveRunner.class)
public class SocketChannelProcessorSpec extends Specification<ChannelRequestProcessor> {
    private final RequestListener listener = mock(RequestListener.class);
    private final ByteBuffer buffer = ByteBuffer.allocate(32);
    private final Stream stream = mock(Stream.class);
    private final ChannelRequestProcessor processor = new ChannelRequestProcessor(stream, buffer, listener);

    public class ChannelThatReachesEndOfStream {
        public ChannelRequestProcessor create() throws IOException {
            checking(new Expectations() {{
                one(stream).read(buffer); will(returnValue(-1));
                one(listener).onEndOfStream();
            }});
            return processor;
        }

        public void invokesEndOfStreamMethodOfTheListener() {
            processor.run();
        }
    }

    public class ChannelThatDoesNotHaveDataAvailableForReading {
        public ChannelRequestProcessor create() throws IOException {
            checking(new Expectations() {{
                one(stream).read(buffer); will(returnValue(0));
            }});
            return processor;
        }

        public void doesNothing() {
            processor.run();
        }
    }

    public class ChannelThatHasDataAvailableForReading {
        public ChannelRequestProcessor create() throws IOException {
            checking(new Expectations() {{
                one(stream).read(buffer); will(returnValue(1));
                one(listener).onReceive(buffer);
            }});
            return processor;
        }

        public void invokesReceiveMethodOfTheListener() {
            processor.run();
        }
    }
}
