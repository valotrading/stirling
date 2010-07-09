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
public enum SideValue implements Formattable<SideValue> {
    BUY('1'),
    SELL('2'),
    BUY_MINUS('3'),
    SELL_PLUS('4'),
    SELL_SHORT('5'),
    SELL_SHORT_EXEMPT('6'),
    UNDISCLOSED('7'),
    CROSS('8'),
    CROSS_SHORT('9'),
    CROSS_SHORT_EXEMPT('A'),
    AS_DEFINED('B'),    /* for multileg */
    OPPOSITE('C');      /* for multileg */

    private char value;
    
    SideValue(char value) {
        this.value = value;
    }

    @Override public String value() {
        return Character.toString(value);
    }

    public static SideValue parse(char value) {
        for (SideValue type : SideValue.values()) {
            if (type.value == value)
                return type;
        }
        throw new InvalidValueForTagException(Character.toString(value));
    }
}