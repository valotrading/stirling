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
package fixengine.messages;

import fixengine.tags.fix42.ClOrdID;
import fixengine.tags.fix42.MaturityMonthYear;
import fixengine.tags.fix42.OrderQty;
import fixengine.tags.fix42.OrigClOrdID;
import fixengine.tags.fix42.SecurityType;
import fixengine.tags.fix42.Side;
import fixengine.tags.fix42.Symbol;
import fixengine.tags.fix42.TransactTime;

public abstract class AbstractOrderCancelRequestMessage extends AbstractMessage implements fixengine.messages.OrderCancelRequestMessage {
    public AbstractOrderCancelRequestMessage(MessageHeader header) {
        super(header);

        fields();
    }

    @Override public void apply(MessageVisitor visitor) {
        visitor.visit(this);
    }

    protected void fields() {
        field(OrigClOrdID.Tag());
        field(ClOrdID.Tag());
        field(Symbol.Tag());
        field(SecurityType.Tag(), Required.NO);
        field(MaturityMonthYear.Tag(), Required.NO);
        field(Side.Tag());
        field(TransactTime.Tag());
        field(OrderQty.Tag());
    }
}
