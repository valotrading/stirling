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
package xtch.itch.templates

import xtch.itch.elements.Fields
import xtch.itch.messages.MessageType

object Templates {
  /**
   * Template for Seconds message as specified in section 4.1.1 of [3].
   */
  val Seconds = new AbstractTemplate {
    val messageType = MessageType.Seconds
    val fields = Fields.Second :: Nil
  }

  /**
   * Template for Milliseconds message as specified in section 4.1.2 of [3].
   */
  val Milliseconds = new AbstractTemplate {
    val messageType = MessageType.Milliseconds
    val fields = Fields.Millisecond :: Nil
  }

  /**
   * Template for System Event message as specified in section 4.2.1 of [3].
   */
  val SystemEvent = new AbstractTemplate {
    val messageType = MessageType.SystemEvent
    val fields = Fields.EventCode :: Nil
  }

  /**
   * Template for Market Segment State message as specified in section 4.2.2 of [3].
   */
  val MarketSegmentState = new AbstractTemplate {
    val messageType = MessageType.MarketSegmentEvent
    val fields = Fields.MarketSegmentID :: Fields.EventCode :: Nil
  }

  /**
   * Template for Order Book Directory message as specified in section 4.3.1 of [3].
   */
  val OrderBookDirectory = new AbstractTemplate {
    val messageType = MessageType.OrderBookDirectory
    val fields = Fields.OrderBook :: Fields.Symbol :: Fields.ISIN :: Fields.FinancialProduct ::
      Fields.TradingCurrency :: Fields.MIC :: Fields.MarketSegmentID :: Fields.NoteCodes ::
      Fields.RoundLotSize :: Nil
  }

  /**
   * Template for Order Book Trading Action message as specified in section 4.3.2 of [3].
   */
  val OrderBookTradingAction = new AbstractTemplate {
    val messageType = MessageType.StockTradingAction
    val fields = Fields.OrderBook :: Fields.TradingState :: Fields.Reserved :: Fields.Reason :: Nil
  }

  /**
   * Template for Add Order message as specified in section 4.4.1 of [3].
   */
  val AddOrder = new AbstractTemplate {
    val messageType = MessageType.AddOrder
    val fields = Fields.OrderReferenceNumber :: Fields.BuyOrSellIndicator :: Fields.Quantity ::
      Fields.OrderBook :: Fields.Price :: Nil
  }

  /**
   * Template for Add Order message as specified in section 4.4.2 of [3].
   */
  val AddOrderMPID = new AbstractTemplate {
    val messageType = MessageType.AddOrderMPID
    val fields = AddOrder.fields :+ Fields.Attribution
  }

  /**
   * Template for Order Executed message as specified in section 4.5.1 of [3].
   */
  val OrderExecuted = new AbstractTemplate {
    val messageType = MessageType.OrderExecuted
    val fields = Fields.OrderReferenceNumber :: Fields.ExecutedQuantity :: Fields.MatchNumber ::
      Fields.Owner :: Fields.Counterparty :: Nil
  }

  /**
   * Template for Order Executed with Price message as specified in section 4.5.2 of [3].
   */
  val OrderExecutedWithPrice = new AbstractTemplate {
    val messageType = MessageType.OrderExecutedWithPrice
    val fields = Fields.OrderReferenceNumber :: Fields.ExecutedQuantity :: Fields.MatchNumber ::
      Fields.Printable :: Fields.TradePrice :: Fields.Owner :: Fields.Counterparty :: Nil
  }
}
