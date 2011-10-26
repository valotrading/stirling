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

import jdave.Specification;
import jdave.junit4.JDaveRunner;
import org.junit.runner.RunWith;

import xtch.types.AbstractTypeSpec;

public class PriceSpec extends AbstractTypeSpec<Price> {
  public class Initialized {
    public void encodeValue() {
      byte[] actual = encode(-120000.12345678);
      byte[] expected = toByteArray(0x40, 0x2b, 0xfe, 0xff, 0xb3, 0x9e, 0x43, 0xff); 
      assertArraysEquals(actual, expected);
    }

    public void encodeAndDecode() {
      assertEncodeAndDecode(-120000.1234567);
    }
  }

  @Override protected void encode(ByteBuffer buffer, Object value) {
    Price.TYPE.encode(buffer, (Double) value, 8);
  }

  @Override protected Double decode(ByteBuffer buffer) {
    return Price.TYPE.decode(buffer, 8);
  }
}
