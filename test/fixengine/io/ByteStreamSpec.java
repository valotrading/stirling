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

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.jmock.Expectations;
import org.junit.runner.RunWith;

/**
 * @author Pekka Enberg 
 */
@RunWith(JDaveRunner.class)
public class ByteStreamSpec extends Specification<ByteStream> {
    private ByteChannel delegatee = mock(ByteChannel.class);
    private ByteBuffer buffer = ByteBuffer.allocate(32);

    public class AnyChannel {
        private ByteStream channel = new ByteStream(delegatee);

        public ByteStream create() {
            return channel;
        }

        public void delegatesWrite() throws IOException {
            checking(new Expectations() {{
                one(delegatee).write(buffer);
            }});
            channel.write(buffer);
        }

        public void delegatesClose() throws IOException {
            checking(new Expectations() {{
                one(delegatee).close();
            }});
            channel.close();
        }
    }

    public class ChannelThatIsClosed {
        private ByteStream channel = new ByteStream(delegatee);

        public ByteStream create() {
            checking(new Expectations() {{
                one(delegatee).isOpen(); will(returnValue(false));
            }});
            return channel;
        }

        public void failsReadOperation() throws IOException {
            specify(channel.read(buffer), must.equal(-1));
        }

        public void isClosed() {
            specify(channel.isClosed(), must.equal(true)); 
        }
    }

    public class ChannelThatIsOpen {
        private ByteStream channel = new ByteStream(delegatee);

        public ByteStream create() {
            checking(new Expectations() {{
                one(delegatee).isOpen(); will(returnValue(true));
            }});
            return channel;
        }

        public void isNotClosed() {
            specify(channel.isClosed(), must.equal(false)); 
        }
    }

    public class ChannelThatHasAlreadyReachedEndOfStream {
        private ByteStream channel = new ByteStream(delegatee);

        public ByteStream create() throws IOException {
            checking(new Expectations() {{
                one(delegatee).isOpen(); will(returnValue(true));
                one(delegatee).read(buffer); will(returnValue(-1));
            }});
            return channel;
        }

        public void failsReadOperation() throws IOException {
            specify(channel.read(buffer), must.equal(-1));
        }
    }
    
    public class ChannelThatReachesEndOfStream {
        private ByteStream channel = new ByteStream(delegatee);

        public ByteStream create() throws IOException {
            checking(new Expectations() {{
                one(delegatee).isOpen(); will(returnValue(true));
                one(delegatee).read(buffer); will(returnValue(10));
                one(delegatee).read(buffer); will(returnValue(-1));
            }});
            return channel;
        }

        public void returnsAmountOfBytesRead() throws IOException {
            specify(channel.read(buffer), must.equal(10));
        }
    }

    public class ChannelThatDoesNotReachEndOfStream {
        private ByteStream channel = new ByteStream(delegatee);

        public ByteStream create() throws IOException {
            checking(new Expectations() {{
                one(delegatee).isOpen(); will(returnValue(true));
                one(delegatee).read(buffer); will(returnValue(20));
                one(delegatee).read(buffer); will(returnValue(10));
                one(delegatee).read(buffer); will(returnValue(0));
            }});
            return channel;
        }

        public void returnsAmountOfBytesRead() throws IOException {
            specify(channel.read(buffer), must.equal(30));
        }
    }
}
