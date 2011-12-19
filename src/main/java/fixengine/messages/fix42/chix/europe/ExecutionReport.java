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
import fixengine.messages.MessageVisitor;
import fixengine.messages.Required;

import fixengine.tags.fix42.Account;
import fixengine.tags.fix42.AvgPx;
import fixengine.tags.fix42.ClOrdID;
import fixengine.tags.fix42.ClientID;
import fixengine.tags.fix42.ContraBroker;
import fixengine.tags.fix42.CumQty;
import fixengine.tags.fix42.Currency;
import fixengine.tags.fix42.ExecID;
import fixengine.tags.fix42.ExecRefID;
import fixengine.tags.fix42.ExecTransType;
import fixengine.tags.fix42.ExecType;
import fixengine.tags.fix42.LastPx;
import fixengine.tags.fix42.LastShares;
import fixengine.tags.fix42.LeavesQty;
import fixengine.tags.fix42.MaxFloor;
import fixengine.tags.fix42.MinQty;
import fixengine.tags.fix42.NoContraBrokers;
import fixengine.tags.fix42.OrdStatus;
import fixengine.tags.fix42.OrdType;
import fixengine.tags.fix42.OrderCapacity;
import fixengine.tags.fix42.OrderID;
import fixengine.tags.fix42.OrderQty;
import fixengine.tags.fix42.OrigClOrdID;
import fixengine.tags.fix42.Price;
import fixengine.tags.fix42.SecurityID;
import fixengine.tags.fix42.Symbol;
import fixengine.tags.fix42.Text;
import fixengine.tags.fix42.TimeInForce;
import fixengine.tags.fix42.TradeDate;
import fixengine.tags.fix42.TransactTime;
import fixengine.tags.fix42.chix.europe.IDSource;
import fixengine.tags.fix42.chix.europe.TradeLiquidityIndicator;
import fixengine.tags.fix42.LastCapacity;
import fixengine.tags.fix42.Side;

public class ExecutionReport extends fixengine.messages.AbstractMessage implements fixengine.messages.ExecutionReport {
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
