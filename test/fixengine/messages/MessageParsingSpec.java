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
package fixengine.messages;

import static fixengine.messages.Field.DELIMITER;
import jdave.Block;
import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.junit.runner.RunWith;

/**
 * @author Pekka Enberg 
 */
@RunWith(JDaveRunner.class)
public class MessageParsingSpec extends Specification<AbstractMessage> {
    private AbstractMessage message = new AbstractMessage(MsgType.LOGON) {
        @Override
        public void apply(MessageVisitor visitor) {
        }
    };

    public class InvalidCheckSum {
        public AbstractMessage create() {
            message.setBodyLength(0);
            return message;
        }

        public void raisesException() {
            specify(new Block() {
                @Override
                public void run() throws Throwable {
                    message.parse(new TokenStream("10=100" + DELIMITER));
                }
            }, must.raise(InvalidCheckSumException.class, "Expected 100, but was: 0"));
        }
    }

    public class InvalidBodyLength {
        public AbstractMessage create() {
            message.setBodyLength(4);
            return message;
        }

        public void raisesException() {
            specify(new Block() {
                @Override
                public void run() throws Throwable {
                    message.parse(new TokenStream("49=X" + DELIMITER + "10=3" + DELIMITER));
                }
            }, must.raise(InvalidBodyLengthException.class, "Expected 4, but was: 5"));
        }
    }
}
