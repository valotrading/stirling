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
package fixengine.messages.fix44.burgundy

import fixengine.messages.AbstractMessage
import fixengine.messages.MessageHeader
import fixengine.messages.MessageVisitor
import fixengine.messages.RepeatingGroup
import fixengine.messages.RepeatingGroupInstance
import fixengine.messages.Required
import fixengine.messages.TimeInForceValue
import fixengine.messages.{OrderCancelRejectMessage => OrderCancelRejectMessageTrait}
import fixengine.tags.Account
import fixengine.tags.AvgPx
import fixengine.tags.ClOrdID
import fixengine.tags.CumQty
import fixengine.tags.Currency
import fixengine.tags.ExecID
import fixengine.tags.ExecInst
import fixengine.tags.ExecRefID
import fixengine.tags.ExecRestatementReason
import fixengine.tags.ExecType
import fixengine.tags.ExpireTime
import fixengine.tags.HandlInst
import fixengine.tags.LastPx
import fixengine.tags.LastShares
import fixengine.tags.LeavesQty
import fixengine.tags.MaxFloor
import fixengine.tags.MaxShow
import fixengine.tags.MinQty
import fixengine.tags.NoPartyIDs
import fixengine.tags.OrdRejReason
import fixengine.tags.OrdStatus
import fixengine.tags.OrdType
import fixengine.tags.OrderCapacity
import fixengine.tags.OrderID
import fixengine.tags.OrderQty
import fixengine.tags.OrigClOrdID
import fixengine.tags.Price
import fixengine.tags.SecondaryOrderID
import fixengine.tags.SecurityID
import fixengine.tags.Side
import fixengine.tags.Symbol
import fixengine.tags.Text
import fixengine.tags.TimeInForce
import fixengine.tags.TradeDate
import fixengine.tags.TransactTime
import fixengine.tags.fix43.AccountType
import fixengine.tags.fix43.MassStatusReqID
import fixengine.tags.fix43.PartyID
import fixengine.tags.fix43.PartyIDSource
import fixengine.tags.fix43.PegOffsetValue
import fixengine.tags.fix43.SecondaryExecID
import fixengine.tags.fix43.SecurityIDSource
import fixengine.tags.fix44.LastLiquidityInd
import fixengine.tags.fix44.LastRptRequested
import fixengine.tags.fix44.PegMoveType
import fixengine.tags.fix44.PegOffsetType
import fixengine.tags.fix44.PegScope
import fixengine.tags.fix44.PeggedPrice
import fixengine.tags.fix44.TotNumReports
import fixengine.tags.fix44.burgundy.DisplayMethod
import fixengine.tags.fix44.burgundy.OrderRestrictions
import fixengine.tags.fix44.burgundy.PartyRole
import fixengine.tags.fix44.burgundy.TrdType
import fixengine.tags.fix50.{MatchType, DisplayLowQty, DisplayHighQty, DisplayMinIncr}

class ExecutionReportMessage(header: MessageHeader) extends AbstractMessage(header) with OrderCancelRejectMessageTrait {
  field(Account.TAG, Required.NO)
  field(AvgPx.TAG)
  field(ClOrdID.TAG, Required.NO)
  field(CumQty.TAG)
  field(Currency.TAG, Required.NO)
  field(ExecID.TAG)
  field(ExecInst.TAG, Required.NO)
  field(ExecRefID.TAG, Required.NO)
  field(HandlInst.TAG, Required.NO)
  field(LastPx.TAG, Required.NO)
  field(LastShares.TAG, Required.NO)
  field(OrderID.TAG)
  field(OrderQty.TAG)
  field(OrdStatus.TAG)
  field(OrdType.TAG, Required.NO)
  field(OrigClOrdID.TAG, Required.NO)
  field(Price.TAG, Required.NO)
  field(Side.TAG, Required.NO)
  field(Symbol.TAG)
  field(SecurityIDSource.Tag, Required.NO)
  field(SecurityID.TAG, Required.NO)
  field(Text.TAG, Required.NO)
  field(TimeInForce.TAG, Required.NO)
  field(TransactTime.TAG, Required.NO)
  field(TradeDate.TAG, Required.NO)
  field(OrdRejReason.TAG, Required.NO)
  field(MinQty.TAG, Required.NO)
  field(DisplayMethod.Tag, Required.NO)
  val requiredWhenDisplayMethodIsRandom = new Required() {
    override def isRequired(): Boolean = getEnum(DisplayMethod.Tag) == DisplayMethod.Random
  }
  field(DisplayLowQty.Tag, requiredWhenDisplayMethodIsRandom)
  field(DisplayHighQty.Tag, requiredWhenDisplayMethodIsRandom)
  field(DisplayMinIncr.Tag, Required.NO)
  field(MaxFloor.TAG, Required.NO)
  field(ExpireTime.TAG, new Required() {
    override def isRequired(): Boolean = getEnum(TimeInForce.TAG) == TimeInForceValue.GOOD_TILL_DATE
  })
  field(ExecType.TAG)
  field(LeavesQty.TAG)
  field(SecondaryOrderID.TAG, Required.NO)
  field(MaxShow.TAG, Required.NO)
  field(PegOffsetValue.TAG, Required.YES)
  field(PegMoveType.TAG, Required.NO)
  field(PegOffsetType.TAG, Required.NO)
  field(PegScope.TAG, Required.NO)
  field(ExecRestatementReason.TAG, Required.NO)
  field(SecondaryExecID.Tag, Required.NO)
  field(OrderCapacity.TAG, Required.NO)
  field(OrderRestrictions.Tag, Required.NO)
  field(MatchType.Tag, Required.NO)
  field(AccountType.TAG, Required.NO)
  field(MassStatusReqID.Tag, Required.NO)
  group(new RepeatingGroup(NoPartyIDs.TAG) {
    override def newInstance(): RepeatingGroupInstance =
      new RepeatingGroupInstance(PartyID.TAG) {
        field(PartyIDSource.TAG)
        field(PartyRole.Tag, Required.NO)
      }
  }, Required.NO)
  field(TrdType.Tag, new Required() {
    override def isRequired(): Boolean = getEnum(MatchType.Tag) == MatchType.TwoPartyTradeReport
  })
  field(PeggedPrice.Tag, Required.NO)
  field(LastLiquidityInd.Tag, Required.NO)
  field(TotNumReports.Tag, Required.NO)
  field(LastRptRequested.Tag, Required.NO)

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
