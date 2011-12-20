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

import stirling.fix.tags.fix42.RefSeqNo;
import stirling.fix.tags.fix42.RefTagId;
import stirling.fix.tags.fix43.SessionRejectReason;
import stirling.fix.tags.fix42.Text;

/**
 * @author Pekka Enberg
 */
public class Reject extends AbstractMessage {
    public Reject(MessageHeader header) {
        super(header);

        field(RefSeqNo.Tag());
        field(RefTagId.Tag(), Required.NO);
        field(SessionRejectReason.Tag(), Required.NO);
        field(Text.Tag(), Required.NO);
    }

    public String reason() {
        return getString(Text.Tag());
    }

    @Override public void apply(MessageVisitor visitor) {
        visitor.visit(this);
    }

    @Override public boolean isAdminMessage() {
        return true;
    }
}
