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
package stirling.nasdaqomx

import stirling.fix.tags._

package object fix {
  val AvgPx            = fix42.AvgPx
  val ClOrdID          = fix42.ClOrdID
  val ClearingAccount  = fix42.ClearingAccount
  val ClearingFirm     = fix42.ClearingFirm
  val ClientID         = fix42.ClientID
  val ContraBroker     = fix42.ContraBroker
  val CumQty           = fix42.CumQty
  val Currency         = fix42.Currency
  val CxlRejReason     = fix42.CxlRejReason
  val CxlRejResponseTo = fix42.CxlRejResponseTo
  val EncryptMethod    = fix42.EncryptMethod
  val ExecBroker       = fix42.ExecBroker
  val ExecID           = fix42.ExecID
  val ExecInst         = fix42.ExecInst
  val ExecRefID        = fix42.ExecRefID
  val ExecTransType    = fix42.ExecTransType
  val ExecType         = fix42.ExecType
  val ExpireTime       = fix42.ExpireTime
  val HandlInst        = fix42.HandlInst
  val HeartBtInt       = fix42.HeartBtInt
  val IDSource         = fix42.IDSource
  val LastMkt          = fix42.LastMkt
  val LastPx           = fix42.LastPx
  val LastShares       = fix42.LastShares
  val LeavesQty        = fix42.LeavesQty
  val MaxFloor         = fix42.MaxFloor
  val MinQty           = fix42.MinQty
  val NoContraBrokers  = fix42.NoContraBrokers
  val OrdRejReason     = fix42.OrdRejReason
  val OrdStatus        = fix42.OrdStatus
  val OrderID          = fix42.OrderID
  val OrigClOrdID      = fix42.OrigClOrdID
  val PegDifference    = fix42.PegDifference
  val Price            = fix42.Price
  val RefMsgType       = fix42.RefMsgType
  val RefSeqNo         = fix42.RefSeqNo
  val RefTagId         = fix42.RefTagId
  val SecondaryOrderID = fix42.SecondaryOrderID
  val SecurityID       = fix42.SecurityID
  val Side             = fix42.Side
  val Symbol           = fix42.Symbol
  val Text             = fix42.Text
  val TimeInForce      = fix42.TimeInForce
  val TradeDate        = fix42.TradeDate
  val TransactTime     = fix42.TransactTime

  val ExecRestatementReason = fix43.ExecRestatementReason
  val OrderCapacity         = fix43.OrderCapacity
  val SecondaryExecID       = fix43.SecondaryExecID
  val SessionRejectReason   = fix43.SessionRejectReason
}
