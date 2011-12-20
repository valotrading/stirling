package stirling.fix.messages.fix44.burgundy

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
import stirling.fix.messages.{
  AbstractMessage,
  MessageHeader,
  MessageVisitor,
  Required,
  TradeCaptureReportRequestAck => TradeCaptureReportRequestAckTrait}
import stirling.fix.tags.fix42.{
  Currency,
  SecurityID,
  Symbol,
  Text
}
import stirling.fix.tags.fix43.{
  SecurityIDSource,
  TradeRequestID,
  TradeRequestType
}
import stirling.fix.tags.fix44.{
  TradeRequestStatus,
  TradeRequestResult
}

class TradeCaptureReportRequestAck(header: MessageHeader) extends AbstractMessage(header) with TradeCaptureReportRequestAckTrait {
  field(TradeRequestID.Tag)
  field(TradeRequestType.Tag)
  field(TradeRequestResult.Tag)
  field(TradeRequestStatus.Tag)
  field(Symbol.Tag, Required.NO)
  field(SecurityIDSource.Tag, Required.NO)
  field(SecurityID.Tag, Required.NO)
  field(Currency.Tag, Required.NO)
  field(Text.Tag, Required.NO)

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
