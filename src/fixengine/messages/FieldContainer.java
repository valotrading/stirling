/*
 * Copyright 2010 the original author or authors.
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

import org.joda.time.DateTime;

public class FieldContainer implements Iterable<Field> {
    private final Map<Integer, Field> fields = new LinkedHashMap<Integer, Field>();

    @Override public Iterator<Field> iterator() {
        return fields.values().iterator();
    }

    public Field lookup(Tag<?> tag) {
        return lookup(tag.value());
    }

    public Field lookup(int tag) {
        return fields.get(tag);
    }

    public boolean isDefined(Tag<?> tag) {
        return lookup(tag) != null;
    }

    public void parse(ByteBuffer b) {
        while (b.hasRemaining()) {
            b.mark();
            int tag = Tag.parseTag(b);
            Field field = lookup(tag);
            if (field == null) {
                if (Tag.isUserDefined(tag))
                    throw new InvalidTagNumberException("Invalid tag number: " + tag);
                b.reset();
                break;
            }
            field.parse(b);
            if (!field.isFormatValid())
                throw new InvalidValueFormatException(field.prettyName() + ": Invalid value format");
            if (!field.isValueValid())
                throw new InvalidValueException(field.prettyName() + ": Invalid value");
        }
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

    public void add(RepeatingGroup group, Required required) {
        group.setRequired(required);
        fields.put(group.countTag().value(), group);
    }

    protected void field(Tag<?> tag) {
        field(tag, Required.YES);
    }

    protected void field(Tag<?> tag, Required required) {
        add(tag, required);
    }

    protected void group(RepeatingGroup group, Required required) {
        add(group, required);
    }

    protected void group(RepeatingGroup group) {
        group(group, Required.YES);
    }

    public boolean hasValue(Tag<?> tag) {
        Field field = lookup(tag);
        return field.hasValue();
    }

    public void setString(Tag<StringField> tag, String value) {
        StringField field = (StringField) lookup(tag);
        field.setValue(value);
    }

    public void setInteger(Tag<IntegerField> tag, Integer value) {
        IntegerField field = (IntegerField) lookup(tag);
        field.setValue(value);
    }

    public void setFloat(Tag<? extends FloatField> tag, Double value) {
        FloatField field = (FloatField) lookup(tag);
        field.setValue(value);
    }

    public void setBoolean(Tag<BooleanField> tag, Boolean value) {
        BooleanField field = (BooleanField) lookup(tag);
        field.setValue(value);
    }

    public void setDateTime(Tag<UtcTimestampField> tag, DateTime value) {
        UtcTimestampField field = (UtcTimestampField) lookup(tag);
        field.setValue(value);
    }

    @SuppressWarnings("unchecked") public <T> void setEnum(Tag<? extends EnumField<Value<T>>> tag, Value<T> value) {
        EnumField<Value<T>> field = (EnumField<Value<T>>) lookup(tag);
        field.setValue(value);
    }

    public String getString(Tag<StringField> tag) {
        StringField field = (StringField) lookup(tag);
        return field.getValue();
    }

    public Integer getInteger(Tag<IntegerField> tag) {
        IntegerField field = (IntegerField) lookup(tag);
        return field.getValue();
    }

    public Double getFloat(Tag<FloatField> tag) {
        FloatField field = (FloatField) lookup(tag);
        return field.getValue();
    }

    public boolean getBoolean(Tag<BooleanField> tag) {
        BooleanField field = (BooleanField) lookup(tag);
        Boolean result = field.getValue();
        if (result == null)
            result = Boolean.FALSE;
        return result;
    }

    public DateTime getDateTime(Tag<UtcTimestampField> tag) {
        UtcTimestampField field = (UtcTimestampField) lookup(tag);
        return field.getValue();
    }

    @SuppressWarnings("unchecked") public <T extends Formattable> T getEnum(Tag<? extends EnumField<T>> tag) {
        EnumField<T> field = (EnumField<T>) lookup(tag);
        return field.getValue();
    }
}
