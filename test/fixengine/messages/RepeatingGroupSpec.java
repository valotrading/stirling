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
public class RepeatingGroupSpec extends Specification<RepeatingGroup<?>> {
    private final IntegerField instanceCount = mock(IntegerField.class);
    private RepeatingGroup<RepeatingGroupInstance> group = new RepeatingGroup<RepeatingGroupInstance>(instanceCount) {
        @Override
        protected RepeatingGroupInstance newInstance() {
            throw new UnsupportedOperationException();
        }
    };

    public class NewGroup {
        public RepeatingGroup<?> create() {
            return group;
        }

        public void isNotParsed() {
            specify(group.isParsed(), must.equal(false));
        }
    }

    public class ParsedGroup {
        private final TokenStream stream = dummy(TokenStream.class);

        public RepeatingGroup<?> create() {
            checking(new Expectations() {{
                one(instanceCount).parse(stream);
                one(instanceCount).getValue(); will(returnValue(new Integer(0)));
            }});
            group.parse(stream);
            return group;
        }

        public void isParsed() {
            specify(group.isParsed(), must.equal(true));
        }
    }

    public class NonEmpty {
        private final RepeatingGroupInstance instance1 = mock(RepeatingGroupInstance.class, "instance1");
        private final RepeatingGroupInstance instance2 = mock(RepeatingGroupInstance.class, "instance2");

        public RepeatingGroup<?> create() {
            group.add(instance1);
            group.add(instance2);
            return group;
        }

        public void hasLengthOfInstanceCountAndInstances() {
            checking(new Expectations() {{
                one(instanceCount).length(); will(returnValue(1));
                one(instance1).length(); will(returnValue(2));
                one(instance2).length(); will(returnValue(3));
            }});
            specify(group.length(), must.equal(6));
        }

        public void hasChecksumOfInstanceCountAndInstances() {
            checking(new Expectations() {{
                one(instanceCount).checksum(); will(returnValue(1));
                one(instance1).checksum(); will(returnValue(2));
                one(instance2).checksum(); will(returnValue(3));
            }});
            specify(group.checksum(), must.equal(6));
        }
    }

    public class Empty {
        public RepeatingGroup<?> create() {
            return group;
        }

        public void hasZeroLength() {
            checking(new Expectations() {{
                one(instanceCount).length(); will(returnValue(0));
            }});
            specify(group.length(), must.equal(0));
        }

        public void hasZeroChecksum() {
            checking(new Expectations() {{
                one(instanceCount).checksum(); will(returnValue(0));
            }});
            specify(group.checksum(), must.equal(0));
        }
    }
}
