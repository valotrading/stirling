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
package stirling.itch.templates.itch186

import stirling.itch.fields.itch186.Fields
import stirling.itch.messages.itch186.MessageType

object Templates {
  /**
   * Template for Seconds message as specified in section 4.1.1.
   */
  val Seconds = new Template {
    val messageType = MessageType.Seconds
    val fields = Fields.Second :: Nil
  }

  /**
   * Template for Milliseconds message as specified in section 4.1.2.
   */
  val Milliseconds = new Template {
    val messageType = MessageType.Milliseconds
    val fields = Fields.Millisecond :: Nil
  }

  /**
   * Template for System Event message as specified in section 4.2.1.
   */
  val SystemEvent = new Template {
    val messageType = MessageType.SystemEvent
    val fields = Fields.EventCode :: Nil
  }

  /**
   * Template for Market Segment State message as specified in section 4.2.2.
   */
  val MarketSegmentState = new Template {
    val messageType = MessageType.MarketSegmentEvent
    val fields = Fields.MarketSegmentID :: Fields.EventCode :: Nil
  }

  /**
   * Template for Order Book Directory message as specified in section 4.3.1.
   */
  val OrderBookDirectory = new Template {
    val messageType = MessageType.OrderBookDirectory
    val fields = Fields.OrderBook :: Fields.Symbol :: Fields.ISIN :: Fields.FinancialProduct ::
      Fields.TradingCurrency :: Fields.MIC :: Fields.MarketSegmentID :: Fields.NoteCodes ::
      Fields.RoundLotSize :: Nil
  }

  /**
   * Template for Order Book Trading Action message as specified in section 4.3.2.
   */
  val OrderBookTradingAction = new Template {
    val messageType = MessageType.StockTradingAction
    val fields = Fields.OrderBook :: Fields.TradingState :: Fields.Reserved :: Fields.Reason :: Nil
  }

  /**
   * Template for Add Order message as specified in section 4.4.1.
   */
  val AddOrder = new Template {
    val messageType = MessageType.AddOrder
    val fields = Fields.OrderReferenceNumber :: Fields.BuyOrSellIndicator :: Fields.Quantity ::
      Fields.OrderBook :: Fields.Price :: Nil
  }

  /**
   * Template for Add Order message as specified in section 4.4.2.
   */
  val AddOrderMPID = new Template {
    val messageType = MessageType.AddOrderMPID
    val fields = AddOrder.fields :+ Fields.Attribution
  }

  /**
   * Template for Order Executed message as specified in section 4.5.1.
   */
  val OrderExecuted = new Template {
    val messageType = MessageType.OrderExecuted
    val fields = Fields.OrderReferenceNumber :: Fields.ExecutedQuantity :: Fields.MatchNumber ::
      Fields.Owner :: Fields.Counterparty :: Nil
  }

  /**
   * Template for Order Executed with Price message as specified in section 4.5.2.
   */
  val OrderExecutedWithPrice = new Template {
    val messageType = MessageType.OrderExecutedWithPrice
    val fields = Fields.OrderReferenceNumber :: Fields.ExecutedQuantity :: Fields.MatchNumber ::
      Fields.Printable :: Fields.TradePrice :: Fields.Owner :: Fields.Counterparty :: Nil
  }

  /**
   * Template for Order Cancel message as specified in section 4.5.3.
   */
  val OrderCancel = new Template {
    val messageType = MessageType.OrderCancel
    val fields = Fields.OrderReferenceNumber :: Fields.CanceledQuantity :: Nil
  }

  /**
   * Template for Order Delete message as specified in section 4.5.4.
   */
  val OrderDelete = new Template {
    val messageType = MessageType.OrderDelete
    val fields = Fields.OrderReferenceNumber :: Nil
  }

  /**
   * Template for Trade message as specified in section 4.6.1.
   */
  val Trade = new Template {
    val messageType = MessageType.Trade
    val fields = Fields.OrderReferenceNumber :: Fields.TradeType :: Fields.Quantity ::
      Fields.OrderBook :: Fields.MatchNumber :: Fields.TradePrice :: Fields.Buyer ::
      Fields.Seller :: Nil
  }

  /**
   * Template for Cross Trade message as specified in section 4.6.2.
   */
  val CrossTrade = new Template {
    val messageType = MessageType.CrossTrade
    val fields = Fields.Quantity :: Fields.OrderBook :: Fields.CrossPrice :: Fields.MatchNumber ::
      Fields.CrossType :: Fields.NumberOfTrades :: Nil
  }

  /**
   * Template for Broken Trade message as specified in section 4.7.
   */
  val BrokenTrade = new Template {
    val messageType = MessageType.BrokenTrade
    val fields = Fields.MatchNumber :: Nil
  }

  /**
   * Template for Net Order Imbalance Indicator message as specified in section 4.8.
   */
  val NOII = new Template {
    val messageType = MessageType.NOII
    val fields = Fields.PairedQuantity :: Fields.ImbalanceQuantity :: Fields.ImbalanceDirection ::
      Fields.OrderBook :: Fields.EquilibriumPrice :: Fields.CrossType :: Fields.BestBidPrice ::
      Fields.BestBidQuantity :: Fields.BestAskPrice :: Fields.BestAskQuantity :: Nil
  }
}
