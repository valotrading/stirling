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
package stirling.fix.messages;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.junit.runner.RunWith;

/**
 * @author Pekka Enberg 
 */
@RunWith(JDaveRunner.class)
public class TagSpec extends Specification<Tag<?>> {
    public class TagInNormalRange {
        public void isNotUserDefined() {
            specify(Tag.isUserDefined(4999), must.equal(false));
        }
    }

    public class TagInUserDefinedRange {
        public void isUserDefined() {
            specify(Tag.isUserDefined(5000), must.equal(true));
        }
    }
    
    public class TagInReservedRange {
        public void isUserDefined() {
            specify(Tag.isUserDefined(10000), must.equal(true));
        }
    }
}