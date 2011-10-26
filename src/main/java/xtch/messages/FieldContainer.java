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
package xtch.messages;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import xtch.elements.Field;

public abstract class FieldContainer {
  protected Map<Field<?>, Object> values = new HashMap<Field<?>, Object>();

  public void set(Field<?> field, Object value) {
    values.put(field, value);
  }

  @SuppressWarnings("unchecked") public <T> T get(Field<T> field) {
    return (T) values.get(field);
  }

  public Set<Field<?>> getFields() {
    return values.keySet();
  }
}
