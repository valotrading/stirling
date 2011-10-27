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
package xtch.turquoise.templates

import xtch.turquoise.Fields
import xtch.turquoise.MessageType

/**
 * Template for Execution Report message as specified in section 7.4.5 of
 * [2].
 */
object ExecutionReport extends AbstractTemplate(MessageType.EXECUTION_REPORT) {
  add(MessageHeader.TEMPLATE);
  add(Fields.APP_ID);
  add(Fields.SEQUENCE_NO);
  add(Fields.EXECUTION_ID);
  add(Fields.CLIENT_ORDER_ID);
  add(Fields.ORDER_ID);
  add(Fields.EXEC_TYPE);
  add(Fields.EXECUTION_REPORT_REF_ID);
  add(Fields.ORDER_STATUS);
  add(Fields.ORDER_REJECT_CODE);
  add(Fields.EXECUTED_PRICE);
  add(Fields.EXECUTED_QTY);
  add(Fields.LEAVES_QTY);
  add(Fields.RESERVED_FIELD_1);
  add(Fields.DISPLAY_QTY);
  add(Fields.COMMON_SYMBOL);
  add(Fields.SIDE);
  add(Fields.SECONDARY_ORDER_ID);
  add(Fields.COUNTERPARTY);
  add(Fields.TRADE_LIQUIDITY_INDICATOR);
  add(Fields.TRADE_MATCH_ID);
  add(Fields.TRANSACT_TIME);
  add(Fields.TARGET_BOOK);
  add(Fields.TYPE_OF_TRADE);
  add(Fields.RESERVED_FIELD_1);
}
