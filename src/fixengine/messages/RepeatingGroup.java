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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public abstract class RepeatingGroup implements Field {
    private final List<RepeatingGroupInstance> instances = new ArrayList<RepeatingGroupInstance>();
    private final Tag<IntegerField> count;

    public RepeatingGroup(Tag<IntegerField> count) {
        this.count = count;
    }

    public Tag<?> countTag() {
        return count;
    }

    public abstract RepeatingGroupInstance newInstance();

    @Override public void parse(ByteBuffer b) {
        IntegerField field = count.newField(Required.NO);
        field.parse(b);
        for (;;) {
            try {
                b.mark();
                int tag = Tag.parseTag(b);
                b.reset();
                RepeatingGroupInstance instance = newInstance();
                if (instance.lookup(tag) == null)
                    break;
                instances.add(instance);
                instance.parse(b);
            } catch (TagMultipleTimesException e) {
                continue;
            }
        }
        if (instances.size() != field.getValue())
            throw new ParseException(count.prettyName() + ": Incorrect NumInGroup count for repeating group. Expected: " + field.getValue() + ", but was: " + instances.size(), SessionRejectReasonValue.NUM_IN_GROUP_MISMATCH);
    }

    @Override public String format() {
        StringBuilder result = new StringBuilder();
        result.append(new IntegerField(count, instances.size()).format());
        for (RepeatingGroupInstance instance : instances) {
            result.append(instance.format());
        }
        return result.toString();
    }

    @Override public boolean hasValue() {
        throw new UnsupportedOperationException();
    }

    @Override public boolean isEmpty() {
        return false;
    }

    @Override public boolean isFormatValid() {
        return true;
    }

    @Override public boolean isMissing() {
        throw new UnsupportedOperationException();
    }

    @Override public boolean isParsed() {
        throw new UnsupportedOperationException();
    }

    @Override public boolean isValueValid() {
        return true;
    }

    @Override public String prettyName() {
        throw new UnsupportedOperationException();
    }

    @Override public void setRequired(Required required) {
        throw new UnsupportedOperationException();
    }
}
