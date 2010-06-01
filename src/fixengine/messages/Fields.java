/*
 * Copyright 2009 the original author or authors.
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

import java.util.ArrayList;
import java.util.List;

import fixengine.Specification;

/**
 * @author Pekka Enberg
 */
public class Fields {
    private final List<Field> fields = new ArrayList<Field>();

    public void add(Field field) {
        fields.add(field);
    }

    public List<Field> getFields() {
        return fields;
    }

    public Field lookup(Tag tag) {
        for (Field field : fields) {
            if (field.supports(tag)) {
                return field;
            }
        }
        return null;
    }

    public boolean contains(Specification<Field> spec) {
        for (Field field : fields) {
            if (spec.isSatisfiedBy(field)) {
                return true;
            }
        }
        return false;
    }

    public String format() {
        StringBuilder result = new StringBuilder();
        for (Field field : fields) {
            result.append(field.format());
        }
        return result.toString();
    }

    public int length() {
        int length = 0;
        for (Field field : fields) {
            length += field.length();
        }
        return length;
    }

    public int checksum() {
        int checksum = 0;
        for (Field field : fields) {
            checksum += field.checksum();
        }
        return checksum;
    }
}