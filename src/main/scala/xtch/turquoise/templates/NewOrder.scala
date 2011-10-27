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
 * Template for NewOrder message as specified in section 7.4.1 of [2].
 */
object NewOrder extends AbstractTemplate(MessageType.NEW_ORDER) {
  add(MessageHeader.TEMPLATE);
  add(Fields.CLIENT_ORDER_ID);
  add(Fields.TRADER_ID);
  add(Fields.ACCOUNT);
  add(Fields.CLEARING_ACCOUNT);
  add(Fields.COMMON_SYMBOL);
  add(Fields.ORDER_TYPE);
  add(Fields.TIME_IN_FORCE);
  add(Fields.EXPIRE_DATE_TIME);
  add(Fields.SIDE);
  add(Fields.ORDER_QTY);
  add(Fields.DISPLAY_QTY);
  add(Fields.LIMIT_PRICE);
  add(Fields.CAPACITY);
  add(Fields.AUTO_CANCEL);
  add(Fields.ORDER_SUB_TYPE);
  add(Fields.RESERVED_FIELD_1);
  add(Fields.RESERVED_FIELD_2);
  add(Fields.TARGET_BOOK);
  add(Fields.EXEC_INSTRUCTION);
  add(Fields.MIN_QTY);
  add(Fields.RESERVED_FIELD_3);
}
