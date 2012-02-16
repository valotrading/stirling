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

class TradeSpec extends TemplateSpec with TradeFixtures

trait TradeFixtures extends TemplateFixtures {
  def buyer = "RR"
  def encoded = "P    65535B     4500   8081234567891000000000RR  ACME"
  def matchNumber = 123456789L
  def message = {
    val message = newMessage(Templates.Trade)
    message.set(fields.OrderReferenceNumber, orderReferenceNumber)
    message.set(fields.TradeType, tradeType)
    message.set(fields.Quantity, quantity)
    message.set(fields.OrderBook, orderBook)
    message.set(fields.MatchNumber, matchNumber)
    message.set(fields.TradePrice, tradePrice)
    message.set(fields.Buyer, buyer)
    message.set(fields.Seller, seller)
    message
  }
  def orderBook = 808L
  def orderReferenceNumber = 65535L
  def quantity = 4500L
  def seller = "ACME"
  def tradePrice = 1000000000L
  def tradeType = "B"
}
