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
import stirling.io.{ByteBuffers, TextFormat}

sealed trait Packet

sealed trait PacketType {
  def parse(buffer: ByteBuffer): Packet

  protected val LF: Byte = 0x0A

  protected def alpha(value: String, length: Int) = TextFormat.alphaPadLeft(value, length, ' ')

  protected def alpha(value: String) = value.getBytes(TextFormat.ASCII)

  protected def numeric(value: Long, length: Int) = TextFormat.numericPadLeft(value, length, ' ')
}

/*
 * 2.1
 */
case class Debug(
  text: String
) extends Packet

object Debug extends PacketType {
  val minLength = 2

  def parse(buffer: ByteBuffer) = Debug(
    text = ByteBuffers.getAlpha(buffer)
  )

  def format(
    buffer: ByteBuffer,
    text:   String
  ) {
    buffer.put('+'.toByte)
    buffer.put(alpha(text))
    buffer.put(LF)
  }
}

/*
 * 2.2.1
 */
case class LoginAccepted(
  session:        String,
  sequenceNumber: Long
) extends Packet

object LoginAccepted extends PacketType {
  val length = 22

  def parse(buffer: ByteBuffer) = LoginAccepted(
    session        = ByteBuffers.getAlpha(buffer, 10).trim,
    sequenceNumber = ByteBuffers.getAlpha(buffer, 10).trim.toLong
  )

  def format(
    buffer:         ByteBuffer,
    session:        String,
    sequenceNumber: Long
  ) {
    buffer.put('A'.toByte)
    buffer.put(alpha(session, 10))
    buffer.put(numeric(sequenceNumber, 10))
    buffer.put(LF)
  }
}

/*
 * 2.2.2
 */
case class LoginRejected(
  rejectReasonCode: Byte
) extends Packet

object LoginRejected extends PacketType {
  val length = 3

  def parse(buffer: ByteBuffer) = LoginRejected(
    rejectReasonCode = buffer.get()
  )

  def format(
    buffer:           ByteBuffer,
    rejectReasonCode: Byte
  ) {
    buffer.put('J'.toByte)
    buffer.put(rejectReasonCode)
    buffer.put(LF)
  }
}

/*
 * 2.2.3
 */
case class SequencedData(
  message: Array[Byte]
) extends Packet

object SequencedData extends PacketType {
  val minLength = 2

  def parse(buffer: ByteBuffer) = SequencedData(
    message = ByteBuffers.unwrap(buffer)
  )

  def format(buffer: ByteBuffer)(formatter: (ByteBuffer) => Unit) {
    buffer.put('S'.toByte)
    formatter(buffer)
    buffer.put(LF)
  }
}

/*
 * 2.2.4
 */
case object ServerHeartbeat extends Packet with PacketType {
  val length = 2

  def parse(buffer: ByteBuffer) = ServerHeartbeat

  def format(buffer: ByteBuffer) {
    buffer.put('H'.toByte)
    buffer.put(LF)
  }
}

/*
 * 2.3.1
 */
case class LoginRequest(
  username:                String,
  password:                String,
  requestedSession:        String,
  requestedSequenceNumber: Long
) extends Packet

object LoginRequest extends PacketType {
  val length = 38

  def parse(buffer: ByteBuffer) = LoginRequest(
    username                = ByteBuffers.getAlpha(buffer,  6).trim,
    password                = ByteBuffers.getAlpha(buffer, 10).trim,
    requestedSession        = ByteBuffers.getAlpha(buffer, 10).trim,
    requestedSequenceNumber = ByteBuffers.getAlpha(buffer, 10).trim.toLong
  )

  def format(
    buffer:                  ByteBuffer,
    username:                String,
    password:                String,
    requestedSession:        String,
    requestedSequenceNumber: Long
  ) {
    buffer.put('L'.toByte)
    buffer.put(alpha(username, 6))
    buffer.put(alpha(password, 10))
    buffer.put(alpha(requestedSession, 10))
    buffer.put(numeric(requestedSequenceNumber, 10))
    buffer.put(LF)
  }
}

/*
 * 2.3.2
 */
case class UnsequencedData(
  message: Array[Byte]
) extends Packet

object UnsequencedData extends PacketType {
  val minLength = 2

  def parse(buffer: ByteBuffer) = UnsequencedData(
    message = ByteBuffers.unwrap(buffer)
  )

  def format(buffer: ByteBuffer)(formatter: (ByteBuffer) => Unit) {
    buffer.put('U'.toByte)
    formatter(buffer)
    buffer.put(LF)
  }
}

/*
 * 2.3.3
 */
case object ClientHeartbeat extends Packet with PacketType {
  val length = 2

  def parse(buffer: ByteBuffer) = ClientHeartbeat

  def format(buffer: ByteBuffer) {
    buffer.put('R'.toByte)
    buffer.put(LF)
  }
}

/*
 * 2.3.4
 */
case object LogoutRequest extends Packet with PacketType {
  val length = 2

  def parse(buffer: ByteBuffer) = LogoutRequest

  def format(buffer: ByteBuffer) {
    buffer.put('O'.toByte)
    buffer.put(LF)
  }
}
