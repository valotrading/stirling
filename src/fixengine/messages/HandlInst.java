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
public enum HandlInst {
    /** Automated execution order, private, no Broker intervention */
    AUTOMATED_ORDER_PRIVATE('1'),

    /** Automated execution order, public, Broker intervention OK */
    AUTOMATED_ORDER_PUBLIC('2'),

    /** Manual order, best execution */
    MANUAL_ORDER('3');

    private char value;
    
    HandlInst(char value) {
        this.value = value;
    }

    public char value() {
        return value;
    }

    public static HandlInst parse(char value) {
        for (HandlInst type : HandlInst.values()) {
            if (type.value == value)
                return type;
        }
        throw new InvalidValueForTagException(Character.toString(value));
    }
}
