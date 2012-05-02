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
package stirling.fix.messages.fix42.chix.europe;

import stirling.fix.messages.DefaultMessageHeader;
import stirling.fix.messages.MessageVisitor;
import stirling.fix.messages.Required;
import stirling.fix.messages.AbstractMessage;

import stirling.fix.tags.fix42.ClOrdID;
import stirling.fix.tags.fix42.CxlRejReason;
import stirling.fix.tags.fix42.OrdStatus;
import stirling.fix.tags.fix42.OrderID;
import stirling.fix.tags.fix42.OrigClOrdID;
import stirling.fix.tags.fix42.Text;

public class OrderCancelReject extends AbstractMessage implements stirling.fix.messages.OrderCancelReject {
    public OrderCancelReject(DefaultMessageHeader header) {
        super(header);

        field(ClOrdID.Tag());
        field(OrderID.Tag());
        field(OrdStatus.Tag());
        field(OrigClOrdID.Tag());
        field(Text.Tag());
        field(CxlRejReason.Tag());
    }

    @Override public void apply(MessageVisitor visitor) {
        visitor.visit(this);
    }
}
