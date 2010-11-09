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
import fixengine.tags.Account;
import fixengine.tags.ClOrdID;
import fixengine.tags.HandlInst;
import fixengine.tags.MaxShow;
import fixengine.tags.MinQty;
import fixengine.tags.OrdType;
import fixengine.tags.OrderQty;
import fixengine.tags.OrigClOrdID;
import fixengine.tags.Price;
import fixengine.tags.Side;
import fixengine.tags.Symbol;
import fixengine.tags.TransactTime;

public class OrderModificationRequestMessage extends fixengine.messages.fix42.OrderModificationRequestMessage {
    public OrderModificationRequestMessage(MessageHeader header) {
        super(header);
    }

    @Override protected void fields() {
        field(Account.TAG, Required.NO);
        field(ClOrdID.TAG);
        field(OrigClOrdID.TAG);
        field(HandlInst.TAG);
        field(OrderQty.TAG);
        field(MaxShow.TAG, Required.NO);
        field(MinQty.TAG, Required.NO);
        field(Price.TAG);
        field(Symbol.TAG, Required.NO);
        field(OrdType.TAG, Required.NO);
        field(Side.TAG, Required.NO);
        field(TransactTime.TAG, Required.NO);
    }
}
