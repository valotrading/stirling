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
package xtch.turquoise.types;

import java.nio.ByteBuffer;
import xtch.types.AbstractType;

public class AsciiString extends AbstractType<String> {
  public static final AsciiString TYPE = new AsciiString();

  @Override public void encode(ByteBuffer buffer, String value, int length) {
    buffer.put(value.getBytes());
    if (length > value.length())
      buffer.put((byte) 0x00);
  }

  @Override public String decode(ByteBuffer buffer, int length) {
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < length; i++) {
      byte ch = buffer.get();
      if (ch == (byte) 0x00)
        break;
      buf.append((char) ch);
    }
    return buf.toString();
  }
}
