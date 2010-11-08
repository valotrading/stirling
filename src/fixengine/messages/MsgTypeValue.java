/*
 * Copyright 2008 the original author or authors.
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
package fixengine.messages;

/**
 * @author Pekka Enberg
 */
public class MsgTypeValue {
    public static final String HEARTBEAT = "0";
    public static final String TEST_REQUEST = "1";
    public static final String RESEND_REQUEST = "2";
    public static final String REJECT = "3";
    public static final String SEQUENCE_RESET = "4";
    public static final String LOGOUT = "5";
    public static final String EXECUTION_REPORT = "8";
    public static final String ORDER_CANCEL_REJECT = "9";
    public static final String LOGON = "A";
    public static final String NEW_ORDER_SINGLE = "D";
    public static final String ORDER_CANCEL_REQUEST = "F";
    public static final String ORDER_MODIFICATION_REQUEST = "G";
    public static final String BUSINESS_MESSAGE_REJECT = "j";
    public static final String ALLOCATION_INSTRUCTION = "J";
    public static final String DONT_KNOW_TRADE = "Q";
}
