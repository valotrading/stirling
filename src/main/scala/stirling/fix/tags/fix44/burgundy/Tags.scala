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
package stirling.fix.tags.fix44.burgundy

import java.lang.{
  Character,
  Integer,
  String
}
import stirling.fix.messages.{
  BooleanTag,
  CharValue,
  EnumTag,
  IntegerTag,
  IntegerValue,
  NumInGroupTag,
  PriceTag,
  StringTag,
  StringValue
}

object SettlType extends EnumTag[Integer](63) {
  val Cash = IntegerValue(1)
  val T2 = IntegerValue(3)
  val T3 = IntegerValue(4)
  val T4 = IntegerValue(5)
  val T5 = IntegerValue(8)
  val T1 = IntegerValue(9)
}
object SecurityType extends EnumTag[String](167) {
  val CommonStock = StringValue("CS")
  val MutualFunds = StringValue("MF")
  val Warrant = StringValue("WAR")
  val StructuredNotes = StringValue("STRUCT")
  val SubscriptionOption = StringValue("SOP")
  val Right = StringValue("RG")
  val Certificate = StringValue("CT")
}
object OrderRestrictions extends EnumTag[Character](529) {
  val IssuerHolding = CharValue('B')
  val IssuePriceStabilization = CharValue('C')
  val ActingAsMarketMaker = CharValue('5')
}
object TrdType extends EnumTag[Integer](828) {
  val RegularTrade = IntegerValue(0)
  val DerivativeRelatedTransaction = IntegerValue(49)
  val PortfolioTrade = IntegerValue(50)
  val VolumeWeightedAverageTrade = IntegerValue(51)
  val MarketplaceGrantedTrade = IntegerValue(52)
}
object InstrAttribType extends EnumTag[Integer](871) {
  val PriceDimension = IntegerValue(101)
  val VolumeDimension = IntegerValue(102)
  val PrimaryMarket = IntegerValue(103)
  val CcpProduct = IntegerValue(104)
  val PostTradePublicTransparent = IntegerValue(105)
  val SecurityShortName= IntegerValue(106)
  val InstrumentCurrency= IntegerValue(308)
  val NoOfInstrumentsOutstanding = IntegerValue(201)
  val KnockOutBarrierHigh = IntegerValue(202)
  val KnockOutBarrierLow = IntegerValue(203)
  val ExerciseToDate = IntegerValue(307)
  val BarrierHigh = IntegerValue(309)
  val BarrierLow = IntegerValue(310)
  val LoanAmount = IntegerValue(301)
  val NominalValue = IntegerValue(302)
  val IssuePrice = IntegerValue(303)
  val DerivativeIssuer= IntegerValue(304)
  val LoanNumber = IntegerValue(305)
  val LeadingManager = IntegerValue(306)
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
