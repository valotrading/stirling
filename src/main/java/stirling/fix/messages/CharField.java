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
public class CharField extends AbstractField<Character> {
    public CharField(Tag<CharField> tag) {
        this(tag, null, Required.YES);
    }

    public CharField(Tag<CharField> tag, Character value) {
        this(tag, value, Required.YES);
    }

    public CharField(Tag<CharField> tag, Character value, Required required) {
        super(tag, value, required);
    }

    @Override
    public void parse(String value) {
        this.value = value.charAt(0);
    }

    @Override
    protected final String value() {
        return Character.toString(charValue());
    }

    protected Character charValue() {
        return value;
    }
}
