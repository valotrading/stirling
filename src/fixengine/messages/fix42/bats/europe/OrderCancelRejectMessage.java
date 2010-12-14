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
package fixengine.messages.fix42.bats.europe;

import fixengine.messages.MessageHeader;
import fixengine.messages.Required;

import fixengine.tags.Account;
import fixengine.tags.ClOrdID;
import fixengine.tags.CxlRejResponseTo;
import fixengine.tags.OrdStatus;
import fixengine.tags.OrderID;
import fixengine.tags.OrigClOrdID;
import fixengine.tags.Text;

import fixengine.tags.fix42.bats.europe.CxlRejReason;

public class OrderCancelRejectMessage extends fixengine.messages.fix42.OrderCancelRejectMessage {
    public OrderCancelRejectMessage(MessageHeader header) {
        super(header);
    }

    @Override public void fields() {
        field(Account.TAG, Required.NO);
        field(ClOrdID.TAG);
        field(OrderID.TAG);
        field(OrdStatus.Tag());
        field(OrigClOrdID.TAG);
        field(Text.TAG);
        field(CxlRejReason.Tag());
        field(CxlRejResponseTo.Tag());
    }
}
