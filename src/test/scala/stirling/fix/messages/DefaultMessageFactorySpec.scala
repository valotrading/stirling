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
package stirling.fix.messages

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import stirling.fix.messages.fix42.DefaultMessageFactory;
import stirling.fix.messages.fix42.MsgTypeValue;

class DefaultMessageFactorySpec extends WordSpec with MustMatchers {
  "DefaultMessageFactory" when {
    val factory = new DefaultMessageFactory
    "given an empty message type" should {
      "reject it" in {
        intercept[InvalidMsgTypeException] {
          factory.create("")
        }
      }
    }
    "given a one character message type" should {
      "accept '0' as Heartbeat" in {
        factory.create("0").getMsgType must equal(MsgTypeValue.HEARTBEAT)
      }
      "accept '9' as OrderCancelReject" in {
        factory.create("9").getMsgType must equal(MsgTypeValue.ORDER_CANCEL_REJECT)
      }
      "reject '!' as invalid" in {
        intercept[InvalidMsgTypeException] {
          factory.create("!")
        }
      }
    }
    "given a two character message type" should {
      "reject 'AA' and 'AI' as unsupported" in {
        List("AA", "AI").foreach { msgType =>
          intercept[UnsupportedMsgTypeException] {
            factory.create(msgType)
          }
        }
      }
      "reject 'aa', 'AJ', 'ZA' and 'ZZ' as invalid" in {
        List("aa", "AJ", "ZA", "ZZ").foreach { msgType =>
          intercept[InvalidMsgTypeException] {
            factory.create(msgType)
          }
        }
      }
    }
    "given a three character message type" should {
      "reject it as invalid" in {
        intercept[InvalidMsgTypeException] {
          factory.create("AAA")
        }
      }
    }
  }
}
