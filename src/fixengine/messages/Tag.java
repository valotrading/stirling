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

import java.nio.ByteBuffer;

import lang.Classes;
import lang.Objects;

/**
 * @author Pekka Enberg 
 */
public class Tag<T extends Field> {
    private transient Class<T> type; // FIXME
    private int value;

    @Deprecated public Tag(int value) {
        this(value, null);
    }

    public Tag(int value, Class<T> type) {
        this.value = value;
        this.type = type;
    }

    public boolean isUserDefined() {
        return value() >= 5000;
    }

    public int length() {
        return Integer.toString(value).length();
    }

    public int checksum() {
        int checksum = 0;
        String s = Integer.toString(value);
        for (int i = 0; i < s.length(); i++)
            checksum += s.charAt(i);
        return checksum;
    }

    public int value() {
        return value;
    }

    public T newField(Required required) {
        T field = Classes.newInstance(type, Tag.class, this);
        field.setRequired(required);
        return field;
    }

    public T parse(ByteBuffer b, Tag<?> previous) throws UnexpectedTagException {
        Tag<?> tag = parseTag(b, previous);
        if (value != tag.value)
            throw new UnexpectedTagException(tag);
        T field = Classes.newInstance(type, Tag.class, tag);
        String value = parseValue(b, field);
        if (!value.isEmpty())
            field.parseValue(value);
        else
            field.parseValue(null);
        return field;
    }

    private static Tag parseTag(ByteBuffer b, Tag previous) {
        StringBuilder result = new StringBuilder();
        for (;;) {
            int ch = b.get();
            if (ch == '=')
                break;
            result.append((char) ch);
        }
        String s = result.toString();
        if (s.contains("" + Field.DELIMITER))
            throw new NonDataValueIncludesFieldDelimiterException(previous.prettyName() + ": Non-data value includes field delimiter (SOH character)");
        Tag tag = new Tag(Integer.parseInt(s));
        if (tag.isUserDefined())
            throw new InvalidTagNumberException("Invalid tag number: " + tag.value());
        return tag;
    }

    private static String parseValue(ByteBuffer b, Field field) {
        StringBuilder result = new StringBuilder();
        for (;;) {
            int ch = b.get();
            if (ch == Field.DELIMITER)
                break;
            result.append((char) ch);
        }
        return result.toString();
    }

    public String prettyName() {
        return getClass().getSimpleName() + "(" + value + ")";
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equal(this, obj);
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
