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
package fixengine.session;

import java.util.SortedSet;
import java.util.TreeSet;

import fixengine.messages.Message;
import fixengine.messages.MessageComparator;

/**
 * Note: this class is <b>not thread-safe</b>! The callers are expected to take
 * care of locking.
 * 
 * @author Pekka Enberg
 */
public class MessageQueue {
    private SortedSet<Message> queue = new TreeSet<Message>(new MessageComparator());
    private Sequence sequence = new Sequence();
    private int maxSeqNum;

    public void skip(Message message) {
        skip(message.getMsgSeqNum());
    }

    public void enqueue(Message message) {
        queue.add(message);
        skip(message.getMsgSeqNum());
    }

    public void skip(int msgSeqNum) {
        if (msgSeqNum == sequence.peek()) {
            sequence.next();
        }
        maxSeqNum = Math.max(msgSeqNum, maxSeqNum);
    }

    public Message dequeue() {
        Message result = queue.first();
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
