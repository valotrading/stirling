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
package stirling.fix.messages;

import jdave.Block;
import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.junit.runner.RunWith;

import stirling.fix.messages.fix42.DefaultMessageFactory;
import stirling.fix.messages.fix42.MsgTypeValue;

/**
 * @author Pekka Enberg
 */
@RunWith(JDaveRunner.class)
public class DefaultMessageFactorySpec extends Specification<DefaultMessageFactory> {
    private MessageFactory factory = new DefaultMessageFactory();

    public class EmptyMsgType {
        public void isNotValid() {
            specify(create(""), must.raise(InvalidMsgTypeException.class));
        }
    }

    public class ValidOneCharacterMsgTypes {
        public void areValid() {
            specify(factory.create("0").getMsgType(), MsgTypeValue.HEARTBEAT);
            specify(factory.create("9").getMsgType(), MsgTypeValue.ORDER_CANCEL_REJECT);
        }
    }

    public class InvalidOneCharacterMsgTypes {
        public void areNotValid() {
            specify(create("!"), must.raise(InvalidMsgTypeException.class));
        }
    }

    public class ValidTwoCharacterMsgTypes {
        public void areValid() {
            specify(create("AA"), must.raise(UnsupportedMsgTypeException.class));
            specify(create("AI"), must.raise(UnsupportedMsgTypeException.class));
        }
    }

    public class InvalidTwoCharacterMsgTypes {
        public void areNotValid() {
            specify(create("aa"), must.raise(InvalidMsgTypeException.class));
            specify(create("AJ"), must.raise(InvalidMsgTypeException.class));

            specify(create("ZA"), must.raise(InvalidMsgTypeException.class));
            specify(create("ZZ"), must.raise(InvalidMsgTypeException.class));
        }
    }

    public class AnyThreeCodeMsgTypes {
        public void areNotValid() {
            specify(create("AAA"), must.raise(InvalidMsgTypeException.class));
        }
    }

    Block create(final String value) {
        return new Block() {
            @Override public void run() throws Throwable {
                factory.create(value);
            }
        };
    }
}
