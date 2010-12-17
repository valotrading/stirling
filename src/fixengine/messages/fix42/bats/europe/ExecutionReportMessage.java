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

import fixengine.tags.fix42.Account;
import fixengine.tags.fix42.AvgPx;
import fixengine.tags.fix42.ClOrdID;
import fixengine.tags.fix42.ClearingAccount;
import fixengine.tags.fix42.ClearingFirm;
import fixengine.tags.fix42.ContraBroker;
import fixengine.tags.fix42.CumQty;
import fixengine.tags.fix42.Currency;
import fixengine.tags.fix42.ExecID;
import fixengine.tags.fix42.ExecRefID;
import fixengine.tags.fix42.ExecTransType;
import fixengine.tags.fix42.ExpireTime;
import fixengine.tags.fix42.LastPx;
import fixengine.tags.fix42.LastShares;
import fixengine.tags.fix42.LeavesQty;
import fixengine.tags.fix42.MaxFloor;
import fixengine.tags.fix42.MinQty;
import fixengine.tags.fix42.NoContraBrokers;
import fixengine.tags.fix42.OrdRejReason;
import fixengine.tags.fix42.OrdStatus;
import fixengine.tags.fix42.OrderID;
import fixengine.tags.fix42.OrderQty;
import fixengine.tags.fix42.OrigClOrdID;
import fixengine.tags.fix42.PegDifference;
import fixengine.tags.fix42.Price;
import fixengine.tags.fix42.SecondaryOrderID;
import fixengine.tags.fix42.SecurityExchange;
import fixengine.tags.fix42.Side;
import fixengine.tags.fix42.Symbol;
import fixengine.tags.fix42.Text;
import fixengine.tags.fix42.TransactTime;
import fixengine.tags.fix42.bats.europe.CentralCounterparty;
import fixengine.tags.fix42.bats.europe.ExecInst;
import fixengine.tags.fix42.bats.europe.ExecType;
import fixengine.tags.fix42.bats.europe.IDSource;
import fixengine.tags.fix42.bats.europe.MTFAccessFee;
import fixengine.tags.fix42.bats.europe.MaxRemovePct;
import fixengine.tags.fix42.bats.europe.SecurityID;
import fixengine.tags.fix42.bats.europe.TimeInForce;
import fixengine.tags.fix42.bats.europe.TradeLiquidityIndicator;
import fixengine.tags.fix43.ExecRestatementReason;

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
        field(TransactTime.Tag());
        field(OrdRejReason.Tag(), Required.NO);
        field(MaxFloor.Tag(), Required.NO);
        field(ExpireTime.Tag(), new Required() {
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
        field(ClearingFirm.Tag(), Required.NO);
        field(ClearingAccount.Tag(), Required.NO);
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
        field(PegDifference.Tag(), Required.NO);
    }
}
