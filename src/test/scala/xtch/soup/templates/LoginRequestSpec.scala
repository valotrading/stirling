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
package xtch.soup.templates

import java.nio.ByteBuffer
import xtch.Spec
import xtch.messages.Message
import xtch.soup.Elements
import xtch.soup.messages.SoupTCP2Message

class LoginRequestSpec extends Spec with LoginRequestFixtures with LoginRequestEncoding {
  "LoginRequest" when {
    "transcoding" must {
      val buffer = encode(message)
      val decodedMessage = message.getTemplate().decode(buffer);
      "retain the username" in {
        message.get(Elements.USERNAME) must equal(username)
      }
      "retain the password" in {
        message.get(Elements.PASSWORD) must equal(password)
      }
      "retain the session" in {
        message.get(Elements.SESSION) must equal(session)
      }
      "retain the sequence number" in {
        message.get(Elements.SEQUENCE_NUMBER) must equal(sequenceNumber)
      }
    }
  }
}

trait LoginRequestFixtures {
  def username = "foo"
  def password = "bar"
  def session = "7"
  def sequenceNumber = "1"
  def message = {
    val message = new SoupTCP2Message(LoginRequest)
    message.set(Elements.USERNAME, username)
    message.set(Elements.PASSWORD, password)
    message.set(Elements.SESSION, session)
    message.set(Elements.SEQUENCE_NUMBER, sequenceNumber)
    message
  }
}

trait LoginRequestEncoding {
  def bufferSize = 1024
  def encode(message: Message) = {
    val buffer = ByteBuffer.allocate(bufferSize)
    message.encode(buffer);
    buffer.position(0)
    buffer
  }
}
