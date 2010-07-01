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

import static fixengine.messages.Field.DELIMITER;
import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.junit.runner.RunWith;

/**
 * @author Pekka Enberg
 */
@RunWith(JDaveRunner.class)
public class AbstractFieldSpec extends Specification<Field> {
    public class MandatoryFieldThatHasValue {
        private Field field;

        public Field create() {
            return field = new AbstractField<String>(new Tag(1)) {
                @Override
                public String name() {
                    return "Account";
                }

                @Override
                protected String value() {
                    return "value";
                }

                @Override
                public boolean hasValue() {
                    return true;
                }

                @Override
                public void parse(String value) {
                }
            };
        }

        public void formatsTagAndValue() {
            specify(field.format(), must.equal("1=value" + Field.DELIMITER));
        }

        public void toStringIsHumanReadable() {
            specify(field.toString(), must.equal("Account(1)=value"));
        }

        public void hasValidFormat() {
            specify(field.isFormatValid(), must.equal(true));
        }

        public void hasValidValue() {
            specify(field.isValueValid(), must.equal(true));
        }
    }

    public class MandatoryFieldThatHasNull {
        private Field field;

        public Field create() {
            return field = new AbstractField<String>(new Tag(1)) {
                @Override
                protected String value() {
                    return null;
                }

                @Override
                public boolean hasValue() {
                    return false;
                }

                @Override
                public void parse(String value) {
                }
            };
        }

        public void formatsTagWithoutValue() {
            specify(field.format(), must.equal("1=" + Field.DELIMITER));
        }

        public void hasValidFormat() {
            specify(field.isFormatValid(), must.equal(true));
        }

        public void hasValidValue() {
            specify(field.isValueValid(), must.equal(true));
        }
    }

    public class OptionalFieldThatHasValue {
        private Field field;

        public Field create() {
            return field = new AbstractField<String>(new Tag(1), null, Required.NO) {
                @Override
                protected String value() {
                    return "value";
                }

                @Override
                public boolean hasValue() {
                    return true;
                }

                @Override
                public void parse(String value) {
                }
            };
        }

        public void formatsTagAndValue() {
            specify(field.format(), must.equal("1=value" + Field.DELIMITER));
        }

        public void hasValidFormat() {
            specify(field.isFormatValid(), must.equal(true));
        }

        public void hasValidValue() {
            specify(field.isValueValid(), must.equal(true));
        }
    }

    public class OptionalFieldThatHasNullValue {
        private Field field;

        public Field create() {
            return field = new AbstractField<String>(new Tag(1), null, Required.NO) {
                @Override
                protected String value() {
                    return null;
                }

                @Override
                public boolean hasValue() {
                    return false;
                }

                @Override
                public void parse(String value) {
                }
            };
        }

        public void formatsNothing() {
            specify(field.format(), must.equal(""));
        }

        public void hasValidFormat() {
            specify(field.isFormatValid(), must.equal(true));
        }

        public void hasValidValue() {
            specify(field.isValueValid(), must.equal(true));
        }
    }

    public class FieldThatHasParsedInvalidValue {
        private Field field;

        public Field create() {
            return field = new AbstractField<String>(new Tag(1), null, Required.NO) {
                @Override
                protected String value() {
                    return null;
                }

                @Override
                public boolean hasValue() {
                    return false;
                }

                @Override
                public void parse(String value) {
                    throw new InvalidValueForTagException(value);
                }
            };
        }

        public void hasValidFormat() {
            specify(field.isFormatValid(), must.equal(true));
        }

        public void doesNotHaveValidValue() {
            field.parse(new TokenStream("=Z" + DELIMITER));
            specify(field.isValueValid(), must.equal(false));
        }
    }

    public class FieldThatHasParsedInvalidFormat {
        private Field field;

        public Field create() {
            return field = new AbstractField<String>(new Tag(1), null, Required.NO) {
                @Override
                protected String value() {
                    return null;
                }

                @Override
                public boolean hasValue() {
                    return false;
                }

                @Override
                public void parse(String value) {
                    throw new InvalidValueFormatException(value);
                }
            };
        }

        public void doesNotHaveValidFormat() {
            field.parse(new TokenStream("=Z" + DELIMITER));
            specify(field.isFormatValid(), must.equal(false));
        }

        public void hasValidValue() {
            specify(field.isValueValid(), must.equal(true));
        }
    }
}
