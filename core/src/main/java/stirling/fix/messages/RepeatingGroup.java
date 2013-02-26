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
package stirling.fix.messages;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import stirling.fix.tags.fix43.SessionRejectReason;

public abstract class RepeatingGroup implements Field {
    private final List<RepeatingGroupInstance> instances = new ArrayList<RepeatingGroupInstance>();
    private final Tag<? extends IntegerField> countTag;
    private IntegerField countField;
    private Required required;

    public RepeatingGroup(Tag<? extends IntegerField> countTag) {
        this(countTag, Required.YES);
    }

    public RepeatingGroup(Tag<? extends IntegerField> countTag, Required required) {
        this.countTag = countTag;
        this.countField = countTag.newField(Required.NO);
        this.required = required;
    }

    public Tag<?> countTag() {
        return countTag;
    }

    public abstract RepeatingGroupInstance newInstance();

    public void addInstance(RepeatingGroupInstance instance) {
        instances.add(instance);
        countField.setValue(instances.size());
    }

    public List<RepeatingGroupInstance> getInstances() {
        return Collections.unmodifiableList(instances);
    }

    @Override public void parse(ByteBuffer b) {
        countField.parse(b);
        while (b.hasRemaining()) {
            try {
                int tag = Tag.peekTag(b);
                RepeatingGroupInstance instance = newInstance();
                if (instance.lookup(tag) == null)
                    break;
                instances.add(instance);
                instance.parse(b);
            } catch (TagMultipleTimesException e) {
                b.reset();
                continue;
            }
        }
        if (instances.size() != countField.getValue())
            throw new ParseException(countTag.prettyName() + ": Incorrect NumInGroup count for repeating group. Expected: " + countField.getValue() +
                                     ", but was: " + instances.size(), SessionRejectReason.NumInGroupMismatch());
    }

    @Override public String format() {
        StringBuilder result = new StringBuilder();
        if (hasValue()) {
            result.append(new IntegerField(countTag, instances.size()).format());
            for (RepeatingGroupInstance instance : instances) {
                result.append(instance.format());
            }
        }
        return result.toString();
    }

    @Override public boolean hasValue() {
        return countField.hasValue();
    }

    @Override public boolean isParsed() {
        return countField.isParsed();
    }

    @Override public Tag<?> tag() {
        throw new UnsupportedOperationException();
    }

    @Override public String prettyName() {
        throw new UnsupportedOperationException();
    }

    @Override public Required required() {
        return required;
    }

    @Override public String toString() {
        StringBuilder result = new StringBuilder();
        if (isParsed()) {
            result.append(new IntegerField(countTag, instances.size()).toString());
            if (instances.size() > 0) {
                result.append(" ");
            }
            for (RepeatingGroupInstance instance : instances) {
                result.append(instance.toString() + " ");
            }
        }
        return result.toString();
    }
}
