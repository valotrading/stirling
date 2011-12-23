/*
 * Copyright 2010 the original author or authors.
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
package stirling.itch.messages

import java.nio.{BufferUnderflowException, ByteBuffer}
import scala.annotation.tailrec
import silvertip.{GarbledMessageException, MessageParser, PartialMessageException}
import stirling.itch.elements.ASCII
import stirling.itch.templates.Templates

object ITCHFileParser extends MessageParser[ITCHMessage] with ASCII {
  val terminator = "\r\n".getBytes(charset)
  val templates = Map(
    MessageType.AddOrder -> Templates.AddOrder,
    MessageType.AddOrderMPID -> Templates.AddOrderMPID,
    MessageType.BrokenTrade -> Templates.BrokenTrade,
    MessageType.CrossTrade -> Templates.CrossTrade,
    MessageType.MarketSegmentEvent -> Templates.MarketSegmentState,
    MessageType.Milliseconds -> Templates.Milliseconds,
    MessageType.NOII -> Templates.NOII,
    MessageType.OrderBookDirectory -> Templates.OrderBookDirectory,
    MessageType.OrderCancel -> Templates.OrderCancel,
    MessageType.OrderDelete -> Templates.OrderDelete,
    MessageType.OrderExecuted -> Templates.OrderExecuted,
    MessageType.OrderExecutedWithPrice -> Templates.OrderExecutedWithPrice,
    MessageType.Seconds -> Templates.Seconds,
    MessageType.StockTradingAction -> Templates.OrderBookTradingAction,
    MessageType.SystemEvent -> Templates.SystemEvent,
    MessageType.Trade -> Templates.Trade
  )
  def decode(buffer: ByteBuffer) = {
    try {
      val messageType = decodeMessageType(buffer)
      templates.get(messageType) match {
        case Some(template) => {
          val message = template.decode(buffer)
          decodeTerminator(buffer)
          message
        }
        case None => throw new GarbledMessageException("Unknown message type %s".format(messageType))
      }
    } catch {
      case _: BufferUnderflowException => throw new PartialMessageException
    }
  }
  @tailrec def decodeMessageType(buffer: ByteBuffer): String = {
    val messageType = buffer.get.toChar
    if (messageType != terminator.head)
      messageType.toString
    else {
      decodeTerminator(buffer, terminator.tail)
      decodeMessageType(buffer)
    }
  }
  def decodeTerminator(buffer: ByteBuffer) {
    decodeTerminator(buffer, terminator)
  }
  def decodeTerminator(buffer: ByteBuffer, terminator: Seq[Byte]) {
    val bytes: Seq[Byte] = new Array[Byte](terminator.length)
    buffer.get(bytes.toArray)
    if (bytes != terminator)
      throw new GarbledMessageException("Expected terminator")
  }
  def parse(buffer: ByteBuffer) = decode(buffer)
}
