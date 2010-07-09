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

import org.jmock.Expectations;
import org.junit.runner.RunWith;

/**
 * @author Pekka Enberg
 */
@RunWith(JDaveRunner.class)
public class FieldsSpec extends Specification<Fields> {
    public class NonEmpty {
        private Field field1 = mock(Field.class, "field1");
        private Field field2 = mock(Field.class, "field2");
        private Fields fields = new Fields();

        public Fields create() {
            fields.add(field1);
            fields.add(field2);
            return fields;
        }

        public void formatsToNonEmptyString() {
            checking(new Expectations() {{
                one(field1).format(); will(returnValue("X"));
                one(field2).format(); will(returnValue("Y"));
            }});
            specify(fields.format(), must.equal("XY"));
        }

        public void looksUpFieldThatSupportsTag() {
            final Tag tag = new Tag(10);
            checking(new Expectations() {{
                one(field1).supports(tag); will(returnValue(false));
                one(field2).supports(tag); will(returnValue(true));
            }});
            specify(fields.lookup(tag), must.equal(field2));
        }

        public void hasSumOfChecksumsAsTotalChecksum() {
            checking(new Expectations() {{
                one(field1).checksum(); will(returnValue(1));
                one(field2).checksum(); will(returnValue(10));
            }});
            specify(fields.checksum(), must.equal(11));
        }
    }

    public class Empty {
        private Fields fields = new Fields();

        public void formatsToEmptyString() {
            specify(fields.format(), must.equal(""));
        }

        public void failsLookup() {
            specify(fields.lookup(new Tag(10)), must.equal(null));
        }
    }
}
