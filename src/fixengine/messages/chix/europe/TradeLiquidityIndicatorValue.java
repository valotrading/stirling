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
package fixengine.messages.chix.europe;

import fixengine.messages.Formattable;
import fixengine.messages.InvalidValueForTagException;

public enum TradeLiquidityIndicatorValue implements Formattable {
    ADDED_LIQUIDITY("A"),             /* Order added liquidity */
    REMOVED_LIQUIDITY("R"),           /* Order removed liquidity */
    ADDED_LIQUIDITY_TO_DARK("D"),     /* Dark trade */
    REMOVED_LIQUIDITY_FROM_DARK("X"); /* Onward routed trade */

    private String value;

    TradeLiquidityIndicatorValue(String value) {
        this.value = value;
    }

    @Override public String value() {
        return value;
    }

    public static TradeLiquidityIndicatorValue parse(String value) {
        for (TradeLiquidityIndicatorValue type : TradeLiquidityIndicatorValue.values()) {
            if (type.value.equals(value))
                return type;
        }
        throw new InvalidValueForTagException(value);
    }
}
