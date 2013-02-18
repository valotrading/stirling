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

class SoupTCPClient[Message](parser: MessageParser[Message], settings: Settings = Defaults) {
  import SoupTCPClient._

  private val linkSupervisor = new LinkSupervisor(settings)

  def connected() {
    linkSupervisor.connect()
  }

  def receive(packet: Packet): Option[Event[Message]] = {
    linkSupervisor.receive()

    packet match {
      case packet: Debug         => onDebug(packet)
      case packet: LoginAccepted => onLoginAccepted(packet)
      case packet: LoginRejected => onLoginRejected(packet)
      case packet: SequencedData => onSequencedData(packet)
      case ServerHeartbeat       => onServerHeartbeat()
      case packet                => onUnexpectedPacket(packet)
    }
  }

  private def onDebug(packet: Debug): Option[Event[Message]] = {
    Some(Event.Debug(packet.text))
  }

  private def onLoginAccepted(packet: LoginAccepted): Option[Event[Message]] = {
    Some(Event.LoginAccepted(packet.session, packet.sequenceNumber))
  }

  private def onLoginRejected(packet: LoginRejected): Option[Event[Message]] = {
    Some(Event.LoginRejected(packet.rejectReasonCode))
  }

  private def onSequencedData(packet: SequencedData): Option[Event[Message]] = {
    if (packet.message.length == 0)
      Some(Event.EndOfSession)
    else
      Some(Event.SequencedData(parser.parse(ByteBuffer.wrap(packet.message))))
  }

  private def onServerHeartbeat(): Option[Event[Message]] = {
    Some(Event.ServerHeartbeat)
  }

  private def onUnexpectedPacket(packet: Packet): Nothing = {
    throw new GarbledMessageException("Unexpected packet: %s".format(packet))
  }

  def login(
    connection:              Connection[Packet],
    username:                String,
    password:                String,
    requestedSession:        String,
    requestedSequenceNumber: Int
  ) {
    val buffer = ByteBuffer.allocate(LoginRequest.length)

    LoginRequest.format(
      buffer                  = buffer,
      username                = username,
      password                = password,
      requestedSession        = requestedSession,
      requestedSequenceNumber = requestedSequenceNumber
    )
    send(connection, buffer)
  }

  def logout(connection: Connection[Packet]) {
    val buffer = ByteBuffer.allocate(LogoutRequest.length)

    LogoutRequest.format(buffer)
    send(connection, buffer)
  }

  def send(connection: Connection[Packet], maxLength: Int)(formatter: (ByteBuffer) => Unit) {
    val buffer = ByteBuffer.allocate(UnsequencedData.minLength + maxLength)

    UnsequencedData.format(buffer)(formatter)
    send(connection, buffer)
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
    val buffer = ByteBuffer.allocate(ClientHeartbeat.length)

    ClientHeartbeat.format(buffer)
    send(connection, buffer)
  }

  private def send(connection: Connection[Packet], buffer: ByteBuffer) {
    buffer.flip()
    connection.send(buffer)
    linkSupervisor.send()
  }
}

object SoupTCPClient {
  sealed trait Event[+Message]

  object Event {
    case class Debug(
      text: String
    ) extends Event[Nothing]

    case class LoginAccepted(
      session:        String,
      sequenceNumber: Long
    ) extends Event[Nothing]

    case class LoginRejected(
      rejectReasonCode: Byte
    ) extends Event[Nothing]

    case class SequencedData[Message](
      message: Message
    ) extends Event[Message]

    case object EndOfSession extends Event[Nothing]

    case object ServerHeartbeat extends Event[Nothing]

    case object LinkDown extends Event[Nothing]
  }
}
