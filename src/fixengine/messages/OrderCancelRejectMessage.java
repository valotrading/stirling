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

import fixengine.tags.ClOrdID;
import fixengine.tags.CxlRejReason;
import fixengine.tags.CxlRejResponseTo;
import fixengine.tags.OrdStatus;
import fixengine.tags.OrderID;
import fixengine.tags.OrigClOrdID;
import fixengine.tags.Text;

/**
 * @author Pekka Enberg 
 */
public class OrderCancelRejectMessage extends AbstractMessage {
    private final StringField origClOrdId = new StringField(OrigClOrdID.TAG);
    private final StringField text = new StringField(Text.TAG, Required.NO);
    private final StringField orderId = new StringField(OrderID.TAG);
    private final StringField clOrdId = new StringField(ClOrdID.TAG);

    public OrderCancelRejectMessage() {
        this(new MessageHeader(MsgTypeValue.ORDER_CANCEL_REJECT));
    }

    public OrderCancelRejectMessage(MessageHeader header) {
        super(header);

        add(orderId);
        add(clOrdId);
        add(origClOrdId);
        field(OrdStatus.TAG);
        field(CxlRejResponseTo.TAG);
        field(CxlRejReason.TAG, Required.NO);
        add(text);
    }

    public String getOrderId() {
        return orderId.getValue();
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

    @Override
    public void apply(MessageVisitor visitor) {
        visitor.visit(this);
    }
}
