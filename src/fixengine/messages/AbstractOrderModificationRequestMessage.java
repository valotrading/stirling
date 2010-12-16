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
package fixengine.messages;

import fixengine.tags.ClOrdID;
import fixengine.tags.Currency;
import fixengine.tags.HandlInst;
import fixengine.tags.MaturityMonthYear;
import fixengine.tags.OrdType;
import fixengine.tags.OrderQty;
import fixengine.tags.OrigClOrdID;
import fixengine.tags.Price;
import fixengine.tags.SecurityType;
import fixengine.tags.fix42.Side;
import fixengine.tags.Symbol;
import fixengine.tags.TransactTime;

/**
 * This class represents the Order Cancel/Replace Request (a.k.a. Order
 * Modification Request) message.
 *
 * @author Pekka Enberg
 */
public class AbstractOrderModificationRequestMessage extends AbstractMessage implements OrderModificationRequestMessage {
    public AbstractOrderModificationRequestMessage(MessageHeader header) {
        super(header);

        fields();
    }

    @Override public void apply(MessageVisitor visitor) {
        visitor.visit(this);
    }

    protected void fields() {
        field(OrigClOrdID.Tag());
        field(ClOrdID.Tag());
        field(HandlInst.Tag());
        field(Symbol.Tag());
        field(SecurityType.Tag(), Required.NO);
        field(MaturityMonthYear.Tag(), Required.NO);
        field(Side.Tag());
        field(TransactTime.TAG);
        field(OrderQty.TAG);
        field(OrdType.Tag());
        field(Currency.Tag(), Required.NO);
        field(Price.TAG, Required.NO);
    }
}
