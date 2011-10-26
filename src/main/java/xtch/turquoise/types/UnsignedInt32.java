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

public class UnsignedInt32 extends AbstractType<Long> {
  public static final UnsignedInt32 TYPE = new UnsignedInt32();

  @Override public void encode(ByteBuffer buffer, Long value, int length) {
    write32bitsAsLittleEndian(buffer, value);
  }

  @Override public Long decode(ByteBuffer buffer, int length) {
    return read32bitsAsLittleEndian(buffer) & 0xffffffffL;
  }
}
