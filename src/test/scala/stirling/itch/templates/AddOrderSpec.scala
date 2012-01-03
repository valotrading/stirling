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
package stirling.itch.templates

import stirling.itch.elements.Fields
import stirling.itch.messages.ITCHMessage

class AddOrderSpec extends TemplateSpec with AddOrderFixtures

trait AddOrderFixtures {
  def buyOrSellIndicator = "S"
  def encoded = "A    65535S     4500   8081000000000"
  def message = {
    val message = ITCHMessage(Templates.AddOrder)
    message.set(Fields.OrderReferenceNumber, orderReferenceNumber)
    message.set(Fields.BuyOrSellIndicator, buyOrSellIndicator)
    message.set(Fields.Quantity, quantity)
    message.set(Fields.OrderBook, orderBook)
    message.set(Fields.Price, price)
    message
  }
  def orderBook = 808L
  def orderReferenceNumber = 65535L
  def price = 1000000000L
  def quantity = 4500L
}
