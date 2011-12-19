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

import jdave.Block;
import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.junit.runner.RunWith;

/**
 * @author Pekka Enberg 
 */
@RunWith(JDaveRunner.class)
public class BooleanFieldSpec extends Specification<BooleanField> {
    @SuppressWarnings("unchecked") private BooleanField field = new BooleanField(dummy(Tag.class));

    public class AnyField {
        public BooleanField create() {
            return field;
        }

        public void parsesTrueStringValue() {
            field.parse("Y");
            specify(field.booleanValue(), must.equal(true));
        }

        public void parsesFalseStringValue() {
            field.parse("N");
            specify(field.booleanValue(), must.equal(false));
        }

        public void raisesExceptionIfAttemptingToParseBogusValue() {
            specify(new Block() {
                @Override
                public void run() throws Throwable {
                    field.parse("123");
                }
            }, must.raise(IllegalArgumentException.class));
        }
    }

    public class FieldThatHasFalseValue {
        public BooleanField create() {
            return field;
        }

        public void isFormattedAsString() {
            specify(field.value(), must.equal("N"));
        }
    }

    public class FieldThatHasTrueValue {
        public BooleanField create() {
            field.setValue(true);
            return field;
        }

        public void isFormattedAsString() {
            specify(field.value(), must.equal("Y"));
        }
    }
}
