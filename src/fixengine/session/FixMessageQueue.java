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
package fixengine.session;

import java.util.SortedSet;
import java.util.TreeSet;

import fixengine.messages.FixMessageComparator;
import fixengine.messages.Message;

import silvertip.FixMessage;

public class FixMessageQueue {
    private SortedSet<FixMessage> queue = new TreeSet<FixMessage>(new FixMessageComparator());
    private Sequence sequence = new Sequence();
    private int maxSeqNum;

    public void skip(Message message) {
        skip(message.getMsgSeqNum());
    }

    public void enqueue(FixMessage message) {
        queue.add(message);
        skip(message.getMsgSeqNum());
    }

    public void skip(int msgSeqNum) {
        if (msgSeqNum == sequence.peek()) {
            sequence.next();
        }
        maxSeqNum = Math.max(msgSeqNum, maxSeqNum);
    }

    public FixMessage dequeue() {
        FixMessage result = queue.first();
        queue.remove(result);
        return result;
    }

    public void reset(int newSeqNum) {
        // Reset the sequence numbers but keep any messages that are on the
        // queue as they need to be processed still.
        sequence.reset(newSeqNum);
        maxSeqNum = 0;
    }

    public boolean hasSeqNumGap() {
        return nextSeqNum() <= maxSeqNum;
    }

    public int nextSeqNum() {
        return sequence.peek();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
