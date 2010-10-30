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

@RunWith(JDaveRunner.class) public class AlphaSpec extends Specification<Alpha> {
  private final int BUFFER_SIZE = 1024;

  public class Initialized {
    public void encodeAndDecode() {
      ByteBuffer buffer = encode('+');
      specify(Alpha.TYPE.decode(buffer, 1) == '+');
    }

    private ByteBuffer encode(Character value) {
      ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
      Alpha.TYPE.encode(buffer, value, 1);
      buffer.position(0);
      return buffer;
    }
  }
}

