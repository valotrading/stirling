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

import fixengine.Config;
import fixengine.io.ObjectOutputStream;
import fixengine.messages.DefaultMessageVisitor;
import fixengine.messages.LogonMessage;
import fixengine.messages.LogoutMessage;
import fixengine.messages.Message;
import fixengine.session.store.SessionStore;

/**
 * @author Pekka Enberg 
 */
public class InitiatorSession extends AbstractSession {
    public InitiatorSession(ObjectOutputStream<Message> stream, Config config, SessionStore store) {
        super(stream, config, store);
    }

    public void logon() {
        store.load(this);
        LogonMessage logonMsg = new LogonMessage(false);
        send(logonMsg);

        processMessage(new DefaultMessageVisitor() {
            @Override
            public void visit(LogonMessage message) {
                if (message.hasValidTargetCompId(config)) {
                    authenticated = true;
                } else {
                    send(new LogoutMessage());
                    disconnect();
                }
            }

            @Override
            public void defaultAction(Message message) {
                authenticated = false;
                disconnect();
            }
        });
    }
}
