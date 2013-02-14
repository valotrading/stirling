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
public class StringField extends AbstractField<String> {
    public StringField(Tag<? extends StringField> tag) {
        this(tag, Required.YES);
    }

    public StringField(Tag<? extends StringField> tag, String value) {
        this(tag, value, Required.YES);
    }

    public StringField(Tag<? extends StringField> tag, String value, Required required) {
        super(tag, value, required);
    }

    public StringField(Tag<? extends StringField> tag, Required required) {
        super(tag, null, required);
    }

    @Override
    public void parse(String value) {
        this.value = value;
    }

    @Override
    protected final String value() {
        return value;
    }
}
