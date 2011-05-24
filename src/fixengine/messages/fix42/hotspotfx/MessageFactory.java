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
package fixengine.messages.fix42.hotspotfx;

import fixengine.messages.MsgTypeValue;

public class MessageFactory extends fixengine.messages.fix42.DefaultMessageFactory {
    public MessageFactory() {
        message(MsgTypeValue.EXECUTION_REPORT, ExecutionReport.class);
        message(MsgTypeValue.ORDER_CANCEL_REJECT, OrderCancelReject.class);
        message(MsgTypeValue.NEW_ORDER_SINGLE, NewOrderSingle.class);
        message(MsgTypeValue.ORDER_CANCEL_REQUEST, OrderCancelRequest.class);
        message(MsgTypeValue.ORDER_MODIFICATION_REQUEST, OrderModificationRequest.class);
        message(MsgTypeValue.ORDER_STATUS_REQUEST, OrderStatusRequest.class);
        message(MsgTypeValue.LOGON, Logon.class);
    }

    @Override public String getProfile() {
        return "hotspot-fx";
    }
}
