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
package stirling.test.nasdaqomx.souptcp10

import java.nio.ByteBuffer
import silvertip.{Connection, MessageParser}
import stirling.nasdaqomx.souptcp10._
import stirling.test.{Act, TestClient}

class SoupTCPTestClient[Message](
  parser:   MessageParser[Message],
  hostname: String,
  port:     Int,
  settings: Settings
) extends TestClient(SoupTCPParser, hostname, port) {
  private val session = new SoupTCPClient(parser, settings)

  override protected def onMessage(connection: Connection[Packet], packet: Packet) {
    session.receive(packet).foreach(receive)
  }

  override protected def keepAlive(connection: Connection[Packet]) {
    session.keepAlive(connection).foreach(receive)
  }

  def debug(text: String) {
    enqueue(Act(
      "debug(%s)".format(text),
      { connection: Connection[Packet] =>
        session.debug(
          connection,
          text
        )
      }
    ))
  }

  def login(
    username: String,
    password: String,
    requestedSession: String,
    requestedSequenceNumber: Int
  ) {
    enqueue(Act(
      "login",
      { connection: Connection[Packet] =>
        session.login(
          connection,
          username,
          password,
          requestedSession,
          requestedSequenceNumber
        )
      }
    ))
  }

  def send(maxLength: Int)(formatter: (ByteBuffer) => Unit) {
    enqueue(Act(
      "send",
      { connection: Connection[Packet] =>
        this.session.send(connection, maxLength)(formatter)
      }
    ))
  }

  def logout() {
    enqueue(Act(
      "logout",
      { connection: Connection[Packet] =>
        session.logout(
          connection
        )
      }
    ))
  }
}
