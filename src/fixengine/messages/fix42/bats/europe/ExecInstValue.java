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
package fixengine.messages.fix42.bats.europe;

import fixengine.messages.Formattable;
import fixengine.messages.InvalidValueForTagException;

public enum ExecInstValue implements Formattable {
    MARKET_PEG('P'),         /* Market Peg (peg buy to MBBO offer, peg sell to MBBO bid) */
    PRIMARY_PEG('R'),        /* Primary Peg (peg buy to MBBO bid, peg sell to MBBO offer) */
    MIDPOINT('M'),           /* Midpoint (peg to MBBO midpoint) */
    ALTERNATE('L'),          /* Alternate Midpoint (less agressive of midpoint and 1 tick inside MBBO */
    BATS_MOC('c'),           /* BATS Market On Close */
    BATS_EXT_DARK_ONLY('u'), /* BATS + External Dark Only */
    BATS_EXT_DARK_LIT('v'),  /* BATS + External Dark + Lit (default for routable orders) */
    BATS_EXT_LIT_ONLY('w');  /* BATS + External Lit Only */

    private char value;

    ExecInstValue(char value) {
        this.value = value;
    }

    @Override public String value() {
        return Character.toString(value);
    }

    public static ExecInstValue parse(char value) {
        for (ExecInstValue type : ExecInstValue.values()) {
            if (type.value == value)
                return type;
        }
        throw new InvalidValueForTagException(Character.toString(value));
    }
}
