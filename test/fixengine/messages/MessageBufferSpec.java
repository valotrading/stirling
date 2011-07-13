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

import jdave.Specification;
import jdave.junit4.JDaveRunner;
import org.junit.runner.RunWith;

import fixengine.messages.fix42.MsgTypeValue;
import fixengine.tags.fix42.MsgType;
import fixengine.tags.fix42.TargetCompID;
import static fixengine.messages.Field.DELIMITER;

@RunWith(JDaveRunner.class) public class MessageBufferSpec extends Specification<MessageBuffer> {
    private final MessageBuffer buffer = new MessageBuffer();

    public class EmptyBuffer {
        public void returnsEmptyStringFromToString() {
            specify(buffer.toString(), must.equal(""));
        }

        public void hasLengthOfZero() {
            specify(buffer.length(), must.equal(0));
        }
    }

    public class BufferThatHasAppendedTag {
        public MessageBuffer create() {
            buffer.append(new StringField(MsgType.Tag(), MsgTypeValue.LOGON));
            return buffer;
        }

        public void hasLengthOfTheAppendTag() {
            specify(buffer.length(), must.equal(5));
        }

        public void returnsAppendedTagFromToString() {
            specify(buffer.toString(), must.equal("35=A" + DELIMITER));
        }

        public void retainsAlreadyAppendedTagsWhenNewTagsAreAppended() {
            buffer.append(new StringField(TargetCompID.Tag(), "IB"));
            specify(buffer.toString(), must.equal("35=A" + DELIMITER + "56=IB" + DELIMITER));
        }
        
        public void retainsAlreadyAppendedTagsAtTheBackWhenNewTagsArePrefixed() {
            buffer.prefix(new StringField(TargetCompID.Tag(), "IB"));
            specify(buffer.toString(), must.equal("56=IB" + DELIMITER + "35=A" + DELIMITER));
        }
    }
}
