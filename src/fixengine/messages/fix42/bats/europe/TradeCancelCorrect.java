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
import fixengine.messages.MessageVisitor;
import fixengine.messages.Required;
import fixengine.messages.UserDefinedMessage;
import fixengine.messages.Value;

import fixengine.tags.fix42.ClOrdID;
import fixengine.tags.fix42.ClearingAccount;
import fixengine.tags.fix42.ClearingFirm;
import fixengine.tags.fix42.Currency;
import fixengine.tags.fix42.ExecID;
import fixengine.tags.fix42.ExecRefID;
import fixengine.tags.fix42.ExecTransType;
import fixengine.tags.fix42.LastPx;
import fixengine.tags.fix42.LastShares;
import fixengine.tags.fix42.OrderID;
import fixengine.tags.fix42.OrigTime;
import fixengine.tags.fix42.SecurityExchange;
import fixengine.tags.fix42.Side;
import fixengine.tags.fix42.Symbol;
import fixengine.tags.fix42.TransactTime;
import fixengine.tags.fix42.bats.europe.CorrectedPrice;
import fixengine.tags.fix42.bats.europe.IDSource;
import fixengine.tags.fix42.bats.europe.SecurityID;
import fixengine.tags.fix42.bats.europe.TradeLiquidityIndicator;

public class TradeCancelCorrect extends UserDefinedMessage {
    public TradeCancelCorrect() {
        this(new MessageHeader(MsgTypeValue.TRADE_CANCEL_CORRECT));
    }

    public TradeCancelCorrect(MessageHeader header) {
        super(header);

        field(ClOrdID.Tag());
        field(Currency.Tag(), Required.NO);
        field(ExecID.Tag());
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
        field(OrigTime.Tag());
        field(SecurityID.Tag(), Required.NO);
        field(Symbol.Tag(), Required.NO);
        field(Side.Tag());
        field(TransactTime.Tag());
        field(SecurityExchange.Tag(), Required.NO);
        field(ClearingFirm.Tag(), Required.NO);
        field(ClearingAccount.Tag(), Required.NO);
        field(CorrectedPrice.Tag(), Required.NO);
        field(TradeLiquidityIndicator.Tag(), Required.NO);
    }

    @Override public void apply(MessageVisitor visitor) {
        visitor.visit(this);
    }
}
