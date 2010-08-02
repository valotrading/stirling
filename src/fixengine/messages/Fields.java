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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import fixengine.tags.MsgType;

/**
 * @author Pekka Enberg
 */
public class Fields implements Iterable<Field> {
    private final Map<Integer, Field> fields = new LinkedHashMap<Integer, Field>();

    @Override public Iterator<Field> iterator() {
        return fields.values().iterator();
    }

    public Field lookup(Tag<?> tag) {
        return fields.get(tag.value());
    }

    public Field lookup(int tag) {
        return fields.get(tag);
    }

    public void parse(ByteBuffer b) {
        Tag<?> previous = MsgType.TAG;
        for (;;) {
            b.mark();
            int tag = Tag.parseTag(b, previous);
            Field field = lookup(tag);
            if (field == null)
                break;
            previous = field.parse(b);
            if (!field.isFormatValid())
                throw new InvalidValueFormatException(field.prettyName() + ": Invalid value format");
            if (!field.isValueValid())
                throw new InvalidValueException(field.prettyName() + ": Invalid value");
        }
        b.reset();
    }

    public String format() {
        StringBuilder result = new StringBuilder();
        for (Field field : fields.values()) {
            result.append(field.format());
        }
        return result.toString();
    }

    public void validate() {
        for (Field field : fields.values()) {
            if (field.isEmpty())
                throw new EmptyTagException(field.prettyName() + ": Empty tag");
        }
    }

    public void add(Tag<?> tag, Required required) {
        this.fields.put(tag.value(), tag.newField(required));
    }
}
