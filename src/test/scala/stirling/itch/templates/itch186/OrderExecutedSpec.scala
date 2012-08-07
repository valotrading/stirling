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

class OrderExecutedSpec extends TemplateSpec with OrderExecutedFixtures

trait OrderExecutedFixtures extends TemplateFixtures {
  def counterparty = "RR"
  def encoded = "E    65535     4500123456789ACMERR  "
  def executedQuantity = 4500L
  def matchNumber = 123456789L
  def message = {
    val message = newMessage(Templates.OrderExecuted)
    message.set(fields.OrderReferenceNumber, orderReferenceNumber)
    message.set(fields.ExecutedQuantity, executedQuantity)
    message.set(fields.MatchNumber, matchNumber)
    message.set(fields.Owner, owner)
    message.set(fields.Counterparty, counterparty)
    message
  }
  def orderReferenceNumber = 65535L
  def owner = "ACME"
}
