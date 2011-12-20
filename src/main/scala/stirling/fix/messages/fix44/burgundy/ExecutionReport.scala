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
package stirling.fix.messages.fix44.burgundy

import stirling.fix.messages.{
  AbstractMessage,
  ExecutionReport => ExecutionReportTrait,
  MessageHeader,
  MessageVisitor,
  RepeatingGroup,
  RepeatingGroupInstance,
  Required
}
import stirling.fix.tags.fix42.{
  Account,
  AvgPx,
  ClOrdID,
  CumQty,
  Currency,
  ExecID,
  ExecInst,
  ExecRefID,
  ExecType,
  ExpireTime,
  HandlInst,
  LastPx,
  LastShares,
  LeavesQty,
  MaxFloor,
  MaxShow,
  MinQty,
  OrdRejReason,
  OrdStatus,
  OrdType,
  OrderID,
  OrderQty,
  OrigClOrdID,
  Price,
  SecondaryOrderID,
  SecurityID,
  Side,
  Symbol,
  Text,
  TimeInForce,
  TradeDate,
  TransactTime
}
import stirling.fix.tags.fix43.{
  AccountType,
  ExecRestatementReason,
  MassStatusReqID,
  NoPartyIDs,
  OrderCapacity,
  PartyID,
  PartyIDSource,
  PegOffsetValue,
  SecondaryExecID,
  SecurityIDSource
}
import stirling.fix.tags.fix44.{
  LastLiquidityInd,
  LastRptRequested,
  PartyRole,
  PegMoveType,
  PegOffsetType,
  PegScope,
  PeggedPrice,
  TotNumReports
}
import stirling.fix.tags.fix44.burgundy.{
  OrderRestrictions,
  TrdType
}
import stirling.fix.tags.fix50.{
  DisplayMethod,
  MatchType,
  DisplayLowQty,
  DisplayHighQty,
  DisplayMinIncr
}

class ExecutionReport(header: MessageHeader) extends AbstractMessage(header) with ExecutionReportTrait with Groups {
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
  parties(Required.NO)
  field(TrdType.Tag, new Required() {
    override def isRequired(): Boolean = getEnum(MatchType.Tag) == MatchType.TwoPartyTradeReport
  })
  field(PeggedPrice.Tag, Required.NO)
  field(LastLiquidityInd.Tag, Required.NO)
  field(TotNumReports.Tag, Required.NO)
  field(LastRptRequested.Tag, Required.NO)

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
