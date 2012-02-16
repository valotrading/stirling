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
import stirling.itch.messages.itch186.ITCHMessage

class AddOrderMPIDSpec extends TemplateSpec with AddOrderMPIDFixtures

trait AddOrderMPIDFixtures extends AddOrderFixtures {
  def attribution = "_MMO"
  override def encoded = "F    65535S     4500   8081000000000_MMO"
  override def message = {
    val message = ITCHMessage(Templates.AddOrderMPID)
    message.set(Fields.OrderReferenceNumber, orderReferenceNumber)
    message.set(Fields.BuyOrSellIndicator, buyOrSellIndicator)
    message.set(Fields.Quantity, quantity)
    message.set(Fields.OrderBook, orderBook)
    message.set(Fields.Price, price)
    message.set(Fields.Attribution, attribution)
    message
  }
}
