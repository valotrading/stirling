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
package fixengine.client;

import fixengine.Config;
import fixengine.io.AbstractProtocolHandler;
import fixengine.io.CodedObjectOutputStream;
import fixengine.messages.HeartbeatMessage;
import fixengine.messages.Message;
import fixengine.messages.MessageConverter;
import fixengine.messages.Session;
import fixengine.messages.TestRequestMessage;
import fixengine.session.HeartBtInt;
import fixengine.session.InitiatorSession;
import fixengine.session.store.SessionStore;

/**
 * @author Pekka Enberg 
 */
public class InitiatorProtocolHandler extends AbstractProtocolHandler<Message> {
    private final Config config;
    protected Session session;
    private long testReqId;
    private SessionStore store;

    public InitiatorProtocolHandler(Config config, HeartBtInt heartBtInt, SessionStore store) {
        super(new MessageConverter(), heartBtInt.testRequest(), heartBtInt.heartbeat());

        this.config = config;
        this.store = store;
    }

    public Session getSession() {
        return session;
    }

    @Override
    protected void init(CodedObjectOutputStream<Message> output) {
        session = new InitiatorSession(output, config, store);
    }

    @Override
    public void onEndOfStream() {
        session.disconnect();
    }

    @Override
    public void onTransmitIdle() {
        session.send(new HeartbeatMessage());
    }

    @Override
    public void onReceiveIdle() {
        TestRequestMessage req = new TestRequestMessage();
        req.setTestReqId(Long.toString(++testReqId));
        session.send(req);
    }

    @Override
    public void onMessageReceived(Message message) {
        session.receive(message);
    }
}
