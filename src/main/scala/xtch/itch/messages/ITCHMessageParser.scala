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
package xtch.itch.messages

import java.nio.{BufferUnderflowException, ByteBuffer}
import silvertip.{GarbledMessageException, MessageParser, PartialMessageException}
import xtch.itch.templates.Templates

object ITCHMessageParser extends MessageParser[ITCHMessage] {
  val templates = Map(MessageType.Seconds -> Templates.Seconds)
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
  def decodeMessageType(buffer: ByteBuffer) = buffer.get.toChar.toString
  def decodeTerminator(buffer: ByteBuffer) {
    val terminator: Seq[Byte] = new Array[Byte](ITCHMessage.terminator.length)
    buffer.get(terminator.toArray)
    if (terminator != Seq(ITCHMessage.terminator: _*))
      throw new GarbledMessageException("Expected terminator")
  }
  def parse(buffer: ByteBuffer) = decode(buffer)
}
