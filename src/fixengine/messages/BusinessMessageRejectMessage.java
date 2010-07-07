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
public class BusinessMessageRejectMessage extends AbstractMessage {
    private final BusinessRejectReasonField businessRejectReason = new BusinessRejectReasonField(); 
    private final RefSeqNoField refSeqNo = new RefSeqNoField(Required.NO);
    private final RefMsgTypeField refMsgType = new RefMsgTypeField();
    private final TextField text = new TextField(Required.NO);

    public BusinessMessageRejectMessage() {
        this(new MessageHeader(MessageType.BUSINESS_MESSAGE_REJECT));
    }

    public BusinessMessageRejectMessage(MessageHeader header) {
        super(header);

        add(refSeqNo);
        add(refMsgType);
        add(businessRejectReason);
        add(text);
    }

    @Override
    public void apply(MessageVisitor visitor) {
        visitor.visit(this);
    }

    public void setRefSeqNo(int refSeqNo) {
        this.refSeqNo.setValue(refSeqNo);
    }

    public void setBusinessRejectReason(BusinessRejectReason reason) {
        this.businessRejectReason.setValue(reason);
    }

    public void setText(String text) {
        this.text.setValue(text);
    }

    public void setRefMsgType(String refMsgType) {
        this.refMsgType.setValue(refMsgType);
    }
}