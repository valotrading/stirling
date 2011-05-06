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
package fixengine.messages.fix42;

import fixengine.messages.AbstractMessage;
import fixengine.messages.MessageHeader;
import fixengine.messages.MessageVisitor;
import fixengine.messages.RepeatingGroup;
import fixengine.messages.RepeatingGroupInstance;
import fixengine.messages.Required;
import fixengine.tags.fix42.AllocAccount;
import fixengine.tags.fix42.AllocID;
import fixengine.tags.fix42.AllocShares;
import fixengine.tags.fix42.AllocTransType;
import fixengine.tags.fix42.AvgPx;
import fixengine.tags.fix42.ClOrdID;
import fixengine.tags.fix42.NoAllocs;
import fixengine.tags.fix42.NoOrders;
import fixengine.tags.fix42.Shares;
import fixengine.tags.fix42.Side;
import fixengine.tags.fix42.Symbol;
import fixengine.tags.fix42.TradeDate;

public class Allocation extends AbstractMessage {
    protected Allocation(MessageHeader header) {
        super(header);

        field(AllocID.Tag());
        field(AllocTransType.Tag());
        field(NoOrders.Tag());          // TODO: repeating group
        field(ClOrdID.Tag());
        field(Side.Tag());
        field(Symbol.Tag());
        field(Shares.Tag());
        field(AvgPx.Tag());
        field(TradeDate.Tag());
        group(new RepeatingGroup(NoAllocs.Tag()) {
            @Override protected RepeatingGroupInstance newInstance() {
                return new RepeatingGroupInstance(AllocAccount.Tag()) {
                    {
                        field(AllocShares.Tag());
                    }
                };
            }
        }, Required.NO);
    }

    @Override public void apply(MessageVisitor visitor) {
        visitor.visit(this);
    }
}
