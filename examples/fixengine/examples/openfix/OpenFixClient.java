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
package fixengine.examples.openfix;

import java.net.InetAddress;

import org.apache.log4j.PropertyConfigurator;

import fixengine.Config;
import fixengine.Version;
import fixengine.client.FixClient;
import fixengine.io.ProtocolHandler;
import fixengine.session.store.SessionStore;
import fixengine.session.store.MongoSessionStore;

/**
 * @author Pekka Enberg
 */
public class OpenFixClient {
    private static final Version VERSION = Version.FIX_4_3;
    private static final String SENDER_COMP_ID = "initiator";
    private static final String TARGET_COMP_ID = "acceptor";
    private static final String HOST = "localhost";
    private static final int PORT = 4000;

    public static void main(String[] args) throws Exception {
        setupLogging();
        run();
    }

    private static void setupLogging() {
        PropertyConfigurator.configure("examples/fixengine/examples/log4j.properties");
    }

    private static void run() throws Exception {
        SessionStore store = new MongoSessionStore("localhost", 27017);
        ProtocolHandler handler = new OpenFixProtocolHandler(getConfig(), store);
        FixClient client = new FixClient(InetAddress.getByName(HOST), PORT, handler);
        client.start();
    }

    private static Config getConfig() {
        Config config = new Config();
        config.setSenderCompId(SENDER_COMP_ID);
        config.setTargetCompId(TARGET_COMP_ID);
        config.setVersion(VERSION);
        return config;
    }
}
