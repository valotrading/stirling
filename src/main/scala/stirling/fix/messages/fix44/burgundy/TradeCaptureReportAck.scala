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
  DefaultMessageHeader,
  MessageVisitor,
  Required,
  TradeCaptureReportAck => TradeCaptureReportAckTrait
}
import stirling.fix.tags.fix42.{
  Currency,
  SecurityID,
  Symbol
}
import stirling.fix.tags.fix44.burgundy.TrdType
import stirling.fix.tags.fix43.{
  TradeReportId,
  ExecType,
  TradeReportTransType,
  SecurityIDSource
}

class TradeCaptureReportAck(header: DefaultMessageHeader) extends AbstractMessage(header) with TradeCaptureReportAckTrait with Groups {
  field(TradeReportId.Tag)
  field(ExecType.Tag)
  field(Symbol.Tag)
  field(SecurityIDSource.Tag, Required.NO)
  field(SecurityID.Tag, Required.NO)
  field(TrdType.Tag, Required.NO)
  field(TradeReportTransType.Tag)
  field(Currency.Tag, Required.NO)

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
