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

public class Price extends AbstractType<Long> {
  public static final Price TYPE = new Price();

  public static final long FACTOR = 100000000L;

  @Override public void encode(ByteBuffer buffer, Long value, int length) {
    long integer = value / FACTOR;
    write32bitsAsLittleEndian(buffer, integer);

    long fractional = value % FACTOR;
    write32bitsAsLittleEndian(buffer, fractional);
  }

  @Override public Long decode(ByteBuffer buffer, int length) {
    long integer = read32bitsAsLittleEndian(buffer);
    long fractional = read32bitsAsLittleEndian(buffer);

    return integer * FACTOR + fractional;
  }

}
