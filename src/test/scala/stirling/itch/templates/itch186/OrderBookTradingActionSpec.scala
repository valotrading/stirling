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

class OrderBookTradingActionSpec extends TemplateSpec with OrderBookTradingActionFixtures

trait OrderBookTradingActionFixtures extends TemplateFixtures {
  def encoded = "H123456T     "
  def message = {
    val message = newMessage(Templates.OrderBookTradingAction)
    message.set(fields.OrderBook, orderBook)
    message.set(fields.TradingState, tradingState)
    message.set(fields.Reserved, " ")
    message.set(fields.Reason, reason)
    message
  }
  def orderBook = 123456L
  def reason = " "
  def tradingState = "T"
}
