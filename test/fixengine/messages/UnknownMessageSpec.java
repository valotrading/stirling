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

import org.junit.runner.RunWith;

/**
 * @author Pekka Enberg 
 */
@RunWith(JDaveRunner.class)
public class UnknownMessageSpec extends Specification<UnknownMessage> {
    public class EmptyMsgType {
        public void isNotValid() {
            specify(message("").hasValidMsgType(), must.equal(false));
        }
    }

    public class ValidOneCharacterMsgTypes {
        public void areValid() {
            specify(message("0").hasValidMsgType(), must.equal(true));
            specify(message("9").hasValidMsgType(), must.equal(true));

            specify(message("A").hasValidMsgType(), must.equal(true));
            specify(message("Z").hasValidMsgType(), must.equal(true));

            specify(message("a").hasValidMsgType(), must.equal(true));
            specify(message("z").hasValidMsgType(), must.equal(true));
        }
    }
    
    public class InvalidOneCharacterMsgTypes {
        public void areNotValid() {
            specify(message("!").hasValidMsgType(), must.equal(false));
        }
    }

    public class ValidTwoCharacterMsgTypes {
        public void areValid() {
            specify(message("AA").hasValidMsgType(), must.equal(true));
            specify(message("AI").hasValidMsgType(), must.equal(true));
        }
    }

    public class InvalidTwoCharacterMsgTypes {
        public void areNotValid() {
            specify(message("aa").hasValidMsgType(), must.equal(false));
            specify(message("AJ").hasValidMsgType(), must.equal(false));

            specify(message("ZA").hasValidMsgType(), must.equal(false));
            specify(message("ZZ").hasValidMsgType(), must.equal(false));
        }
    }

    public class AnyThreeCodeMsgTypes {
        public void areNotValid() {
            specify(message("AAA").hasValidMsgType(), must.equal(false));
        }
    }

    private UnknownMessage message(String msgType) {
        MessageHeader header = new MessageHeader();
        header.setMsgType(msgType);
        return new UnknownMessage(header);
    }
}
