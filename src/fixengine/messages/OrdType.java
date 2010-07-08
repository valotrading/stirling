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
public enum OrdType implements Formattable<OrdType> {
    MARKET('1'),
    LIMIT('2'),
    STOP('3'),
    STOP_LIMIT('4'),
    MARKET_ON_CLOSE('5'),       /* Deprecated */
    WITH_OR_WITHOUT('6'),
    LIMIT_OR_BETTER('7'),
    LIMIT_WITH_OR_WITHOUT('8'),
    ON_BASIS('9'),
    ON_CLOSE('A'),              /* Deprecated */
    LIMIT_ON_CLOSE('B'),        /* Deprecated */
    FOREX_MARKET('C'),          /* Deprecated */
    PREVIOUSLY_QUOTED('D'),
    PREVIOUSLY_INDICATED('E'),
    FOREX_LIMIT('F'),           /* Deprecated */
    FOREX_SWAP('G'),
    FOREX_PREVIOUSLY_QUOTED('H'), /* Deprecated */
    FUNARI('I'),
    MARKET_IF_TOUCHED('J'),
    MARKET_WITH_LEFTOVER_AS_LIMIT('K'),
    PREVIOUS_FUND_VALUATION_POINT('L'),
    NEXT_FUND_VALUATION_POINT('M'),
    PEGGED('P');

    private char value;
    
    OrdType(char value) {
        this.value = value;
    }

    @Override public String value() {
        return Character.toString(value);
    }

    public static OrdType parse(char value) {
        for (OrdType type : OrdType.values()) {
            if (type.value == value)
                return type;
        }
        throw new InvalidValueForTagException(Character.toString(value));
    }
}
