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

import java.io.IOException;
import java.io.Reader;

import jdave.Block;
import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.jmock.Expectations;
import org.junit.runner.RunWith;

/**
 * @author Pekka Enberg 
 */
@RunWith(JDaveRunner.class)
public class TokenStreamSpec extends Specification<TokenStream> {
    public class EmptyTokenStream {
        private final TokenStream tokens = new TokenStream("");

        public TokenStream create() {
            return tokens;
        }

        public void returnsNullTag() {
            specify(tokens.tag(), must.equal(null));
        }
    }

    public class TokenStreamThatHasTagAsNextToken {
        private final TokenStream tokens = new TokenStream("448=id");

        public TokenStream create() {
            return tokens;
        }

        public void preservesTokenStreamUponPush() {
            Tag tag = tokens.tag();
            tokens.push(tag.toString());
            specify(tokens.tag(), must.equal(tag));
        }
    }

    public class TokenStreamThatHasTokens {
        private final TokenStream tokens = new TokenStream("xyz");

        public TokenStream create() {
            return tokens;
        }

        public void returnsNextTokenAsLookahead() {
            specify(tokens.lookahead(), must.equal('x'));
        }

        public void advancesStreamByOneTokenIfMatchingIsSuccessful() {
            tokens.match(tokens.lookahead());
            specify(tokens.lookahead(), must.equal('y'));
        }

        public void raisesParseExceptionIfMatchingFails() {
            specify(new Block() {
                @Override
                public void run() throws Throwable {
                    tokens.match('y');
                }
            }, must.raise(UnexpectedTokenException.class, "Expected 'y', but was: 'x'"));
        }
    }

    public class TokenStreamThatIsBackedByReaderThatHasIoErrors {
        private final Reader reader = mock(Reader.class);

        public void raisesParseException() throws IOException {
            checking(new Expectations() {{
                one(reader).read(); will(throwException(new IOException()));
            }});
            specify(new Block() {
                @Override
                public void run() throws Throwable {
                    new TokenStream(reader);
                }
            }, must.raise(ParseException.class));
        }
    }
}
