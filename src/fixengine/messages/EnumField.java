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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Pekka Enberg 
 */
public class EnumField<T extends Formattable> extends AbstractField<T> {
    public EnumField(Tag<? extends EnumField<T>> tag) {
        this(tag, Required.YES);
    }

    public EnumField(Tag<? extends EnumField<T>> tag, T value) {
        this(tag, value, Required.YES);
    }

    public EnumField(Tag<? extends EnumField<T>> tag, T value, Required required) {
        super(tag, value, required);
    }

    public EnumField(Tag<? extends EnumField<T>> tag, Required required) {
        super(tag, null, required);
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override public void parse(String value) {
        if (tag() instanceof EnumTag) {
            for (Field field : tagClass().getDeclaredFields()) {
                if (!field.getType().equals(Value.class))
                    continue;
                T candidate = enumValue(field);
                if (candidate.value().toString().equals(value)) {
                    this.value = candidate;
                    return;
                }
            }
            throw new InvalidValueForTagException(value);
        }
        throw new UnsupportedOperationException("parsing not implemented for this tag type");
    }

    @Override protected String value() {
        if (value == null) {
            return null;
        }
        return value.value();
    }

    @SuppressWarnings("unchecked") private T enumValue(Field field) {
        try {
            return (T) method(field).invoke(tag());
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Method method(Field field) {
        try {
            return tagClass().getDeclaredMethod(field.getName());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Class<? extends Tag> tagClass() {
        return tag().getClass();
    }
}
