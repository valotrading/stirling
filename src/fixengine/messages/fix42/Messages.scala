/*
 * Copyright 2008 the original author or authors.
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
package fixengine.messages.fix42;

import fixengine.messages.{
  AbstractMessage,
  MessageHeader,
  MessageVisitor,
  BusinessMessageReject => BusinessMessageRejectTrait,
  Required
}
import fixengine.tags.fix42.{
  AvgPx,
  BusinessRejectReason,
  BusinessRejectRefID,
  ClOrdID,
  CumQty,
  Currency,
  EncryptMethod,
  ExDestination,
  ExecID,
  ExecTransType,
  ExecType,
  HeartBtInt,
  LastMkt,
  LastPx,
  LastShares,
  LeavesQty,
  MaturityMonthYear,
  OrdRejReason,
  OrdStatus,
  OrdType,
  OrderID,
  OrderQty,
  OrigClOrdID,
  Price,
  RefMsgType,
  RefSeqNo,
  ResetSeqNumFlag,
  SecurityType,
  Side,
  Symbol,
  Text,
  TimeInForce,
  TransactTime
}

class BusinessMessageReject(header: MessageHeader) extends AbstractMessage(header) with BusinessMessageRejectTrait {
  field(RefSeqNo.Tag, Required.NO)
  field(RefMsgType.Tag)
  field(Text.Tag, Required.NO)
  field(BusinessRejectReason.Tag)
  field(BusinessRejectRefID.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class ExecutionReport(header: MessageHeader) extends AbstractMessage(header) with fixengine.messages.ExecutionReport {
  field(OrderID.Tag)
  field(ClOrdID.Tag, Required.NO)
  field(OrigClOrdID.Tag, Required.NO)
  field(ExecID.Tag)
  field(ExecTransType.Tag)
  field(ExecType.Tag)
  field(OrdStatus.Tag)
  field(OrdRejReason.Tag, Required.NO)
  field(Symbol.Tag)
  field(SecurityType.Tag, Required.NO)
  field(MaturityMonthYear.Tag, Required.NO)
  field(Side.Tag)
  field(OrderQty.Tag)
  field(LastShares.Tag, Required.NO)
  field(LastPx.Tag, Required.NO)
  field(LeavesQty.Tag)
  field(OrdType.Tag, Required.NO)
  field(Price.Tag, new Required {
    override def isRequired = OrdType.Limit.equals(getEnum(OrdType.Tag))
  })
  field(TimeInForce.Tag, Required.NO)
  field(CumQty.Tag)
  field(AvgPx.Tag)
  field(TransactTime.Tag, Required.NO)
  field(Text.Tag, Required.NO)
  field(ExDestination.Tag, Required.NO)
  field(LastMkt.Tag, Required.NO)
  field(Currency.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}

class LogonMessage(header: MessageHeader) extends AbstractMessage(header) with fixengine.messages.LogonMessage {
  field(EncryptMethod.Tag)
  field(HeartBtInt.Tag)
  field(ResetSeqNumFlag.Tag, Required.NO)
  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
