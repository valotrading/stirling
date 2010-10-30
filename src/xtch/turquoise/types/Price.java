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

public class Price extends AbstractType<Double> {
  public static final Price TYPE = new Price();

  private static final long FRAC_TO_INT_FACTOR = 100000000L;

  @Override public void encode(ByteBuffer buffer, Double value, int length) {
    long integer = value.longValue();
    writeAsLittleEndian(buffer, integer);

    long fractional = (long) ((double) (value - integer) * FRAC_TO_INT_FACTOR);
    writeAsLittleEndian(buffer, fractional);
  }

  @Override public Double decode(ByteBuffer buffer, int length) {
    return readAsLittleEndian(buffer) + (double) readAsLittleEndian(buffer) / (double) FRAC_TO_INT_FACTOR;
  }

  private void writeAsLittleEndian(ByteBuffer buffer, long value) {
    buffer.put((byte) (value >> 0  & 0xff));
    buffer.put((byte) (value >> 8  & 0xff));
    buffer.put((byte) (value >> 16 & 0xff));
    buffer.put((byte) (value >> 24 & 0xff));
  }

  private long readAsLittleEndian(ByteBuffer buffer) {
    long result = 0;
    result |= (buffer.get() & 0xff) << 0;
    result |= (buffer.get() & 0xff) << 8;
    result |= (buffer.get() & 0xff) << 16;
    result |= (buffer.get() & 0xff) << 24;
    return result;
  }
}
