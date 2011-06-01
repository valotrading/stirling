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

import fixengine.tags.fix42.ClOrdID;
import fixengine.tags.fix42.Currency;
import fixengine.tags.fix42.HandlInst;
import fixengine.tags.fix42.MaturityMonthYear;
import fixengine.tags.fix42.OrdType;
import fixengine.tags.fix42.OrderQty;
import fixengine.tags.fix42.OrigClOrdID;
import fixengine.tags.fix42.Price;
import fixengine.tags.fix42.SecurityType;
import fixengine.tags.fix42.Side;
import fixengine.tags.fix42.Symbol;
import fixengine.tags.fix42.TransactTime;

/**
 * This class represents the Order Cancel/Replace Request (a.k.a. Order
 * Modification Request) message.
 *
 * @author Pekka Enberg
 */
public class AbstractOrderModificationRequestMessage extends AbstractMessage implements OrderModificationRequest {
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
        field(TransactTime.Tag());
        field(OrderQty.Tag());
        field(OrdType.Tag());
        field(Currency.Tag(), Required.NO);
        field(Price.Tag(), Required.NO);
    }
}
