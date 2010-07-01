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

/**
 * @author Pekka Enberg 
 */
public class ExecutionReportMessage extends AbstractMessage {
    private final ExecRestatementReasonField execRestatementReason = new ExecRestatementReasonField(Required.NO);
    private final MaturityMonthYearField maturityMonthYear = new MaturityMonthYearField(Required.NO);
    private final ExDestinationField exDestination = new ExDestinationField(Required.NO);
    private final PartiesRepeatingGroup parties = new PartiesRepeatingGroup(Required.NO);
    private final OrdRejReasonField ordRejReason = new OrdRejReasonField(Required.NO);
    private final SecurityTypeField securityType = new SecurityTypeField(Required.NO);
    private final TransactTimeField transactTime = new TransactTimeField(Required.NO);
    private final OrigClOrdIdField origClOrdId = new OrigClOrdIdField(Required.NO);
    private final TimeInForceField timeInForce = new TimeInForceField(Required.NO);
    private final ExecTransTypeField execTransType = new ExecTransTypeField();
    private final CurrencyField currency = new CurrencyField(Required.NO);
    private final ClOrdIdField clOrdId = new ClOrdIdField(Required.NO);
    private final LastMktField lastMkt = new LastMktField(Required.NO);
    private final LastQtyField lastQty = new LastQtyField(Required.NO);
    private final OrdTypeField ordType = new OrdTypeField(Required.NO);
    private final LastPxField lastPx = new LastPxField(Required.NO);
    private final LeavesQtyField leavesQty = new LeavesQtyField();
    private final OrdStatusField ordStatus = new OrdStatusField();
    private final PriceField price = new PriceField(Required.NO);
    private final ExecTypeField execType = new ExecTypeField();
    private final OrderQtyField orderQty = new OrderQtyField();
    private final TextField text = new TextField(Required.NO);
    private final OrderIdField orderId = new OrderIdField();
    private final CumQtyField cumQty = new CumQtyField();
    private final ExecIdField execId = new ExecIdField();
    private final SymbolField symbol = new SymbolField();
    private final AvgPxField avgPx = new AvgPxField();
    private final SideField side = new SideField();

    public ExecutionReportMessage() {
        this(new MessageHeader(MsgType.EXECUTION_REPORT));
    }

    public ExecutionReportMessage(MessageHeader header) {
        super(header);

        add(orderId);
        add(clOrdId);
        add(origClOrdId);
        add(parties);
        add(execId);
        add(execTransType);
        add(execType);
        add(ordStatus);
        add(ordRejReason);
        add(execRestatementReason);
        add(symbol);
        add(securityType);
        add(maturityMonthYear);
        add(side);
        add(orderQty);
        add(lastQty);
        add(lastPx);
        add(leavesQty);
        add(ordType);
        add(price);
        add(timeInForce);
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

    public void setOrdStatus(OrdStatus ordStatus) {
        this.ordStatus.setValue(ordStatus);
    }

    public OrdStatus getOrdStatus() {
        return ordStatus.getValue();
    }

    public void setExecId(String execId) {
        this.execId.setValue(execId);
    }

    public void setExecType(ExecType execType) {
        this.execType.setValue(execType);
    }

    public ExecType getExecType() {
        return execType.getValue();
    }

    public ExecRestatementReason getExecRestatementReason() {
        return execRestatementReason.getValue();
    }

    public void setSymbol(String symbol) {
        this.symbol.setValue(symbol);
    }

    public void setOrdType(OrdType ordType) {
        this.ordType.setValue(ordType);
    }

    public void setSide(Side side) {
        this.side.setValue(side);
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

    public void add(Party party) {
        this.parties.add(party);
    }

    @Override
    public void apply(MessageVisitor visitor) {
        visitor.visit(this);
    }
}
