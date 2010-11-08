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
import fixengine.tags.ClOrdID;
import fixengine.tags.OrigClOrdID;
import fixengine.tags.Side;
import fixengine.tags.Symbol;
import fixengine.tags.TransactTime;

public class OrderCancelRequestMessage extends fixengine.messages.fix42.OrderCancelRequestMessage {
    public OrderCancelRequestMessage(MessageHeader header) {
        super(header);
    }

    @Override protected void fields() {
        field(ClOrdID.TAG);
        field(OrigClOrdID.TAG);
        field(Symbol.TAG);
        field(Side.TAG);
        field(TransactTime.TAG);
    }
}
