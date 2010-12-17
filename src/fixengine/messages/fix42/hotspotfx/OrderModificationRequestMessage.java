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
import fixengine.tags.fix42.HandlInst;
import fixengine.tags.fix42.MaxShow;
import fixengine.tags.fix42.MinQty;
import fixengine.tags.fix42.OrdType;
import fixengine.tags.fix42.OrderQty;
import fixengine.tags.fix42.OrigClOrdID;
import fixengine.tags.fix42.Price;
import fixengine.tags.fix42.Side;
import fixengine.tags.fix42.Symbol;
import fixengine.tags.fix42.TransactTime;

public class OrderModificationRequestMessage extends fixengine.messages.fix42.OrderModificationRequestMessage {
    public OrderModificationRequestMessage(MessageHeader header) {
        super(header);
    }

    @Override protected void fields() {
        field(Account.Tag(), Required.NO);
        field(ClOrdID.Tag());
        field(OrigClOrdID.Tag());
        field(HandlInst.Tag());
        field(OrderQty.Tag());
        field(MaxShow.Tag(), Required.NO);
        field(MinQty.Tag(), Required.NO);
        field(Price.Tag());
        field(Symbol.Tag(), Required.NO);
        field(OrdType.Tag(), Required.NO);
        field(Side.Tag(), Required.NO);
        field(TransactTime.Tag(), Required.NO);
    }
}
