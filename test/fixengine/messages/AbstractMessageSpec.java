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
import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.jmock.Expectations;
import org.joda.time.DateTime;
import org.junit.runner.RunWith;

import fixengine.Version;

/**
 * @author Pekka Enberg 
 */
@RunWith(JDaveRunner.class)
public class AbstractMessageSpec extends Specification<AbstractMessage> {
    private AbstractMessage message = new AbstractMessage(MsgType.LOGON) {
        @Override
        public void apply(MessageVisitor visitor) {
        }
    };

    public class AnyMessage {
        public void parsesUserDefinedFields() {
            final Tag tag = mock(Tag.class);
            checking(new Expectations() {{
                one(tag).checksum(); will(returnValue(0));
                one(tag).length(); will(returnValue(0));
                one(tag).isUserDefined(); will(returnValue(true));
            }});
            message.parseField(tag, new TokenStream("=1234" + DELIMITER));
        }
    }

    public class MessageThatHasFields {
        public AbstractMessage create() {
            message.setBeginString(Version.FIX_4_3.value());
            message.setSenderCompId("id");
            message.setTargetCompId("IB");
            message.setMsgSeqNum(1);
            message.add(new EncryptMethodField(EncryptMethod.NONE));
            message.add(new HeartBtIntField(30));
            return message;
        }

        public void formatsWholeMessageToOutputStream() throws Exception {
            specify(message.format(), must.equal("8=FIX.4.3" + DELIMITER
                    + "9=48" + DELIMITER + "35=A" + DELIMITER + "49=id"
                    + DELIMITER + "56=IB" + DELIMITER + "34=1" + DELIMITER
                    + "43=N" + DELIMITER + "97=N" + DELIMITER + "52="
                    + DELIMITER + "98=0" + DELIMITER + "108=30" + DELIMITER
                    + "10=025" + DELIMITER));
        }
    }

    public class MessageThatHasNoFields {
        public AbstractMessage create() {
            message.setBeginString(Version.FIX_4_3.value());
            message.setSenderCompId("id");
            message.setTargetCompId("IB");
            message.setMsgSeqNum(1);
            return message;
        }

        public void formatsHeaderAndTrailerToOutputStream() throws Exception {
            specify(message.format(), must.equal("8=FIX.4.3" + DELIMITER
                    + "9=36" + DELIMITER + "35=A" + DELIMITER + "49=id"
                    + DELIMITER + "56=IB" + DELIMITER + "34=1" + DELIMITER
                    + "43=N" + DELIMITER + "97=N" + DELIMITER + "52="
                    + DELIMITER + "10=253" + DELIMITER));
        }
    }

    public class MessageThatDoesNotHaveOnBehalfOfOrDeliverToCompIdsDefined {
        public AbstractMessage create() {
            return message;
        }

        public void isPointToPoint() {
            specify(message.isPointToPoint(), must.equal(true));
        }
    }

    public class MessageThatHasOnBehalfOfIdDefined {
        public AbstractMessage create() {
            message.setOnBehalfOfCompId("A");
            return message;
        }

        public void isNotPointToPoint() {
            specify(message.isPointToPoint(), must.equal(false));
        }
    }

    public class MessageThatHasDeliverToCompIdDefined {
        public AbstractMessage create() {
            message.setDeliverToCompId("B");
            return message;
        }

        public void isNotPointToPoint() {
            specify(message.isPointToPoint(), must.equal(false));
        }
    }

    public class MessageThatHasMandatoryFieldsWithValue {
        private Field field = mock(Field.class);

        public AbstractMessage create() {
            message.setBeginString(Version.FIX_4_3.value());
            message.setSenderCompId("id");
            message.setTargetCompId("IB");
            message.setSendingTime(new DateTime());
            message.setMsgSeqNum(1);
            message.add(field);
            return message;
        }

        public void hasAllRequiredFields() {
            checking(new Expectations() {{
                one(field).isMissing(); will(returnValue(false));
            }});
            specify(message.contains(new fixengine.Specification<Field>() {
                @Override
                public boolean isSatisfiedBy(Field field) {
                    return field.isMissing();
                }
            }), must.equal(false));
        }
   }

    public class MessageThatHasMandatoryFieldWithoutValue {
        private Field field = mock(Field.class);

        public AbstractMessage create() {
            message.setBeginString(Version.FIX_4_3.value());
            message.setSenderCompId("id");
            message.setTargetCompId("IB");
            message.setMsgSeqNum(1);
            message.setSendingTime(new DateTime());
            message.add(field);
            return message;
        }

        public void doesNotHaveAllRequiredFields() {
            checking(new Expectations() {{
                one(field).isMissing(); will(returnValue(true));
            }});
            specify(message.contains(new fixengine.Specification<Field>() {
                @Override
                public boolean isSatisfiedBy(Field field) {
                    return field.isMissing();
                }
            }), must.equal(true));
        }
   }
}