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
package xtch.turquoise;

public class MessageType {
  public static final MessageType TYPE = new MessageType();

  /**
   * Administrative message types as specified in section 7.1.1 of [2].
   */
  public static final String LOGON = "A";
  public static final String LOGON_REPLY = "B";
  public static final String LOGOUT = "5";
  public static final String HEARTBEAT = "0";
  public static final String MISSED_MESSAGE_REQUEST = "M";
  public static final String MISSED_MESSAGE_REQUEST_ACK = "N";
  public static final String MISSED_MESSAGE_REPORT = "P";
  public static final String REJECT = "3";

  /**
   * Client-initiated message types as specified in section 7.1.2.1 of [2].
   */
  public static final String NEW_ORDER = "D";
  public static final String ORDER_CANCEL_REQUEST ="F";
  public static final String ORDER_MASS_CANCEL_REQUEST = "q";
  public static final String ORDER_CANCEL_REPLACE_REQUEST = "G";

  /**
   * Server-initiated message types as specified in section 7.1.2.2 of [2].
   */
  public static final String EXECUTION_REPORT = "8";
  public static final String ORDER_CANCEL_REJECT ="9";
  public static final String ORDER_MASS_CANCEL_REPORT = "r";

  /**
   * Other server-initiated message types as specified in section 7.1.3.1 of [2].
   */
  public static final String BUSINESS_MESSAGE_REJECT = "j";

  private MessageType() {
  }
}
