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

/**
 * This class represents the Order Cancel/Replace Request (a.k.a. Order
 * Modification Request) message.
 * 
 * @author Pekka Enberg
 */
public class OrderModificationRequestMessage extends AbstractMessage implements RequestMessage, CancelRequestMessage {
    private final MaturityMonthYearField maturityMonthYear = new MaturityMonthYearField(Required.NO);
    private final SecurityTypeField securityType = new SecurityTypeField(Required.NO);
    private final TransactTimeField transactTime = new TransactTimeField();
    private final CurrencyField currency = new CurrencyField(Required.NO);
    private final OrigClOrdIdField origClOrdId = new OrigClOrdIdField();
    private final HandlInstField handlInst = new HandlInstField();
    private final PriceField price = new PriceField(Required.NO);
    private final OrderQtyField orderQty = new OrderQtyField();
    private final ClOrdIdField clOrdId = new ClOrdIdField();
    private final OrdTypeField ordType = new OrdTypeField();
    private final SymbolField symbol = new SymbolField();
    private final SideField side = new SideField();

    public OrderModificationRequestMessage() {
        this(new MessageHeader(MsgType.ORDER_MODIFICATION_REQUEST));
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
