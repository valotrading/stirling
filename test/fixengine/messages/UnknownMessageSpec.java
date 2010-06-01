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
            specify(new UnknownMessage("").hasValidMsgType(), must.equal(false));
        }
    }

    public class ValidOneCharacterMsgTypes {
        public void areValid() {
            specify(new UnknownMessage("0").hasValidMsgType(), must.equal(true));
            specify(new UnknownMessage("9").hasValidMsgType(), must.equal(true));

            specify(new UnknownMessage("A").hasValidMsgType(), must.equal(true));
            specify(new UnknownMessage("Z").hasValidMsgType(), must.equal(true));

            specify(new UnknownMessage("a").hasValidMsgType(), must.equal(true));
            specify(new UnknownMessage("z").hasValidMsgType(), must.equal(true));
        }
    }
    
    public class InvalidOneCharacterMsgTypes {
        public void areNotValid() {
            specify(new UnknownMessage("!").hasValidMsgType(), must.equal(false));
        }
    }

    public class ValidTwoCharacterMsgTypes {
        public void areValid() {
            specify(new UnknownMessage("AA").hasValidMsgType(), must.equal(true));
            specify(new UnknownMessage("AI").hasValidMsgType(), must.equal(true));
        }
    }

    public class InvalidTwoCharacterMsgTypes {
        public void areNotValid() {
            specify(new UnknownMessage("aa").hasValidMsgType(), must.equal(false));
            specify(new UnknownMessage("AJ").hasValidMsgType(), must.equal(false));

            specify(new UnknownMessage("ZA").hasValidMsgType(), must.equal(false));
            specify(new UnknownMessage("ZZ").hasValidMsgType(), must.equal(false));
        }
    }

    public class AnyThreeCodeMsgTypes {
        public void areNotValid() {
            specify(new UnknownMessage("AAA").hasValidMsgType(), must.equal(false));
        }
    }
}
