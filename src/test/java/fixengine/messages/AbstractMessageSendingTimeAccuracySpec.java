/*
 * Copyright 2009 the original author or authors.
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
package fixengine.messages;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.joda.time.DateTime;
import org.junit.runner.RunWith;

import fixengine.messages.fix42.MsgTypeValue;

/**
 * @author Pekka Enberg 
 */
@RunWith(JDaveRunner.class)
public class AbstractMessageSendingTimeAccuracySpec extends Specification<AbstractMessage> {
    private static final DateTime IN_PAST = new DateTime(1);
    private static final DateTime NOW = new DateTime(2);
    private static final DateTime IN_FUTURE = new DateTime(3);

    private AbstractMessage message = new AbstractMessage(MsgTypeValue.LOGON) {
        @Override
        public void apply(MessageVisitor visitor) {
        }
    };

    public class MessageThatDoesNotHaveOrigSendingTime {
        public AbstractMessage create() {
            message.setPossDupFlag(true);
            message.setSendingTime(NOW);
            return message;
        }

        public void hasAccurateSendingTime() {
            specify(message.hasOrigSendTimeAfterSendingTime(), must.equal(true));
        }
    }

    public class MessageThatDoesHasOrigSendingTimeEqualToSendingTime {
        public AbstractMessage create() {
            message.setPossDupFlag(true);
            message.setSendingTime(NOW);
            message.setOrigSendingTime(NOW);
            return message;
        }

        public void hasAccuratenSendingTime() {
            specify(message.hasOrigSendTimeAfterSendingTime(), must.equal(true));
        }
    }

    public class MessageThatDoesHasOrigSendingTimeLessThanSendingTime {
        public AbstractMessage create() {
            message.setPossDupFlag(true);
            message.setSendingTime(NOW);
            message.setOrigSendingTime(IN_PAST);
            return message;
        }

        public void hasAccuratenSendingTime() {
            specify(message.hasOrigSendTimeAfterSendingTime(), must.equal(true));
        }
    }

    public class MessageThatDoesHasOrigSendingTimeGreaterThanSendingTime {
        public AbstractMessage create() {
            message.setPossDupFlag(true);
            message.setSendingTime(NOW);
            message.setOrigSendingTime(IN_FUTURE);
            return message;
        }

        public void hasAccuratenSendingTime() {
            specify(message.hasOrigSendTimeAfterSendingTime(), must.equal(false));
        }
    }
}
