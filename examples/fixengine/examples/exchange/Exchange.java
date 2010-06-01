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

import java.net.InetAddress;

import org.apache.log4j.PropertyConfigurator;

import fixengine.server.FixServer;

/**
 * @author Pekka Enberg
 */
public class Exchange {
    private static String HOST = "localhost";
    private static int PORT = 4000;

    public static void main(String[] args) throws Exception {
        setupLogging();
        run();
    }

    private static void setupLogging() {
        PropertyConfigurator.configure("examples/fixengine/examples/log4j.properties");
    }

    private static void run() throws Exception {
        ExchangeProtocolHandlerFactory factory = new ExchangeProtocolHandlerFactory();
        FixServer server = new FixServer(InetAddress.getByName(HOST), PORT, factory);

        System.out.println("Starting server on " + HOST + ":" + PORT);
        server.start();
    }
}