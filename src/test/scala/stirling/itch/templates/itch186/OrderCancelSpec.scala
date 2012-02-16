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

class OrderCancelSpec extends TemplateSpec with OrderCancelFixtures

trait OrderCancelFixtures extends TemplateFixtures {
  def canceledQuantity = 4500L
  def encoded = "X    65535     4500"
  def message = {
    val message = newMessage(Templates.OrderCancel)
    message.set(fields.OrderReferenceNumber, orderReferenceNumber)
    message.set(fields.CanceledQuantity, canceledQuantity)
    message
  }
  def orderReferenceNumber = 65535L
}
