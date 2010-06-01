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
public class IntegerFieldSpec extends Specification<IntegerField> {
    private IntegerField field = new IntegerField(dummy(Tag.class));

    public class AnyField {
        public IntegerField create() {
            return field;
        }

        public void parsesNegativeValues() {
            field.parse("-23");
            specify(field.intValue(), must.equal(-23));
        }

        public void parsesLeadingZeros() {
            field.parse("00023");
            specify(field.intValue(), must.equal(23));
        }

        public void failsToParseFloats() {
            specify(new Block() {
                @Override
                public void run() throws Throwable {
                    field.parse("1.23");
                }
            }, must.raise(InvalidFormatForTagException.class));
        }

        public void failsToParseNonNumericStrings() {
            specify(new Block() {
                @Override
                public void run() throws Throwable {
                    field.parse("ZZ");
                }
            }, must.raise(InvalidFormatForTagException.class));
        }
    }
}
