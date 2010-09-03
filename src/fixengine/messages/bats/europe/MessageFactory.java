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
package fixengine.messages.bats.europe;

import fixengine.messages.Message;
import fixengine.messages.MsgTypeValue;
import fixengine.messages.MessageHeader;

import static fixengine.messages.MsgTypeValue.EXECUTION_REPORT;
import static fixengine.messages.MsgTypeValue.NEW_ORDER_SINGLE;
import static fixengine.messages.MsgTypeValue.ORDER_CANCEL_REJECT;
import static fixengine.messages.MsgTypeValue.ORDER_CANCEL_REQUEST;
import static fixengine.messages.MsgTypeValue.ORDER_MODIFICATION_REQUEST;

public class MessageFactory extends fixengine.messages.DefaultMessageFactory {
    @Override public Message create(MsgTypeValue type) {
        switch (type) {
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
        return super.create(type);
    }

    @Override public Message create(MsgTypeValue type, MessageHeader header) {
        switch (type) {
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
        }
        return super.create(type, header);
    }

    @Override public String getTagsPackage() {
      return "fixengine.tags.bats.europe";
    }
}
