/*
 * Copyright 2012 the original author or authors.
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

import org.joda.time.DateTime;

public interface FieldContainer extends Formattable, Iterable<Field>, Parseable {
    Field lookup(int tag);
    Field lookup(Tag<?> tag);
    boolean isDefined(Tag<?> tag);
    boolean hasValue(Tag<?> tag);
    String getString(Tag<? extends StringField> tag);
    Integer getInteger(Tag<? extends IntegerField> tag);
    Double getFloat(Tag<? extends FloatField> tag);
    boolean getBoolean(Tag<? extends BooleanField> tag);
    <T extends EnumType> T getEnum(Tag<? extends EnumField<T>> tag);
    DateTime getDateTime(Tag<? extends AbstractField<DateTime>> tag);
    void setString(Tag<? extends StringField> tag, String value);
    void setInteger(Tag<? extends IntegerField> tag, Integer value);
    void setFloat(Tag<? extends FloatField> tag, Double value);
    void setBoolean(Tag<? extends BooleanField> tag, Boolean value);
    <T> void setEnum(Tag<? extends EnumField<Value<T>>> tag, Value<T> value);
    void setDateTime(Tag<? extends AbstractField<DateTime>> tag, DateTime value);
}
