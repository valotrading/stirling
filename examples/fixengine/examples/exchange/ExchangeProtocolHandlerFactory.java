/*
 * Copyright 2009 the original author or authors.
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
package fixengine.examples.exchange;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fixengine.Config;
import fixengine.Version;
import fixengine.io.ProtocolHandler;
import fixengine.io.ProtocolHandlerFactory;
import fixengine.session.AuthenticationManager;
import fixengine.session.ConnectionManager;
import fixengine.session.HeartBtInt;

/**
 * @author Pekka Enberg 
 */
public class ExchangeProtocolHandlerFactory implements ProtocolHandlerFactory {
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private static final HeartBtInt HEART_BT_INT = new HeartBtInt(30);
    private static final String SENDER_COMP_ID = "acceptor";
    private static final String TARGET_COMP_ID = "initiator";

    @Override
    public ProtocolHandler newProtocolHandler() {
        return new ExchangeProtocolHandler(executor, getConfig(), HEART_BT_INT) {
            @Override
            protected void onEstablished() {
                session.logon();
                if (!session.isAuthenticated()) {
                    session.disconnect();
                }
            }

            @Override
            protected AuthenticationManager getAuthenticationManager() {
                return new AuthenticationManager() {
                    @Override
                    public boolean authenticate(String senderCompId) {
                        return true;
                    }
                };
            }

            @Override
            protected ConnectionManager getConnectionManager() {
                return new ConnectionManager() {
                    @Override
                    public boolean connect(String senderCompId) {
                        return true;
                    }

                    @Override
                    public void disconnect(String senderCompId) {
                    }
                };
            }
        };
    }

    private Config getConfig() {
        Config result = new Config();
        result.setSenderCompId(SENDER_COMP_ID);
        result.setTargetCompId(TARGET_COMP_ID);
        result.setVersion(Version.FIX_4_3);
        return result;
    }
}
