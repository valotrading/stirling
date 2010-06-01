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
package fixengine.spec;

import java.util.ArrayList;
import java.util.List;

import fixengine.io.ObjectOutputStream;
import fixengine.messages.Message;

/**
 * @author Pekka Enberg 
 */
public class StubMessageStream implements ObjectOutputStream<Message> {
    private List<Message> output = new ArrayList<Message>();
    private List<Message> input = new ArrayList<Message>();
    private boolean closed;

    /** Write the next message to be received by the client. */
    public void write(Message message) {
        output.add(message);
    }

    /** Read the next message sent to this server. */
    public Message read() {
        return input.remove(0);
    }

    @Override
    public void writeObject(Message message) {
        input.add(message);
    }

    @Override
    public void close() {
        this.closed = true;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }
}
