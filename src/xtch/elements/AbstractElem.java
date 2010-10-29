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

import xtch.types.Type;

public abstract class AbstractElem<T> implements Elem<T> {
  private final Type<T> type;
  private final int length;

  protected AbstractElem(Type<T> type, int length) {
    this.type = type;
    this.length = length;
  }

  protected AbstractElem(Type<T> type) {
    this(type, Integer.MAX_VALUE);
  }

  @Override public void encode(ByteBuffer buffer, Object value) {
    type.encode(buffer, (T) value, length);
  }

  @Override public T decode(ByteBuffer buffer) {
    return type.decode(buffer, length);
  }
}
