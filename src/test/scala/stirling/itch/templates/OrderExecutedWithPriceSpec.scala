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

class OrderExecutedWithPriceSpec extends TemplateSpec with OrderExecutedWithPriceFixtures

trait OrderExecutedWithPriceFixtures {
  def counterparty = "RR"
  def encoded = "C    65535     4500123456789Y1000000000ACMERR  \r\n"
  def executedQuantity = 4500L
  def matchNumber = 123456789L
  def message = {
    val message = ITCHMessage(Templates.OrderExecutedWithPrice)
    message.set(Fields.OrderReferenceNumber, orderReferenceNumber)
    message.set(Fields.ExecutedQuantity, executedQuantity)
    message.set(Fields.MatchNumber, matchNumber)
    message.set(Fields.Printable,  printable)
    message.set(Fields.TradePrice, tradePrice)
    message.set(Fields.Owner, owner)
    message.set(Fields.Counterparty, counterparty)
    message
  }
  def orderReferenceNumber = 65535L
  def owner = "ACME"
  def printable = "Y"
  def tradePrice = 1000000000L
}
