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
import fixengine.tags.Price;
import fixengine.tags.SecurityType;
import fixengine.tags.Symbol;
import fixengine.tags.TransactTime;

/**
 * This class represents the Order Cancel/Replace Request (a.k.a. Order
 * Modification Request) message.
 * 
 * @author Pekka Enberg
 */
public class OrderModificationRequestMessage extends AbstractMessage implements RequestMessage, CancelRequestMessage {
    private final StringField maturityMonthYear = new StringField(MaturityMonthYear.TAG, Required.NO);
    private final StringField securityType = new StringField(SecurityType.TAG, Required.NO);
    private final UtcTimestampField transactTime = new UtcTimestampField(TransactTime.TAG);
    private final StringField currency = new StringField(Currency.TAG, Required.NO);
    private final StringField origClOrdId = new StringField(OrigClOrdID.TAG);
    private final HandlInstField handlInst = new HandlInstField();
    private final FloatField price = new FloatField(Price.TAG, Required.NO);
    private final FloatField orderQty = new FloatField(OrderQty.TAG);
    private final StringField clOrdId = new StringField(ClOrdID.TAG);
    private final OrdTypeField ordType = new OrdTypeField();
    private final StringField symbol = new StringField(Symbol.TAG);
    private final SideField side = new SideField();

    public OrderModificationRequestMessage() {
        this(new MessageHeader(MessageType.ORDER_MODIFICATION_REQUEST));
    }

    public OrderModificationRequestMessage(MessageHeader header) {
        super(header);

        add(origClOrdId);
        add(clOrdId);
        add(handlInst);
        add(symbol);
        add(securityType);
        add(maturityMonthYear);
        add(side);
        add(transactTime);
        add(orderQty);
        add(ordType);
        add(currency);
        add(price);
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

    public void setHandlInst(HandlInst handlInst) {
        this.handlInst.setValue(handlInst);
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

    public void setSide(Side side) {
        this.side.setValue(side);
    }

    public Side getSide() {
        return side.getValue();
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

    public void setOrdType(OrdType ordType) {
        this.ordType.setValue(ordType);
    }

    public void setCurrency(String currency) {
        this.currency.setValue(currency);
    }
    
    public void setPrice(double price) {
        this.price.setValue(price);
    }

    public OrdType getOrdType() {
        return this.ordType.getValue();
    }

    @Override
    public void apply(MessageVisitor visitor) {
        visitor.visit(this);
    }
}
