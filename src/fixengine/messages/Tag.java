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
    private Class<T> type;
    private int value;

    public Tag(int value, Class<T> type) {
        this.value = value;
        this.type = type;
    }

    public static boolean isUserDefined(int tag) {
        return tag >= 5000;
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

    public int parse(ByteBuffer b, Tag<?> previous) throws UnexpectedTagException {
        int tag = parseTag(b, previous);
        if (value != tag)
            throw new UnexpectedTagException(tag);
        return tag;
    }

    private static int parseTag(ByteBuffer b, Tag previous) {
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
        int tag = Integer.parseInt(s);
        if (isUserDefined(tag))
            throw new InvalidTagNumberException("Invalid tag number: " + tag);
        return tag;
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
