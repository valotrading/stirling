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
package fixengine.messages.fix42.bats.europe

import fixengine.messages.MessageHeader
import fixengine.messages.MessageVisitor
import fixengine.messages.Required
import fixengine.messages.UserDefinedMessage;
import fixengine.messages.Value;
import fixengine.tags.fix42.{
  Account,
  AvgPx,
  ClOrdID,
  ClearingAccount,
  ClearingFirm,
  ContraBroker,
  CumQty,
  Currency,
  CxlRejResponseTo,
  ExecID,
  ExecRefID,
  ExecTransType,
  ExpireTime,
  LastPx,
  LastShares,
  LeavesQty,
  MaxFloor,
  MinQty,
  NoContraBrokers,
  OrdRejReason,
  OrdStatus,
  OrdType,
  OrderCapacity,
  OrderID,
  OrderQty,
  OrigClOrdID,
  OrigTime,
  PegDifference,
  Price,
  SecondaryOrderID,
  SecurityExchange,
  Side,
  Symbol,
  Text,
  TransactTime
}
import fixengine.tags.fix42.bats.europe.{
  CancelOrigOnReject,
  CentralCounterparty,
  CorrectedPrice,
  CxlRejReason,
  DisplayIndicator,
  ExecInst,
  ExecType,
  IDSource,
  MTFAccessFee,
  MaxRemovePct,
  OrigCompID,
  OrigSubID,
  PreventParticipantMatch,
  RoutingInst,
  SecurityID,
  TimeInForce,
  TradeLiquidityIndicator
}
import fixengine.tags.fix43.ExecRestatementReason

class OrderCancelReject(header: MessageHeader) extends fixengine.messages.fix42.OrderCancelReject(header) {
  field(Account.Tag, Required.NO)
  field(ClOrdID.Tag)
  field(OrderID.Tag)
  field(OrdStatus.Tag)
  field(OrigClOrdID.Tag)
  field(Text.Tag)
  field(CxlRejReason.Tag)
  field(CxlRejResponseTo.Tag)
}

class ExecutionReport(header: MessageHeader) extends fixengine.messages.fix42.ExecutionReport(header) {
  field(Account.Tag, Required.NO)
  field(ClOrdID.Tag)
  field(CumQty.Tag)
  field(Currency.Tag, Required.NO)
  field(ExecID.Tag)
  field(ExecInst.Tag, Required.NO)
  field(ExecRefID.Tag, new Required {
    override def isRequired: Boolean = {
      val value = getEnum(ExecTransType.Tag)
      value.equals(ExecTransType.Cancel) || value.equals(ExecTransType.Correct)
    }
  })
  field(ExecTransType.Tag)
  field(IDSource.Tag, Required.NO)
  field(LastPx.Tag)
  field(LastShares.Tag)
  field(OrderID.Tag)
  field(OrderQty.Tag)
  field(OrdStatus.Tag)
  field(OrigClOrdID.Tag, Required.NO)
  field(Price.Tag, Required.NO)
  field(AvgPx.Tag, Required.NO)
  field(SecurityID.Tag, Required.NO)
  field(Side.Tag)
  field(Symbol.Tag, Required.NO)
  field(Text.Tag, Required.NO)
  field(TimeInForce.Tag, Required.NO)
  field(TransactTime.Tag)
  field(OrdRejReason.Tag, Required.NO)
  field(MaxFloor.Tag, Required.NO)
  field(ExpireTime.Tag, new Required {
    override def isRequired: Boolean = TimeInForce.GoodTillDate.equals(getEnum(TimeInForce.Tag))
  });
  field(ExecType.Tag)
  field(LeavesQty.Tag)
  field(SecondaryOrderID.Tag, Required.NO)
  field(SecurityExchange.Tag, Required.NO)
  field(ContraBroker.Tag, Required.NO)
  field(ExecRestatementReason.Tag, new Required {
    override def isRequired: Boolean = getEnum(ExecType.Tag).equals(ExecType.Restated)
  })
  field(NoContraBrokers.Tag, new Required {
    override def isRequired: Boolean = ExecType.isTrade(getEnum(ExecType.Tag))
  })
  field(ClearingFirm.Tag, Required.NO)
  field(ClearingAccount.Tag, Required.NO)
  field(CentralCounterparty.Tag, Required.NO)
  field(MTFAccessFee.Tag, new Required {
    override def isRequired: Boolean = ExecType.isTrade(getEnum(ExecType.Tag))
  })
  field(TradeLiquidityIndicator.Tag, new Required {
    override def isRequired: Boolean = ExecType.isTrade(getEnum(ExecType.Tag))
  })
  field(MaxRemovePct.Tag, Required.NO)
  field(MinQty.Tag, Required.NO)
  field(PegDifference.Tag, Required.NO)
}

