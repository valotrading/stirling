/*
 * Copyright 2010 the original author or authors.
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
package fixengine.session;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.jmock.Expectations;
import org.junit.runner.RunWith;

import fixengine.messages.FixMessage;

@RunWith(JDaveRunner.class) public class MessageQueueSpec extends Specification<MessageQueue> {
    private MessageQueue queue = new MessageQueue();

    public class EmptyQueue {
        public MessageQueue create() {
            return queue;
        }

        public void doesNotHaveSequenceNumberGap() {
            specify(queue.hasSeqNumGap(), must.equal(false));
        }

        public void isEmpty() {
            specify(queue.isEmpty(), must.equal(true));
        }
    }

    public class QueueThatHasInOrderMessages {
        private FixMessage message1 = mock(FixMessage.class, "message1");
        private FixMessage message2 = mock(FixMessage.class, "message2");

        public MessageQueue create() {
            checking(new Expectations() {{
                allowing(message1).getMsgSeqNum(); will(returnValue(1));
                allowing(message2).getMsgSeqNum(); will(returnValue(2));
            }});
            queue.enqueue(message1);
            queue.enqueue(message2);
            return queue;
        }

        public void isNotEmpty() {
            specify(queue.isEmpty(), must.equal(false));
        }

        public void returnsQueuedMessagesInOrder() {
            specify(queue.dequeue(), must.equal(message1));
            specify(queue.dequeue(), must.equal(message2));
        }

        public void doesNotHaveSequenceNumberGap() {
            specify(queue.hasSeqNumGap(), must.equal(false));
        }
    }

    public class QueueThatHasMissingMessage {
        private FixMessage outOfOrderMsg = mock(FixMessage.class);

        public MessageQueue create() {
            checking(new Expectations() {{
                allowing(outOfOrderMsg).getMsgSeqNum(); will(returnValue(2));
            }});
            queue.enqueue(outOfOrderMsg);
            return queue;
        }

        public void isNotEmpty() {
            specify(queue.isEmpty(), must.equal(false));
        }

        public void hasSequenceNumberGap() {
            specify(queue.hasSeqNumGap(), must.equal(true));
        }

        public void hasNextSeqNumSetToTheFirstMissingMessage() {
            specify(queue.nextSeqNum(), must.equal(1));
        }
    }

    public class QueueThatHasOutOfOrderMessages {
        private FixMessage message2 = mock(FixMessage.class, "message2");
        private FixMessage message1 = mock(FixMessage.class, "message1");

        public MessageQueue create() {
            checking(new Expectations() {{
                allowing(message2).getMsgSeqNum(); will(returnValue(2));
                allowing(message1).getMsgSeqNum(); will(returnValue(1));
            }});
            queue.enqueue(message2);
            queue.enqueue(message1);
            return queue;
        }

        public void isNotEmpty() {
            specify(queue.isEmpty(), must.equal(false));
        }

        public void returnsReceivedMessagesInSequenceNumberOrder() {
            specify(queue.dequeue(), must.equal(message1));
            specify(queue.dequeue(), must.equal(message2));
        }

        public void hasSequenceNumberGap() {
            specify(queue.hasSeqNumGap(), must.equal(true));
        }

        public void hasNextSeqNumSetToTheFirstOutOfMessageThatWasNotResent() {
            specify(queue.nextSeqNum(), must.equal(2));
        }
    }

    public class QueueThatIsReset {
        private FixMessage message2 = mock(FixMessage.class, "message2");
        private FixMessage message1 = mock(FixMessage.class, "message1");

        public MessageQueue create() {
            checking(new Expectations() {{
                allowing(message2).getMsgSeqNum(); will(returnValue(2));
                allowing(message1).getMsgSeqNum(); will(returnValue(1));
            }});
            queue.enqueue(message2);
            queue.enqueue(message1);
            queue.reset(2);
            return queue;
        }

        public void isNotEmpty() {
            specify(queue.isEmpty(), must.equal(false));
        }

        public void doesNotHaveSequenceNumberGap() {
            specify(queue.hasSeqNumGap(), must.equal(false));
        }

        public void hasNextSeqNumReset() {
            specify(queue.nextSeqNum(), must.equal(2));
        }
    }

    public class QueueThatHasConsecutiveSeqNumMismatches {
        private FixMessage message1 = mock(FixMessage.class, "message1");
        private FixMessage message2 = mock(FixMessage.class, "message2");
        private FixMessage message3 = mock(FixMessage.class, "message3");

        public MessageQueue create() {
            checking(new Expectations() {{
                allowing(message1).getMsgSeqNum(); will(returnValue(2));
                allowing(message2).getMsgSeqNum(); will(returnValue(2));
                allowing(message3).getMsgSeqNum(); will(returnValue(2));
            }});
            queue.enqueue(message1);
            queue.enqueue(message2);
            queue.enqueue(message3);
            return queue;
        }

        public void hasConsecutiveSeqNumMismatches() {
            specify(queue.getNumConsecutiveSeqNumMismatches(), must.equal(3));
        }
    }
}
