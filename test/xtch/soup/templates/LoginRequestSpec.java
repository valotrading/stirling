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
package xtch.soup.templates;

import java.nio.ByteBuffer;

import jdave.Specification;
import jdave.junit4.JDaveRunner;
import org.junit.runner.RunWith;

import xtch.messages.Message;
import xtch.soup.Elements;
import xtch.soup.messages.SoupTCP2Message;

@RunWith(JDaveRunner.class) public class LoginRequestSpec extends Specification<LoginRequest> {
  private final int BUFFER_SIZE = 1024;
  private final String USERNAME = "foo";
  private final String PASSWORD = "bar";
  private final String SESSION = "7";
  private final String SEQUENCE_NUMBER = "1";
 
  public class Initialized {
    private SoupTCP2Message origMessage;

    public SoupTCP2Message create() {
      origMessage = new SoupTCP2Message(LoginRequest.TEMPLATE);
      origMessage.set(Elements.USERNAME, USERNAME);
      origMessage.set(Elements.PASSWORD, PASSWORD);
      origMessage.set(Elements.SESSION, SESSION);
      origMessage.set(Elements.SEQUENCE_NUMBER, SEQUENCE_NUMBER);
      return origMessage;
    }

    public void encodeAndDecode() {
      ByteBuffer buffer = encode(origMessage);
      SoupTCP2Message message = (SoupTCP2Message) origMessage.getTemplate().decode(buffer);
      specify(message.get(Elements.USERNAME), must.equal(USERNAME));
      specify(message.get(Elements.PASSWORD), must.equal(PASSWORD));
      specify(message.get(Elements.SESSION), must.equal(SESSION));
      specify(message.get(Elements.SEQUENCE_NUMBER), must.equal(SEQUENCE_NUMBER));
    }

    private ByteBuffer encode(Message message) {
      ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
      message.encode(buffer);
      buffer.position(0);
      return buffer;
    }
  }
}
