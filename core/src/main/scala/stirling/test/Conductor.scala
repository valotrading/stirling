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
import silvertip.Events

object Conductor {
  import Assertions._

  sealed trait Status
  case object Done                                    extends Status
  case class  Error(error: Exception)                 extends Status
  case class  Timeout(pendingActions: Seq[Action[_]]) extends Status

  val DefaultTimeout = 2000

  def conduct(
    actors:  Seq[TestActor[_]],
    timeout: Long = DefaultTimeout
  ) {
    val events = Events.open()

    actors.foreach(_.start())

    actors.flatMap(_.eventSource).foreach(events.register)

    val status = loop(
      events  = events,
      actors  = actors,
      started = System.currentTimeMillis,
      timeout = timeout
    )

    actors.reverse.foreach(_.stop())

    events.stop()

    status match {
      case Error(exception) =>
        throw exception
      case Timeout(pendingActions) =>
        fail("Timeout, pending actions: %s".format(pendingActions.mkString(", ")))
      case Done =>
        Unit
    }

    actors.flatMap(_.unhandledEvents).foreach { event =>
      fail("Unhandled event: %s".format(event))
    }

    actors.flatMap(_.actions).foreach { action =>
      fail("Unexecuted action: %s".format(action))
    }
  }

  @tailrec
  private def loop(events: Events, actors: Seq[TestActor[_]], started: Long, timeout: Long): Status = {
    val now = System.currentTimeMillis

    val pendingActions = actors.flatMap(_.actions)

    if (pendingActions.isEmpty)
      return Done

    if (now  - started >= timeout)
      return Timeout(pendingActions)

    try {
      actors.foreach(_.act())

      events.process(50)

      actors.foreach(_.react())
    } catch {
      case e: Exception =>
        return Error(e)
    }

    return loop(events, actors, started, timeout)
  }
}
