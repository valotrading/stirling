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

import fixengine.messages.{
  AbstractMessage,
  MassQuote => MassQuoteTrait,
  MessageHeader,
  MessageVisitor,
  RepeatingGroup,
  RepeatingGroupInstance,
  Required
}
import fixengine.tags.fix43.SecurityIDSource
import fixengine.tags.fix42.{
  Account,
  BidPx,
  BidSize,
  Currency,
  DefBidSize,
  NoQuoteEntries,
  NoQuoteSets,
  OfferPx,
  OfferSize,
  QuoteEntryID,
  QuoteID,
  QuoteResponseLevel,
  QuoteSetID,
  SecurityID,
  Symbol,
  TotQuoteEntries
}

class MassQuote(header: MessageHeader) extends AbstractMessage(header) with MassQuoteTrait with Groups {
  field(QuoteID.Tag)
  field(QuoteResponseLevel.Tag, Required.NO)
  field(DefBidSize.Tag, Required.NO)
  field(NoQuoteEntries.Tag, Required.NO)
  field(Account.Tag, Required.NO)

  def quoteSetGrp() {
    group(new RepeatingGroup(NoQuoteSets.Tag) {
      override def newInstance(): RepeatingGroupInstance =
        new RepeatingGroupInstance(QuoteSetID.Tag) {
          field(TotQuoteEntries.Tag)

          def quoteEntryGrp() {
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
                }
            })
          }

          quoteEntryGrp()
        }
    })
  }

  quoteSetGrp()

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
