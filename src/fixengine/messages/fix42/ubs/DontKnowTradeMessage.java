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
import fixengine.tags.fix42.DKReason;
import fixengine.tags.fix42.ExecID;
import fixengine.tags.fix42.LastPx;
import fixengine.tags.fix42.LastShares;
import fixengine.tags.fix42.OrderID;
import fixengine.tags.fix42.OrderQty;
import fixengine.tags.fix42.Side;
import fixengine.tags.fix42.Symbol;

public class DontKnowTradeMessage extends fixengine.messages.fix42.DontKnowTradeMessage {
    public DontKnowTradeMessage(MessageHeader header) {
        super(header);
    }

    @Override protected void fields() {
        field(OrderID.Tag());
        field(ExecID.Tag());
        field(DKReason.Tag());
        field(Symbol.Tag());
        field(Side.Tag());
        field(OrderQty.Tag(), Required.NO);
        field(LastShares.Tag(), Required.NO);
        field(LastPx.Tag(), Required.NO);
    }
}
