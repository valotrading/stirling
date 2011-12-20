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
package stirling.fix.messages;


/**
 * @author Pekka Enberg 
 */
public class MessageBuffer {
    private final StringBuilder buffer = new StringBuilder();

    public void append(Field field) {
        append(field.format());
    }

    public void append(String s) {
        buffer.append(s);
    }

    public void prefix(Field field) {
        buffer.insert(0, field.format());
    }

    public int length() {
        return buffer.length();
    }

    public int checksum() {
        return Checksums.checksum(buffer);
    }

    @Override
    public String toString() {
        return buffer.toString();
    }
}
