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
package stirling.fix.messages.fix44.mbtrading

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import stirling.fix.messages.fix44.MsgTypeValue._

class MessageFactorySpec extends WordSpec with MustMatchers with MessageFactoryFixtures {
  "MessageFactory" should {
    "create all message types successfully" in {
      msgTypes.foreach { msgType =>
        factory.create(msgType).getMsgType must equal(msgType)
      }
    }
  }
}

trait MessageFactoryFixtures {
  def factory = new MessageFactory
  def msgTypes = List(
    EXECUTION_REPORT,
    ORDER_CANCEL_REQUEST,
    ORDER_CANCEL_REJECT,
    BUSINESS_MESSAGE_REJECT,
    NEW_ORDER_SINGLE,
    ORDER_CANCEL_REPLACE_REQUEST,
    LOGON,
    NEW_ORDER_MULTILEG,
    REQUEST_FOR_POSITIONS,
    POSITION_REPORT,
    TRADING_SESSION_STATUS,
    NEWS,
    COLLATERAL_REPORT,
    COLLATERAL_INQUIRY,
    COLLATERAL_INQUIRY_ACK,
    REQUEST_FOR_POSITIONS_ACK
  )
}
