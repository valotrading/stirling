/*
 * Copyright 2012 the original author or authors.
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
package stirling.itch.bats.top120

import stirling.itch.ByteString

sealed trait Message {
  def payload: ByteString

  def messageType = payload.byteAt(0)

  override def toString = payload.toString
}

sealed trait MessageType {
  def apply(payload: ByteString): Message

  def size: Int
}

/*
 * Section 4.2
 */
class LogonAccepted(val payload: ByteString) extends Message

object LogonAccepted extends MessageType {
  def apply(payload: ByteString) = new LogonAccepted(payload)

  val size = 2
}

/*
 * Section 4.3
 */
class LogonRejected(val payload: ByteString) extends Message {
  def rejectReason: Byte = payload.byteAt(1)
}

object LogonRejected extends MessageType {
  def apply(payload: ByteString) = new LogonRejected(payload)

  val size = 3
}

/*
 * Section 5.1.2
 */
class ExpandedSpin(val payload: ByteString) extends Message {
  def timestamp:        Long       = payload.slice(1, 8).toLong
  def symbol:           ByteString = payload.slice(9, 8)
  def bidPrice:         Long       = payload.slice(17, 10).toLong
  def bidQuantity:      Long       = payload.slice(27, 6).toLong
  def askPrice:         Long       = payload.slice(33, 10).toLong
  def askQuantity:      Long       = payload.slice(43, 6).toLong
  def lastTradeTime:    Long       = payload.slice(49, 8).toLong
  def lastTradePrice:   Long       = payload.slice(57, 10).toLong
  def lastTradeSize:    Long       = payload.slice(67, 6).toLong
  def cumulativeVolume: Long       = payload.slice(73, 9).toLong
  def haltStatus:       Byte       = payload.byteAt(82)
  def regShoAction:     Byte       = payload.byteAt(83)
  def reserved1:        Byte       = payload.byteAt(84)
  def reserved2:        Byte       = payload.byteAt(85)
}

object ExpandedSpin extends MessageType {
  def apply(payload: ByteString) = new ExpandedSpin(payload)

  val size = 87
}

/*
 * Section 5.2
 */
class SpinDone(val payload: ByteString) extends Message

object SpinDone extends MessageType {
  def apply(payload: ByteString) = new SpinDone(payload)

  val size = 2
}

/*
 * Section 6.1
 */
class ServerHeartbeat(val payload: ByteString) extends Message

object ServerHeartbeat extends MessageType {
  def apply(payload: ByteString) = new ServerHeartbeat(payload)

  val size = 2
}

/*
 * Section 7.1
 */
class Seconds(val payload: ByteString) extends Message {
  def seconds: Long = payload.slice(1, 5).toLong
}

object Seconds extends MessageType {
  def apply(payload: ByteString) = new Seconds(payload)

  val size = 7
}

/*
 * Section 7.2
 */
class Milliseconds(val payload: ByteString) extends Message {
  def milliseconds: Long = payload.slice(1, 3).toLong
}

object Milliseconds extends MessageType {
  def apply(payload: ByteString) = new Milliseconds(payload)

  val size = 5
}

/*
 * Section 8.1.1
 */
class ExpandedBidUpdate(val payload: ByteString) extends Message {
  def symbol:      ByteString = payload.slice(1, 8)
  def bidPrice:    Long       = payload.slice(9, 10).toLong
  def bidQuantity: Long       = payload.slice(19, 6).toLong
}

object ExpandedBidUpdate extends MessageType {
  def apply(payload: ByteString) = new ExpandedBidUpdate(payload)

  val size = 26
}

/*
 * Section 8.1.2
 */
class LongBidUpdate(val payload: ByteString) extends Message {
  def symbol:      ByteString = payload.slice(1, 6)
  def bidPrice:    Long       = payload.slice(7, 10).toLong
  def bidQuantity: Long       = payload.slice(17, 6).toLong
}

object LongBidUpdate extends MessageType {
  def apply(payload: ByteString) = new LongBidUpdate(payload)

  val size = 24
}

/*
 * Section 8.1.3
 */
class ShortBidUpdate(val payload: ByteString) extends Message {
  def symbol:      ByteString = payload.slice(1, 4)
  def bidPrice:    Long       = payload.slice(5, 5).toLong
  def bidQuantity: Long       = payload.slice(10, 5).toLong
}

object ShortBidUpdate extends MessageType {
  def apply(payload: ByteString) = new ShortBidUpdate(payload)

  val size = 16
}

/*
 * Section 8.1.4
 */
class ExpandedAskUpdate(val payload: ByteString) extends Message {
  def symbol:      ByteString = payload.slice(1, 8)
  def askPrice:    Long       = payload.slice(9, 10).toLong
  def askQuantity: Long       = payload.slice(19, 6).toLong
}

object ExpandedAskUpdate extends MessageType {
  def apply(payload: ByteString) = new ExpandedAskUpdate(payload)

