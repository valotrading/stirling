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

import fixengine.messages.HeartBtIntField;

/**
 * @author Pekka Enberg 
 */
@RunWith(JDaveRunner.class)
public class HeartBtIntFieldSpec extends Specification<HeartBtIntField> {
    private final MessageBuffer out = new MessageBuffer();
    private HeartBtIntField field;

    public class HeartbeatOfTwentySeconds {
        public HeartBtIntField create() {
            return field = new HeartBtIntField(20);
        }

        public void formatsToWriter() throws Exception {
            out.append(field);
            specify(out.toString(), must.equal("108=20\001"));
        }
    }

    public class HeartbeatOfThirtySeconds {
        public HeartBtIntField create() {
            return field = new HeartBtIntField(30);
        }

        public void formatsToWriter() throws Exception {
            out.append(field);
            specify(out.toString(), must.equal("108=30\001"));
        }
    }
}
