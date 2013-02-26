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

import stirling.fix.tags.fix43.SessionRejectReason;

import java.nio.ByteBuffer;

public class RepeatingGroupInstance extends DefaultFieldContainer implements Field {
    private final Tag<?> delimiter;

    public RepeatingGroupInstance(Tag<?> delimiter) {
        field(this.delimiter = delimiter);
    }

    @Override public void parse(ByteBuffer b) {
        int tag = Tag.peekTag(b);
        if (tag != delimiter.value())
            throw new ParseException(lookup(tag).prettyName() + ": Repeating group fields out of order", SessionRejectReason.OutOfOrderGroupField());
        super.parse(b);
    }

    @Override public String format() {
        return super.format();
    }

    @Override public boolean hasValue() {
        return false;
    }

    @Override public boolean isEmpty() {
        return false;
    }

    @Override public boolean isMissing() {
        return false;
    }

    @Override public boolean isConditional() {
        return false;
    }

    @Override public boolean isParsed() {
        return false;
    }

    @Override public Tag<?> tag() {
        throw new UnsupportedOperationException();
    }

    @Override public String prettyName() {
        return "";
    }

    @Override public Required required() {
        throw new UnsupportedOperationException();
    }
}
