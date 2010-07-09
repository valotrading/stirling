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

import fixengine.tags.GapFillFlag;
import fixengine.tags.NewSeqNo;

/**
 * @author Pekka Enberg 
 */
public class SequenceResetMessage extends AbstractMessage {
    private BooleanField gapFillFlag = new BooleanField(GapFillFlag.TAG, Required.NO);
    private IntegerField newSeqNo = new IntegerField(NewSeqNo.TAG);

    public SequenceResetMessage() {
        this(new MessageHeader(MsgTypeValue.SEQUENCE_RESET));
    }

    public SequenceResetMessage(MessageHeader header) {
        super(header);
        
        add(gapFillFlag);
        add(newSeqNo);
    }
    
    @Override
    public void apply(MessageVisitor visitor) {
        visitor.visit(this);
    }

    public void setGapFillFlag(boolean gapFillFlag) {
        this.gapFillFlag.setValue(gapFillFlag);
    }
    
    public boolean getGapFillFlag() {
        return gapFillFlag.getValue();
    }

    public void setNewSeqNo(int newSeqNo) {
        this.newSeqNo.setValue(newSeqNo);
    }
    
    public int getNewSeqNo() {
        return newSeqNo.getValue();
    }

    public boolean isResetOk(int nextSeqNum) {
        /*
         * If the other end is forcing a sequence reset, don't care about
         * MsgSeqNum of the SequenceReset message.
         */
        if (!getGapFillFlag())
            return true;
        return getMsgSeqNum() == nextSeqNum;
    }
}
