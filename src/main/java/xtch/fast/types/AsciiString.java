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

/** 
 * An ASCII string element type as specified in FAST Specification version
 * 1.1, section 10.6.3. 
 */
public class AsciiString extends AbstractType<String> {
  public static final AsciiString TYPE = new AsciiString();

  private AsciiString() {
  }

  @Override public void encode(ByteBuffer buffer, String value, int length) {
    byte[] bytes = value.getBytes();
    if (containsStopBit(bytes))
      throw new IllegalArgumentException("Argument contains stop bit (FAST Specification: ERR D11)");
    bytes[bytes.length - 1] = addStopBit(bytes[bytes.length - 1]);
    buffer.put(bytes);
  }

  @Override protected Decoder<String> createDecoder() {
    return new Decoder<String>() {
      StringBuffer output = new StringBuffer();

      @Override public String getResult() {
        return output.toString();
      }

      @Override public void process(byte b) {
        output.append((char) b);
      }
    };
  }
}
