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
package fixengine.messages.fix42.bats.europe;

import fixengine.messages.MessageHeader;
import fixengine.messages.Required;
import fixengine.messages.Value;

import fixengine.tags.Account;
import fixengine.tags.AvgPx;
import fixengine.tags.ClOrdID;
import fixengine.tags.ContraBroker;
import fixengine.tags.CumQty;
import fixengine.tags.Currency;
import fixengine.tags.ExecID;
import fixengine.tags.ExecRefID;
import fixengine.tags.ExecRestatementReason;
import fixengine.tags.ExpireTime;
import fixengine.tags.LastPx;
import fixengine.tags.LastShares;
import fixengine.tags.LeavesQty;
import fixengine.tags.MaxFloor;
import fixengine.tags.MinQty;
import fixengine.tags.NoContraBrokers;
import fixengine.tags.OrdRejReason;
import fixengine.tags.OrdStatus;
import fixengine.tags.OrderID;
import fixengine.tags.OrderQty;
import fixengine.tags.OrigClOrdID;
import fixengine.tags.PegDifference;
import fixengine.tags.Price;
import fixengine.tags.SecondaryOrderID;
import fixengine.tags.SecurityExchange;
import fixengine.tags.fix42.Side;
import fixengine.tags.Symbol;
import fixengine.tags.Text;
import fixengine.tags.TransactTime;

import fixengine.tags.fix42.ClearingAccount;
import fixengine.tags.fix42.ClearingFirm;
import fixengine.tags.fix42.ExecTransType;
import fixengine.tags.fix42.bats.europe.CentralCounterparty;
import fixengine.tags.fix42.bats.europe.ExecInst;
import fixengine.tags.fix42.bats.europe.ExecType;
import fixengine.tags.fix42.bats.europe.IDSource;
import fixengine.tags.fix42.bats.europe.MTFAccessFee;
import fixengine.tags.fix42.bats.europe.MaxRemovePct;
import fixengine.tags.fix42.bats.europe.SecurityID;
import fixengine.tags.fix42.bats.europe.TimeInForce;
import fixengine.tags.fix42.bats.europe.TradeLiquidityIndicator;

public class ExecutionReportMessage extends fixengine.messages.fix42.ExecutionReportMessage {
    public ExecutionReportMessage(MessageHeader header) {
        super(header);
    }

    @Override protected void fields() {
        field(Account.Tag(), Required.NO);
        field(ClOrdID.Tag());
        field(CumQty.Tag());
        field(Currency.Tag(), Required.NO);
        field(ExecID.Tag());
        field(ExecInst.Tag(), Required.NO);
        field(ExecRefID.Tag(), new Required() {
            @Override public boolean isRequired() {
                Value<?> value = getEnum(ExecTransType.Tag());
                return value.equals(ExecTransType.Cancel()) || value.equals(ExecTransType.Correct());
            }
        });
        field(ExecTransType.Tag());
        field(IDSource.Tag(), Required.NO);
        field(LastPx.Tag());
        field(LastShares.Tag());
        field(OrderID.Tag());
        field(OrderQty.Tag());
        field(OrdStatus.Tag());
        field(OrigClOrdID.Tag(), Required.NO);
        field(Price.Tag(), Required.NO);
        field(AvgPx.Tag(), Required.NO);
        field(SecurityID.Tag(), Required.NO);
        field(Side.Tag());
        field(Symbol.Tag(), Required.NO);
        field(Text.Tag(), Required.NO);
        field(TimeInForce.Tag(), Required.NO);
        field(TransactTime.TAG);
        field(OrdRejReason.Tag(), Required.NO);
        field(MaxFloor.Tag(), Required.NO);
        field(ExpireTime.TAG, new Required() {
            @Override public boolean isRequired() {
                return TimeInForce.GoodTillDate().equals(getEnum(TimeInForce.Tag()));
            }
        });
        field(ExecType.Tag());
        field(LeavesQty.Tag());
        field(SecondaryOrderID.Tag(), Required.NO);
        field(SecurityExchange.Tag(), Required.NO);
        field(ContraBroker.Tag(), Required.NO);
        field(ExecRestatementReason.Tag(), new Required() {
            @Override public boolean isRequired() {
                return getEnum(ExecType.Tag()).equals(ExecType.Restated());
            }
        });
        field(NoContraBrokers.Tag(), new Required() {
            @Override public boolean isRequired() {
                return ExecType.isTrade(getEnum(ExecType.Tag()));
            }
        });
        field(ClearingFirm.TAG, Required.NO);
        field(ClearingAccount.TAG, Required.NO);
        field(CentralCounterparty.Tag(), Required.NO);
        field(MTFAccessFee.Tag(), new Required() {
            @Override public boolean isRequired() {
                return ExecType.isTrade(getEnum(ExecType.Tag()));
            }
        });
        field(TradeLiquidityIndicator.Tag(), new Required() {
            @Override public boolean isRequired() {
                return ExecType.isTrade(getEnum(ExecType.Tag()));
            }
        });
        field(MaxRemovePct.Tag(), Required.NO);
        field(MinQty.Tag(), Required.NO);
        field(PegDifference.TAG, Required.NO);
    }
}
