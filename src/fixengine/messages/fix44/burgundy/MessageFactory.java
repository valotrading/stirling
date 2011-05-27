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
package fixengine.messages.fix44.burgundy;

import fixengine.messages.fix42.OrderCancelRequestMessage;
import fixengine.messages.fix44.NewOrderSingleMessage;
import static fixengine.messages.MsgTypeValue.*;
import static fixengine.messages.fix44.burgundy.MsgTypeValue.*;

public class MessageFactory extends fixengine.messages.fix42.DefaultMessageFactory {
  public MessageFactory() {
    message(BUSINESS_MESSAGE_REJECT, BusinessMessageReject.class);
    message(DONT_KNOW_TRADE, DontKnowTrade.class);
    message(EXECUTION_REPORT, ExecutionReport.class);
    message(MASS_QUOTE, MassQuote.class);
    message(MASS_QUOTE_ACKNOWLEDGEMENT, MassQuoteAcknowledgement.class);
    message(NEW_ORDER_SINGLE, NewOrderSingleMessage.class);
    message(NEWS, News.class);
    message(ORDER_CANCEL_REJECT, OrderCancelReject.class);
    message(ORDER_CANCEL_REPLACE_REQUEST, OrderCancelReplaceRequest.class);
    message(ORDER_CANCEL_REQUEST, OrderCancelRequestMessage.class);
    message(ORDER_MASS_CANCEL_REPORT, OrderMassCancelReport.class);
    message(ORDER_MASS_CANCEL_REQUEST, OrderMassCancelRequest.class);
    message(ORDER_MASS_STATUS_REQUEST, OrderMassStatusRequest.class);
    message(SECURITY_LIST, SecurityList.class);
    message(SECURITY_LIST_REQUEST, SecurityListRequest.class);
    message(TRADE_CAPTURE_REPORT, TradeCaptureReport.class);
    message(TRADE_CAPTURE_REPORT_ACK, TradeCaptureReportAck.class);
    message(TRADE_CAPTURE_REPORT_REQUEST, TradeCaptureReportRequest.class);
    message(TRADE_CAPTURE_REPORT_REQUEST_ACK, TradeCaptureReportRequestAck.class);
  }

  @Override public String getProfile() {
    return "burgundy";
  }
}
