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
package fixengine.messages.bats.europe;

import fixengine.messages.MessageHeader;
import fixengine.messages.Required;

import fixengine.messages.bats.europe.ExecTransTypeValue;

import fixengine.tags.Account;
import fixengine.tags.AvgPx;
import fixengine.tags.ClOrdID;
import fixengine.tags.ClearingAccount;
import fixengine.tags.ClearingFirm;
import fixengine.tags.ContraBroker;
import fixengine.tags.CumQty;
import fixengine.tags.Currency;
import fixengine.tags.ExecID;
import fixengine.tags.ExecRefID;
import fixengine.tags.ExecRestatementReason;
import fixengine.tags.ExpireTime;
import fixengine.tags.LastPx;
import fixengine.tags.LastQty;
import fixengine.tags.LeavesQty;
import fixengine.tags.MaxFloor;
import fixengine.tags.NoContraBrokers;
import fixengine.tags.OrdRejReason;
import fixengine.tags.OrdStatus;
import fixengine.tags.OrderID;
import fixengine.tags.OrderQty;
import fixengine.tags.OrigClOrdID;
import fixengine.tags.Price;
import fixengine.tags.SecondaryOrderID;
import fixengine.tags.SecurityExchange;
import fixengine.tags.Symbol;
import fixengine.tags.Text;

import fixengine.tags.bats.europe.CentralCounterparty;
import fixengine.tags.bats.europe.ExecInst;
import fixengine.tags.bats.europe.ExecTransType;
import fixengine.tags.bats.europe.ExecType;
import fixengine.tags.bats.europe.IDSource;
import fixengine.tags.bats.europe.MTFAccessFee;
import fixengine.tags.bats.europe.SecurityID;
import fixengine.tags.bats.europe.Side;
import fixengine.tags.bats.europe.TimeInForce;
import fixengine.tags.bats.europe.TradeLiquidityIndicator;
import fixengine.tags.bats.europe.TransactTime;

public class ExecutionReportMessage extends fixengine.messages.ExecutionReportMessage {
    public ExecutionReportMessage() {
      super();
    }

    public ExecutionReportMessage(MessageHeader header) {
        super(header);
    }

    @Override public void fields() {
        field(Account.TAG, Required.NO);
        field(ClOrdID.TAG);
        field(CumQty.TAG);
        field(Currency.TAG, Required.NO);
        field(ExecID.TAG);
        field(ExecInst.TAG, Required.NO);
        field(ExecRefID.TAG, new Required() {
            @Override public boolean isRequired() {
                ExecTransTypeValue value = getEnum(ExecTransType.TAG);
                return value.equals(ExecTransTypeValue.CANCEL) || value.equals(ExecTransTypeValue.CORRECT);
            }
        });
        field(ExecTransType.TAG);
        field(IDSource.TAG, Required.NO);
        field(LastPx.TAG);
        field(LastQty.TAG);
        field(OrderID.TAG);
        field(OrderQty.TAG);
        field(OrdStatus.TAG);
        field(OrigClOrdID.TAG, Required.NO);
        field(Price.TAG, Required.NO);
        field(AvgPx.TAG, Required.NO);
        field(SecurityID.TAG, Required.NO);
        field(Side.TAG);
        field(Symbol.TAG, Required.NO);
        field(Text.TAG, Required.NO);
        field(TimeInForce.TAG, Required.NO);
        field(TransactTime.TAG);
        field(OrdRejReason.TAG, Required.NO);
        field(MaxFloor.TAG, Required.NO);
        field(ExpireTime.TAG, new Required() {
            @Override public boolean isRequired() {
                return TimeInForceValue.GOOD_TILL_DATE.equals(getEnum(TimeInForce.TAG));
            }
        });
        field(ExecType.TAG);
        field(LeavesQty.TAG);
        field(SecondaryOrderID.TAG, Required.NO);
        field(SecurityExchange.TAG, Required.NO);
        field(ContraBroker.TAG, Required.NO);
        field(ExecRestatementReason.TAG, new Required() {
            @Override public boolean isRequired() {
                return getEnum(ExecType.TAG).equals(ExecTypeValue.RESTATED);
            }
        });
        field(NoContraBrokers.TAG, new Required() {
            @Override public boolean isRequired() {
                return getEnum(ExecType.TAG).isTrade();
            }
        });
        field(ClearingFirm.TAG, Required.NO);
        field(ClearingAccount.TAG, Required.NO);
        field(CentralCounterparty.TAG, Required.NO);
        field(MTFAccessFee.TAG, new Required() {
            @Override public boolean isRequired() {
                return getEnum(ExecType.TAG).isTrade();
            }
        });
        field(TradeLiquidityIndicator.TAG, new Required() {
            @Override public boolean isRequired() {
                return getEnum(ExecType.TAG).isTrade();
            }
        });
    }
}
