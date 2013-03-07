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
package stirling.mbtrading.fix

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers

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
  import MsgTypeValue._

  def factory = MessageFactory
  def msgTypes = List(
    ExecutionReport,
    OrderCancelRequest,
    OrderCancelReject,
    BusinessMessageReject,
    NewOrderSingle,
    OrderCancelReplaceRequest,
    Logon,
    NewOrderMultiLeg,
    RequestForPositions,
    PositionReport,
    TradingSessionStatus,
    News,
    CollateralReport,
    CollateralInquiry,
    CollateralInquiryAck,
    RequestForPositionsAck
  )
}
