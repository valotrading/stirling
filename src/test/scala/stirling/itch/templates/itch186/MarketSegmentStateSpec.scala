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

class MarketSegmentStateSpec extends TemplateSpec with MarketSegmentStateFixtures

trait MarketSegmentStateFixtures extends TemplateFixtures {
  def encoded = "O123C"
  def eventCode = "C"
  def marketSegmentId = 123L
  def message = {
    val message = newMessage(Templates.MarketSegmentState)
    message.set(fields.MarketSegmentID, marketSegmentId)
    message.set(fields.EventCode, eventCode)
    message
  }
}
