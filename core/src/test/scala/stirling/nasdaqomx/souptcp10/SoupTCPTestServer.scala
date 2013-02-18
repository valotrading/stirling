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
package stirling.nasdaqomx.souptcp10

import java.nio.ByteBuffer
import silvertip.{Connection, MessageParser}
import stirling.nasdaqomx.souptcp10.support.{Act, TestServer}

class SoupTCPTestServer[Message](
  parser:   MessageParser[Message],
  port:     Int,
  settings: Settings
) extends TestServer(SoupTCPParser, port) {
  private val session = new SoupTCPServer(parser, settings)

  override protected def onMessage(connection: Connection[Packet], packet: Packet) {
    session.receive(packet).foreach(receive)
  }

  override protected def onIdle(connection: Connection[Packet]) {
    session.keepAlive(connection).foreach(receive)
  }

  def debug(text: String) {
    enqueue(Act(
      "debug(%s)".format(text),
      { connection: Connection[Packet] =>
        this.session.debug(
          connection,
          text
        )
      }
    ))
  }

  def acceptLogin(
    session:        String,
    sequenceNumber: Long
  ) {
    enqueue(Act(
      "acceptLogin",
      { connection: Connection[Packet] =>
        this.session.acceptLogin(
          connection,
          session,
          sequenceNumber
        )
      }
    ))
  }

  def rejectLogin(rejectReasonCode: Byte) {
    enqueue(Act(
      "rejectLogin",
      { connection: Connection[Packet] =>
        this.session.rejectLogin(
          connection,
          rejectReasonCode
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

  def endSession() {
    enqueue(Act(
      "endSession",
      { connection: Connection[Packet] =>
        this.session.endSession(
          connection
        )
      }
    ))
  }
}
