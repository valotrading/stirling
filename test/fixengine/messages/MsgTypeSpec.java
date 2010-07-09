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

import jdave.Block;
import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.junit.runner.RunWith;

/**
 * @author Pekka Enberg 
 */
@RunWith(JDaveRunner.class)
public class MsgTypeSpec extends Specification<MsgTypeValue> {
    public class EmptyMsgType {
        public void isNotValid() {
            specify(parse(""), must.raise(InvalidMsgTypeException.class));
        }
    }

    public class ValidOneCharacterMsgTypes {
        public void areValid() {
            specify(MsgTypeValue.parse("0"), must.equal(MsgTypeValue.HEARTBEAT));
            specify(MsgTypeValue.parse("9"), must.equal(MsgTypeValue.ORDER_CANCEL_REJECT));
        }
    }
    
    public class InvalidOneCharacterMsgTypes {
        public void areNotValid() {
            specify(parse("!"), must.raise(InvalidMsgTypeException.class));
        }
    }

    public class ValidTwoCharacterMsgTypes {
        public void areValid() {
            specify(parse("AA"), must.raise(UnsupportedMsgTypeException.class));
            specify(parse("AI"), must.raise(UnsupportedMsgTypeException.class));
        }
    }

    public class InvalidTwoCharacterMsgTypes {
        public void areNotValid() {
            specify(parse("aa"), must.raise(InvalidMsgTypeException.class));
            specify(parse("AJ"), must.raise(InvalidMsgTypeException.class));

            specify(parse("ZA"), must.raise(InvalidMsgTypeException.class));
            specify(parse("ZZ"), must.raise(InvalidMsgTypeException.class));
        }
    }

    public class AnyThreeCodeMsgTypes {
        public void areNotValid() {
            specify(parse("AAA"), must.raise(InvalidMsgTypeException.class));
        }
    }
    
    Block parse(final String value) {
        return new Block() {
            @Override public void run() throws Throwable {
                MsgTypeValue.parse(value);
            }
            
        };
    }
}
