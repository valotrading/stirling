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
  MessageHeader,
  MessageVisitor,
  RepeatingGroup,
  RepeatingGroupInstance,
  Required,
  TradeCaptureReportRequest => TradeCaptureReportRequestTrait
}
import stirling.fix.tags.fix42.{
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
import stirling.fix.tags.fix43.{
  NoDates,
  SecurityIDSource,
  TradeRequestID,
  TradeRequestType
}
import stirling.fix.tags.fix44.burgundy.TrdType

class TradeCaptureReportRequest(header: MessageHeader) extends AbstractMessage(header) with TradeCaptureReportRequestTrait with Groups{
  field(TradeRequestID.Tag)
  field(TradeRequestType.Tag)
  parties(Required.NO)
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
