/*
 * Copyright 2009 the original author or authors.
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

import org.joda.time.DateTime
import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import stirling.fix.messages.fix42.MsgTypeValue

class AbstractMessageSendingTimeAccuracySpec extends WordSpec with MustMatchers
  with AbstractMessageFixtures with TimeFixtures {
  "AbstractMessage" when {
    "it does not have original sending time" should {
      val message = newMessage
      message.setPossDupFlag(true)
      message.setSendingTime(now)
      "have accurate sending time" in {
        message.hasOrigSendTimeAfterSendingTime must equal(true)
      }
    }
    "has original sending time equal to sending time" should {
      val message = newMessage
      message.setPossDupFlag(true)
      message.setSendingTime(now)
      message.setOrigSendingTime(now)
      "have accurate sending time" in {
        message.hasOrigSendTimeAfterSendingTime must equal(true)
      }
    }
    "has original sending time less than sending time" should {
      val message = newMessage
      message.setPossDupFlag(true)
      message.setSendingTime(now)
      message.setOrigSendingTime(inPast)
      "have accurate sending time" in {
        message.hasOrigSendTimeAfterSendingTime must equal(true)
      }
    }
    "has original sending time greater than sending time" should {
      val message = newMessage
      message.setPossDupFlag(true)
      message.setSendingTime(now)
      message.setOrigSendingTime(inFuture)
      "have accurate sending time" in {
        message.hasOrigSendTimeAfterSendingTime must equal (false)
      }
    }
  }
}

trait AbstractMessageFixtures {
  def newMessage = new AbstractMessage(MsgTypeValue.LOGON) {
    override def apply(visitor: MessageVisitor) {
    }
  }
}

trait TimeFixtures {
  def inPast = new DateTime(1)
  def now = new DateTime(2)
  def inFuture = new DateTime(3)
}
