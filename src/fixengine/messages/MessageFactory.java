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

public interface MessageFactory {
    Message create(MsgTypeValue type);
    Message create(MsgTypeValue type, MessageHeader header);
/*
    LogonMessage logonMessage();
    LogonMessage logonMessage(MessageHeader header);

    LogoutMessage logoutMessage();
    LogoutMessage logoutMessage(MessageHeader header);

    HeartbeatMessage heartbeatMessage();
    HeartbeatMessage heartbeatMessage(MessageHeader header);

    ResendRequestMessage resendRequestMessage();
    ResendRequestMessage resendRequestMessage(MessageHeader header);

    SequenceResetMessage sequenceResetMessage();
    SequenceResetMessage sequenceResetMessage(MessageHeader header);

    TestRequestMessage testRequestMessage();
    TestRequestMessage testRequestMessage(MessageHeader header);

    RejectMessage rejectMessage();
    RejectMessage rejectMessage(MessageHeader header);

    BusinessMessageRejectMessage businessMessageRejectMessage();
    BusinessMessageRejectMessage businessMessageRejectMessage(MessageHeader header);

    ExecutionReportMessage executionReportMessage(MessageHeader header);
    OrderCancelRejectMessage orderCancelRejectMessage(MessageHeader header);
    NewOrderSingleMessage newOrderSingleMessage(MessageHeader header);
    OrderCancelRequestMessage orderCancelRequestMessage(MessageHeader header);
    OrderModificationRequestMessage orderModificationRequestMessage(MessageHeader header);
    AllocationMessage allocationMessage(MessageHeader header);
*/
}
