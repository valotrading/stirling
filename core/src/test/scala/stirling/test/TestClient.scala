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

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.util.Iterator
import scala.annotation.tailrec
import scala.collection.JavaConversions._
import silvertip.{Connection, MessageParser}

abstract class TestClient[Message](val parser: MessageParser[Message], val hostname: String, val port: Int) extends TestActor[Message] {
  private var connection: Option[Connection[Message]] = None

  override def eventSource = connection

  override def start() {
    val channel = SocketChannel.open()
    channel.connect(new InetSocketAddress(hostname, port))
    channel.configureBlocking(false)
    channel.socket.setTcpNoDelay(true)
    connection = Some(new Connection(channel, parser, new Connection.Callback[Message] {
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
    }))
  }

  override def stop() {
    connection.foreach(_.close())
    connection = None
  }

  override def act() = connection match {
    case Some(connection) => act(connection)
    case None             => Unit
  }

  protected def onMessage(connection: Connection[Message], message: Message)

  protected def onIdle(connection: Connection[Message])
}
