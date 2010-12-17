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
package fixengine.messages.fix44;

import fixengine.messages.AbstractNewOrderSingleMessage;
import fixengine.messages.MessageHeader;
import fixengine.messages.Required;
import fixengine.tags.fix42.ClOrdID;
import fixengine.tags.fix42.Currency;
import fixengine.tags.fix42.CustomerOrFirm;
import fixengine.tags.fix42.ExDestination;
import fixengine.tags.fix42.HandlInst;
import fixengine.tags.fix42.MaturityMonthYear;
import fixengine.tags.fix42.OrdType;
import fixengine.tags.fix42.OrderQty;
import fixengine.tags.fix42.Price;
import fixengine.tags.fix42.SecurityType;
import fixengine.tags.fix42.Side;
import fixengine.tags.fix42.Symbol;
import fixengine.tags.fix42.TransactTime;
import fixengine.tags.fix42.TimeInForce;

public class NewOrderSingleMessage extends AbstractNewOrderSingleMessage {
    public NewOrderSingleMessage(MessageHeader header) {
        super(header);
    }

    @Override protected void fields() {
        field(ClOrdID.Tag());
        field(Currency.Tag(), Required.NO);
        field(HandlInst.Tag());
        field(ExDestination.Tag());
        field(Side.Tag());
        field(TransactTime.Tag());
        field(OrdType.Tag());
        field(Symbol.Tag());
        field(SecurityType.Tag(), Required.NO);
        field(MaturityMonthYear.Tag(), Required.NO);
        field(OrderQty.Tag());
        field(CustomerOrFirm.Tag(), Required.NO);
        field(Price.Tag(), Required.NO);
        field(TimeInForce.Tag(), Required.NO);
    }
}
