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

import org.junit.runner.RunWith;

import fixengine.messages.CheckSumField;

/**
 * @author Pekka Enberg 
 */
@RunWith(JDaveRunner.class)
public class CheckSumFieldSpec extends Specification<CheckSumField> {
    private MessageBuffer out = new MessageBuffer();
    private CheckSumField field;

    public class CheckSumLessThanTen {
        public CheckSumField create() {
            return field = new CheckSumField(9);
        }

        public void isPaddedWithTwoZeros() throws Exception {
            out.append(field);
            specify(out.toString(), must.equal("10=009" + DELIMITER));
        }
    }
    
    public class CheckSumLessThanHundred {
        public CheckSumField create() {
            return field = new CheckSumField(99);
        }

        public void isPaddedWithOneZero() throws Exception {
            out.append(field);
            specify(out.toString(), must.equal("10=099" + DELIMITER));
        }
    }

    public class CheckSumMoreThanHundred {
        public CheckSumField create() {
            return field = new CheckSumField(255);
        }

        public void isNotPadded() throws Exception {
            out.append(field);
            specify(out.toString(), must.equal("10=255" + DELIMITER));
        }
    }
}
