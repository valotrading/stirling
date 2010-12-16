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
import fixengine.messages.Value;
import fixengine.tags.AvgPx;
import fixengine.tags.ClOrdID;
import fixengine.tags.CumQty;
import fixengine.tags.ExecID;
import fixengine.tags.ExecRefID;
import fixengine.tags.ExecType;
import fixengine.tags.fix42.LastCapacity;
import fixengine.tags.LastMkt;
import fixengine.tags.LastPx;
import fixengine.tags.LastShares;
import fixengine.tags.LeavesQty;
import fixengine.tags.OrdStatus;
import fixengine.tags.OrderID;
import fixengine.tags.OrderQty;
import fixengine.tags.OrigClOrdID;
import fixengine.tags.fix42.Side;
import fixengine.tags.Symbol;
import fixengine.tags.TransactTime;
import fixengine.tags.fix42.ExecTransType;

public class ExecutionReportMessage extends fixengine.messages.fix42.ExecutionReportMessage {
    public ExecutionReportMessage(MessageHeader header) {
        super(header);
    }

    @Override protected void fields() {
        field(OrderID.Tag());
        field(ClOrdID.Tag());
        field(OrigClOrdID.Tag(), Required.NO);
        field(ExecID.Tag());
        field(ExecType.Tag());
        field(ExecTransType.Tag());
        field(ExecRefID.Tag(), new Required() {
            @Override public boolean isRequired() {
                Value<?> value = getEnum(ExecTransType.Tag());
                return value.equals(ExecTransType.Cancel()) || value.equals(ExecTransType.Correct());
            }
        });
        field(Symbol.Tag());
        field(Side.Tag());
        field(OrderQty.TAG);
        field(OrdStatus.Tag());
        field(LastShares.TAG, Required.NO);
        field(LastPx.TAG, Required.NO);
        field(LastMkt.Tag(), Required.NO);
        field(LastCapacity.Tag(), Required.NO);
        field(LeavesQty.TAG);
        field(CumQty.TAG);
        field(AvgPx.TAG);
        field(TransactTime.TAG);
    }
}