  val size = 26
}

/*
 * Section 8.1.5
 */
class LongAskUpdate(val payload: ByteString) extends Message {
  def symbol:      ByteString = payload.slice(1, 6)
  def askPrice:    Long       = payload.slice(7, 10).toLong
  def askQuantity: Long       = payload.slice(17, 6).toLong
}

object LongAskUpdate extends MessageType {
  def apply(payload: ByteString) = new LongAskUpdate(payload)

  val size = 24
}

/*
 * Section 8.1.6
 */
class ShortAskUpdate(val payload: ByteString) extends Message {
  def symbol:      ByteString = payload.slice(1, 4)
  def askPrice:    Long       = payload.slice(5, 5).toLong
  def askQuantity: Long       = payload.slice(10, 5).toLong
}

object ShortAskUpdate extends MessageType {
  def apply(payload: ByteString) = new ShortAskUpdate(payload)

  val size = 16
}

/*
 * Section 8.2.1
 */
class ExpandedTwoSidedUpdate(val payload: ByteString) extends Message {
  def symbol:      ByteString = payload.slice(1, 8)
  def bidPrice:    Long       = payload.slice(9, 10).toLong
  def bidQuantity: Long       = payload.slice(19, 6).toLong
  def askPrice:    Long       = payload.slice(25, 10).toLong
  def askQuantity: Long       = payload.slice(35, 6).toLong
}

object ExpandedTwoSidedUpdate extends MessageType {
  def apply(payload: ByteString) = new ExpandedTwoSidedUpdate(payload)

  val size = 42
}

/*
 * Section 8.2.2
 */
class LongTwoSidedUpdate(val payload: ByteString) extends Message {
  def symbol:      ByteString = payload.slice(1, 6)
  def bidPrice:    Long       = payload.slice(7, 10).toLong
  def bidQuantity: Long       = payload.slice(17, 6).toLong
  def askPrice:    Long       = payload.slice(23, 10).toLong
  def askQuantity: Long       = payload.slice(33, 6).toLong
}

object LongTwoSidedUpdate extends MessageType {
  def apply(payload: ByteString) = new LongTwoSidedUpdate(payload)

  val size = 40
}

/*
 * Section 8.2.3
 */
class ShortTwoSidedUpdate(val payload: ByteString) extends Message {
  def symbol:      ByteString = payload.slice(1, 4)
  def bidPrice:    Long       = payload.slice(5, 5).toLong
  def bidQuantity: Long       = payload.slice(10, 5).toLong
  def askPrice:    Long       = payload.slice(15, 5).toLong
  def askQuantity: Long       = payload.slice(20, 5).toLong
}

object ShortTwoSidedUpdate extends MessageType {
  def apply(payload: ByteString) = new ShortTwoSidedUpdate(payload)

  val size = 26
}

/*
 * Section 9.1
 */
class ExpandedTrade(val payload: ByteString) extends Message {
  def symbol:           ByteString = payload.slice(1, 8)
  def lastPrice:        Long       = payload.slice(9, 10).toLong
  def lastQuantity:     Long       = payload.slice(19, 6).toLong
  def cumulativeVolume: Long       = payload.slice(25, 9).toLong
}

object ExpandedTrade extends MessageType {
  def apply(payload: ByteString) = new ExpandedTrade(payload)

  val size = 35
}

/*
 * Section 9.2
 */
class LongTrade(val payload: ByteString) extends Message {
  def symbol:           ByteString = payload.slice(1, 6)
  def lastPrice:        Long       = payload.slice(7, 10).toLong
  def lastQuantity:     Long       = payload.slice(17, 6).toLong
  def cumulativeVolume: Long       = payload.slice(23, 9).toLong
}

object LongTrade extends MessageType {
  def apply(payload: ByteString) = new LongTrade(payload)

  val size = 33
}

/*
 * Section 9.3
 */
class ShortTrade(val payload: ByteString) extends Message {
  def symbol:           ByteString = payload.slice(1, 4)
  def lastPrice:        Long       = payload.slice(5, 5).toLong
  def lastQuantity:     Long       = payload.slice(10, 5).toLong
  def cumulativeVolume: Long       = payload.slice(15, 7).toLong
}

object ShortTrade extends MessageType {
  def apply(payload: ByteString) = new ShortTrade(payload)

  val size = 23
}

/*
 * Section 10.1
 */
class TradingStatus(val payload: ByteString) extends Message {
  def symbol:       ByteString = payload.slice(1, 8)
  def haltStatus:   Byte       = payload.byteAt(9)
  def regShoAction: Byte       = payload.byteAt(10)
  def reserved1:    Byte       = payload.byteAt(11)
  def reserved2:    Byte       = payload.byteAt(12)
}

object TradingStatus extends MessageType {
  def apply(payload: ByteString) = new TradingStatus(payload)

  val size = 14
}
