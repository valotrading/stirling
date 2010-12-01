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
import xtch.types.Type;

import silvertip.PartialMessageException;

public abstract class AbstractType<T> implements Type<T> {
  protected static final byte ALL_BUT_STOP_BIT_MASK = (byte) 0x7F;
  protected static final byte STOP_BIT_MASK = (byte) 0x80;

  @Override public T decode(ByteBuffer buffer, int length) {
    Decoder<T> decoder = createDecoder();
    int startPosition = buffer.position();
    while (buffer.hasRemaining()) {
      byte b = buffer.get();
      decoder.process(decodeByte(b));
      if (hasStopBit(b) || buffer.position() - startPosition == length)
        return decoder.getResult();
    }
    throw new RuntimeException("partial message");
  }

  protected abstract Decoder<T> createDecoder();

  protected final boolean containsStopBit(byte[] bytes) {
    for (byte b : bytes) {
      if (hasStopBit(b))
        return true;
    }
    return false;
  }

  protected final byte decodeByte(byte b) {
    return (byte) (b & ALL_BUT_STOP_BIT_MASK);
  }

  protected final boolean hasStopBit(byte b) {
    return (b & STOP_BIT_MASK) != 0;
  }

  protected final byte addStopBit(byte b) {
    return (byte) (b | STOP_BIT_MASK);
  }
}
