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

import static fixengine.messages.Field.DELIMITER;
import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.jmock.Expectations;
import org.junit.runner.RunWith;

/**
 * @author Pekka Enberg 
 */
@RunWith(JDaveRunner.class)
public class RepeatingGroupInstanceParsingSpec extends Specification<RepeatingGroupInstance> {
    private final RepeatingGroupInstance instance = new RepeatingGroupInstance();

    public class InstanceThatHasRequiredField {
        private final StringField required = new StringField(new Tag(488), null, Required.YES);

        public RepeatingGroupInstance create() {
            instance.add(required);
            return instance;
        }

        public void parsesValueForInstanceFields() {
            instance.parse(new TokenStream("488=id" + DELIMITER));
            specify(required.getValue(), must.equal("id"));
        }
    }

    public class InstanceThatHasMissingFields {
        private final Field missing = mock(Field.class);

        public RepeatingGroupInstance create() {
            instance.add(missing);
            return instance;
        }

        public void hasNotHaveRequiredFields() {
            checking(new Expectations() {{
                one(missing).isMissing(); will(returnValue(true));
            }});
            specify(instance.contains(new fixengine.Specification<Field>() {
                @Override
                public boolean isSatisfiedBy(Field field) {
                    return field.isMissing();
                }
            }), must.equal(true));
        }
    }
    
    public class InstanceThatHasOnlyOptionalFields {
       public RepeatingGroupInstance create() {
            return instance;
        }
   }
}