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

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.junit.runner.RunWith;

/**
 * @author Pekka Enberg 
 */
@RunWith(JDaveRunner.class)
public class TagSpec extends Specification<Tag> {
    public class TagInNormalRange {
        private final Tag tag = new Tag(4999);

        public void isNotUserDefined() {
            specify(tag.isUserDefined(), must.equal(false));
        }
    }

    public class TagInUserDefinedRange {
        private final Tag tag = new Tag(5000);

        public void isUserDefined() {
            specify(tag.isUserDefined(), must.equal(true));
        }
    }
    
    public class TagInReservedRange {
        private final Tag tag = new Tag(10000);

        public void isUserDefined() {
            specify(tag.isUserDefined(), must.equal(true));
        }
    }
}