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
import org.jmock.Sequence;
import org.junit.runner.RunWith;

/**
 * @author Pekka Enberg 
 */
@RunWith(JDaveRunner.class)
public class RepeatingGroupFormattingSpec extends Specification<RepeatingGroup<?>> {
    private final RepeatingGroupInstance instance = mock(RepeatingGroupInstance.class);
    private final IntegerField instanceCount = mock(IntegerField.class);

    private RepeatingGroup<RepeatingGroupInstance> group = new RepeatingGroup<RepeatingGroupInstance>(instanceCount) {
        @Override
        protected RepeatingGroupInstance newInstance() {
            return instance;
        }
    };

    public class NoInstanceCount {
        public RepeatingGroup<?> create() {
            final Sequence sequence = sequence("sequence");
            checking(new Expectations() {{
                one(instanceCount).getValue(); will(returnValue(null)); inSequence(sequence);
            }});
            return group;
        }

        public void producesEmptyString() {
            specify(group.format(), must.equal(""));
        }
    }

    public class ZeroInstanceCount {
        public RepeatingGroup<?> create() {
            final Sequence sequence = sequence("sequence");
            checking(new Expectations() {{
                one(instanceCount).getValue(); will(returnValue(new Integer(0))); inSequence(sequence);
            }});
            return group;
        }

        public void producesEmptyString() {
            specify(group.format(), must.equal(""));
        }
    }

    public class NonZeroInstanceCount {
        private final RepeatingGroupInstance instance1 = mock(RepeatingGroupInstance.class, "instance1");
        private final RepeatingGroupInstance instance2 = mock(RepeatingGroupInstance.class, "instance2");

        public RepeatingGroup<?> create() {
            final Sequence sequence = sequence("sequence");
            checking(new Expectations() {{
                one(instanceCount).getValue(); will(returnValue(new Integer(2))); inSequence(sequence);
                one(instance1).format(); will(returnValue("X"));
                one(instance2).format(); will(returnValue("Y"));
            }});
            group.add(instance1);
            group.add(instance2);
            return group;
        }

        public void producesNonEmptyString() {
            specify(group.format(), must.equal("XY"));
        }
   }
}
