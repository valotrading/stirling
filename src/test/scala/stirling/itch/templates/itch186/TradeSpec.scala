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

import stirling.itch.fields.itch186.Fields
import stirling.itch.messages.itch186.ITCHMessage

class TradeSpec extends TemplateSpec with TradeFixtures

trait TradeFixtures {
  def buyer = "RR"
  def encoded = "P    65535B     4500   8081234567891000000000RR  ACME"
  def matchNumber = 123456789L
  def message = {
    val message = ITCHMessage(Templates.Trade)
    message.set(Fields.OrderReferenceNumber, orderReferenceNumber)
    message.set(Fields.TradeType, tradeType)
    message.set(Fields.Quantity, quantity)
    message.set(Fields.OrderBook, orderBook)
    message.set(Fields.MatchNumber, matchNumber)
    message.set(Fields.TradePrice, tradePrice)
    message.set(Fields.Buyer, buyer)
    message.set(Fields.Seller, seller)
    message
  }
  def orderBook = 808L
  def orderReferenceNumber = 65535L
  def quantity = 4500L
  def seller = "ACME"
  def tradePrice = 1000000000L
  def tradeType = "B"
}
