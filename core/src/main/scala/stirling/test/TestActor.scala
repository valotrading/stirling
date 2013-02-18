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

import org.scalatest.Assertions
import scala.annotation.tailrec
import silvertip.{Connection, EventSource}

abstract class TestActor[Message] {
  private var enqueuedActions         = Seq[Action[Message]]()
  private var unhandledReceivedEvents = Seq[Any]()
  private var handledReceivedEvents   = Seq[Any]()

  def eventSource: Option[EventSource]

  def actions: Seq[Action[Message]] = enqueuedActions

  def unhandledEvents: Seq[Any] = unhandledReceivedEvents

  def handledEvents: Seq[Any] = handledReceivedEvents

  def start()

  def stop()

  def act()

  @tailrec
  protected final def act(connection: Connection[Message]) {
    actions.headOption match {
      case None =>
        Unit
      case Some(_: React) =>
        Unit
      case Some(action: Act[Message]) =>
        action.procedure(connection)
        enqueuedActions = enqueuedActions.tail
        act(connection)
    }
  }

  @tailrec
  final def react() {
    (actions.headOption, unhandledEvents.headOption) match {
      case (_, None) =>
        Unit
      case (None, _) =>
        Unit
      case (Some(_: Act[Message]), _) =>
        Unit
      case (Some(action: React), Some(event)) =>
        action.procedure(event)
        enqueuedActions         = enqueuedActions.tail
        unhandledReceivedEvents = unhandledReceivedEvents.tail
        handledReceivedEvents   = handledReceivedEvents :+ event
        react()
    }
  }

  def expect(expected: Any) {
    import Assertions._

    enqueue(React(
      "expect(%s)".format(expected),
      { received: Any =>
        assert(received === expected)
      }
    ))
  }

  def expectOne() {
    enqueue(React(
      "expectOne",
      { received: Any => Unit }
    ))
  }

  protected def enqueue(action: Action[Message]) {
    enqueuedActions = enqueuedActions :+ action
  }

  protected def receive(event: Any) {
    unhandledReceivedEvents = unhandledReceivedEvents :+ event
  }
}
