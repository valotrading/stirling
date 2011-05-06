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
package fixengine.messages.fix42.hotspotfx;

import fixengine.messages.MessageHeader;
import fixengine.messages.Required;
import fixengine.tags.fix42.Account;
import fixengine.tags.fix42.AvgPx;
import fixengine.tags.fix42.ClOrdID;
import fixengine.tags.fix42.ContraBroker;
import fixengine.tags.fix42.CumQty;
import fixengine.tags.fix42.Currency;
import fixengine.tags.fix42.ExecBroker;
import fixengine.tags.fix42.ExecID;
import fixengine.tags.fix42.ExecInst;
import fixengine.tags.fix42.ExecTransType;
import fixengine.tags.fix42.ExecType;
import fixengine.tags.fix42.ExpireTime;
import fixengine.tags.fix42.FutSettDate;
import fixengine.tags.fix42.LastPx;
import fixengine.tags.fix42.LastShares;
import fixengine.tags.fix42.LeavesQty;
import fixengine.tags.fix42.MaxShow;
import fixengine.tags.fix42.MinQty;
import fixengine.tags.fix42.NoContraBrokers;
import fixengine.tags.fix42.OrdStatus;
import fixengine.tags.fix42.OrderID;
import fixengine.tags.fix42.OrderQty2;
import fixengine.tags.fix42.OrderQty;
import fixengine.tags.fix42.OrigClOrdID;
import fixengine.tags.fix42.Price;
import fixengine.tags.fix42.SecurityType;
import fixengine.tags.fix42.SettlCurrAmt;
import fixengine.tags.fix42.SettlCurrency;
import fixengine.tags.fix42.Side;
import fixengine.tags.fix42.Symbol;
import fixengine.tags.fix42.Text;
import fixengine.tags.fix42.TimeInForce;
import fixengine.tags.fix42.TradeDate;
import fixengine.tags.fix42.TransactTime;
import fixengine.tags.fix44.TradeLinkID;

public class ExecutionReport extends fixengine.messages.fix42.ExecutionReport {
    public ExecutionReport(MessageHeader header) {
        super(header);
    }

    @Override protected void fields() {
        field(Account.Tag(), Required.NO);
        field(ClOrdID.Tag());
        field(ExecID.Tag());
        field(ExecInst.Tag(), Required.NO);
        field(ExecTransType.Tag());
        field(OrderID.Tag());
        field(OrdStatus.Tag());
        field(OrigClOrdID.Tag(), Required.NO);
        field(OrderQty.Tag(), Required.NO);
        field(Price.Tag(), Required.NO);
        field(TimeInForce.Tag(), Required.NO);
        field(Currency.Tag(), Required.NO);
        field(Side.Tag());
        field(Symbol.Tag());
        field(ExecBroker.Tag(), Required.NO);
        field(LeavesQty.Tag());
        field(CumQty.Tag());
        field(MinQty.Tag(), Required.NO);
        field(OrderQty2.Tag(), Required.NO);
        field(FutSettDate.Tag(), Required.NO);
        field(TradeDate.Tag(), Required.NO);
        field(SettlCurrAmt.Tag(), Required.NO);
        field(SettlCurrency.Tag(), Required.NO);
        field(SecurityType.Tag());
        field(MaxShow.Tag(), Required.NO);
        field(ExecType.Tag());
        field(AvgPx.Tag());
        field(LastShares.Tag(), Required.NO);
        field(LastPx.Tag(), Required.NO);
        field(NoContraBrokers.Tag(), Required.NO);
        field(ContraBroker.Tag(), Required.NO);
        field(Text.Tag(), Required.NO);
        field(TransactTime.Tag());
        field(ExpireTime.Tag(), Required.NO);
        field(TradeLinkID.Tag(), Required.NO);
    }
}
