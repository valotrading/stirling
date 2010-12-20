package fixengine.messages.fix44.burgundy

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

import fixengine.messages.{
  AbstractMessage,
  MessageHeader,
  MessageVisitor,
  RepeatingGroup,
  RepeatingGroupInstance,
  Required,
  TradeCaptureReportRequest => TradeCaptureReportRequestTrait
}
import fixengine.tags.fix42.{
  Currency,
  ExecType,
  LastPx,
  LastShares,
  SecurityID,
  Side,
  Symbol,
  TradeDate,
  TransactTime
}
import fixengine.tags.fix43.{
  NoDates,
  NoPartyIDs,
  PartyID,
  PartyIDSource,
  SecurityIDSource,
  TradeRequestID,
  TradeRequestType
}
import fixengine.tags.fix44.PartyRole
import fixengine.tags.fix44.burgundy.TrdType

class TradeCaptureReportRequest(header: MessageHeader) extends AbstractMessage(header) with TradeCaptureReportRequestTrait {
  field(TradeRequestID.Tag)
  field(TradeRequestType.Tag)
  group(new RepeatingGroup(NoPartyIDs.Tag) {
    override def newInstance(): RepeatingGroupInstance =
      new RepeatingGroupInstance(PartyID.Tag) {
        field(PartyIDSource.Tag)
        field(PartyRole.Tag, Required.NO)
      }
  }, Required.NO)
  field(Symbol.Tag, Required.NO)
  field(SecurityIDSource.Tag, Required.NO)
  field(SecurityID.Tag, Required.NO)
  field(Currency.Tag, Required.NO)
  group(new RepeatingGroup(NoDates.Tag) {
    override def newInstance(): RepeatingGroupInstance =
      new RepeatingGroupInstance(TradeDate.Tag) {
        field(TransactTime.Tag, Required.NO)
      }
  }, Required.NO)

  field(LastPx.Tag, Required.NO)
  field(LastShares.Tag, Required.NO)
  field(Side.Tag, Required.NO)
  field(ExecType.Tag, Required.NO)
  field(TrdType.Tag, Required.NO)

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
