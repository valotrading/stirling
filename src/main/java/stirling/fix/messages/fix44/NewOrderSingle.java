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
package stirling.fix.messages.fix44;

import stirling.fix.messages.AbstractMessage;
import stirling.fix.messages.MessageHeader;
import stirling.fix.messages.MessageVisitor;
import stirling.fix.messages.Required;
import stirling.fix.tags.fix42.ClOrdID;
import stirling.fix.tags.fix42.Currency;
import stirling.fix.tags.fix42.CustomerOrFirm;
import stirling.fix.tags.fix42.ExDestination;
import stirling.fix.tags.fix42.HandlInst;
import stirling.fix.tags.fix42.MaturityMonthYear;
import stirling.fix.tags.fix42.OrdType;
import stirling.fix.tags.fix42.OrderQty;
import stirling.fix.tags.fix42.Price;
import stirling.fix.tags.fix42.SecurityType;
import stirling.fix.tags.fix42.Side;
import stirling.fix.tags.fix42.Symbol;
import stirling.fix.tags.fix42.TransactTime;
import stirling.fix.tags.fix42.TimeInForce;

public class NewOrderSingle extends AbstractMessage implements stirling.fix.messages.NewOrderSingle {
    public NewOrderSingle(MessageHeader header) {
        super(header);
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

    @Override public void apply(MessageVisitor visitor) {
        visitor.visit(this);
    }
}
