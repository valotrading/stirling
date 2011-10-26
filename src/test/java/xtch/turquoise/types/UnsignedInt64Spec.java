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

import jdave.Specification;
import jdave.junit4.JDaveRunner;
import org.junit.runner.RunWith;

import xtch.types.AbstractTypeSpec;

public class UnsignedInt64Spec extends AbstractTypeSpec<UnsignedInt64> {
  public class Initialized {
    public void encodeValue() {
      byte[] expected = toByteArray(0xfe, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff);
      byte[] actual = encode(new BigInteger(expected));
      assertArraysEquals(actual, expected);
    }

    public void encodeAndDecode() {
      byte[] bytes = toByteArray(0xfa, 0xa7, 0xb8, 0x07, 0xf2, 0xff, 0xff, 0xff);
      assertEncodeAndDecode(new BigInteger(bytes));
    }
  }

  @Override protected void encode(ByteBuffer buffer, Object value) {
    UnsignedInt64.TYPE.encode(buffer, (BigInteger) value, 8);
  }

  @Override protected BigInteger decode(ByteBuffer buffer) {
    return UnsignedInt64.TYPE.decode(buffer, 8);
  }
}
