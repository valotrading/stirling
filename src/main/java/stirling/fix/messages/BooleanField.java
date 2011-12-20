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
public class BooleanField extends AbstractField<Boolean> {
    public BooleanField(Tag<BooleanField> tag) {
        this(tag, null, Required.YES);
    }

    public BooleanField(Tag<BooleanField> tag, Required required) {
        this(tag, null, required);
    }

    public BooleanField(Tag<BooleanField> tag, Boolean value, Required required) {
        super(tag, value, required);
    }

    @Override
    public void parse(String value) {
        if ("Y".equals(value))
            this.value = Boolean.TRUE;
        else if ("N".equals(value))
            this.value = Boolean.FALSE;
        else
            throw new IllegalArgumentException(value);
    }

    @Override
    protected final String value() {
        if (Boolean.TRUE.equals(booleanValue())) {
            return "Y";
        }
        return "N";
    }

    protected Boolean booleanValue() {
        return value;
    }
}
