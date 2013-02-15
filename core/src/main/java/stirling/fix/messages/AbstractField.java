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

import java.nio.ByteBuffer;

import stirling.lang.Objects;

/**
 * @author Pekka Enberg 
 */
public abstract class AbstractField<T> implements Field {
    protected boolean validFormat = true;
    private boolean validValue = true;
    private Required required;
    private String name;
    private boolean defined;
    private final Tag<?> tag;
    protected T value;

    public AbstractField(Tag<?> tag, T value, Required required) {
        this.required = required;
        this.value = value;
        this.tag = tag;
    }

    @Override public Required isRequired() {
        return required;
    }

    @Override public void setRequired(Required required) {
        this.required = required;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public Tag<?> tag() {
        return tag;
    }

    @Override public boolean isEmpty() {
        return !hasValue() && defined;
    }

    @Override public boolean isParsed() {
        return defined;
    }

    @Override public boolean hasValue() {
        return value != null;
    }

    @Override public boolean hasSingleTag() {
        return true;
    }

    public boolean isFormatValid() {
        return validFormat;
    }

    public boolean isValueValid() {
        return validValue;
    }

    @Override public boolean isMissing() {
        return required.isRequired() && !hasValue();
    }

    @Override public boolean isConditional() {
        return required.isConditional();
    }

    @Override public void parse(ByteBuffer b) {
        if (isParsed())
            throw new TagMultipleTimesException(prettyName() + ": Tag multiple times");
        String value = parseValue(b);
        if (!value.isEmpty())
            parseValue(value);
        else
            parseValue((String) null);
    }

    public static String parseValue(ByteBuffer b) {
        StringBuilder result = new StringBuilder();
        for (;;) {
            int ch = b.get();
            if (ch == Field.DELIMITER)
                break;
            result.append((char) ch);
        }
        return result.toString();
    }

    public void parseValue(String value) {
        defined = true;
        if (value == null) {
            return;
        }
        try {
            parse(value);
        } catch (InvalidValueForTagException e) {
            validValue = false;
        } catch (InvalidValueFormatException e) {
            validFormat = false;
        }
    }

    public abstract void parse(String value);

    public String format() {
        String value = null;
        if (hasValue()) {
            value = value();
        }
        if (value == null) {
            return "";
        }
        return tag + "=" + value + DELIMITER;
    }

    protected abstract String value();

    @Override public boolean equals(Object obj) {
        return Objects.equal(this, obj);
    }

    @Override public int hashCode() {
        return Objects.hashCode(this);
    }

    public String prettyName() {
        return name() + "(" + tag() + ")";
    }

    private String name() {
        if (name == null) {
            name = parseFieldName();
        }
        return name;
    }

    private String parseFieldName() {
        if (!tag.getClass().equals(Tag.class)) {
            return ClassNameHelper.removeTrailingDollar(tag.getClass().getSimpleName());
        }
        String s = ClassNameHelper.removeTrailingDollar(getClass().getSimpleName());
        if (s.length() < 5) {
            return s;
        }
        return s.substring(0, s.length() - 5);
    }

    @Override public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(name());
        result.append("(");
        result.append(tag.value());
        result.append(")=");
        if (hasValue()) {
            result.append(value());
        }
        return result.toString();
    }
}
