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
package fixengine.examples.exchange;

import fixengine.messages.CancelRequestMessage;
import fixengine.messages.DefaultMessageVisitor;
import fixengine.messages.ExecType;
import fixengine.messages.ExecutionReportMessage;
import fixengine.messages.Message;
import fixengine.messages.NewOrderSingleMessage;
import fixengine.messages.OrdStatus;
import fixengine.messages.OrderCancelRejectMessage;
import fixengine.messages.OrderCancelRequestMessage;
import fixengine.messages.OrderModificationRequestMessage;
import fixengine.messages.Party;
import fixengine.messages.PartyRole;
import fixengine.messages.RequestMessage;
import fixengine.messages.Session;

/**
 * @author Pekka Enberg 
 */
public class ExchangeMessageProcessor extends DefaultMessageVisitor {
    private final OrderStore store;
    private final Session session;

    public ExchangeMessageProcessor(Session session, OrderStore store) {
        this.session = session;
        this.store = store;
    }

    @Override
    public void defaultAction(Message message) {
        System.out.println(message);
    }

    @Override
    public void visit(NewOrderSingleMessage message) {
        Order order = new Order.Builder(message.getClOrdId()).build();
        Message response = null;
        if (store.put(order)) {
            response = newExecReport(message, ExecType.NEW, OrdStatus.NEW);
        } else {
            response = newExecReport(message, ExecType.REJECTED, OrdStatus.REJECTED);
        }
        session.send(response);
    }

    @Override
    public void visit(OrderCancelRequestMessage message) {
        Message response = null;
        if (store.remove(message.getOrigClOrdId())) {
            response = newExecReport(message, ExecType.CANCELED, OrdStatus.CANCELED);
        } else {
            response = newCancelReject(message, OrdStatus.NEW);
        }
        session.send(response);
    }

    @Override
    public void visit(OrderModificationRequestMessage message) {
        Order order = new Order.Builder(message.getClOrdId()).build();
        Message response = null;
        if (store.replace(message.getOrigClOrdId(), order)) {
            response = newExecReport(message, ExecType.REPLACE, OrdStatus.REPLACED);
        } else {
            response = newCancelReject(message, OrdStatus.NEW);
        }
        session.send(response);
    }

    private Message newExecReport(RequestMessage message, ExecType type, OrdStatus status) {
        Party party = new Party();
        party.setPartyRole(PartyRole.EXECUTING_SYSTEM);

        ExecutionReportMessage result = new ExecutionReportMessage();
        result.setOrigClOrdId(message.getOrigClOrdId());
        result.setClOrdId(message.getClOrdId());
        result.setOrderId("order-id");
        result.setOrdType(message.getOrdType());
        result.setSide(message.getSide());
        result.add(party);
        result.setExecId("0");
        result.setExecType(type);
        result.setOrdStatus(status);
        result.setSymbol(message.getSymbol());
        result.setOrderQty(message.getOrderQty());
        result.setCumQty(message.getOrderQty());
        result.setLeavesQty(0.0);
        result.setAvgPx(10.0);
        return result;
    }

    private Message newCancelReject(CancelRequestMessage message, OrdStatus status) {
        OrderCancelRejectMessage result = new OrderCancelRejectMessage();
        result.setClOrdId(message.getClOrdId());
        result.setOrigClOrdId(message.getOrigClOrdId());
        result.setOrdStatus(status);
        return result;
    }
}