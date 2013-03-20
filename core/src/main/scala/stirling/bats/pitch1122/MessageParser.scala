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

import scala.annotation.switch
import silvertip.GarbledMessageException
import stirling.bats.pitch1120
import stirling.io.ByteBuffers

object MessageParser extends pitch1120.MessageParser[Message] {
  override protected def messageType(messageType: Byte) = (messageType: @switch) match {
    case 's' => SymbolClear
    case 'A' => AddOrderShort
    case 'd' => AddOrderLong
    case 'E' => OrderExecuted
    case 'X' => OrderCancel
    case 'P' => TradeShort
    case 'r' => TradeLong
    case 'B' => TradeBreak
    case 'H' => TradingStatus
    case 'I' => AuctionUpdate
    case 'J' => AuctionSummary
    case 'R' => RetailPriceImprovement
    case  x  => throw new GarbledMessageException("Unknown message type: " + x)
  }
}
