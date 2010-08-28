/*
 * Copyright 2010 the original author or authors.
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

public class DefaultMessageFactory implements MessageFactory {
    @Override public Message create(MsgTypeValue type) {
        switch (type) {
        case LOGON:
            return new LogonMessage();
        case LOGOUT:
            return new LogoutMessage();
        case HEARTBEAT:
            return new HeartbeatMessage();
        case RESEND_REQUEST:
            return new ResendRequestMessage();
        case SEQUENCE_RESET:
            return new SequenceResetMessage();
        case TEST_REQUEST:
            return new TestRequestMessage();
        case REJECT:
            return new RejectMessage();
        case BUSINESS_MESSAGE_REJECT:
            return new BusinessMessageRejectMessage();
        case EXECUTION_REPORT:
            return new ExecutionReportMessage();
        case ORDER_CANCEL_REJECT:
            return new OrderCancelRejectMessage();
        case NEW_ORDER_SINGLE:
            return new NewOrderSingleMessage();
        case ORDER_CANCEL_REQUEST:
            return new OrderCancelRequestMessage();
        case ORDER_MODIFICATION_REQUEST:
            return new OrderModificationRequestMessage();
        }
        throw new RuntimeException("unknown message type: " + type);
    }

    @Override public Message create(MsgTypeValue type, MessageHeader header) {
        switch (type) {
        case LOGON:
            return new LogonMessage(header);
        case LOGOUT:
            return new LogoutMessage(header);
        case HEARTBEAT:
            return new HeartbeatMessage(header);
        case RESEND_REQUEST:
            return new ResendRequestMessage(header);
        case SEQUENCE_RESET:
            return new SequenceResetMessage(header);
        case TEST_REQUEST:
            return new TestRequestMessage(header);
        case REJECT:
            return new RejectMessage(header);
        case BUSINESS_MESSAGE_REJECT:
            return new BusinessMessageRejectMessage(header);
        case EXECUTION_REPORT:
            return new ExecutionReportMessage(header);
        case ORDER_CANCEL_REJECT:
            return new OrderCancelRejectMessage(header);
        case NEW_ORDER_SINGLE:
            return new NewOrderSingleMessage(header);
        case ORDER_CANCEL_REQUEST:
            return new OrderCancelRequestMessage(header);
        case ORDER_MODIFICATION_REQUEST:
            return new OrderModificationRequestMessage(header);
        case ALLOCATION_INSTRUCTION:
            return new AllocationMessage(header);
        }
        throw new RuntimeException("unknown message type: " + type);
    }
}
