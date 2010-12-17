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
package fixengine.messages.fix42.ubs;

import fixengine.messages.MessageHeader;
import fixengine.messages.Required;
import fixengine.tags.fix42.ClOrdID;
import fixengine.tags.fix42.ExecInst;
import fixengine.tags.fix42.MinQty;
import fixengine.tags.fix42.OrdType;
import fixengine.tags.fix42.OrderCapacity;
import fixengine.tags.fix42.OrderQty;
import fixengine.tags.fix42.Price;
import fixengine.tags.fix42.Side;
import fixengine.tags.fix42.Symbol;
import fixengine.tags.fix42.TimeInForce;
import fixengine.tags.fix42.TransactTime;
import fixengine.tags.fix42.ubs.Internalization;

public class NewOrderSingleMessage extends fixengine.messages.fix42.NewOrderSingleMessage {
    public NewOrderSingleMessage(MessageHeader header) {
        super(header);
    }

    @Override protected void fields() {
        field(ClOrdID.Tag());
        field(ExecInst.Tag());
        field(Symbol.Tag());
        field(Side.Tag());
        field(OrderQty.Tag());
        field(OrdType.Tag());
        field(TransactTime.Tag());
        field(OrderCapacity.Tag());
        field(Price.Tag(), new Required() {
            @Override public boolean isRequired() {
                return getEnum(OrdType.Tag()).equals(OrdType.Limit());
            }
        });
        field(TimeInForce.Tag(), Required.NO);
        field(MinQty.Tag(), Required.NO);
        field(Internalization.Tag(), Required.NO);
    }
}
