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

/**
 * @author Kare Nuorteva
 */
public class MsgTypeValue {
  public static final String MASS_QUOTE = "i";
  public static final String MASS_QUOTE_ACKNOWLEDGEMENT = "b";
  public static final String NEWS = "B";
  public static final String ORDER_CANCEL_REPLACE_REQUEST = "G";
  public static final String ORDER_MASS_CANCEL_REPORT = "r";
  public static final String ORDER_MASS_CANCEL_REQUEST = "q";
  public static final String ORDER_MASS_STATUS_REQUEST = "AF";
  public static final String SECURITY_LIST = "y";
  public static final String SECURITY_LIST_REQUEST = "x";
  public static final String TRADE_CAPTURE_REPORT = "AE";
  public static final String TRADE_CAPTURE_REPORT_ACK = "AR";
  public static final String TRADE_CAPTURE_REPORT_REQUEST = "AD";
  public static final String TRADE_CAPTURE_REPORT_REQUEST_ACK = "AQ";
}
