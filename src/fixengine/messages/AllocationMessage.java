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

import fixengine.tags.AllocAccount;
import fixengine.tags.AllocID;
import fixengine.tags.AllocShares;
import fixengine.tags.AllocTransType;
import fixengine.tags.AvgPx;
import fixengine.tags.ClOrdID;
import fixengine.tags.NoAllocs;
import fixengine.tags.NoOrders;
import fixengine.tags.Shares;
import fixengine.tags.Side;
import fixengine.tags.Symbol;
import fixengine.tags.TradeDate;

public class AllocationMessage extends AbstractMessage {
    protected AllocationMessage(MessageHeader header) {
        super(header);

        field(AllocID.TAG);
        field(AllocTransType.TAG);
        field(NoOrders.TAG);          // TODO: repeating group
        field(ClOrdID.TAG);
        field(Side.TAG);
        field(Symbol.TAG);
        field(Shares.TAG);
        field(AvgPx.TAG);
        field(TradeDate.TAG);
        group(new RepeatingGroup(NoAllocs.TAG) {
            @Override public RepeatingGroupInstance newInstance() {
                return new RepeatingGroupInstance(AllocAccount.TAG) {
                    {
                        field(AllocShares.TAG);
                    }
                };
            }
        });
    }

    @Override public void apply(MessageVisitor visitor) {
        visitor.visit(this);
    }
}
