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
package fixengine.tags.fix44.burgundy;

import fixengine.messages.EnumField;
import fixengine.messages.Required;
import fixengine.messages.Tag;
import fixengine.messages.fix44.burgundy.ExecInstValue;

public class ExecInst extends Tag<ExecInst.Field> {
    public static final ExecInst TAG = new ExecInst();

    public ExecInst() {
        super(18, Field.class);
    }

    public static class Field extends EnumField<ExecInstValue> {
        public Field(Tag<Field> tag) {
            super(tag, Required.YES);
        }

        @Override public void parse(String value) {
            this.value = ExecInstValue.parse(value.charAt(0));
        }
    }
}
