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
public enum ExecType implements Formattable<ExecType> {
    NEW('0'),
    PARTIAL_FILL('1'),
    FILL('2'),
    DONE_FOR_DAY('3'),
    CANCELED('4'),
    REPLACE('5'),
    PENDING_CANCEL('6'),
    STOPPED('7'),
    REJECTED('8'),
    SUSPENDED('9'),
    PENDING_NEW('A'),
    CALCULATED('B'),
    EXPIRED('C'),
    RESTATED('D'),
    PENDING_REPLACE('E'),
    TRADE('F'),
    TRADE_CORRECT('G'),
    TRADE_CANCEL('H'),
    ORDER_STATUS('I');

    private char value;
    
    ExecType(char value) {
        this.value = value;
    }

    @Override public String value() {
        return Character.toString(value);
    }

    public static ExecType parse(char value) {
        for (ExecType type : ExecType.values()) {
            if (type.value == value)
                return type;
        }
        throw new InvalidValueForTagException(Character.toString(value));
    }
}
