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
import fixengine.tags.OrdType;
import fixengine.tags.OrderQty;
import fixengine.tags.Side;
import fixengine.tags.Symbol;

public abstract class AbstractNewOrderSingleMessage extends AbstractMessage implements NewOrderSingleMessage {
    protected AbstractNewOrderSingleMessage(MessageHeader header) {
        super(header);

        fields();
    }

    @Override public void apply(MessageVisitor visitor) {
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
    }
}