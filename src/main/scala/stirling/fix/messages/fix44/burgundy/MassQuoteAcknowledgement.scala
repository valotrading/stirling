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
  RepeatingGroupInstance,
  RepeatingGroup,
  AbstractMessage,
  MassQuoteAcknowledgement => MassQuoteAcknowledgementTrait,
  MessageHeader,
  MessageVisitor,
  Required
}
import stirling.fix.tags.fix43.SecurityIDSource
import stirling.fix.tags.fix42.{
  OfferSize,
  BidSize,
  OfferPx,
  BidPx,
  SecurityID,
  QuoteEntryID,
  Currency,
  NoQuoteEntries,
  TotQuoteEntries,
  QuoteSetID,
  NoQuoteSets,
  Symbol,
  Text,
  QuoteID
}
import stirling.fix.tags.fix44.{
  QuoteEntryRejectReason,
  QuoteRejectReason,
  QuoteAckStatus
}

class MassQuoteAcknowledgement(header: MessageHeader) extends AbstractMessage(header) with MassQuoteAcknowledgementTrait with Groups {
  field(QuoteID.Tag)
  field(QuoteAckStatus.Tag)
  field(QuoteRejectReason.Tag, Required.NO)
  field(Text.Tag, Required.NO)

  def quoteSetAckGrp() {
    group(new RepeatingGroup(NoQuoteSets.Tag) {
      override def newInstance(): RepeatingGroupInstance =

        new RepeatingGroupInstance(QuoteSetID.Tag) {
          field(TotQuoteEntries.Tag, Required.NO)

          def quoteEntryAckGrp() {
            group(new RepeatingGroup(NoQuoteEntries.Tag) {
              override def newInstance(): RepeatingGroupInstance =
                new RepeatingGroupInstance(QuoteEntryID.Tag) {
                  field(Symbol.Tag)
                  field(SecurityIDSource.Tag, Required.NO)
                  field(SecurityID.Tag, Required.NO)
                  field(BidPx.Tag, Required.NO)
                  field(OfferPx.Tag, Required.NO)
                  field(BidSize.Tag, Required.NO)
                  field(OfferSize.Tag, Required.NO)
                  field(Currency.Tag)
                  field(QuoteEntryRejectReason.Tag, Required.NO)
                }
            })
          }

          quoteEntryAckGrp()
        }
    })
  }

  quoteSetAckGrp()

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
