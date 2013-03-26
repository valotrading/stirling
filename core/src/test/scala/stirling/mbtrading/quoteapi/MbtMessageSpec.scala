/*
 * Copyright 2012 the original author or authors.
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
package stirling.mbtrading.quoteapi

import java.nio.ByteBuffer
import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import silvertip.PartialMessageException
import stirling.mbtrading.quoteapi.MbtMessage._

class MbtMessageSpec extends WordSpec with MustMatchers with MbtMessageFixtures {
  "MbtMessage" when {
    "formatting" must {
      "format valid messages with one or more fields" in {
        val message = Login()
          .updated(Tag.Password, "password")
          .updated(Tag.Username, "username")
        message.toString must equal("L|101=password;100=username\n")
      }
      "format valid messages with zero fields" in {
        Heartbeat().toString must equal("9|\n")
      }
    }
    "parsing" must {
      "parse non-partial messages" in {
        val message = parse(strToByteBuffer("L|2041=2;8055=B;2004=1.0\n"))
        message.messageType must equal(Type.Login)
        message.getDouble(Tag.LastAsk) must equal(Some(1.0))
        message.getString(Tag.InfoMsgFrom) must equal(Some("B"))
        message.getInt(Tag.ContractSize) must equal(Some(2))
      }
      "parse partial messages" in {
        evaluating {
          parse(strToByteBuffer("L|2041=A;8055=B"))
        } must produce [PartialMessageException]
      }
    }
  }
}

trait MbtMessageFixtures {
  def strToByteBuffer(message: String) = {
    val buffer = ByteBuffer.allocate(32)
    buffer.put(message.getBytes)
    buffer.flip
    buffer
  }
}
