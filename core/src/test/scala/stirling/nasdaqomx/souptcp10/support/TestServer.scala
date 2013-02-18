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
package stirling.nasdaqomx.souptcp10.support

import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.util.Iterator
import scala.collection.JavaConversions._
import silvertip.{Connection, MessageParser, Server}

abstract class TestServer[Message](parser: MessageParser[Message], val port: Int) extends TestActor[Message] {
  private var server:           Option[Server]              = None
  private var clientConnection: Option[Connection[Message]] = None

  override def eventSource = server

  override def start() {
    server = Some(Server.accept(port, new Server.ConnectionFactory[Message] {
      def newConnection(channel: SocketChannel) = {
        channel.socket.setTcpNoDelay(true)
        val connection = new Connection(channel, parser, new Connection.Callback[Message] {
          def connected(connection: Connection[Message]) {
            receive(Event.Connected)
          }
          def messages(connection: Connection[Message], messages: Iterator[Message]) {
            messages.foreach(onMessage(connection, _))
          }
          def idle(connection: Connection[Message]) {
            onIdle(connection)
          }
          def closed(connection: Connection[Message]) {
            receive(Event.Closed)
          }
          def garbledMessage(connection: Connection[Message], message: String, data: Array[Byte]) {
            receive(Event.GarbledMessage(message, data))
          }
          def sent(buffer: ByteBuffer) = Unit
        })

        clientConnection.foreach(_.close())
        clientConnection = Some(connection)
        connection
      }
    }))
  }

  override def stop() = {
    clientConnection.foreach(_.close())
    clientConnection = None

    server.foreach(_.close())
    server = None
  }

  override def act() = clientConnection match {
    case Some(connection) => act(connection)
    case None             => Unit
  }

  protected def onMessage(connection: Connection[Message], message: Message)

  protected def onIdle(connection: Connection[Message])
}
