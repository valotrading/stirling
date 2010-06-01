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

import jdave.Block;
import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.junit.runner.RunWith;

/**
 * @author Pekka Enberg 
 */
@RunWith(JDaveRunner.class)
public class TokenSpec extends Specification<Token> {
    public class NonNumericToken {
        private final Token token = new Token(Token.Type.TAG_OR_VALUE, "A");

        public void raisesExceptionIfConvertedToInt() {
            specify(new Block() {
                @Override
                public void run() throws Throwable {
                    token.intValue(); 
                }
            }, must.raise(InvalidNumberFormatException.class));
        }
    }
}
