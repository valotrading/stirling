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

import static fixengine.examples.exchange.HasOrdStatus.hasOrdStatus;
import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.jmock.Expectations;
import org.junit.runner.RunWith;

import fixengine.messages.NewOrderSingleMessage;
import fixengine.messages.OrdStatus;
import fixengine.messages.OrdType;
import fixengine.messages.OrderCancelRejectMessage;
import fixengine.messages.OrderCancelRequestMessage;
import fixengine.messages.OrderModificationRequestMessage;
import fixengine.messages.Session;

/**
 * @author Pekka Enberg 
 */
@RunWith(JDaveRunner.class)
public class ExchangeMessageProcessorSpec extends Specification<ExchangeMessageProcessor> {
    private final OrderStore store = mock(OrderStore.class);
    private final Session session = mock(Session.class);
    private ExchangeMessageProcessor processor;

    public class NewOrderSingle {
        public NewOrderSingleMessage message = new NewOrderSingleMessage();
        
        public ExchangeMessageProcessor create() {
            message.setOrdType(OrdType.LIMIT);
            message.setOrderQty(1.0);
            return processor;
        }

        public void storesOrderAndSendsExecutionReportWithOrdStatusNew() {
            checking(new Expectations() {{
                one(store).put(with(any(Order.class))); will(returnValue(true));
                one(session).send(with(hasOrdStatus(OrdStatus.NEW)));
            }});
            processor.visit(message);
       }
    }

    public class NewOrderSingleWithDuplicateClOrdId {
       public void sendsExecutionReportWithOrdStatusRejected() {
           checking(new Expectations() {{
               one(store).put(with(any(Order.class))); will(returnValue(false));
               one(session).send(with(hasOrdStatus(OrdStatus.REJECTED)));
           }});
           NewOrderSingleMessage message = new NewOrderSingleMessage();
           message.setOrdType(OrdType.LIMIT);
           message.setOrderQty(1.0);
           processor.visit(message);
       }
    }

    public class OrderCancelRequest {
        public OrderCancelRequestMessage message = new OrderCancelRequestMessage();
        
        public ExchangeMessageProcessor create() {
            message.setOrigClOrdId("orig-id");
            message.setOrderQty(1.0);
            return processor;
        }

        public void isRejectedIfOrderIdIsUnknown() {
            checking(new Expectations() {{
                one(store).remove("orig-id"); will(returnValue(false));
                one(session).send(with(any(OrderCancelRejectMessage.class)));
            }});
            processor.visit(message);
        }

        public void storesOrderAndSendsExecutionReportWithOrdStatusNew() {
            checking(new Expectations() {{
                one(store).remove("orig-id"); will(returnValue(true));
                one(session).send(with(hasOrdStatus(OrdStatus.CANCELED)));
            }});
            processor.visit(message);
       }
    }

    public class OrderModificationRequest {
        public OrderModificationRequestMessage message = new OrderModificationRequestMessage();
        
        public ExchangeMessageProcessor create() {
            message.setOrigClOrdId("orig-id");
            message.setClOrdId("new-id");
            message.setOrderQty(1.0);
            return processor;
        }

        public void isRejectedIfOrderIdIsUnknown() {
            checking(new Expectations() {{
                one(store).replace(with("orig-id"), with(any(Order.class))); will(returnValue(false));
                one(session).send(with(any(OrderCancelRejectMessage.class)));
            }});
            processor.visit(message);
        }

        public void storesOrderAndSendsExecutionReportWithOrdStatusNew() {
            checking(new Expectations() {{
                one(store).replace(with("orig-id"), with(any(Order.class))); will(returnValue(true));
                one(session).send(with(hasOrdStatus(OrdStatus.REPLACED)));
            }});
            processor.visit(message);
       }
    }
   
    @Override
    public void create() throws Exception {
        processor = new ExchangeMessageProcessor(session, store);
   }
}