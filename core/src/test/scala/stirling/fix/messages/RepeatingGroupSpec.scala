/*
 * Copyright 2013 the original author or authors.
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
import scala.collection.JavaConversions._
import stirling.fix.messages.fix44.MsgTypeValue
import stirling.mbtrading.fix._

class RepeatingGroupSpec extends WordSpec with MustMatchers with RepeatingGroupFixtures {
  "RepeatingGroup" when {
    "missing or empty" should {
      val message = messageWithoutOrWithEmptyRepeatingGroup
      val group = message.getGroup(NoLegs.Tag)

      "have no repeating group instances" in {
        group.getInstances must have length(0)
      }
    }
    "having one repeating group instance" should {
      val message = messageWithOneRepeatingGroupInstance
      val group = message.getGroup(NoLegs.Tag)

      "have one instance" in {
        group.getInstances must have length(1)
      }
      "have correct LegSymbol(600) in the instance" in {
        group.getInstances.head.getString(LegSymbol.Tag) must equal("SPY")
      }
      "have correct Password(554) in the instance" in {
        group.getInstances.head.getString(Password.Tag) must equal("YPS")
      }
    }
    "having two repeating group instances" should {
      val message = messageWithTwoRepeatingGroupInstances
      val group = message.getGroup(NoLegs.Tag)

      "have two instances" in {
        group.getInstances must have length(2)
      }
      "have correct LegSymbol(600) in the first instance" in {
        group.getInstances.head.getString(LegSymbol.Tag) must equal("SPY")
      }
      "have correct Password(554) in the first instance" in {
        group.getInstances.head.getString(Password.Tag) must equal("YPS")
      }
      "have correct LegSymbol(600) in the last instance" in {
        group.getInstances.last.getString(LegSymbol.Tag) must equal("DIA")
      }
      "have correct Password(554) in the last instance" in {
        group.getInstances.last.getString(Password.Tag) must equal("AID")
      }
    }
  }
}

trait RepeatingGroupFixtures {
  def messageWithoutOrWithEmptyRepeatingGroup = {
    val message = stirling.mbtrading.fix.MessageFactory.create(MsgTypeValue.NEW_ORDER_MULTILEG)

    message.setString(Account.Tag, "1")
    message.setString(ClOrdID.Tag, "2")
    message.setEnum(HandlInst.Tag, HandlInst.AutomatedOrderPrivate)
    message.setFloat(OrderQty.Tag, 3.0)
    message.setEnum(OrdType.Tag, OrdType.Market)
    message.setEnum(Side.Tag, Side.Buy)
    message.setEnum(TimeInForce.Tag, TimeInForce.ImmediateOrCancel)
    message.setDateTime(TransactTime.Tag, DateTime.now)
    message.setString(ExDestination.Tag, "BATS")

    message
  }

  def messageWithOneRepeatingGroupInstance = {
    val message = messageWithoutOrWithEmptyRepeatingGroup
    val group = message.getGroup(NoLegs.Tag)

    group.addInstance(repeatingGroupInstance(group, "SPY", "YPS"))

    message
  }

  def messageWithTwoRepeatingGroupInstances = {
    val message = messageWithOneRepeatingGroupInstance
    val group = message.getGroup(NoLegs.Tag)

    group.addInstance(repeatingGroupInstance(group, "DIA", "AID"))

    message
  }

  def repeatingGroupInstance(group: RepeatingGroup, legSymbol: String, password: String) = {
    val instance = group.newInstance

    instance.setString(LegSymbol.Tag, legSymbol)
    instance.setString(LegCFICode.Tag, "4")
    instance.setDateTime(LegMaturityMonthYear.Tag, DateTime.now)
    instance.setFloat(LegStrikePrice.Tag, 5.0)
    instance.setEnum(LegPositionEffect.Tag, LegPositionEffect.Open)
    instance.setEnum(LegSide.Tag, LegSide.Buy)
    instance.setFloat(LegRatioQty.Tag, 6.0)
    instance.setString(LegRefID.Tag, "7")
    instance.setString(Username.Tag, "8")
    instance.setString(Password.Tag, password)

    instance
  }
}
