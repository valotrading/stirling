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
import fixengine.tags.ClOrdID;
import fixengine.tags.Currency;
import fixengine.tags.ExecID;
import fixengine.tags.ExecRefID;
import fixengine.tags.LastPx;
import fixengine.tags.LastShares;
import fixengine.tags.OrderID;
import fixengine.tags.OrigTime;
import fixengine.tags.SecurityExchange;
import fixengine.tags.Side;
import fixengine.tags.Symbol;
import fixengine.tags.TransactTime;
import fixengine.tags.fix42.ClearingAccount;
import fixengine.tags.fix42.ClearingFirm;
import fixengine.tags.fix42.bats.europe.CorrectedPrice;
import fixengine.tags.fix42.bats.europe.ExecTransType;
import fixengine.tags.fix42.bats.europe.IDSource;
import fixengine.tags.fix42.bats.europe.SecurityID;
import fixengine.tags.fix42.bats.europe.TradeLiquidityIndicator;

public class TradeCancelCorrectMessage extends UserDefinedMessage {
    public TradeCancelCorrectMessage() {
        this(new MessageHeader(MsgTypeValue.TRADE_CANCEL_CORRECT));
    }

    public TradeCancelCorrectMessage(MessageHeader header) {
        super(header);

        field(ClOrdID.TAG);
        field(Currency.TAG, Required.NO);
        field(ExecID.TAG);
        field(ExecRefID.TAG, new Required() {
            @Override public boolean isRequired() {
                ExecTransTypeValue value = getEnum(ExecTransType.TAG);
                return value.equals(ExecTransTypeValue.CANCEL) || value.equals(ExecTransTypeValue.CORRECT);
            }
        });
        field(ExecTransType.TAG);
        field(IDSource.TAG, Required.NO);
        field(LastPx.TAG);
        field(LastShares.TAG);
        field(OrderID.TAG);
        field(OrigTime.TAG);
        field(SecurityID.TAG, Required.NO);
        field(Symbol.TAG, Required.NO);
        field(Side.TAG);
        field(TransactTime.TAG);
        field(SecurityExchange.TAG, Required.NO);
        field(ClearingFirm.TAG, Required.NO);
        field(ClearingAccount.TAG, Required.NO);
        field(CorrectedPrice.TAG, Required.NO);
        field(TradeLiquidityIndicator.TAG, Required.NO);
    }

    @Override public void apply(MessageVisitor visitor) {
        visitor.visit(this);
    }
}
