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
package stirling.itch.messages.itch41

import java.nio.ByteBuffer

class Message

/**
 * Time Stamp - Seconds message as specified in section 4.1.
 */
case class Seconds(seconds: Int) extends Message

/**
 * System Event message as specified in section 4.2.
 */
case class SystemEvent(
  nanoSeconds:       Int,
  systemMessageType: SystemMessageType
) extends Message

/**
 * Stock Directory message as specified in section 4.3.1.
 */
case class StockDirectory(
  nanoSeconds:              Int,
  stock:                    ByteBuffer,
  marketCategory:           MarketCategory,
  financialStatusIndicator: FinancialStatusIndicator,
  roundLotSize:             Int,
  roundLotsOnly:            Boolean
) extends Message

/**
 * Stock Trading Action message as specified in section 4.3.2.
 */
case class StockTradingAction(
  nanoSeconds:  Int,
  stock:        ByteBuffer,
  tradingState: TradingState,
  reserved:     Byte,
  reason:       ByteBuffer
) extends Message

/**
 * Reg SHO Short Sale Price Test Restriction Indicator message as specified in section 4.3.3.
 */
case class RegSHOShortSalePriceTestRestrictedIndicator(
  nanoSeconds: Int,
  stock:       ByteBuffer,
  shoAction:   RegSHOAction
) extends Message

/**
 * Market Participant Position message as specified in section 4.3.4.
 */
case class MarketParticipantPosition(
  nanoSeconds: Int,
  mpid:        ByteBuffer,
  stock:       ByteBuffer,
  isPrimary:   Boolean,
  mode:        MarketMakerMode,
  status:      MarketParticipantState
) extends Message

/**
 * Add Order message as specified in section 4.3.5.
 */
case class AddOrder(
  nanoSeconds:      Int,
  referenceNumber:  Long,
  buySellIndicator: BuySellIndicator,
  shares:           Int,
  stock:            ByteBuffer,
  price:            Int,
  attribution:      Option[ByteBuffer] = None
) extends Message

/**
 * Order Executed message as specified in section 4.3.6.
 */
case class OrderExecuted(
  nanoSeconds:     Int,
  referenceNumber: Long,
  executedShares:  Int,
  matchNumber:     Long,
  printable:       Option[Boolean] = None,
  price:           Option[Int] = None
) extends Message

/**
 * Order Executed message as specified in section 4.3.7.
 */
case class OrderCancel(
  nanoSeconds:     Int,
  referenceNumber: Long,
  canceledShares:  Int
) extends Message

/**
 * Order Executed message as specified in section 4.3.8.
 */
case class OrderDelete(
  nanoSeconds:     Int,
  referenceNumber: Long
) extends Message

/**
 * Order Executed message as specified in section 4.3.9.
 */
case class OrderReplace(
  nanoSeconds:             Int,
  originalReferenceNumber: Long,
  newReferenceNumber:      Long,
  shares:                  Int,
  price:                   Int
) extends Message

/**
 * Order Executed message as specified in section 4.3.10.
 */
case class Trade(
  nanoSeconds:          Int,
  orderReferenceNumber: Long,
  buySellIndicator:     BuySellIndicator,
  shares:               Int,
  stock:                ByteBuffer,
  price:                Int,
  matchNumber:          Long
) extends Message

/**
 * Order Executed message as specified in section 4.3.10.
 */
case class CrossTrade(
  nanoSeconds: Int,
  shares:      Long,
  stock:       ByteBuffer,
  crossPrice:  Int,
  matchNumber: Long,
  crossType:   CrossType
) extends Message

/**
 * Order Executed message as specified in section 4.3.10.
 */
case class BrokenTrade(
  nanoSeconds: Int,
  matchNumber: Long
) extends Message

/**
 * Order Executed message as specified in section 4.3.10.
 */
case class NetOrderImbalanceIndicator(
  nanoSeconds:            Int,
  pairedShares:           Long,
  imbalance:              Long,
  imbalanceDirection:     ImbalanceDirection,
  stock:                  ByteBuffer,
  farPrice:               Int,
  nearPrice:              Int,
  currentReferencePrice:  Int,
  crossType:              CrossType,
  priceVarianceIndicator: PriceVarianceIndicator
) extends Message
