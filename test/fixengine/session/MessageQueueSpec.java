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
        private FixMessage message1 = message(1);
        private FixMessage message2 = message(2);

        public MessageQueue create() {
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
        private FixMessage outOfOrderMsg = message(2);

        public MessageQueue create() {
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
        private FixMessage message2 = message(2);
        private FixMessage message1 = message(1);

        public MessageQueue create() {
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
        private FixMessage message2 = message(2);
        private FixMessage message1 = message(1);

        public MessageQueue create() {
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
        private FixMessage message1 = message(2);
        private FixMessage message2 = message(2);
        private FixMessage message3 = message(2);

        public MessageQueue create() {
            queue.enqueue(message1);
            queue.enqueue(message2);
            queue.enqueue(message3);
            return queue;
        }

        public void hasConsecutiveSeqNumMismatches() {
            specify(queue.getOutOfOrderCount(), must.equal(3));
        }
    }

    private static final String MSG_TYPE = "0";

    private FixMessage message(int msgSeqNum) {
        FixMessage msg = FixMessage.fromString(Integer.toString(msgSeqNum), MSG_TYPE);
        msg.setMsgSeqNum(msgSeqNum);
        return msg;
    }
}
