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
import fixengine.tags.Account;
import fixengine.tags.AvgPx;
import fixengine.tags.ClOrdID;
import fixengine.tags.ContraBroker;
import fixengine.tags.CumQty;
import fixengine.tags.Currency;
import fixengine.tags.ExecBroker;
import fixengine.tags.ExecID;
import fixengine.tags.ExecInst;
import fixengine.tags.ExecType;
import fixengine.tags.ExpireTime;
import fixengine.tags.FutSettDate;
import fixengine.tags.LastPx;
import fixengine.tags.LastShares;
import fixengine.tags.LeavesQty;
import fixengine.tags.MaxShow;
import fixengine.tags.MinQty;
import fixengine.tags.NoContraBrokers;
import fixengine.tags.OrdStatus;
import fixengine.tags.OrderID;
import fixengine.tags.OrderQty;
import fixengine.tags.OrderQty2;
import fixengine.tags.OrigClOrdID;
import fixengine.tags.Price;
import fixengine.tags.SecurityType;
import fixengine.tags.SettlCurrAmt;
import fixengine.tags.SettlCurrency;
import fixengine.tags.Symbol;
import fixengine.tags.Text;
import fixengine.tags.fix42.TimeInForce;
import fixengine.tags.TradeDate;
import fixengine.tags.TransactTime;
import fixengine.tags.fix42.ExecTransType;
import fixengine.tags.fix42.Side;
import fixengine.tags.fix44.TradeLinkID;

public class ExecutionReportMessage extends fixengine.messages.fix42.ExecutionReportMessage {
    public ExecutionReportMessage(MessageHeader header) {
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
