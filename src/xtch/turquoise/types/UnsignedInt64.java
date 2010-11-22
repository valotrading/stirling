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

import java.math.BigInteger;
import java.nio.ByteBuffer;
import xtch.types.AbstractType;

public class UnsignedInt64 extends AbstractType<BigInteger> {
  public static final UnsignedInt64 TYPE = new UnsignedInt64();

  @Override public void encode(ByteBuffer buffer, BigInteger value, int length) {
    buffer.put(value.toByteArray());
  }

  @Override public BigInteger decode(ByteBuffer buffer, int length) {
    byte[] bytes = new byte[length];
    buffer.get(bytes);
    return new BigInteger(bytes);
  }
}
