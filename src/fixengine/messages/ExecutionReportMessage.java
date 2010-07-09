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
package fixengine.messages;

import fixengine.tags.AvgPx;
import fixengine.tags.ClOrdID;
import fixengine.tags.CumQty;
import fixengine.tags.Currency;
import fixengine.tags.ExDestination;
import fixengine.tags.ExecID;
import fixengine.tags.ExecRestatementReason;
import fixengine.tags.ExecTransType;
import fixengine.tags.ExecType;
import fixengine.tags.LastMkt;
import fixengine.tags.LastPx;
import fixengine.tags.LastQty;
import fixengine.tags.LeavesQty;
import fixengine.tags.MaturityMonthYear;
import fixengine.tags.OrdRejReason;
import fixengine.tags.OrdStatus;
import fixengine.tags.OrdType;
import fixengine.tags.OrderID;
import fixengine.tags.OrderQty;
import fixengine.tags.OrigClOrdID;
import fixengine.tags.Price;
import fixengine.tags.SecurityType;
import fixengine.tags.Side;
import fixengine.tags.Symbol;
import fixengine.tags.Text;
import fixengine.tags.TimeInForce;
import fixengine.tags.TransactTime;

/**
 * @author Pekka Enberg 
 */
public class ExecutionReportMessage extends AbstractMessage {
    private final StringField maturityMonthYear = new StringField(MaturityMonthYear.TAG, Required.NO);
    private final StringField exDestination = new StringField(ExDestination.TAG, Required.NO);
    private final StringField securityType = new StringField(SecurityType.TAG, Required.NO);
    private final UtcTimestampField transactTime = new UtcTimestampField(TransactTime.TAG, Required.NO);
    private final StringField origClOrdId = new StringField(OrigClOrdID.TAG, Required.NO);
    private final StringField execTransType = new StringField(ExecTransType.TAG);
    private final StringField currency = new StringField(Currency.TAG, Required.NO);
    private final StringField clOrdId = new StringField(ClOrdID.TAG, Required.NO);
    private final StringField lastMkt = new StringField(LastMkt.TAG, Required.NO);
    private final FloatField lastQty = new FloatField(LastQty.TAG, Required.NO);
    private final FloatField lastPx = new FloatField(LastPx.TAG, Required.NO);
    private final FloatField leavesQty = new FloatField(LeavesQty.TAG);
    private final FloatField price = new FloatField(Price.TAG, Required.NO);
    private final FloatField orderQty = new FloatField(OrderQty.TAG);
    private final StringField text = new StringField(Text.TAG, Required.NO);
    private final StringField orderId = new StringField(OrderID.TAG);
    private final FloatField cumQty = new FloatField(CumQty.TAG);
    private final StringField execId = new StringField(ExecID.TAG);
    private final StringField symbol = new StringField(Symbol.TAG);
    private final FloatField avgPx = new FloatField(AvgPx.TAG);

    public ExecutionReportMessage() {
        this(new MessageHeader(MsgTypeValue.EXECUTION_REPORT));
    }

    public ExecutionReportMessage(MessageHeader header) {
        super(header);

        add(orderId);
        add(clOrdId);
        add(origClOrdId);
        add(execId);
        add(execTransType);
        field(ExecType.TAG);
        field(OrdStatus.TAG);
        field(OrdRejReason.TAG, Required.NO);
        field(ExecRestatementReason.TAG, Required.NO);
        add(symbol);
        add(securityType);
        add(maturityMonthYear);
        field(Side.TAG);
        add(orderQty);
        add(lastQty);
        add(lastPx);
        add(leavesQty);
        field(OrdType.TAG, Required.NO);
        add(price);
        field(TimeInForce.TAG, Required.NO);
        add(cumQty);
        add(avgPx);
        add(transactTime);
        add(text);
        add(exDestination);
        add(lastMkt);
        add(currency);
    }

    public void setOrderId(String orderId) {
        this.orderId.setValue(orderId);
    }

    public void setClOrdId(String clOrdId) {
        this.clOrdId.setValue(clOrdId);
    }

    public String getClOrdId() {
        return clOrdId.getValue();
    }

    public void setOrigClOrdId(String origClOrdId) {
        this.origClOrdId.setValue(origClOrdId);
    }

    public String getOrigClOrdId() {
        return origClOrdId.getValue();
    }

    public String getText() {
        return text.getValue();
    }

    public void setExecId(String execId) {
        this.execId.setValue(execId);
    }

    public void setSymbol(String symbol) {
        this.symbol.setValue(symbol);
    }

    public void setOrderQty(double orderQty) {
        this.orderQty.setValue(orderQty);
    }

    public void setLeavesQty(double leavesQty) {
        this.leavesQty.setValue(leavesQty);
    }

    public void setCumQty(double cumQty) {
        this.cumQty.setValue(cumQty);
    }

    public void setAvgPx(double avgPx) {
        this.avgPx.setValue(avgPx);
    }

    @Override
    public void apply(MessageVisitor visitor) {
        visitor.visit(this);
    }
}
