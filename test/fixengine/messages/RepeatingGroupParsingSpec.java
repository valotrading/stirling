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
public class RepeatingGroupParsingSpec extends Specification<RepeatingGroup<?>> {
    private final RepeatingGroupInstance instance = mock(RepeatingGroupInstance.class);
    private final IntegerField instanceCount = mock(IntegerField.class);
    private final TokenStream tokens = mock(TokenStream.class);

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
                one(instanceCount).parse(tokens); inSequence(sequence);
                one(instanceCount).getValue(); will(returnValue(null)); inSequence(sequence);
                never(instance).parse(tokens); inSequence(sequence);
            }});
            group.parse(tokens);
            return group;
        }

        public void hasNoInstances() {
            specify(group.getInstances().isEmpty(), must.equal(true));
        }
    }

    public class ZeroInstanceCount {
        public RepeatingGroup<?> create() {
            final Sequence sequence = sequence("sequence");
            checking(new Expectations() {{
                one(instanceCount).parse(tokens); inSequence(sequence);
                one(instanceCount).getValue(); will(returnValue(0)); inSequence(sequence);
                never(instance).parse(tokens); inSequence(sequence);
            }});
            group.parse(tokens);
            return group;
        }

        public void hasNoInstances() {
            specify(group.getInstances().isEmpty(), must.equal(true));
        }
    }

    public class NonZeroInstanceCount {
        public RepeatingGroup<?> create() {
            final Sequence sequence = sequence("sequence");
            checking(new Expectations() {{
                one(instanceCount).parse(tokens); inSequence(sequence);
                one(instanceCount).getValue(); will(returnValue(new Integer(2))); inSequence(sequence);
                exactly(2).of(instance).parse(tokens); inSequence(sequence);
            }});
            group.parse(tokens);
            return group;
        }

        public void hasInstances() {
            specify(group.getInstances().size(), must.equal(2));
        }
   }
}