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
import fixengine.tags.Currency;
import fixengine.tags.HandlInst;
import fixengine.tags.MaxShow;
import fixengine.tags.MinQty;
import fixengine.tags.OrdType;
import fixengine.tags.OrderQty;
import fixengine.tags.PegDifference;
import fixengine.tags.Price;
import fixengine.tags.fix42.Side;
import fixengine.tags.Symbol;
import fixengine.tags.TimeInForce;
import fixengine.tags.TransactTime;
import fixengine.tags.fix42.chix.europe.ExecInst;
import fixengine.tags.fix44.TradeLinkID;

public class NewOrderSingleMessage extends fixengine.messages.fix42.NewOrderSingleMessage {
    public NewOrderSingleMessage(MessageHeader header) {
        super(header);
    }

    @Override protected void fields() {
        field(Account.TAG, Required.NO);
        field(ClOrdID.TAG);
        field(ExecInst.TAG, Required.NO);
        field(HandlInst.TAG);
        field(Currency.TAG, Required.NO);
        field(OrderQty.TAG);
        field(MaxShow.TAG, Required.NO);
        field(MinQty.TAG, Required.NO);
        field(Price.TAG);
        field(Side.Tag());
        field(Symbol.TAG);
        field(OrdType.TAG);
        field(TimeInForce.TAG, Required.NO);
        field(TransactTime.TAG, Required.NO);
        field(PegDifference.TAG, Required.NO);
        field(TradeLinkID.TAG, Required.NO);
    }
}
