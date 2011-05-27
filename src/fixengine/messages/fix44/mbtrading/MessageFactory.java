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
import static fixengine.messages.MsgTypeValue.*;

public class MessageFactory extends fixengine.messages.fix42.DefaultMessageFactory {
    public MessageFactory() {
        message(EXECUTION_REPORT, ExecutionReport.class);
        message(ORDER_CANCEL_REQUEST, OrderCancelRequest.class);
        message(BUSINESS_MESSAGE_REJECT, BusinessMessageReject.class);
        message(NEW_ORDER_SINGLE, NewOrderSingle.class);
        message(ORDER_MODIFICATION_REQUEST, OrderModificationRequest.class);
        message(LOGON, Logon.class);
        message(NEW_ORDER_MULTILEG, NewOrderMultiLeg.class);
        message(REQUEST_FOR_POSITIONS, RequestForPositions.class);
        message(POSITION_REPORT, PositionReport.class);
        message(TRADING_SESSION_STATUS, TradingSessionStatus.class);
        message(NEWS_MESSAGE, News.class);
        message(COLLATERAL_REPORT, CollateralReport.class);
        message(COLLATERAL_INQUIRY_ACKNOWLEDGMENT, CollateralInquiryAcknowledgment.class);
        message(REQUEST_FOR_POSITION_ACKNOWLEDGMENT, RequestForPositionAcknowledgment.class);
    }

    @Override public String getProfile() {
        return "mb-trading";
    }
}
