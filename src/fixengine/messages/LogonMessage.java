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

import static fixengine.messages.EncryptMethod.NONE;

/**
 * @author Pekka Enberg 
 */
public class LogonMessage extends AbstractMessage {
    private ResetSeqNumFlagField resetSeqNumFlag = new ResetSeqNumFlagField(Required.NO);

    public LogonMessage() {
        this(true);
    }

    public LogonMessage(MessageHeader header) {
        this(header, true);
    }

    public LogonMessage(boolean useResetSeqNumFlagTag) {
        this(new MessageHeader(MessageType.LOGON), useResetSeqNumFlagTag);
    }

    public LogonMessage(MessageHeader header, boolean useResetSeqNumFlagTag) {
        super(header);

        add(new EncryptMethodField(NONE));
        add(new HeartBtIntField(30));

        if (useResetSeqNumFlagTag) add(resetSeqNumFlag);
    }

    @Override
    public void apply(MessageVisitor visitor) {
        visitor.visit(this);
    }

    public void setResetSeqNumFlag(boolean resetSeqNumFlag) {
        this.resetSeqNumFlag.setValue(resetSeqNumFlag);
    }
}
