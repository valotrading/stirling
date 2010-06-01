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
import fixengine.messages.NullMessage;

/**
 * @author Pekka Enberg 
 */
public class AcceptorSession extends AbstractSession {
    private final AuthenticationManager am;
    private final ConnectionManager cm;

    public AcceptorSession(ObjectOutputStream<Message> stream, Config config, AuthenticationManager am, ConnectionManager cm) {
        super(stream, config);
        this.am = am;
        this.cm = cm;
    }

    public void logon() {
        // The initial Logon message can be out-of-order but we still accept it.
        // Sequence numbers are synchronized after authentication if necessary.
        processMessage(new DefaultMessageVisitor() {
            @Override
            public void defaultAction(Message message) {
                logon(message);
            }

            @Override
            public void visit(NullMessage message) {
                authenticated = false;
                disconnect();
            }
        });
    }

    private void logon(Message message) {
        config.setTargetCompId(message.getSenderCompId());
        message.apply(new DefaultMessageVisitor() {
            @Override
            public void visit(LogonMessage message) {
                if (cm.connect(config.getTargetCompId())) {
                    authenticated = am.authenticate(config.getTargetCompId());
                    if (authenticated) {
                        send(new LogonMessage());
                    } else {
                        disconnect();
                    }
                } else {
                    disconnect();
                }
            }

            @Override
            public void defaultAction(Message message) {
                send(new LogoutMessage());
                authenticated = false;
                disconnect();
            }
        });
    }

    @Override
    public void disconnect() {
        cm.disconnect(config.getTargetCompId());
        stream.close();
        synchronized (queue) {
            queue.notify();
        }
    }
}