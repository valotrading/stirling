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
package stirling.test.mbtrading.quoteapi

import silvertip.Connection
import stirling.mbtrading.quoteapi.{MbtMessage, MbtMessageParser, MbtSession}
import stirling.test.{Act, TestClient}

class MbtTestClient(hostname: String, port: Int) extends TestClient(MbtMessageParser, hostname, port) {
  private val session = new MbtSession()

  override protected def onMessage(connection: Connection[MbtMessage], message: MbtMessage) {
    receive(session.receive(message))
  }

  override protected def keepAlive(connection: Connection[MbtMessage]) = Unit

  def send(message: MbtMessage) {
    enqueue(Act(
      "send",
      { connection: Connection[MbtMessage] =>
        session.send(connection, message)
      }
    ))
  }
}
