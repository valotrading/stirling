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
package stirling.fix.messages.fix43;

public class MsgTypeValue {
    /* FIX 4.2 */
    public static final String HEARTBEAT = "0";
    public static final String TEST_REQUEST = "1";
    public static final String RESEND_REQUEST = "2";
    public static final String REJECT = "3";
    public static final String SEQUENCE_RESET = "4";
    public static final String LOGOUT = "5";
    public static final String INDICATION_OF_INTEREST = "6";
    public static final String ADVERTISEMENT = "7";
    public static final String EXECUTION_REPORT = "8";
    public static final String ORDER_CANCEL_REJECT = "9";
    public static final String LOGON = "A";
    public static final String NEWS = "B";
    public static final String EMAIL = "C";
    public static final String NEW_ORDER_SINGLE = "D";
    public static final String NEW_ORDER_LIST = "E";
    public static final String ORDER_CANCEL_REQUEST = "F";
    public static final String ORDER_CANCEL_REPLACE_REQUEST = "G";
    public static final String ORDER_STATUS_REQUEST = "H";
    public static final String ALLOCATION_INSTRUCTION = "J";
    public static final String LIST_CANCEL_REQUEST = "L";
    public static final String LIST_STATUS_REQUEST = "M";
    public static final String LIST_STATUS = "N";
    public static final String ALLOCATION_ACK = "P";
    public static final String DONT_KNOW_TRADE = "Q";
    public static final String QUOTE_REQUEST = "R";
    public static final String QUOTE = "S";
    public static final String SETTLEMENT_INSTRUCTIONS = "T";
    public static final String MARKET_DATA_REQUEST = "V";
    public static final String MARKET_DATA_SNAPSHOT_FULL_REFRESH = "W";
    public static final String MARKET_DATA_INCREMENTAL_REFRESH = "X";
    public static final String MARKET_DATA_REQUEST_REJECT = "Y";
    public static final String QUOTE_CANCEL = "Z";
    public static final String QUOTE_STATUS_REQUEST = "a";
    public static final String MASS_QUOTE_ACKNOWLEDGEMENT = "b";
    public static final String SECURITY_DEFINITION_REQUEST = "c";
    public static final String SECURITY_DEFINITION = "d";
    public static final String SECURITY_STATUS_REQUEST = "e";
    public static final String SECURITY_STATUS = "f";
    public static final String TRADING_SESSION_STATUS_REQUEST = "g";
    public static final String TRADING_SESSION_STATUS = "h";
    public static final String MASS_QUOTE = "i";
    public static final String BUSINESS_MESSAGE_REJECT = "j";
    public static final String BID_REQUEST = "k";
    public static final String BID_RESPONSE = "l";
    public static final String LIST_STRIKE_PRICE = "m";
    /* FIX 4.3 */
    public static final String XML_NON_FIX = "n";
    public static final String REGISTRATION_INSTRUCTIONS = "o";
    public static final String REGISTRATION_INSTRUCTIONS_RESPONSE = "p";
    public static final String ORDER_MASS_CANCEL_REQUEST = "q";
    public static final String ORDER_MASS_CANCEL_REPORT = "r";
    public static final String NEW_ORDER_CROSS = "s";
    public static final String CROSS_ORDER_CANCEL_REPLACE_REQUEST = "t";
    public static final String CROSS_ORDER_CANCEL_REQUEST = "u";
    public static final String SECURITY_TYPE_REQUEST = "v";
    public static final String SECURITY_TYPES = "w";
    public static final String SECURITY_LIST_REQUEST = "x";
    public static final String SECURITY_LIST = "y";
    public static final String DERIVATIVE_SECURITY_LIST_REQUEST = "z";
    public static final String DERIVATIVE_SECURITY_LIST = "AA";
    public static final String NEW_ORDER_MULTILEG = "AB";
    public static final String MULTILEG_ORDER_CANCEL_REPLACE_REQUEST = "AC";
    public static final String TRADE_CAPTURE_REPORT_REQUEST = "AD";
    public static final String TRADE_CAPTURE_REPORT = "AE";
    public static final String ORDER_MASS_STATUS_REQUEST = "AF";
    public static final String QUOTE_REQUEST_REJECT = "AG";
    public static final String RFQ_REQUEST = "AH";
    public static final String QUOTE_STATUS_REPORT = "AI";
}
