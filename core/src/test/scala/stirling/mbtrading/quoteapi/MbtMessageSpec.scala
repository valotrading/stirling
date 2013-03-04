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
        val message = MbtMessage(Type.Login)
          .set(Tag.Password, "password") .set(Tag.Username, "username")
        message.format must equal("L|101=password;100=username\n")
      }
      "format valid messages with zero fields" in {
        MbtMessage(Type.Heartbeat).format must equal("9|\n")
      }
    }
    "merging" must {
      "merge copies messages from the given message" in {
        MbtMessage(Type.Login)
          .set(Tag.Username, "username")
        .merge(MbtMessage(Type.Login)
          .set(Tag.Password, "password")
        ) must equal(MbtMessage(Type.Login)
          .set(Tag.Username, "username")
          .set(Tag.Password, "password")
        )
      }
      "throw an IllegalArgumentException if message types differ" in {
        intercept[IllegalArgumentException] {
          MbtMessage(Type.Login)
          .merge(MbtMessage(Type.Subscription))
        }
      }
    }
    "parsing" must {
      "parse non-partial messages" in {
        val message = parse(strToByteBuffer("L|2041=A;8055=B;2004=C\n"))
        message.msgType must equal(Type.Login)
        message.fields must equal(Map(Tag.LastAsk -> "C", Tag.InfoMsgFrom -> "B", Tag.ContractSize -> "A"))
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
