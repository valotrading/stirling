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
import fixengine.tags.fix42.ClOrdID;
import fixengine.tags.fix42.OrdStatus;
import fixengine.tags.fix42.OrderID;
import fixengine.tags.fix42.OrigClOrdID;

public class OrderCancelReject extends fixengine.messages.fix42.OrderCancelReject {
    public OrderCancelReject(MessageHeader header) {
        super(header);
    }

    @Override public void fields() {
        field(ClOrdID.Tag());
        field(OrigClOrdID.Tag());
        field(OrderID.Tag());
        field(OrdStatus.Tag());
    }
}
