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

import org.joda.time.DateTime;

import fixengine.tags.ClOrdID;
import fixengine.tags.Currency;
import fixengine.tags.MaturityMonthYear;
import fixengine.tags.OrderQty;
import fixengine.tags.OrigClOrdID;
import fixengine.tags.SecurityType;
import fixengine.tags.Side;
import fixengine.tags.Symbol;
import fixengine.tags.TransactTime;

/**
 * @author Pekka Enberg 
 */
public class OrderCancelRequestMessage extends AbstractMessage implements RequestMessage, CancelRequestMessage {
    private final StringField maturityMonthYear = new StringField(MaturityMonthYear.TAG, Required.NO);
    private final StringField securityType = new StringField(SecurityType.TAG, Required.NO);
    private final UtcTimestampField transactTime = new UtcTimestampField(TransactTime.TAG);
    private final StringField currency = new StringField(Currency.TAG, Required.NO);
    private final StringField origClOrdId = new StringField(OrigClOrdID.TAG);
    private final FloatField orderQty = new FloatField(OrderQty.TAG);
    private final StringField clOrdId = new StringField(ClOrdID.TAG);
    private final StringField symbol = new StringField(Symbol.TAG);

    public OrderCancelRequestMessage() {
        this(new MessageHeader(MsgTypeValue.ORDER_CANCEL_REQUEST));
    }

    public OrderCancelRequestMessage(MessageHeader header) {
        super(header);

        add(origClOrdId);
        add(clOrdId);
        add(symbol);
        add(securityType);
        add(maturityMonthYear);
        field(Side.TAG);
        add(transactTime);
        add(orderQty);
    }

    public void setOrigClOrdId(String origClOrdId) {
        this.origClOrdId.setValue(origClOrdId);
    }

    public String getOrigClOrdId() {
        return origClOrdId.getValue();
    }

    public void setClOrdId(String clOrdId) {
        this.clOrdId.setValue(clOrdId);
    }

    public String getClOrdId() {
        return clOrdId.getValue();
    }

    public void setCurrency(String currency) {
        this.currency.setValue(currency);
    }

    public void setSymbol(String symbol) {
        this.symbol.setValue(symbol);
    }

    public String getSymbol() {
        return symbol.getValue();
    }

    public void setSecurityType(String securityType) {
        this.securityType.setValue(securityType);
    }

    public void setMaturityMonthYear(String maturityMonthYear) {
        this.maturityMonthYear.setValue(maturityMonthYear);
    }
    
    public void setTransactTime(DateTime transactTime) {
        this.transactTime.setValue(transactTime);
    }

    public void setOrderQty(double orderQty) {
        this.orderQty.setValue(orderQty);
    }

    public double getOrderQty() {
        return orderQty.getValue();
    }

    public OrdTypeValue getOrdType() {
        return null;
    }

    @Override public SideValue getSide() {
        return getEnum(Side.TAG);
    }

    @Override
    public void apply(MessageVisitor visitor) {
        visitor.visit(this);
    }
}
