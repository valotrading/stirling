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

import lang.Objects;

/**
 * @author Pekka Enberg 
 */
public class Token implements CharSequence {
    public enum Type {
        ASSIGN,
        DELIMITER,
        END_OF_STREAM,
        TAG_OR_VALUE,
    }

    private final String value;
    private final Type type;

    public Token(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public String value() {
        return value;
    }

    public boolean isType(Type type) {
        return this.type.equals(type);
    }
    
    public Tag toTag() {
        return new Tag(intValue());
    }

    public int intValue() {
        try {
            return Integer.parseInt(value());
        } catch (NumberFormatException e) {
            throw new InvalidNumberFormatException(value());
        }
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    @Override
    public int length() {
        return value.length();
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return value.subSequence(start, end);
    }
    
    @Override
    public String toString() {
        return Objects.toString(this);
    }
}
