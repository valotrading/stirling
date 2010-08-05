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
public class IntegerField extends AbstractField<Integer> {
    public IntegerField(Tag<IntegerField> tag) {
        this(tag, null, Required.YES);
    }

    public IntegerField(Tag<IntegerField> tag, Integer value) {
        this(tag, value, Required.YES);
    }

    public IntegerField(Tag<IntegerField> tag, Integer value, Required required) {
        super(tag, value, required);
    }

    @Override
    public void parse(String value) {
        try {
            this.value = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            validFormat = false;
        }
    }

    @Override
    protected final String value() {
        return Integer.toString(intValue());
    }

    protected Integer intValue() {
        return value;
    }
}
