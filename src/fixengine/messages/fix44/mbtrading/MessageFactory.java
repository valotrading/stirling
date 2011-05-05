/*
 * Copyright 2011 the original author or authors.
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
package fixengine.messages.fix44.mbtrading;

import static fixengine.messages.fix44.mbtrading.MsgTypeValue.*;

import fixengine.messages.MsgTypeValue;
import fixengine.messages.fix44.mbtrading.LogonMessage;

public class MessageFactory extends fixengine.messages.fix42.DefaultMessageFactory {
    public MessageFactory() {
        message(MsgTypeValue.EXECUTION_REPORT, ExecutionReportMessage.class);
        message(MsgTypeValue.ORDER_CANCEL_REQUEST, OrderCancelRequest.class);
        message(MsgTypeValue.BUSINESS_MESSAGE_REJECT, BusinessMessageReject.class);
        message(MsgTypeValue.LOGON, Logon.class);
        message(MsgTypeValue.NEW_ORDER_SINGLE, NewOrderSingle.class);
        message(MsgTypeValue.ORDER_MODIFICATION_REQUEST, OrderModificationRequest.class);
        message(MsgTypeValue.LOGON, LogonMessage.class);
        message(NEW_ORDER_MULTILEG, NewOrderMultiLeg.class);
        message(REQUEST_FOR_POSITIONS, RequestForPositions.class);
        message(POSITION_REPORT, PositionReportMessage.class);
        message(TRADING_SESSION_STATUS, TradingSessionStatus.class);
        message(NEWS_MESSAGE, NewsMessage.class);
        message(COLLATERAL_REPORT, CollateralReportMessage.class);
        message(COLLATERAL_INQUIRY_ACKNOWLEDGMENT, CollateralInquiryAcknowledgmentMessage.class);
        message(REQUEST_FOR_POSITION_ACKNOWLEDGMENT, RequestForPositionAcknowledgmentMessage.class);
    }

    @Override public String getProfile() {
        return "mb-trading";
    }
}
