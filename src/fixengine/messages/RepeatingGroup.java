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

/**
 * @author Pekka Enberg 
 */
public abstract class RepeatingGroup<T extends RepeatingGroupInstance> implements Field {
    private final List<T> instances = new ArrayList<T>();
    private final IntegerField instanceCount;
    private boolean parsed;

    public RepeatingGroup(IntegerField instanceCount) {
        this.instanceCount = instanceCount;
    }

    public void add(T instance) {
        instances.add(instance);
    }

    public List<T> getInstances() {
        return instances;
    }

    @Override public void setRequired(Required required) {
        throw new UnsupportedOperationException();
    }

    @Override public boolean hasValue() {
        throw new UnsupportedOperationException();
    }

    public void parseValue(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String format() {
        Integer count = instanceCount.getValue();
        if (count == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append(instances.get(i).format());
        }
        return result.toString();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isFormatValid() {
        return true;
    }

    @Override
    public boolean isMissing() {
        return false;
    }

    @Override
    public boolean isValueValid() {
        return true;
    }

    @Override
    public boolean isUserDefined() {
        return false;
    }

    @Override
    public boolean isDuplicate() {
        return false;
    }

    @Override
    public int checksum() {
        int result = instanceCount.checksum();
        for (T instance : instances) {
            result += instance.checksum();
        }
        return result;
    }

    @Override
    public int length() {
        int result = instanceCount.length();
        for (T instance : instances) {
            result += instance.length();
        }
        return result;
    }

    @Override
    public boolean isParsed() {
        return parsed;
    }

    @Override
    public String name() {
        return instanceCount.name();
    }

    @Override
    public boolean supports(Tag tag) {
        return instanceCount.supports(tag);
    }

    @Override
    public Tag tag() {
        return instanceCount.tag();
    }

    @Override
    public String prettyName() {
        throw new UnsupportedOperationException();
    }

    protected abstract T newInstance();
}