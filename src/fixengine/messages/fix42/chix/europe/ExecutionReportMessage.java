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
package fixengine.messages.fix42.chix.europe;

import fixengine.messages.MessageHeader;
import fixengine.messages.Required;

import fixengine.tags.Account;
import fixengine.tags.AvgPx;
import fixengine.tags.ClOrdID;
import fixengine.tags.ContraBroker;
import fixengine.tags.CumQty;
import fixengine.tags.Currency;
import fixengine.tags.ExecID;
import fixengine.tags.ExecRefID;
import fixengine.tags.ExecType;
import fixengine.tags.LastCapacity;
import fixengine.tags.LastPx;
import fixengine.tags.LastShares;
import fixengine.tags.LeavesQty;
import fixengine.tags.MaxFloor;
import fixengine.tags.MinQty;
import fixengine.tags.NoContraBrokers;
import fixengine.tags.OrdStatus;
import fixengine.tags.OrdType;
import fixengine.tags.OrderCapacity;
import fixengine.tags.OrderID;
import fixengine.tags.OrderQty;
import fixengine.tags.OrigClOrdID;
import fixengine.tags.Price;
import fixengine.tags.SecurityID;
import fixengine.tags.fix42.Side;
import fixengine.tags.Symbol;
import fixengine.tags.Text;
import fixengine.tags.TimeInForce;
import fixengine.tags.TradeDate;
import fixengine.tags.TransactTime;

import fixengine.tags.fix42.ClientID;
import fixengine.tags.fix42.ExecTransType;
import fixengine.tags.fix42.chix.europe.IDSource;
import fixengine.tags.fix42.chix.europe.TradeLiquidityIndicator;

public class ExecutionReportMessage extends fixengine.messages.fix42.ExecutionReportMessage {
    public ExecutionReportMessage(MessageHeader header) {
        super(header);
    }

    @Override protected void fields() {
        field(Account.TAG, Required.NO);
        field(AvgPx.TAG);
        field(ClOrdID.TAG, Required.NO);
        field(CumQty.TAG);
        field(Currency.TAG, Required.NO);
        field(ExecID.TAG);
        field(ExecRefID.TAG, Required.NO);
        field(ExecTransType.TAG);
        field(IDSource.TAG, Required.NO);
        field(LastCapacity.TAG, Required.NO);
        field(LastPx.TAG);
        field(LastShares.TAG);
        field(OrderID.TAG);
        field(OrderQty.TAG);
        field(OrdStatus.TAG);
        field(OrdType.TAG, Required.NO);
        field(OrigClOrdID.TAG, Required.NO);
        field(Price.TAG, Required.NO);
        field(OrderCapacity.TAG, Required.NO);
        field(SecurityID.TAG, new Required() {
            @Override public boolean isRequired() {
                return hasValue(IDSource.TAG);
            }
        });
        field(Side.Tag());
        field(Symbol.TAG);
        field(Text.TAG, Required.NO);
        field(TimeInForce.TAG, Required.NO);
        field(TransactTime.TAG);
        field(TradeDate.TAG, Required.NO);
        field(ClientID.TAG, Required.NO);
        field(MinQty.TAG, Required.NO);
        field(MaxFloor.TAG, Required.NO);
        field(ExecType.TAG);
        field(LeavesQty.TAG);
        field(NoContraBrokers.TAG, Required.NO);
        field(ContraBroker.TAG, Required.NO);
        field(TradeLiquidityIndicator.TAG, Required.NO);
    }
}
