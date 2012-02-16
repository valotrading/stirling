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

class CrossTradeSpec extends TemplateSpec with CrossTradeFixtures

trait CrossTradeFixtures {
  def crossPrice = 1000000000L
  def crossType = "O"
  def encoded = "Q     4500   8081000000000123456789O    875000"
  def matchNumber = 123456789L
  def message = {
    val message = ITCHMessage(Templates.CrossTrade)
    message.set(Fields.Quantity, quantity)
    message.set(Fields.OrderBook, orderBook)
    message.set(Fields.CrossPrice, crossPrice)
    message.set(Fields.MatchNumber, matchNumber)
    message.set(Fields.CrossType, crossType)
    message.set(Fields.NumberOfTrades, numberOfTrades)
    message
  }
  def numberOfTrades = 875000L
  def orderBook = 808L
  def quantity = 4500L
}
