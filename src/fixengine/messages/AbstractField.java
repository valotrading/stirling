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

import lang.Objects;

/**
 * @author Pekka Enberg 
 */
public abstract class AbstractField<T> implements Field {
    protected boolean validFormat = true;
    private boolean validValue = true;
    private Required required;
    private final String name;
    private boolean defined;
    private final Tag tag;
    private int checksum;
    private int length;
    protected T value;

    public AbstractField(Tag tag) {
        this(tag, null, Required.YES);
    }

    public AbstractField(Tag tag, T value, Required required) {
        this.required = required;
        this.value = value;
        this.tag = tag;
        this.name = parseFieldName();
    }
    
    private String parseFieldName() {
        if (!tag.getClass().equals(Tag.class)) {
            return tag.getClass().getSimpleName();
        }
        String s = getClass().getSimpleName();
        if (s.length() < 5) {
            return s;
        }
        return s.substring(0, s.length() - 5);
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

    @Override
    public String name() {
        return name;
    }

    @Override
    public Tag tag() {
        return tag;
    }

    public int checksum() {
        return checksum;
    }

    public int length() {
        return length;
    }

    public boolean supports(Tag tag) {
        return tag.equals(this.tag);
    }

    @Override
    public boolean isEmpty() {
        return !hasValue() && defined;
    }

    @Override
    public boolean isParsed() {
        return defined;
    }
    
    @Override
    public boolean hasValue() {
        return value != null;
    }

    public boolean isFormatValid() {
        return validFormat;
    }

    public boolean isValueValid() {
        return validValue;
    }

    @Override
    public boolean isMissing() {
        return required.equals(Required.YES) && !hasValue();
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
            if (isOptional())
                return "";
            value = "";
        }
        return tag + "=" + value + DELIMITER;
    }

    private boolean isOptional() {
        return required.equals(Required.NO);
    }

    protected abstract String value();

    @Override
    public boolean equals(Object obj) {
        return Objects.equal(this, obj);
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this);
    }

    public String prettyName() {
        return name() + "(" + tag() + ")";
    }

    @Override
    public String toString() {
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
