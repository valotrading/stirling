/*
 * Copyright 2008 the original author or authors.
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
package stirling.fix.messages

import org.scalatest.{OneInstancePerTest, WordSpec}
import org.scalatest.matchers.MustMatchers
import stirling.fix.messages.fix42.MsgTypeValue
import stirling.fix.tags.fix42.{MsgType, TargetCompID}
import stirling.fix.messages.Field.DELIMITER

class MessageBufferSpec extends WordSpec with MustMatchers with OneInstancePerTest {
  "MessageBuffer" when {
    "empty" should {
      val buffer = new MessageBuffer
      "have a length of zero" in {
        buffer.length must equal(0)
      }
      "return the empty string in formatting" in {
        buffer.toString must equal("")
      }
    }
    "a tag is appended" should {
      val buffer = new MessageBuffer
      buffer.append(new StringField(MsgType.Tag, MsgTypeValue.LOGON))
      "have the length of the appended tag" in {
        buffer.length must equal(5)
      }
      "return the appended tag in formatting" in {
        buffer.toString must equal("35=A" + DELIMITER)
      }
      "retain the appended tag when another tag is appended" in {
        buffer.append(new StringField(TargetCompID.Tag, "IB"))
        buffer.toString must equal("35=A" + DELIMITER + "56=IB" + DELIMITER)
      }
      "retain the appended tag when another tag is prefixed" in {
        buffer.prefix(new StringField(TargetCompID.Tag, "IB"))
        buffer.toString must equal("56=IB" + DELIMITER + "35=A" + DELIMITER)
      }
    }
  }
}
