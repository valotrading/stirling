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
package fixengine.tags.fix44.burgundy

import java.lang.{
  Character,
  Integer,
  String
}
import fixengine.messages.{
  BooleanTag,
  EnumTag,
  IntegerTag,
  NumInGroupTag,
  PriceTag,
  StringTag,
  Value
}

object SettlType extends EnumTag[Integer](63) {
  val Cash = Value(1)
  val T2 = Value(3)
  val T3 = Value(4)
  val T4 = Value(5)
  val T5 = Value(8)
  val T1 = Value(9)
}
object SecurityType extends EnumTag[String](167) {
  val CommonStock = Value("CS")
  val MutualFunds = Value("MF")
  val Warrant = Value("WAR")
  val StructuredNotes = Value("STRUCT")
  val SubscriptionOption = Value("SOP")
  val Right = Value("RG")
  val Certificate = Value("CT")
}
object OrderRestrictions extends EnumTag[Character](529) {
  val IssuerHolding = Value('B')
  val IssuePriceStabilization = Value('C')
  val ActingAsMarketMaker = Value('5')
}
object TrdType extends EnumTag[Integer](828) {
  val RegularTrade = Value(0)
  val DerivativeRelatedTransaction = Value(49)
  val PortfolioTrade = Value(50)
  val VolumeWeightedAverageTrade = Value(51)
  val MarketplaceGrantedTrade = Value(52)
}
object InstrAttribType extends EnumTag[Integer](871) {
  val PriceDimension = Value(101)
  val VolumeDimension = Value(102)
  val PrimaryMarket = Value(103)
  val CcpProduct = Value(104)
  val PostTradePublicTransparent = Value(105)
  val SecurityShortName= Value(106)
  val InstrumentCurrency= Value(308)
  val NoOfInstrumentsOutstanding = Value(201)
  val KnockOutBarrierHigh = Value(202)
  val KnockOutBarrierLow = Value(203)
  val ExerciseToDate = Value(307)
  val BarrierHigh = Value(309)
  val BarrierLow = Value(310)
  val LoanAmount = Value(301)
  val NominalValue = Value(302)
  val IssuePrice = Value(303)
  val DerivativeIssuer= Value(304)
  val LoanNumber = Value(305)
  val LeadingManager = Value(306)
}
object TradeSeqNo extends IntegerTag(7554)
object TradeSeqNoSeries extends IntegerTag(7555)
object NoTradeDeqNoSeries extends NumInGroupTag(7565)
object IncludeTickRules extends BooleanTag(10001)
object NoTickRules extends NumInGroupTag(11205)
object StartTickPriceRange extends PriceTag(11206)
object TickIncrement extends PriceTag(11208)
object TickRuleID extends StringTag(11209)
object LargeInScaleLimit extends PriceTag(11210)
object CSD extends StringTag(11211)
object AllowedOrderTransparency extends IntegerTag(11212)
object SuppressSecurityStatus extends BooleanTag(11218)
object ValidityPeriod extends IntegerTag(11220)
