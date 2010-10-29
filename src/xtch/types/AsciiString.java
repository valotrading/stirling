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
package xtch.types;

import java.nio.ByteBuffer;

public class AsciiString extends AbstractType<String> {
  public static final AsciiString TYPE = new AsciiString();

  @Override public void encode(ByteBuffer buffer, String value, int length) {
    validateLength(length);
    buffer.put(rightPadded(value.getBytes(), length));
  }

  @Override public String decode(ByteBuffer buffer, int length) {
    validateLength(length);
    byte[] bytes = new byte[length];
    buffer.get(bytes, 0, length);
    return new String(bytes).trim();
  }

  private void validateLength(int length) {
    if (length == Integer.MAX_VALUE)
      throw new UnsupportedOperationException("variable length fields not supported");
  }
}
