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
public enum ExecRestatementReason implements Formattable<ExecRestatementReason> {
    GT_CORPORATE_ACTION(0),
    GT_RENEWAL_RESTATEMENT(1), /* (no corporate action) */
    VERBAL_CHANGE(2),
    REPRICING_OF_ORDER(3),
    BROKER_OPTION(4),
    PARTIAL_DECLINE_OF_ORDER_QTY(5), /* (e.g. exchange-initiated partial cancel) */
    CANCEL_ON_TRADING_HALT(6),
    CANCEL_ON_SYSTEM_FAILURE(7),
    MARKET_OPTION(8);

    private int value;
    
    ExecRestatementReason(int value) {
        this.value = value;
    }

    @Override public String value() {
        return Integer.toString(value);
    }

    public static ExecRestatementReason parse(int value) {
        for (ExecRestatementReason type : ExecRestatementReason.values()) {
            if (type.value == value)
                return type;
        }
        throw new InvalidValueForTagException(Integer.toString(value));
    }
}
