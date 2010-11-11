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

public class SignedInt8Spec extends AbstractTypeSpec<SignedInt8> {
  public class InitializedSignedInt8 {
    public void encodeValue() {
      byte[] actual = encode(-127);
      byte[] expected = toByteArray(0x81); 
      assertArraysEquals(actual, expected);
    }

    public void encodeAndDecode() {
      assertEncodeAndDecode(-128);
    }
  }

  @Override protected void encode(ByteBuffer buffer, Object value) {
    SignedInt8.TYPE.encode(buffer, (Integer) value, 1);
  }

  @Override protected Integer decode(ByteBuffer buffer) {
    return SignedInt8.TYPE.decode(buffer, 1);
  }
}
