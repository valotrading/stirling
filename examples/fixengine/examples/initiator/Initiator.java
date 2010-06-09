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
package fixengine.examples.initiator;

import java.net.InetAddress;

import lang.DefaultTimeSource;
import lang.TimeSource;

import org.apache.log4j.PropertyConfigurator;

import fixengine.Config;
import fixengine.Specification;
import fixengine.Version;
import fixengine.client.FixClient;
import fixengine.client.InitiatorProtocolHandler;
import fixengine.io.ProtocolHandler;
import fixengine.messages.CustomerOrFirm;
import fixengine.messages.DefaultMessageVisitor;
import fixengine.messages.ExecutionReportMessage;
import fixengine.messages.HandlInst;
import fixengine.messages.Message;
import fixengine.messages.NewOrderSingleMessage;
import fixengine.messages.OrdStatus;
import fixengine.messages.OrdType;
import fixengine.messages.OrderCancelRejectMessage;
import fixengine.messages.OrderCancelRequestMessage;
import fixengine.messages.OrderModificationRequestMessage;
import fixengine.messages.Side;
import fixengine.session.HeartBtInt;
import fixengine.session.store.SessionStore;
import fixengine.session.store.MongoSessionStore;

/**
 * @author Pekka Enberg
 */
public class Initiator {
    private static final TimeSource timeSource = new DefaultTimeSource();
    private static final String HOST = "localhost";
    private static final int PORT = 4000;
    private static final String SENDER_COMP_ID = "initiator";
    private static final String TARGET_COMP_ID = "acceptor";

    public static void main(String[] args) throws Exception {
        SessionStore store = new MongoSessionStore("localhost", 27017);
        PropertyConfigurator.configure("examples/fixengine/examples/log4j.properties");

        ProtocolHandler handler = newProtocolHandler(getConfig(), new HeartBtInt(30), store);
        FixClient client = new FixClient(InetAddress.getByName(HOST), PORT, handler);
        client.start();
    }

    private static ProtocolHandler newProtocolHandler(Config config, HeartBtInt heartBtInt, SessionStore store) {
        return new InitiatorProtocolHandler(config, heartBtInt, store) {
            @Override
            public void onEstablished() {
                session.logon();
                if (!session.isAuthenticated()) {
                    System.out.println("Authentication failed. Exiting...");
                    System.exit(1);
                }
                System.out.println("Authentication OK");

                String orderId = generateClOrdId();
                submitLimitOrder(orderId);

                String newOrderId = generateClOrdId();
                updateLimitOrder(orderId, newOrderId);

                String cancelId = generateClOrdId();
                cancelLimitOrder(newOrderId, cancelId);

                session.logout();
                if (session.isAuthenticated()) {
                    System.out.println("Logout failed. Exiting...");
                    System.exit(1);
                }
                System.exit(0);
            }

            private void submitLimitOrder(String clOrdId) {
                send(newLimitOrder(clOrdId));
                waitForResponse(new ClOrderIdIsSpecification(clOrdId).and(new OrderStatusIsSpecification(OrdStatus.NEW)));
            }

            private void updateLimitOrder(String origClOrdId, String clOrdId) {
                send(newOrderUpdate(origClOrdId, clOrdId));
                waitForResponse(new ClOrderIdIsSpecification(clOrdId).and(new OrderStatusIsSpecification(OrdStatus.REPLACED)));
            }

            private void cancelLimitOrder(String origClOrdId, String clOrdId) {
                send(newCancelOrder(origClOrdId, clOrdId));
                waitForResponse(new ClOrderIdIsSpecification(clOrdId).and(new OrderStatusIsSpecification(OrdStatus.CANCELED)));
            }

            private void send(Message message) {
                session.send(message);
            }

            private void waitForResponse(Specification<Message> specification) {
                Message response;
                do {
                    response = session.processMessage(new DefaultMessageVisitor());
                    trace(response);
                } while (!specification.isSatisfiedBy(response));
            }
        };
    }

