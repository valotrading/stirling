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
package fixengine.examples.console.commands;

import java.io.Console;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import fixengine.Config;
import fixengine.Version;
import fixengine.client.FixClient;
import fixengine.client.InitiatorProtocolHandler;
import fixengine.io.ProtocolHandler;
import fixengine.messages.DefaultMessageVisitor;
import fixengine.messages.Message;
import fixengine.session.HeartBtInt;
import fixengine.session.store.SessionStore;

import fixengine.examples.console.ConsoleClient.State;

/**
 * @author Karim Osman
 */
public class Logon implements Command {
    private static final Version VERSION = Version.FIX_4_2;
    private static final String SENDER_COMP_ID = "initiator";
    private static final String TARGET_COMP_ID = "OPENFIX";

    private SessionStore store;

    public Logon(SessionStore store) {
        this.store = store;
    }

    public String name() {
        return "logon";
    }

    public void execute(State state, Console console, Scanner scanner) {
        try {
            ProtocolHandler handler = newProtocolHandler(getConfig(), new HeartBtInt(30));
            state.client = new FixClient(host(console, scanner), port(console, scanner), handler);
            state.client.start();
        } catch(CommandArgException e) {
            console.printf(e.getMessage() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private InetAddress host(Console console, Scanner scanner) throws CommandArgException {
        if (!scanner.hasNext())
            throw new CommandArgException("hostname must be specified");
        try {
          return InetAddress.getByName(scanner.next());
        } catch (UnknownHostException e) {
            throw new CommandArgException("unknown hostname");
        }
    }

    private int port(Console console, Scanner scanner) throws CommandArgException {
        if (!scanner.hasNext())
            throw new CommandArgException("port must be specified");
        try {
          return Integer.parseInt(scanner.next());
        } catch (NumberFormatException e) {
            throw new CommandArgException("invalid port");
        }
    }

    private ProtocolHandler newProtocolHandler(Config config, HeartBtInt heartBtInt) {
        return new InitiatorProtocolHandler(config, heartBtInt, store) {
            @Override
            public void onEstablished() {
                session.logon();
                if (!session.isAuthenticated()) {
                    System.out.println("Authentication failed. Exiting...");
                    System.exit(1);
                }
                System.out.println("Authentication OK");
                Message response = session.processMessage(new DefaultMessageVisitor());
                System.out.println(response);
            }
        };
    }

    private static Config getConfig() {
        Config config = new Config();
        config.setSenderCompId(SENDER_COMP_ID);
        config.setTargetCompId(TARGET_COMP_ID);
        config.setVersion(VERSION);
        return config;
    }
}
