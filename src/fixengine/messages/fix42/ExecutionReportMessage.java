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
import fixengine.tags.fix42.AvgPx;
import fixengine.tags.fix42.ClOrdID;
import fixengine.tags.fix42.CumQty;
import fixengine.tags.fix42.Currency;
import fixengine.tags.fix42.ExDestination;
import fixengine.tags.fix42.ExecID;
import fixengine.tags.fix42.ExecTransType;
import fixengine.tags.fix42.ExecType;
import fixengine.tags.fix42.LastMkt;
import fixengine.tags.fix42.LastPx;
import fixengine.tags.fix42.LastShares;
import fixengine.tags.fix42.LeavesQty;
import fixengine.tags.fix42.MaturityMonthYear;
import fixengine.tags.fix42.OrdRejReason;
import fixengine.tags.fix42.OrdStatus;
import fixengine.tags.fix42.OrdType;
import fixengine.tags.fix42.OrderID;
import fixengine.tags.fix42.OrderQty;
import fixengine.tags.fix42.OrigClOrdID;
import fixengine.tags.fix42.Price;
import fixengine.tags.fix42.SecurityType;
import fixengine.tags.fix42.Side;
import fixengine.tags.fix42.Symbol;
import fixengine.tags.fix42.Text;
import fixengine.tags.fix42.TimeInForce;
import fixengine.tags.fix42.TransactTime;
import fixengine.tags.fix43.ExecRestatementReason;

/**
 * @author Pekka Enberg 
 */
public class ExecutionReportMessage extends AbstractMessage implements fixengine.messages.ExecutionReportMessage {
    public ExecutionReportMessage(MessageHeader header) {
        super(header);

        fields();
    }

    @Override public void apply(MessageVisitor visitor) {
        visitor.visit(this);
    }

    protected void fields() {
        field(OrderID.Tag());
        field(ClOrdID.Tag(), Required.NO);
        field(OrigClOrdID.Tag(), Required.NO);
        field(ExecID.Tag());
        field(ExecTransType.Tag());
        field(ExecType.Tag());
        field(OrdStatus.Tag());
        field(OrdRejReason.Tag(), Required.NO);
        field(ExecRestatementReason.Tag(), Required.NO);
        field(Symbol.Tag());
        field(SecurityType.Tag(), Required.NO);
        field(MaturityMonthYear.Tag(), Required.NO);
        field(Side.Tag());
        field(OrderQty.Tag());
        field(LastShares.Tag(), Required.NO);
        field(LastPx.Tag(), Required.NO);
        field(LeavesQty.Tag());
        field(OrdType.Tag(), Required.NO);
        field(Price.Tag(), new Required() {
            @Override public boolean isRequired() {
                return OrdType.Limit().equals(getEnum(OrdType.Tag()));
            }
        });
        field(TimeInForce.Tag(), Required.NO);
        field(CumQty.Tag());
        field(AvgPx.Tag());
        field(TransactTime.Tag(), Required.NO);
        field(Text.Tag(), Required.NO);
        field(ExDestination.Tag(), Required.NO);
        field(LastMkt.Tag(), Required.NO);
        field(Currency.Tag(), Required.NO);
    }
}
