/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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
package xtch.turquoise.templates

import xtch.turquoise.Fields
import xtch.turquoise.MessageType

/**
 * Template for Business Reject message as specified in section 7.5.1 of [2].
 */
object BusinessReject extends AbstractTemplate(MessageType.BusinessMessageReject) {
  add(MessageHeader.TEMPLATE)
  add(Fields.APP_ID)
  add(Fields.SEQUENCE_NO)
  add(Fields.CLIENT_ORDER_ID)
  add(Fields.ORDER_ID)
  add(Fields.TRANSACT_TIME)
  add(Fields.RESERVED_FIELD_1)
}

/**
 * Template for Execution Report message as specified in section 7.4.5 of
 * [2].
 */
object ExecutionReport extends AbstractTemplate(MessageType.ExecutionReport) {
  add(MessageHeader.TEMPLATE)
  add(Fields.APP_ID)
  add(Fields.SEQUENCE_NO)
  add(Fields.EXECUTION_ID)
  add(Fields.CLIENT_ORDER_ID)
  add(Fields.ORDER_ID)
  add(Fields.EXEC_TYPE)
  add(Fields.EXECUTION_REPORT_REF_ID)
  add(Fields.ORDER_STATUS)
  add(Fields.ORDER_REJECT_CODE)
  add(Fields.EXECUTED_PRICE)
  add(Fields.EXECUTED_QTY)
  add(Fields.LEAVES_QTY)
  add(Fields.RESERVED_FIELD_1)
  add(Fields.DISPLAY_QTY)
  add(Fields.COMMON_SYMBOL)
  add(Fields.SIDE)
  add(Fields.SECONDARY_ORDER_ID)
  add(Fields.COUNTERPARTY)
  add(Fields.TRADE_LIQUIDITY_INDICATOR)
  add(Fields.TRADE_MATCH_ID)
  add(Fields.TRANSACT_TIME)
  add(Fields.TARGET_BOOK)
  add(Fields.TYPE_OF_TRADE)
  add(Fields.RESERVED_FIELD_1)
}

/**
 * Template for Heartbeat message as specified in section 7.3.4 of [2].
 */
object Heartbeat extends AbstractTemplate(MessageType.Heartbeat)

/**
 * Template for Logon message as specified in section 7.3.1 of [2].
 */
object Logon extends AbstractTemplate(MessageType.Logon) {
  add(MessageHeader.TEMPLATE)
  add(Fields.COMP_ID)
  add(Fields.PASSWORD)
  add(Fields.NEW_PASSWORD)
  add(Fields.MESSAGE_VERSION)
}

/**
 * Template for LogonReply message as specified in section 7.3.2 of [2].
 */
object LogonReply extends AbstractTemplate(MessageType.LogonReply) {
  add(MessageHeader.TEMPLATE)
  add(Fields.REJECT_CODE)
  add(Fields.PASSWORD_EXPIRY_DAY_COUNT)
}

/**
 * Template for Logout message as specified in section 7.3.3 of [2].
 */
object Logout extends AbstractTemplate(MessageType.Logout) {
  add(MessageHeader.TEMPLATE)
  add(Fields.LOGOUT_REASON)
}

/**
 * Template for MissedMessageReport message as specified in section 7.3.7 of [2].
 */
object MissedMessageReport extends AbstractTemplate(MessageType.MissedMessageReport) {
  add(MessageHeader.TEMPLATE)
  add(Fields.RESPONSE_TYPE)
}

/**
 * Template for MissedMessageRequest message as specified in section 7.3.5 of [2].
 */
object MissedMessageRequest extends AbstractTemplate(MessageType.MissedMessageRequest) {
  add(MessageHeader.TEMPLATE)
  add(Fields.APP_ID)
  add(Fields.LAST_MSG_SEQ_NUM)
}

/**
 * Template for MissedMessageRequestAck message as specified in section 7.3.6 of [2].
 */
object MissedMessageRequestAck extends AbstractTemplate(MessageType.MissedMessageRequestAck) {
  add(MessageHeader.TEMPLATE)
  add(Fields.RESPONSE_TYPE)
}

/**
 * Template for NewOrder message as specified in section 7.4.1 of [2].
 */
