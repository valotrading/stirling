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
package fixengine.messages;

public class RawMessageBuilder {
    StringBuilder s = new StringBuilder();

    public RawMessageBuilder field(Tag<?> tag, String value) {
        return field(tag.value(), value);
    }

    public RawMessageBuilder field(int tag, String value) {
        s.append(tag);
        s.append('=');
        s.append(value);
        s.append(Field.DELIMITER);
        return this;
    }

    @Override public String toString() {
        return s.toString();
    }
}