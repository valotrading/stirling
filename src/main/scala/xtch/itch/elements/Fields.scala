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
package xtch.itch.elements

import xtch.itch.types.{Alpha, Numeric}

object Fields {
  val Attribution = Field("Attribution", Alpha(4))
  val BuyOrSellIndicator = Field("BuyOrSellIndicator", Alpha(1))
  val EventCode = Field("EventCode", Alpha(1))
  val FinancialProduct = Field("FinancialProduct", Numeric(3))
  val ISIN = Field("ISIN", Alpha(12))
  val MIC = Field("MIC", Alpha(4))
  val MarketSegmentID = Field("MarketSegmentID", Numeric(3))
  val Millisecond = Field("Millisecond", Numeric(3))
  val NoteCodes = Field("NoteCodes", Numeric(8))
  val OrderBook = Field("OrderBook", Numeric(6))
  val OrderReferenceNumber = Field("OrderReferenceNumber", Numeric(9))
  val Price = Field("Price", Numeric(10))
  val Quantity = Field("Quantity", Numeric(9))
  val Reason = Field("Reason", Alpha(4))
  val Reserved = Field("Reserved", Alpha(1))
  val RoundLotSize = Field("RoundLotSize", Numeric(9))
  val Second = Field("Second", Numeric(5))
  val Symbol = Field("Symbol", Alpha(16))
  val TradingCurrency = Field("TradingCurrency", Alpha(3))
  val TradingState = Field("TradingState", Alpha(1))
}
