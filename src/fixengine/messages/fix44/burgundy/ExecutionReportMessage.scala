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
import fixengine.messages.{ExecutionReportMessage => ExecutionReportMessageTrait}
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
import fixengine.tags.OrderID
import fixengine.tags.OrderQty
import fixengine.tags.OrigClOrdID
import fixengine.tags.Price
import fixengine.tags.SecondaryOrderID
import fixengine.tags.SecurityID
import fixengine.tags.Symbol
import fixengine.tags.Text
import fixengine.tags.TradeDate
import fixengine.tags.TransactTime
import fixengine.tags.fix42.Side
import fixengine.tags.fix42.TimeInForce
import fixengine.tags.fix43.AccountType
import fixengine.tags.fix43.MassStatusReqID
import fixengine.tags.fix43.OrderCapacity
import fixengine.tags.fix43.PartyID
import fixengine.tags.fix43.PartyIDSource
import fixengine.tags.fix43.PegOffsetValue
import fixengine.tags.fix43.SecondaryExecID
import fixengine.tags.fix43.SecurityIDSource
import fixengine.tags.fix44.LastLiquidityInd
import fixengine.tags.fix44.LastRptRequested
import fixengine.tags.fix44.PartyRole
import fixengine.tags.fix44.PegMoveType
import fixengine.tags.fix44.PegOffsetType
import fixengine.tags.fix44.PegScope
import fixengine.tags.fix44.PeggedPrice
import fixengine.tags.fix44.TotNumReports
import fixengine.tags.fix44.burgundy.OrderRestrictions
import fixengine.tags.fix44.burgundy.TrdType
import fixengine.tags.fix50.{DisplayMethod, MatchType, DisplayLowQty, DisplayHighQty, DisplayMinIncr}

class ExecutionReportMessage(header: MessageHeader) extends AbstractMessage(header) with ExecutionReportMessageTrait {
  field(Account.Tag, Required.NO)
  field(AvgPx.Tag)
  field(ClOrdID.Tag, Required.NO)
  field(CumQty.Tag)
  field(Currency.Tag, Required.NO)
  field(ExecID.Tag)
  field(ExecInst.Tag, Required.NO)
  field(ExecRefID.Tag, Required.NO)
  field(HandlInst.Tag, Required.NO)
  field(LastPx.Tag, Required.NO)
  field(LastShares.Tag, Required.NO)
  field(OrderID.Tag)
  field(OrderQty.Tag)
  field(OrdStatus.Tag)
  field(OrdType.Tag, Required.NO)
  field(OrigClOrdID.Tag, Required.NO)
  field(Price.Tag, Required.NO)
  field(Side.Tag, Required.NO)
  field(Symbol.Tag)
  field(SecurityIDSource.Tag, Required.NO)
  field(SecurityID.Tag, Required.NO)
  field(Text.Tag, Required.NO)
  field(TimeInForce.Tag, Required.NO)
  field(TransactTime.Tag, Required.NO)
  field(TradeDate.Tag, Required.NO)
  field(OrdRejReason.Tag, Required.NO)
  field(MinQty.Tag, Required.NO)
  field(DisplayMethod.Tag, Required.NO)
  val requiredWhenDisplayMethodIsRandom = new Required() {
    override def isRequired(): Boolean = getEnum(DisplayMethod.Tag) == DisplayMethod.Random
  }
  field(DisplayLowQty.Tag, requiredWhenDisplayMethodIsRandom)
  field(DisplayHighQty.Tag, requiredWhenDisplayMethodIsRandom)
  field(DisplayMinIncr.Tag, Required.NO)
  field(MaxFloor.Tag, Required.NO)
  field(ExpireTime.Tag, new Required() {
    override def isRequired(): Boolean = getEnum(TimeInForce.Tag) == TimeInForce.GoodTillDate
  })
  field(ExecType.Tag)
  field(LeavesQty.Tag)
  field(SecondaryOrderID.Tag, Required.NO)
  field(MaxShow.Tag, Required.NO)
  field(PegOffsetValue.Tag, Required.YES)
  field(PegMoveType.Tag, Required.NO)
  field(PegOffsetType.Tag, Required.NO)
  field(PegScope.Tag, Required.NO)
  field(ExecRestatementReason.Tag, Required.NO)
  field(SecondaryExecID.Tag, Required.NO)
  field(OrderCapacity.Tag, Required.NO)
  field(OrderRestrictions.Tag, Required.NO)
  field(MatchType.Tag, Required.NO)
  field(AccountType.Tag, Required.NO)
  field(MassStatusReqID.Tag, Required.NO)
  group(new RepeatingGroup(NoPartyIDs.Tag) {
    override def newInstance(): RepeatingGroupInstance =
      new RepeatingGroupInstance(PartyID.Tag) {
        field(PartyIDSource.Tag)
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
