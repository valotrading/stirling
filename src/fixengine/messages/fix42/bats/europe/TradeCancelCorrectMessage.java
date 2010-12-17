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

import fixengine.tags.ClOrdID;
import fixengine.tags.Currency;
import fixengine.tags.ExecID;
import fixengine.tags.ExecRefID;
import fixengine.tags.LastPx;
import fixengine.tags.LastShares;
import fixengine.tags.OrderID;
import fixengine.tags.OrigTime;
import fixengine.tags.SecurityExchange;
import fixengine.tags.fix42.Side;
import fixengine.tags.Symbol;
import fixengine.tags.TransactTime;
import fixengine.tags.fix42.ClearingAccount;
import fixengine.tags.fix42.ClearingFirm;
import fixengine.tags.fix42.ExecTransType;
import fixengine.tags.fix42.bats.europe.CorrectedPrice;
import fixengine.tags.fix42.bats.europe.IDSource;
import fixengine.tags.fix42.bats.europe.SecurityID;
import fixengine.tags.fix42.bats.europe.TradeLiquidityIndicator;

public class TradeCancelCorrectMessage extends UserDefinedMessage {
    public TradeCancelCorrectMessage() {
        this(new MessageHeader(MsgTypeValue.TRADE_CANCEL_CORRECT));
    }

    public TradeCancelCorrectMessage(MessageHeader header) {
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
        field(OrigTime.TAG);
        field(SecurityID.Tag(), Required.NO);
        field(Symbol.Tag(), Required.NO);
        field(Side.Tag());
        field(TransactTime.TAG);
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
