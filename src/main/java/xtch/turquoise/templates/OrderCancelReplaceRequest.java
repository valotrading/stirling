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
package xtch.turquoise.templates;

import xtch.turquoise.Fields;
import xtch.turquoise.MessageType;

/** 
 * Template for Order Cancel/Replace Request message as specified in section
 * 7.4.2 of [2].
 */
public class OrderCancelReplaceRequest extends AbstractTemplate {
  public static final OrderCancelReplaceRequest TEMPLATE = new OrderCancelReplaceRequest();

  private OrderCancelReplaceRequest() {
    super(MessageType.LOGOUT);
    add(MessageHeader.TEMPLATE);
    add(Fields.CLIENT_ORDER_ID);
    add(Fields.ORIGINAL_CLIENT_ORDER_ID);
    add(Fields.ORDER_ID);
    add(Fields.COMMON_SYMBOL);
    add(Fields.EXPIRE_DATE_TIME);
    add(Fields.ORDER_QTY);
    add(Fields.DISPLAY_QTY);
    add(Fields.LIMIT_PRICE);
    add(Fields.ACCOUNT);
    add(Fields.TIME_IN_FORCE);
    add(Fields.SIDE);
    add(Fields.RESERVED_FIELD_1);
    add(Fields.TARGET_BOOK);
    add(Fields.EXEC_INSTRUCTION);
    add(Fields.MIN_QTY);
    add(Fields.RESERVED_FIELD_2);
  }
}
