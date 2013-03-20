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
package stirling.bats.pitch1122

import stirling.bats.pitch1120
import stirling.bats.pitch1120.{Commons, MessageType}
import stirling.io.ByteString

sealed trait Message {
  def messageType: Byte
}

/*
 * Section 4.2
 */
class SymbolClear(val payload: ByteString) extends Message with Commons {
  def stockSymbol: ByteString = payload.slice(9, 8)
}

object SymbolClear extends MessageType[Message] {
  def apply(payload: ByteString) = new SymbolClear(payload)

  val size = 17
}

/*
 * Section 4.3
 */
class AddOrderShort(payload: ByteString) extends pitch1120.AddOrderShort(payload) with Message

object AddOrderShort extends MessageType[Message] {
  def apply(payload: ByteString) = new AddOrderShort(payload)

  val size = pitch1120.AddOrderShort.size
}

/*
 * Section 4.3
 */
class AddOrderLong(payload: ByteString) extends pitch1120.AddOrderLong(payload) with Message

object AddOrderLong extends MessageType[Message] {
  def apply(payload: ByteString) = new AddOrderLong(payload)

  val size = pitch1120.AddOrderLong.size
}

/*
 * Section 4.4.1
 */
class OrderExecuted(payload: ByteString) extends pitch1120.OrderExecuted(payload) with Message

object OrderExecuted extends MessageType[Message] {
  def apply(payload: ByteString) = new OrderExecuted(payload)

  val size = pitch1120.OrderExecuted.size
}

/*
 * Section 4.4.2
 */
class OrderCancel(payload: ByteString) extends pitch1120.OrderCancel(payload) with Message

object OrderCancel extends MessageType[Message] {
  def apply(payload: ByteString) = new OrderCancel(payload)

  val size = pitch1120.OrderCancel.size
}

/*
 * Section 4.5
 */
class TradeShort(payload: ByteString) extends pitch1120.TradeShort(payload) with Message

object TradeShort extends MessageType[Message] {
  def apply(payload: ByteString) = new TradeShort(payload)

  val size = pitch1120.TradeShort.size
}

/*
 * Section 4.5
 */
class TradeLong(payload: ByteString) extends pitch1120.TradeLong(payload) with Message

object TradeLong extends MessageType[Message] {
  def apply(payload: ByteString) = new TradeLong(payload)

  val size = pitch1120.TradeLong.size
}

/*
 * Section 4.6
 */
class TradeBreak(payload: ByteString) extends pitch1120.TradeBreak(payload) with Message

object TradeBreak extends MessageType[Message] {
  def apply(payload: ByteString) = new TradeBreak(payload)

  val size = pitch1120.TradeBreak.size
}

/*
 * Section 4.7
 */
class TradingStatus(payload: ByteString) extends pitch1120.TradingStatus(payload) with Message

object TradingStatus extends MessageType[Message] {
  def apply(payload: ByteString) = new TradingStatus(payload)

  val size = pitch1120.TradingStatus.size
}

/*
 * Section 4.8
 */
class AuctionUpdate(payload: ByteString) extends pitch1120.AuctionUpdate(payload) with Message

object AuctionUpdate extends MessageType[Message] {
  def apply(payload: ByteString) = new AuctionUpdate(payload)

  val size = pitch1120.AuctionUpdate.size
}

/*
 * Section 4.9
 */
class AuctionSummary(payload: ByteString) extends pitch1120.AuctionSummary(payload) with Message

object AuctionSummary extends MessageType[Message] {
  def apply(payload: ByteString) = new AuctionSummary(payload)

  val size = pitch1120.AuctionSummary.size
}

/*
 * Section 4.10
 */
class RetailPriceImprovement(payload: ByteString) extends pitch1120.RetailPriceImprovement(payload) with Message

object RetailPriceImprovement extends MessageType[Message] {
  def apply(payload: ByteString) = new RetailPriceImprovement(payload)

  val size = pitch1120.RetailPriceImprovement.size
}
