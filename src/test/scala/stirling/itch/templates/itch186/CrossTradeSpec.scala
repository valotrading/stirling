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

class CrossTradeSpec extends TemplateSpec with CrossTradeFixtures

trait CrossTradeFixtures extends TemplateFixtures {
  def crossPrice = 1000000000L
  def crossType = "O"
  def encoded = "Q     4500   8081000000000123456789O    875000"
  def matchNumber = 123456789L
  def message = {
    val message = newMessage(Templates.CrossTrade)
    message.set(fields.Quantity, quantity)
    message.set(fields.OrderBook, orderBook)
    message.set(fields.CrossPrice, crossPrice)
    message.set(fields.MatchNumber, matchNumber)
    message.set(fields.CrossType, crossType)
    message.set(fields.NumberOfTrades, numberOfTrades)
    message
  }
  def numberOfTrades = 875000L
  def orderBook = 808L
  def quantity = 4500L
}
