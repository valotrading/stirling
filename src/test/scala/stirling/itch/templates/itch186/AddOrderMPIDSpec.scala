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

class AddOrderMPIDSpec extends TemplateSpec with AddOrderMPIDFixtures

trait AddOrderMPIDFixtures extends AddOrderFixtures {
  def attribution = "_MMO"
  override def encoded = "F    65535S     4500   8081000000000_MMO"
  override def message = {
    val message = newMessage(Templates.AddOrderMPID)
    message.set(fields.OrderReferenceNumber, orderReferenceNumber)
    message.set(fields.BuyOrSellIndicator, buyOrSellIndicator)
    message.set(fields.Quantity, quantity)
    message.set(fields.OrderBook, orderBook)
    message.set(fields.Price, price)
    message.set(fields.Attribution, attribution)
    message
  }
}
