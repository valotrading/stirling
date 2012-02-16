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
package stirling.itch.fields

import stirling.itch.types.{Alpha, Numeric}

object Fields {
  val Attribution = Field("Attribution", Alpha(4))
  val BestAskPrice = Field("BestAskPrice", Numeric(10))
  val BestAskQuantity = Field("BestAskQuantity", Numeric(9))
  val BestBidPrice = Field("BestBidPrice", Numeric(10))
  val BestBidQuantity = Field("BestBidQuantity", Numeric(9))
  val BuyOrSellIndicator = Field("BuyOrSellIndicator", Alpha(1))
  val Buyer = Field("Buyer", Alpha(4))
  val CanceledQuantity = Field("CanceledQuantity", Numeric(9))
  val CrossPrice = Field("CrossPrice", Numeric(10))
  val CrossType = Field("CrossType", Alpha(1))
  val Counterparty = Field("Counterparty", Alpha(4))
  val EquilibriumPrice = Field("EquilibriumPrice", Numeric(10))
  val EventCode = Field("EventCode", Alpha(1))
  val ExecutedQuantity = Field("ExecutedQuantity", Numeric(9))
  val FinancialProduct = Field("FinancialProduct", Numeric(3))
  val ISIN = Field("ISIN", Alpha(12))
  val ImbalanceDirection = Field("ImbalanceDirection", Alpha(1))
  val ImbalanceQuantity = Field("ImbalanceQuantity", Numeric(9))
  val MIC = Field("MIC", Alpha(4))
  val MarketSegmentID = Field("MarketSegmentID", Numeric(3))
  val MatchNumber = Field("MatchNumber", Numeric(9))
  val Millisecond = Field("Millisecond", Numeric(3))
  val NoteCodes = Field("NoteCodes", Numeric(8))
  val NumberOfTrades = Field("NumberOfTrades", Numeric(10))
  val OrderBook = Field("OrderBook", Numeric(6))
  val OrderReferenceNumber = Field("OrderReferenceNumber", Numeric(9))
  val Owner = Field("Owner", Alpha(4))
  val PairedQuantity = Field("PairedQuantity", Numeric(9))
  val Price = Field("Price", Numeric(10))
  val Printable = Field("Printable", Alpha(1))
  val Quantity = Field("Quantity", Numeric(9))
  val Reason = Field("Reason", Alpha(4))
  val Reserved = Field("Reserved", Alpha(1))
  val RoundLotSize = Field("RoundLotSize", Numeric(9))
  val Second = Field("Second", Numeric(5))
  val Seller = Field("Seller", Alpha(4))
  val Symbol = Field("Symbol", Alpha(16))
  val TradePrice = Field("TradePrice", Numeric(10))
  val TradeType = Field("TradeType", Alpha(1))
  val TradingCurrency = Field("TradingCurrency", Alpha(3))
  val TradingState = Field("TradingState", Alpha(1))
}
