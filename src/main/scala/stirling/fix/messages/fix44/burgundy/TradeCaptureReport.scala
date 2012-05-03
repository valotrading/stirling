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
  RepeatingGroup,
  RepeatingGroupInstance,
  AbstractMessage,
  MessageHeader,
  MessageVisitor,
  Required,
  TradeCaptureReport => TradeCaptureReportTrait
}
import stirling.fix.tags.fix42.{
  Text,
  Account,
  ClOrdID,
  Currency,
  OrderID,
  TransactTime,
  TradeDate,
  LastShares,
  LastPx,
  OrderQty,
  SecurityID,
  Symbol,
  Side
}
import stirling.fix.tags.fix43.{
  AccountType,
  OrderCapacity,
  NoSides,
  TradeReportTransType,
  PreviouslyReported,
  SecurityIDSource,
  TradeRequestID
}
import stirling.fix.tags.fix44.burgundy.{
  OrderRestrictions,
  ValidityPeriod,
  TrdType
}
import stirling.fix.tags.fix50.MatchType

class TradeCaptureReport(header: MessageHeader) extends AbstractMessage(header) with TradeCaptureReportTrait with Groups {
  field(TradeRequestID.Tag)
  field(PreviouslyReported.Tag)
  field(Symbol.Tag)
  field(SecurityIDSource.Tag, Required.NO)
  field(SecurityID.Tag, Required.NO)
  field(OrderQty.Tag)
  field(LastPx.Tag)
  field(LastShares.Tag)
  field(TradeDate.Tag)
  field(TransactTime.Tag)
  field(TradeReportTransType.Tag)
  field(MatchType.Tag, Required.NO)
  field(TrdType.Tag, new Required() {
    override def isRequired(): Boolean = getEnum(MatchType.Tag) == MatchType.TwoPartyTradeReport
  })
  field(ValidityPeriod.Tag, Required.NO)

  def trdCapRptSideGrp() {
    group(new RepeatingGroup(NoSides.Tag) {
        override def newInstance(): RepeatingGroupInstance =
          new RepeatingGroupInstance(Side.Tag) {
            field(OrderID.Tag)
            field(Currency.Tag)
            field(ClOrdID.Tag, Required.NO)
            field(Account.Tag, Required.NO)
            field(Text.Tag, Required.NO)
            field(OrderCapacity.Tag, Required.NO)
            field(OrderRestrictions.Tag, Required.NO)
            field(AccountType.Tag, Required.NO)
            parties()
          }
      })
  }

  trdCapRptSideGrp()
  field(TradeRequestID.Tag, Required.NO)

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
