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
package xtch.elements;

import java.nio.ByteBuffer;
import java.util.Arrays;

import xtch.types.Type;

public class Field<T> extends AbstractElem<T> implements Comparable<Field<?>> {
  private final String name;
  private int length = Integer.MAX_VALUE;

  protected Field(String name, Type<T> type, int length) {
    super(type, length);
    this.name = name;
    this.length = length;
  }

  protected Field(String name, Type<T> type) {
    this(name, type, Integer.MAX_VALUE);
  }

  public String getName() {
    return name;
  }

  @Override public int compareTo(Field<?> field) {
    return getName().compareTo(field.getName());
  }
}
