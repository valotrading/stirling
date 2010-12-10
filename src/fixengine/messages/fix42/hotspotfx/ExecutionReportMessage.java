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
        field(Account.TAG, Required.NO);
        field(ClOrdID.TAG);
        field(ExecID.TAG);
        field(ExecInst.TAG, Required.NO);
        field(ExecTransType.Tag());
        field(OrderID.TAG);
        field(OrdStatus.TAG);
        field(OrigClOrdID.TAG, Required.NO);
        field(OrderQty.TAG, Required.NO);
        field(Price.TAG, Required.NO);
        field(TimeInForce.Tag(), Required.NO);
        field(Currency.TAG, Required.NO);
        field(Side.Tag());
        field(Symbol.TAG);
        field(ExecBroker.TAG, Required.NO);
        field(LeavesQty.TAG);
        field(CumQty.TAG);
        field(MinQty.TAG, Required.NO);
        field(OrderQty2.TAG, Required.NO);
        field(FutSettDate.TAG, Required.NO);
        field(TradeDate.TAG, Required.NO);
        field(SettlCurrAmt.TAG, Required.NO);
        field(SettlCurrency.TAG, Required.NO);
        field(SecurityType.TAG);
        field(MaxShow.TAG, Required.NO);
        field(ExecType.TAG);
        field(AvgPx.TAG);
        field(LastShares.TAG, Required.NO);
        field(LastPx.TAG, Required.NO);
        field(NoContraBrokers.TAG, Required.NO);
        field(ContraBroker.TAG, Required.NO);
        field(Text.TAG, Required.NO);
        field(TransactTime.TAG);
        field(ExpireTime.TAG, Required.NO);
        field(TradeLinkID.TAG, Required.NO);
    }
}
