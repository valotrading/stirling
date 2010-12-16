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
import fixengine.tags.fix42.LastCapacity;
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
import fixengine.tags.fix42.TimeInForce;
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
        field(Account.Tag(), Required.NO);
        field(AvgPx.Tag());
        field(ClOrdID.Tag(), Required.NO);
        field(CumQty.Tag());
        field(Currency.Tag(), Required.NO);
        field(ExecID.Tag());
        field(ExecRefID.Tag(), Required.NO);
        field(ExecTransType.Tag());
        field(IDSource.Tag(), Required.NO);
        field(LastCapacity.Tag(), Required.NO);
        field(LastPx.Tag());
        field(LastShares.Tag());
        field(OrderID.Tag());
        field(OrderQty.Tag());
        field(OrdStatus.Tag());
        field(OrdType.Tag(), Required.NO);
        field(OrigClOrdID.Tag(), Required.NO);
        field(Price.Tag(), Required.NO);
        field(OrderCapacity.Tag(), Required.NO);
        field(SecurityID.Tag(), new Required() {
            @Override public boolean isRequired() {
                return hasValue(IDSource.Tag());
            }
        });
        field(Side.Tag());
        field(Symbol.Tag());
        field(Text.Tag(), Required.NO);
        field(TimeInForce.Tag(), Required.NO);
        field(TransactTime.TAG);
        field(TradeDate.Tag(), Required.NO);
        field(ClientID.TAG, Required.NO);
        field(MinQty.Tag(), Required.NO);
        field(MaxFloor.Tag(), Required.NO);
        field(ExecType.Tag());
        field(LeavesQty.Tag());
        field(NoContraBrokers.Tag(), Required.NO);
        field(ContraBroker.Tag(), Required.NO);
        field(TradeLiquidityIndicator.Tag(), Required.NO);
    }
}
