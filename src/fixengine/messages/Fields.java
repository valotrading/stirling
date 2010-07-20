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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fixengine.tags.MsgType;

/**
 * @author Pekka Enberg
 */
public class Fields implements Iterable<Field> {
    private final List<Field> fields = new ArrayList<Field>();

    public void add(Field field) {
        fields.add(field);
    }

    @Override public Iterator<Field> iterator() {
        return fields.iterator();
    }

    public Field lookup(Tag tag) {
        for (Field field : fields) {
            if (field.supports(tag)) {
                return field;
            }
        }
        return null;
    }

    public void parse(ByteBuffer b) {
        Field previous = new StringField(MsgType.TAG);
        for (;;) {
            b.mark();
            Tag tag = parseTag(b, previous);
            Field field = lookup(tag);
            if (field == null)
                break;
            if (field.isParsed())
                throw new TagMultipleTimesException(field.prettyName() + ": Tag multiple times");
            String value = parseValue(b, field);
            if (!value.isEmpty())
                field.parseValue(value);
            else
                field.parseValue(null);
            if (!field.isFormatValid())
                throw new InvalidValueFormatException(field.prettyName() + ": Invalid value format");
            if (!field.isValueValid())
                throw new InvalidValueException(field.prettyName() + ": Invalid value");
            previous = field;
        }
        b.reset();
    }

    private static Tag parseTag(ByteBuffer b, Field previous) {
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

    public String format() {
        StringBuilder result = new StringBuilder();
        for (Field field : fields) {
            result.append(field.format());
        }
        return result.toString();
    }

    public void validate() {
        for (Field field : fields) {
            if (field.isEmpty())
                throw new EmptyTagException(field.prettyName() + ": Empty tag");
        }
    }

    public void add(Tag<?> tag, Required required) {
        this.fields.add(tag.newField(required));
    }
}
