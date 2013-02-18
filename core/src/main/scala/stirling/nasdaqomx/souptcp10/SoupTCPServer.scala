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
import silvertip.{Connection, GarbledMessageException, MessageParser}

class SoupTCPServer[Message](parser: MessageParser[Message], settings: Settings = Defaults) {
  import SoupTCPServer._

  private val linkSupervisor = new LinkSupervisor(settings)

  def connected() {
    linkSupervisor.connect()
  }

  def receive(packet: Packet): Option[Event[Message]] = {
    linkSupervisor.receive()

    packet match {
      case packet: Debug           => onDebug(packet)
      case packet: LoginRequest    => onLoginRequest(packet)
      case packet: UnsequencedData => onUnsequencedData(packet)
      case LogoutRequest           => onLogoutRequest()
      case ClientHeartbeat         => onClientHeartbeat()
      case packet                  => onUnexpectedPacket(packet)
    }
  }

  private def onDebug(packet: Debug): Option[Event[Message]] = {
    Some(Event.Debug(packet.text))
  }

  private def onLoginRequest(packet: LoginRequest): Option[Event[Message]] = {
    Some(Event.LoginRequest(
      username                = packet.username,
      password                = packet.password,
      requestedSession        = packet.requestedSession,
      requestedSequenceNumber = packet.requestedSequenceNumber
    ))
  }

  private def onUnsequencedData(packet: UnsequencedData): Option[Event[Message]] = {
    Some(Event.UnsequencedData(parser.parse(ByteBuffer.wrap(packet.message))))
  }

  private def onLogoutRequest(): Option[Event[Message]] = {
    Some(Event.LogoutRequest)
  }

  private def onClientHeartbeat(): Option[Event[Message]] = {
    Some(Event.ClientHeartbeat)
  }

  private def onUnexpectedPacket(packet: Packet): Nothing = {
    throw new GarbledMessageException("Unexpected packet: %s".format(packet))
  }

  def acceptLogin(connection: Connection[Packet], session: String, sequenceNumber: Long) {
    val buffer = ByteBuffer.allocate(LoginAccepted.length)

    LoginAccepted.format(
      buffer         = buffer,
      session        = session,
      sequenceNumber = sequenceNumber
    )
    send(connection, buffer)
  }

  def rejectLogin(connection: Connection[Packet], loginRejectCode: Byte) {
    val buffer = ByteBuffer.allocate(LoginRejected.length)

    LoginRejected.format(buffer, loginRejectCode)
    send(connection, buffer)
  }

  def send(connection: Connection[Packet], maxLength: Int)(formatter: (ByteBuffer) => Unit) {
    val buffer = ByteBuffer.allocate(SequencedData.minLength + maxLength)

    SequencedData.format(buffer)(formatter)
    send(connection, buffer)
  }

  def endSession(connection: Connection[Packet]) {
    send(connection, 0) { buffer => Unit }
  }

  def debug(connection: Connection[Packet], text: String) {
    val buffer = ByteBuffer.allocate(Debug.minLength + text.length)

    Debug.format(buffer, text)
    send(connection, buffer)
  }

  def keepAlive(connection: Connection[Packet]): Option[Event.LinkDown.type] = {
    linkSupervisor.idle() match {
      case Some(LinkSupervisor.Event.Heartbeat) =>
        heartbeat(connection)
        None
      case Some(LinkSupervisor.Event.LinkDown) =>
        Some(Event.LinkDown)
      case None =>
        None
    }
  }

  private def heartbeat(connection: Connection[Packet]) {
    val buffer = ByteBuffer.allocate(ServerHeartbeat.length)

    ServerHeartbeat.format(buffer)
    send(connection, buffer)
  }

  private def send(connection: Connection[Packet], buffer: ByteBuffer) {
    buffer.flip()
    connection.send(buffer)
    linkSupervisor.send()
  }
}

object SoupTCPServer {
  sealed trait Event[+Message]

  object Event {
    case class Debug(
      text: String
    ) extends Event[Nothing]

    case class LoginRequest(
      username:                String,
      password:                String,
      requestedSession:        String,
      requestedSequenceNumber: Long
    ) extends Event[Nothing]

    case class UnsequencedData[Message](
      message: Message
    ) extends Event[Message]

    case object LogoutRequest extends Event[Nothing]

    case object ClientHeartbeat extends Event[Nothing]

    case object LinkDown extends Event[Nothing]
  }
}
