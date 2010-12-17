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
package fixengine.messages.fix42.hotspotfx;

import fixengine.messages.MessageHeader;
import fixengine.messages.Required;
import fixengine.tags.fix42.Account;
import fixengine.tags.fix42.ClOrdID;
import fixengine.tags.fix42.CxlRejReason;
import fixengine.tags.fix42.CxlRejResponseTo;
import fixengine.tags.fix42.OrderID;
import fixengine.tags.fix42.OrigClOrdID;
import fixengine.tags.fix42.Symbol;
import fixengine.tags.fix42.Text;

public class OrderCancelRejectMessage extends fixengine.messages.fix42.OrderCancelRejectMessage {
    public OrderCancelRejectMessage(MessageHeader header) {
        super(header);
    }

    @Override public void fields() {
        field(Account.Tag(), Required.NO);
        field(ClOrdID.Tag());
        field(OrderID.Tag());
        field(OrigClOrdID.Tag());
        field(Symbol.Tag());
        field(CxlRejResponseTo.Tag());
        field(CxlRejReason.Tag(), Required.NO);
        field(Text.Tag(), Required.NO);
    }
}
