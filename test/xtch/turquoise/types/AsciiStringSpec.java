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

@RunWith(JDaveRunner.class) public class AsciiStringSpec extends AbstractTypeSpec<AsciiString> {
  public class Initialized {
    public void encodeValueOfMaxLength() {
      byte[] actual = encode("0123456789");
      byte[] expected = toByteArray(48, 49, 50, 51, 52, 53, 54, 55, 56, 57); 
      assertArraysEquals(actual, expected);
    }

    public void encodeValueOfSmallerThanMaxLength() {
      byte[] actual = encode("01234567");
      byte[] expected = toByteArray(48, 49, 50, 51, 52, 53, 54, 55, 0x00); 
      assertArraysEquals(actual, expected);
    }

    public void encodeAndDecode() {
      assertEncodeAndDecode("foobar");
    }
  }

  @Override protected void encode(ByteBuffer buffer, Object value) {
    AsciiString.TYPE.encode(buffer, (String) value, 10);
  }

  @Override protected String decode(ByteBuffer buffer) {
    return AsciiString.TYPE.decode(buffer, 10);
  }
}
