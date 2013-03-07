/*
 * Copyright 2013 the original author or authors.
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
package stirling.test

import silvertip.EventSource

trait TestActor[Message] {
  def eventSource: Option[EventSource]

  def actions: Seq[Action[Message]]

  def unhandledEvents: Seq[Any]

  def handledEvents: Seq[Any]

  def start()

  def stop()

  def act()

  def react()
}
