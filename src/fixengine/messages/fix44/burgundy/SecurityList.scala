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
  MessageHeader,
  MessageVisitor,
  RepeatingGroup,
  RepeatingGroupInstance,
  Required,
  SecurityList => SecurityListTrait
}
import fixengine.tags.fix42.{
  ContractMultiplier,
  Currency,
  Issuer,
  NoRelatedSym,
  PutOrCall,
  SecurityDesc,
  SecurityExchange,
  SecurityID,
  SecurityReqID,
  SecurityResponseID,
  StrikePrice,
  Symbol,
  Text,
  TotalNumSecurities,
  UnderlyingCurrency,
  UnderlyingSecurityDesc,
  UnderlyingSecurityID,
  UnderlyingSecurityType,
  UnderlyingSymbol
}
import fixengine.tags.fix44.{
  EventDate,
  EventType,
  InstrAttribValue,
  LastFragment,
  NoEvents,
  NoInstrAttrib,
  NoUnderlyings,
  SecuritySubType,
  StrikeCurrency,
  UnderlyingSecurityIDSource
}
import fixengine.tags.fix44.burgundy.{
  AllowedOrderTransparency,
  CSD,
  InstrAttribType,
  LargeInScaleLimit,
  NoTickRules,
  SecurityType,
  SettlType,
  StartTickPriceRange,
  TickIncrement,
  TickRuleID,
  TradeSeqNoSeries
}
import fixengine.tags.fix50sp1.{
  ExerciseStyle,
  MarketID,
  MarketSegmentID,
  SettlMethod
}
import fixengine.tags.fix43.{
  NoSecurityAltID,
  NoUnderlyingSecurityAltID,
  RoundLot,
  SecurityAltID,
  SecurityAltIDSource,
  SecurityIDSource,
  SecurityRequestResult,
  UnderlyingSecurityAltID,
  UnderlyingSecurityAltIDSource
}

class SecurityList(header: MessageHeader) extends AbstractMessage(header) with SecurityListTrait {
  field(SecurityReqID.Tag)
  field(SecurityResponseID.Tag)

  def tickRulesGrp() {
    group(new RepeatingGroup(NoTickRules.Tag) {
      override def newInstance() =
        new RepeatingGroupInstance(StartTickPriceRange.Tag) {
          field(TickIncrement.Tag)
          field(TickRuleID.Tag)
        }
    }, Required.NO)
  }

  tickRulesGrp()

  field(TotalNumSecurities.Tag, Required.NO)
  field(LastFragment.Tag, Required.NO)
  field(SecurityRequestResult.Tag, Required.NO)
  field(MarketID.Tag, Required.NO)
  field(MarketSegmentID.Tag, Required.NO)
  group(new RepeatingGroup(NoRelatedSym.Tag) {
    override def newInstance(): RepeatingGroupInstance =
      new RepeatingGroupInstance(Symbol.Tag) {
        def instrument() {
          field(SecurityIDSource.Tag)
          field(SecurityID.Tag)

          def secAltIDGrp() {
            group(new RepeatingGroup(NoSecurityAltID.Tag) {
              override def newInstance() =
                new RepeatingGroupInstance(SecurityAltID.Tag) {
                  field(SecurityAltIDSource.Tag)
                }
            })
          }

          secAltIDGrp()
          field(SecurityType.Tag)
          field(SecuritySubType.Tag, Required.NO)

          field(SecurityDesc.Tag, Required.NO)
          field(StrikePrice.Tag, Required.NO)
          field(StrikeCurrency.Tag, Required.NO)
          field(SettlMethod.Tag, Required.NO)
          field(ExerciseStyle.Tag, Required.NO)
          field(PutOrCall.Tag, Required.NO)
          field(ContractMultiplier.Tag, Required.NO)
          field(SecurityExchange.Tag, Required.NO)
          field(Issuer.Tag, Required.NO)

          def eventGrp() {
            group(new RepeatingGroup(NoEvents.Tag) {
              override def newInstance() =
                new RepeatingGroupInstance(EventType.Tag) {
                  field(EventDate.Tag)
                }
            })
          }

          eventGrp()
        }

        instrument()

        def instrumentExtension() {
          group(new RepeatingGroup(NoInstrAttrib.Tag) {
            override def newInstance() =
              new RepeatingGroupInstance(InstrAttribType.Tag) {
                field(InstrAttribValue.Tag)
              }
          }, Required.NO)

        }

        instrumentExtension()

        def undInstrmtGrp() {
          group(new RepeatingGroup(NoUnderlyings.Tag) {
            override def newInstance() =
              new RepeatingGroupInstance(UnderlyingSymbol.Tag) {
                field(UnderlyingSecurityID.Tag, Required.NO)
                field(UnderlyingSecurityIDSource.Tag, Required.NO)

                def undSecAltIDGrp() {
                  group(new RepeatingGroup(NoUnderlyingSecurityAltID.Tag) {
                    override def newInstance() =
                      new RepeatingGroupInstance(UnderlyingSecurityAltID.Tag) {
                        field(UnderlyingSecurityAltIDSource.Tag)
                      }
                  })
                }

                undSecAltIDGrp()

                field(UnderlyingSecurityType.Tag)
                field(UnderlyingSecurityDesc.Tag, Required.NO)
                field(UnderlyingCurrency.Tag, Required.NO)
              }
          })

        }

        undInstrmtGrp()

        field(Currency.Tag)
        field(TickRuleID.Tag, Required.NO)
        field(CSD.Tag, Required.NO)
        field(AllowedOrderTransparency.Tag, Required.NO)
        field(LargeInScaleLimit.Tag, Required.NO)
        field(TradeSeqNoSeries.Tag, Required.NO)
        field(RoundLot.Tag, Required.NO)
        field(Text.Tag, Required.NO)
        field(SettlType.Tag, Required.NO)
      }
  })

  override def apply(visitor: MessageVisitor) = visitor.visit(this)
}