    private static String generateClOrdId() {
        return SENDER_COMP_ID + System.currentTimeMillis();
    }

    private static NewOrderSingleMessage newLimitOrder(String clOrdId) {
        NewOrderSingleMessage result = new NewOrderSingleMessage();
        result.setClOrdId(clOrdId);
        result.setHandlInst(HandlInst.AUTOMATED_ORDER_PRIVATE);
        result.setExDestination("SMART");
        result.setSide(Side.BUY);
        result.setOrdType(OrdType.LIMIT);
        result.setSymbol("AAPL");
        result.setCurrency("USD");
        result.setOrderQty(100);
        result.setPrice(15.0);
        result.setTransactTime(timeSource.currentTime());
        result.setCustomerOrFirm(CustomerOrFirm.CUSTOMER);
        return result;
    }

    private static OrderModificationRequestMessage newOrderUpdate(String origClOrdId, String clOrdId) {
        OrderModificationRequestMessage result = new OrderModificationRequestMessage();
        result.setOrigClOrdId(origClOrdId);
        result.setClOrdId(clOrdId);
        result.setHandlInst(HandlInst.AUTOMATED_ORDER_PRIVATE);
        result.setSide(Side.BUY);
        result.setOrdType(OrdType.LIMIT);
        result.setSymbol("AAPL");
        result.setCurrency("USD");
        result.setOrderQty(100);
        result.setPrice(25.0);
        result.setTransactTime(timeSource.currentTime());
        return result;
    }

    private static OrderCancelRequestMessage newCancelOrder(String origClOrdId, String clOrdId) {
        OrderCancelRequestMessage result = new OrderCancelRequestMessage();
        result.setOrigClOrdId(origClOrdId);
        result.setClOrdId(clOrdId);
        result.setSide(Side.SELL);
        result.setSymbol("DAX");
        result.setSecurityType("FUT");
        result.setMaturityMonthYear("200812");
        result.setOrderQty(1);
        result.setTransactTime(timeSource.currentTime());
        return result;
    }

    private static void trace(Message response) {
        response.apply(new DefaultMessageVisitor() {
            @Override
            public void visit(ExecutionReportMessage message) {
                trace(message);
            }

            @Override
            public void visit(OrderCancelRejectMessage message) {
                trace(message);
            }
        });
    }

    private static void trace(ExecutionReportMessage message) {
        StringBuilder trace = new StringBuilder();
        trace.append("ExecutionReport");
        trace.append(",ClOrdId=" + message.getClOrdId());
        if (message.getOrigClOrdId() != null) {
            trace.append(",OrigClOrdId=" + message.getOrigClOrdId());
        }
        trace.append(",OrdStatus=" + message.getOrdStatus());
        trace.append(",ExecType=" + message.getExecType());
        if (message.getText() != null) {
            trace.append(",Text=" + message.getText());
        }
        if (message.getExecRestatementReason() != null) {
            trace.append(",ExecRestatementReason="
                    + message.getExecRestatementReason());
        }
        System.out.println(trace.toString());
    }

    private static void trace(OrderCancelRejectMessage message) {
        StringBuilder trace = new StringBuilder();
        trace.append("OrderCancelReject");
        trace.append(",OrderId=" + message.getOrderId());
        trace.append(",ClOrdId=" + message.getClOrdId());
        trace.append(",OrigClOrdId=" + message.getOrigClOrdId());
        trace.append(",OrdStatus=" + message.getOrdStatus());
        if (message.getText() != null) {
            trace.append(",Text=" + message.getText());
        }
        System.out.println(trace.toString());
    }

    private static Config getConfig() {
        Config config = new Config();
        config.setSenderCompId(SENDER_COMP_ID);
        config.setTargetCompId(TARGET_COMP_ID);
        config.setVersion(Version.FIX_4_3);
        return config;
    }
}
