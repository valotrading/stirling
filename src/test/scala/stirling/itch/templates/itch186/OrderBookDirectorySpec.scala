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
package stirling.itch.templates.itch186

class OrderBookDirectorySpec extends TemplateSpec with OrderBookDirectoryFixtures

trait OrderBookDirectoryFixtures extends TemplateFixtures {
  def encoded = "R727000Acme Corp       US0000000000  1USDNYSE 10       0100000000"
  def financialProduct = 1L
  def isin = "US0000000000"
  def marketSegmentId = 10L
  def message = {
    val message = newMessage(Templates.OrderBookDirectory)
    message.set(fields.OrderBook, orderBook)
    message.set(fields.Symbol, symbol)
    message.set(fields.ISIN, isin)
    message.set(fields.FinancialProduct, financialProduct)
    message.set(fields.TradingCurrency, tradingCurrency)
    message.set(fields.MIC, mic)
    message.set(fields.MarketSegmentID, marketSegmentId)
    message.set(fields.NoteCodes, noteCodes)
    message.set(fields.RoundLotSize, roundLotSize)
    message
  }
  def mic = "NYSE"
  def noteCodes = 0L
  def orderBook = 727000L
  def roundLotSize = 100000000L
  def symbol = "Acme Corp"
  def tradingCurrency = "USD"
}