object NewOrder extends AbstractTemplate(MessageType.NewOrder) {
  add(MessageHeader.TEMPLATE)
  add(Fields.CLIENT_ORDER_ID)
  add(Fields.TRADER_ID)
  add(Fields.ACCOUNT)
  add(Fields.CLEARING_ACCOUNT)
  add(Fields.COMMON_SYMBOL)
  add(Fields.ORDER_TYPE)
  add(Fields.TIME_IN_FORCE)
  add(Fields.EXPIRE_DATE_TIME)
  add(Fields.SIDE)
  add(Fields.ORDER_QTY)
  add(Fields.DISPLAY_QTY)
  add(Fields.LIMIT_PRICE)
  add(Fields.CAPACITY)
  add(Fields.AUTO_CANCEL)
  add(Fields.ORDER_SUB_TYPE)
  add(Fields.RESERVED_FIELD_1)
  add(Fields.RESERVED_FIELD_2)
  add(Fields.TARGET_BOOK)
  add(Fields.EXEC_INSTRUCTION)
  add(Fields.MIN_QTY)
  add(Fields.RESERVED_FIELD_3)
}

/**
 * Template for Order Cancel Reject message as specified in section 7.4.6 of
 * [2].
 */
object OrderCancelReject extends AbstractTemplate(MessageType.OrderCancelReject) {
  add(MessageHeader.TEMPLATE)
  add(Fields.APP_ID)
  add(Fields.SEQUENCE_NO)
  add(Fields.CLIENT_ORDER_ID)
  add(Fields.ORDER_ID)
  add(Fields.CANCEL_REJECT_REASON)
  add(Fields.TRANSACT_TIME)
  add(Fields.RESERVED_FIELD_1)
}

/**
 * Template for Order Cancel/Replace Request message as specified in section
 * 7.4.2 of [2].
 */
object OrderCancelReplaceRequest extends AbstractTemplate(MessageType.OrderCancelReplaceRequest) {
  add(MessageHeader.TEMPLATE)
  add(Fields.CLIENT_ORDER_ID)
  add(Fields.ORIGINAL_CLIENT_ORDER_ID)
  add(Fields.ORDER_ID)
  add(Fields.COMMON_SYMBOL)
  add(Fields.EXPIRE_DATE_TIME)
  add(Fields.ORDER_QTY)
  add(Fields.DISPLAY_QTY)
  add(Fields.LIMIT_PRICE)
  add(Fields.ACCOUNT)
  add(Fields.TIME_IN_FORCE)
  add(Fields.SIDE)
  add(Fields.RESERVED_FIELD_1)
  add(Fields.TARGET_BOOK)
  add(Fields.EXEC_INSTRUCTION)
  add(Fields.MIN_QTY)
  add(Fields.RESERVED_FIELD_2)
}

/**
 * Template for Order Cancel Request message as specified in section 7.4.3 of
 * [2].
 */
object OrderCancelRequest extends AbstractTemplate(MessageType.OrderCancelRequest) {
  add(MessageHeader.TEMPLATE)
  add(Fields.CLIENT_ORDER_ID)
  add(Fields.ORIGINAL_CLIENT_ORDER_ID)
  add(Fields.ORDER_ID)
  add(Fields.SIDE)
  add(Fields.TARGET_BOOK)
  add(Fields.RESERVED_FIELD_1)
}

/**
 * Template for OrderMassCancelReport message as specified in section 7.4.7 of [2].
 */
object OrderMassCancelReport extends AbstractTemplate(MessageType.OrderMassCancelReport) {
  add(MessageHeader.TEMPLATE)
  add(Fields.APP_ID)
  add(Fields.SEQUENCE_NO)
  add(Fields.CLIENT_ORDER_ID)
  add(Fields.MASS_CANCEL_RESPONSE)
  add(Fields.MASS_CANCEL_REJECT_REASON)
  add(Fields.RESERVED_FIELD_1)
  add(Fields.TRANSACT_TIME)
  add(Fields.RESERVED_FIELD_2)
}

/**
 * Template for Order Mass Cancel Request message as specified in section 7.4.4
 * of [2].
 */
object OrderMassCancelRequest extends AbstractTemplate(MessageType.OrderMassCancelRequest) {
  add(MessageHeader.TEMPLATE)
  add(Fields.CLIENT_ORDER_ID)
  add(Fields.MASS_CANCEL_TYPE)
  add(Fields.COMMON_SYMBOL)
  add(Fields.SEGMENT)
  add(Fields.RESERVED_FIELD_1)
  add(Fields.TARGET_BOOK)
  add(Fields.RESERVED_FIELD_2)
}

/**
 * Template for Reject message as specified in section 7.3.8 of [2].
 */
object Reject extends AbstractTemplate(MessageType.Reject) {
  add(MessageHeader.TEMPLATE)
  add(Fields.REJECT_CODE)
  add(Fields.REJECT_REASON)
  add(Fields.REJECTED_MESSAGE_TYPE)
  add(Fields.CLIENT_ORDER_ID)
}
