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
package fixengine.messages;

import fixengine.tags.ClOrdID;
import fixengine.tags.Currency;
import fixengine.tags.CustomerOrFirm;
import fixengine.tags.ExDestination;
import fixengine.tags.HandlInst;
import fixengine.tags.MaturityMonthYear;
import fixengine.tags.OrdType;
import fixengine.tags.OrderQty;
import fixengine.tags.Price;
import fixengine.tags.SecurityType;
import fixengine.tags.Side;
import fixengine.tags.Symbol;
import fixengine.tags.TransactTime;

/**
 * @author Pekka Enberg 
 */
public class NewOrderSingleMessage extends AbstractMessage implements RequestMessage {

    public NewOrderSingleMessage() {
        this(new MessageHeader(MsgTypeValue.NEW_ORDER_SINGLE));
    }

    public NewOrderSingleMessage(MessageHeader header) {
        super(header);

        fields();
    }

    @Override
    public void apply(MessageVisitor visitor) {
        visitor.visit(this);
    }

    public String getOrigClOrdId() {
        return null;
    }

    @Override public SideValue getSide() {
        return getEnum(Side.TAG);
    }

    @Override public OrdTypeValue getOrdType() {
        return getEnum(OrdType.TAG);
    }

    @Override public String getClOrdId() {
        return getString(ClOrdID.TAG);
    }

    @Override public double getOrderQty() {
        return getFloat(OrderQty.TAG);
    }

    @Override public String getSymbol() {
        return getString(Symbol.TAG);
    }

    protected void fields() {
        field(ClOrdID.TAG);
        field(Currency.TAG, Required.NO);
        field(HandlInst.TAG);
        field(ExDestination.TAG);
        field(Side.TAG);
        field(TransactTime.TAG);
        field(OrdType.TAG);
        field(Symbol.TAG);
        field(SecurityType.TAG, Required.NO);
        field(MaturityMonthYear.TAG, Required.NO);
        field(OrderQty.TAG);
        field(CustomerOrFirm.TAG, Required.NO);
        field(Price.TAG, Required.NO);
    }
}
