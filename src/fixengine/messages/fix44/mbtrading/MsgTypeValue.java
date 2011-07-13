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

/**
 * @author Kare Nuorteva
 */
public class MsgTypeValue {
    public static final String NEW_ORDER_MULTILEG = "AB";
    public static final String POSITION_REPORT = "AP";
    public static final String NEWS_MESSAGE = "B";
    public static final String COLLATERAL_REPORT = "BA";
    public static final String COLLATERAL_INQUIRY_ACKNOWLEDGMENT = "BG";
    public static final String REQUEST_FOR_POSITION_ACKNOWLEDGMENT = "AO";
}
