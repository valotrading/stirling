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
package stirling.itch.messages.itch186

object MessageType {
  /**
   * Time messages as specified in section 4.1 of [1].
   */
  val Seconds = 'T'
  val Milliseconds = 'M'

  /**
   * Event and state change messages as specified in section 4.2 of [1].
   */
  val SystemEvent = 'S'
  val MarketSegmentEvent = 'O'

  /**
   * Stock related messages as specified in section 4.3 of [1].
   */
  val OrderBookDirectory = 'R'
  val StockTradingAction = 'H'

  /**
   * Add order messages as specified in section 4.4 of [1].
   */
  val AddOrder = 'A'
  val AddOrderMPID = 'F'

  /**
   * Modify order messages as specified in section 4.5 of [1].
   */
  val OrderExecuted = 'E'
  val OrderExecutedWithPrice = 'C'
  val OrderCancel = 'X'
  val OrderDelete = 'D'

  /**
   * Trade messages as specified in section 4.6 of [1].
   */
  val Trade = 'P'
  val CrossTrade = 'Q'

  /**
   * Broken Trade/Order Execution message as specified in 4.7 of [1].
   */
  val BrokenTrade = 'B'

  /**
   * Net Order Imbalance Indicator as specified in 4.8 of [1].
   */
  val NOII = 'I'
}
