/*
 * Copyright 2008 the original author or authors.
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
package fixengine.messages.fix42;

import fixengine.messages.AbstractMessage;
import fixengine.messages.MessageHeader;
import fixengine.messages.MessageVisitor;
import fixengine.messages.Required;
import fixengine.tags.fix42.ClOrdID;
import fixengine.tags.fix42.CxlRejReason;
import fixengine.tags.fix42.CxlRejResponseTo;
import fixengine.tags.fix42.OrdStatus;
import fixengine.tags.fix42.OrderID;
import fixengine.tags.fix42.OrigClOrdID;
import fixengine.tags.fix42.Text;

/**
 * @author Pekka Enberg 
 */
public class OrderCancelReject extends AbstractMessage implements fixengine.messages.OrderCancelReject {
    public OrderCancelReject(MessageHeader header) {
        super(header);

        fields();
    }

    @Override public void apply(MessageVisitor visitor) {
        visitor.visit(this);
    }

    protected void fields() {
        field(OrderID.Tag());
        field(ClOrdID.Tag());
        field(OrigClOrdID.Tag());
        field(OrdStatus.Tag());
        field(CxlRejResponseTo.Tag());
        field(CxlRejReason.Tag(), Required.NO);
        field(Text.Tag(), Required.NO);
    }
}
