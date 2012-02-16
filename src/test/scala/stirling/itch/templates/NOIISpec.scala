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

import stirling.itch.fields.itch186.Fields
import stirling.itch.messages.ITCHMessage

class NOIISpec extends TemplateSpec with NOIIFixtures

trait NOIIFixtures {
  def bestAskPrice = 15L
  def bestAskQuantity = 100L
  def bestBidPrice = 16L
  def bestBidQuantity = 2300L
  def crossType = "C"
  def encoded = "I     4500999999999S   808         0C        16     2300        15      100"
  def equilibriumPrice = 0L
  def imbalanceDirection = "S"
  def imbalanceQuantity = 999999999L
  def matchNumber = 123456789L
  def message = {
    val message = ITCHMessage(Templates.NOII)
    message.set(Fields.PairedQuantity, pairedQuantity)
    message.set(Fields.ImbalanceQuantity, imbalanceQuantity)
    message.set(Fields.ImbalanceDirection, imbalanceDirection)
    message.set(Fields.OrderBook, orderBook)
    message.set(Fields.EquilibriumPrice, equilibriumPrice)
    message.set(Fields.CrossType, crossType)
    message.set(Fields.BestBidPrice, bestBidPrice)
    message.set(Fields.BestBidQuantity, bestBidQuantity)
    message.set(Fields.BestAskPrice, bestAskPrice)
    message.set(Fields.BestAskQuantity, bestAskQuantity)
    message
  }
  def numberOfTrades = 875000L
  def orderBook = 808L
  def pairedQuantity = 4500L
}
