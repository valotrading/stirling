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
public class Tag {
    private int value;

    public Tag(int value) {
        this.value = value;
    }

    public boolean isUserDefined() {
        return value() >= 5000;
    }

    public int length() {
        return Integer.toString(value).length();
    }

    public int checksum() {
        int checksum = 0;
        String s = Integer.toString(value);
        for (int i = 0; i < s.length(); i++)
            checksum += s.charAt(i);
        return checksum;
    }

    public int value() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equal(this, obj);
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
