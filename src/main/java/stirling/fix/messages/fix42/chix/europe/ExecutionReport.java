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
package stirling.fix.messages.fix42.chix.europe;

import stirling.fix.messages.MessageHeader;
import stirling.fix.messages.MessageVisitor;
import stirling.fix.messages.Required;

import stirling.fix.tags.fix42.Account;
import stirling.fix.tags.fix42.AvgPx;
import stirling.fix.tags.fix42.ClOrdID;
import stirling.fix.tags.fix42.ClientID;
import stirling.fix.tags.fix42.ContraBroker;
import stirling.fix.tags.fix42.CumQty;
import stirling.fix.tags.fix42.Currency;
import stirling.fix.tags.fix42.ExecID;
import stirling.fix.tags.fix42.ExecRefID;
import stirling.fix.tags.fix42.ExecTransType;
import stirling.fix.tags.fix42.ExecType;
import stirling.fix.tags.fix42.LastPx;
import stirling.fix.tags.fix42.LastShares;
import stirling.fix.tags.fix42.LeavesQty;
import stirling.fix.tags.fix42.MaxFloor;
import stirling.fix.tags.fix42.MinQty;
import stirling.fix.tags.fix42.NoContraBrokers;
import stirling.fix.tags.fix42.OrdStatus;
import stirling.fix.tags.fix42.OrdType;
import stirling.fix.tags.fix42.OrderCapacity;
import stirling.fix.tags.fix42.OrderID;
import stirling.fix.tags.fix42.OrderQty;
import stirling.fix.tags.fix42.OrigClOrdID;
import stirling.fix.tags.fix42.Price;
import stirling.fix.tags.fix42.SecurityID;
import stirling.fix.tags.fix42.Symbol;
import stirling.fix.tags.fix42.Text;
import stirling.fix.tags.fix42.TimeInForce;
import stirling.fix.tags.fix42.TradeDate;
import stirling.fix.tags.fix42.TransactTime;
import stirling.fix.tags.fix42.chix.europe.IDSource;
import stirling.fix.tags.fix42.chix.europe.TradeLiquidityIndicator;
import stirling.fix.tags.fix42.LastCapacity;
import stirling.fix.tags.fix42.Side;

public class ExecutionReport extends stirling.fix.messages.AbstractMessage implements stirling.fix.messages.ExecutionReport {
    public ExecutionReport(MessageHeader header) {
        super(header);

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
        field(TransactTime.Tag());
        field(TradeDate.Tag(), Required.NO);
        field(ClientID.Tag(), Required.NO);
        field(MinQty.Tag(), Required.NO);
        field(MaxFloor.Tag(), Required.NO);
        field(ExecType.Tag());
        field(LeavesQty.Tag());
        field(NoContraBrokers.Tag(), Required.NO);
        field(ContraBroker.Tag(), Required.NO);
        field(TradeLiquidityIndicator.Tag(), Required.NO);
    }

    @Override public void apply(MessageVisitor visitor) {
        visitor.visit(this);
    }
}
