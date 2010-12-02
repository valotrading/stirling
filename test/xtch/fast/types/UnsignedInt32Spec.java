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
package xtch.fast.types;

import java.nio.ByteBuffer;

import jdave.Block;
import jdave.junit4.JDaveRunner;
import org.junit.runner.RunWith;

import xtch.types.AbstractTypeSpec;

@RunWith(JDaveRunner.class) public class UnsignedInt32Spec extends AbstractTypeSpec<UnsignedInt32> {
  public class Initialized {
    public void encodeValue() {
      byte[] actual = encode(22714L);
      byte[] expected = toByteArray(0x01, 0x31, 0xba); 
      assertArraysEquals(actual, expected);
    }

    public void encodeAndDecode() {
      assertEncodeAndDecode(231343L);
    }
  }

  @Override protected void encode(ByteBuffer buffer, Object value) {
    UnsignedInt32.TYPE.encode(buffer, (Long) value, 4);
  }

  @Override protected Object decode(ByteBuffer buffer) {
    return UnsignedInt32.TYPE.decode(buffer, 4);
  }
}