class NewOrderSingle(header: MessageHeader) extends fixengine.messages.fix42.NewOrderSingleMessage(header) {
  field(Account.Tag, Required.NO)
  field(ClOrdID.Tag)
  field(Currency.Tag, new Required {
    override def isRequired: Boolean = getEnum(IDSource.Tag).equals(IDSource.ISIN)
  })
  field(ExecInst.Tag, Required.NO)
  field(IDSource.Tag, new Required {
    override def isRequired: Boolean = !hasValue(Symbol.Tag)
  })
  field(OrderQty.Tag)
  field(OrdType.Tag)
  field(Price.Tag, new Required {
    override def isRequired: Boolean = getEnum(OrdType.Tag).equals(OrdType.Limit)
  })
  field(OrderCapacity.Tag, Required.NO)
  field(SecurityID.Tag, new Required {
    override def isRequired: Boolean = hasValue(IDSource.Tag)
  })
  field(Side.Tag)
  field(Symbol.Tag, Required.NO)
  field(TimeInForce.Tag, Required.NO)
  field(MinQty.Tag, Required.NO)
  field(MaxFloor.Tag, Required.NO)
  field(ExpireTime.Tag, new Required {
    override def isRequired: Boolean = getEnum(TimeInForce.Tag).equals(TimeInForce.GoodTillDate)
  })
  field(SecurityExchange.Tag, new Required() {
    override def isRequired: Boolean = getEnum(IDSource.Tag).equals(IDSource.ISIN)
  })
  field(PegDifference.Tag, Required.NO)
  field(ClearingFirm.Tag, Required.NO)
  field(ClearingAccount.Tag, Required.NO)
  field(PreventParticipantMatch.Tag, Required.NO)
  field(RoutingInst.Tag, Required.NO)
  field(DisplayIndicator.Tag, Required.NO)
  field(MaxRemovePct.Tag, new Required {
    override def isRequired: Boolean = getEnum(RoutingInst.Tag).equals(RoutingInst.PostOnlyAtLimit)
  })
  field(OrigCompID.Tag, Required.NO)
  field(OrigSubID.Tag, Required.NO)
}

class OrderCancelRequest(header: MessageHeader) extends fixengine.messages.fix42.OrderCancelRequestMessage(header) {
  field(Account.Tag, Required.NO)
  field(ClOrdID.Tag)
  field(Currency.Tag, new Required {
    override def isRequired: Boolean = getEnum(IDSource.Tag).equals(IDSource.ISIN)
  })
  field(IDSource.Tag, new Required {
    override def isRequired: Boolean = !hasValue(Symbol.Tag)
  })
  field(OrderID.Tag)
  field(OrderQty.Tag)
  field(OrigClOrdID.Tag)
  field(SecurityID.Tag, new Required {
    override def isRequired: Boolean = hasValue(IDSource.Tag)
  })
  field(Side.Tag)
  field(Symbol.Tag, Required.NO)
  field(SecurityExchange.Tag, new Required {
    override def isRequired: Boolean = getEnum(IDSource.Tag).equals(IDSource.ISIN)
  })
}

class TradeCancelCorrect(header: MessageHeader) extends UserDefinedMessage(header) {
  field(ClOrdID.Tag)
  field(Currency.Tag, Required.NO)
  field(ExecID.Tag)
  field(ExecRefID.Tag, new Required {
    override def isRequired: Boolean = {
      val value = getEnum(ExecTransType.Tag)
      value.equals(ExecTransType.Cancel) || value.equals(ExecTransType.Correct)
    }
  })
  field(ExecTransType.Tag)
  field(IDSource.Tag, Required.NO)
  field(LastPx.Tag)
  field(LastShares.Tag)
  field(OrderID.Tag)
  field(OrigTime.Tag)
  field(SecurityID.Tag, Required.NO)
  field(Symbol.Tag, Required.NO)
  field(Side.Tag)
  field(TransactTime.Tag)
  field(SecurityExchange.Tag, Required.NO)
  field(ClearingFirm.Tag, Required.NO)
  field(ClearingAccount.Tag, Required.NO)
  field(CorrectedPrice.Tag, Required.NO)
  field(TradeLiquidityIndicator.Tag, Required.NO)

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class OrderModificationRequest(header: MessageHeader) extends fixengine.messages.fix42.OrderModificationRequestMessage(header) {
  field(Account.Tag, Required.NO)
  field(ClOrdID.Tag)
  field(Currency.Tag, new Required {
    override def isRequired: Boolean = getEnum(IDSource.Tag).equals(IDSource.ISIN)
  })
  field(IDSource.Tag, new Required {
    override def isRequired: Boolean = !hasValue(Symbol.Tag)
  })
  field(OrderID.Tag)
  field(OrderQty.Tag)
  field(OrdType.Tag)
  field(OrigClOrdID.Tag)
  field(Price.Tag, new Required {
    override def isRequired: Boolean = getEnum(OrdType.Tag).equals(OrdType.Limit)
  })
  field(SecurityID.Tag, new Required {
    override def isRequired: Boolean = hasValue(IDSource.Tag)
  })
  field(Side.Tag)
  field(Symbol.Tag, Required.NO)
  field(SecurityExchange.Tag, new Required {
    override def isRequired: Boolean = getEnum(IDSource.Tag).equals(IDSource.ISIN)
  })
  field(CancelOrigOnReject.Tag, Required.NO)
}
