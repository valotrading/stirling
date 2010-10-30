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
package xtch.types;

import java.nio.ByteBuffer;
import java.util.Arrays;

import jdave.Specification;
import jdave.junit4.JDaveRunner;
import org.junit.runner.RunWith;

@RunWith(JDaveRunner.class) public abstract class AbstractTypeSpec<T> extends Specification<T> {
  private final int BUFFER_SIZE = 1024;

  protected byte[] toByteArray(int... data) {
    byte[] bytes = new byte[data.length];
    for (int i = 0; i < data.length; i++)
      bytes[i] = (byte) data[i];
    return bytes;
  }

  protected void assertArraysEquals(byte[] a1, byte[] a2) {
    specify(a1.length, must.equal(a2.length));
    for (int i = 0; i < a1.length; i++)
      specify(a1[i], must.equal(a2[i]));
  }

  protected void assertEncodeAndDecode(Object value) {
    ByteBuffer buffer = ByteBuffer.wrap(encode(value));
    specify(value, decode(buffer));
  }

  protected byte[] encode(Object value) {
    ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
    encode(buffer, value);
    buffer.flip();
    byte[] bytes = new byte[buffer.limit()];
    buffer.get(bytes, 0, bytes.length);
    return bytes;
  }
 
  protected abstract void encode(ByteBuffer buffer, Object value);

  protected abstract Object decode(ByteBuffer buffer);
}
