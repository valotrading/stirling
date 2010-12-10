/*
 * Copyright 2008 the original author or authors.
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
package fixengine.messages.fix42;

import fixengine.messages.AbstractMessage;
import fixengine.messages.MessageHeader;
import fixengine.messages.MessageVisitor;
import fixengine.messages.Required;
import fixengine.tags.DKReason;
import fixengine.tags.ExecID;
import fixengine.tags.LastPx;
import fixengine.tags.LastShares;
import fixengine.tags.MaturityMonthYear;
import fixengine.tags.OrderID;
import fixengine.tags.OrderQty;
import fixengine.tags.SecurityExchange;
import fixengine.tags.SecurityID;
import fixengine.tags.SecurityType;
import fixengine.tags.Symbol;
import fixengine.tags.Text;
import fixengine.tags.fix42.Side;
import fixengine.tags.fix42.IDSource;

public class DontKnowTradeMessage extends AbstractMessage {
    public DontKnowTradeMessage(MessageHeader header) {
        super(header);

        fields();
    }

    @Override public void apply(MessageVisitor visitor) {
        visitor.visit(this);
    }

    protected void fields() {
        field(OrderID.TAG);
        field(ExecID.TAG);
        field(DKReason.TAG);
        field(Symbol.TAG);
        /* SymbolSfx(65) */
        field(SecurityID.TAG, Required.NO);
        field(IDSource.TAG, Required.NO);
        field(SecurityType.TAG, Required.NO);
        field(MaturityMonthYear.TAG, Required.NO);
        /* MaturityDay(205) */
        /* PutOrCall(201) */
        /* StrikePrice(202) */
        /* OptAttribute(206) */
        /* ContractMultiplier(201) */
        /* CouponRate(223) */
        field(SecurityExchange.TAG, Required.NO);
        /* Issuer(106) */
        /* EncodedIssuerLen(348) */
        /* EncodedIssuer(349) */
        /* SecurityDesc(107) */
        /* EncodedSecurityDescLen(350) */
        /* EncodedSecurityDesc(351) */
        field(Side.Tag());
        field(OrderQty.TAG, Required.NO);
        /* CashOrderQty(152) */
        field(LastShares.TAG, Required.NO);
        field(LastPx.TAG, Required.NO);
        field(Text.TAG, Required.NO);
        /* EncodedTextLen(354) */
        /* EncodedText(355) */
    }
}
