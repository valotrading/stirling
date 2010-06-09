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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import fixengine.Config;
import fixengine.client.InitiatorProtocolHandler;
import fixengine.messages.DefaultMessageVisitor;
import fixengine.messages.Message;
import fixengine.messages.SequenceResetMessage;
import fixengine.session.HeartBtInt;
import fixengine.session.store.SessionStore;

/**
 * @author Pekka Enberg
 */
public class OpenFixProtocolHandler extends InitiatorProtocolHandler {
    private static final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

    public OpenFixProtocolHandler(Config config, SessionStore store) {
        super(config, new HeartBtInt(30), store);
    }

    @Override
    public void onEstablished() {
        session.logon();
        if (!session.isAuthenticated()) {
            System.out.println("Authentication failed. Exiting...");
            System.exit(1);
        }
        System.out.println("Authentication OK");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!session.isDisconnected()) {
                    session.processMessage(new DefaultMessageVisitor() {
                        @Override
                        public void defaultAction(Message message) {
                            System.out.println(message);
                        }
                    });
                }
            }
        }).start();

        System.out.println("Welcome to interactive OpenFIX certification client.");
        System.out.println();
        System.out.println("Please type 'HELP' if you need assistance on how to use this client.");

        while (!session.isDisconnected()) {
            System.out.print(">>> ");
            String command = readLine();

            if (command == null) {
                break;
            }

            if (command.contains("HELP")) {
                System.out.println("Available commands:");
                System.out.println("\tLOGOUT - terminate this FIX session");
                System.out.println("\tRESET <number> - reset sequence numbers");
            }

            if (command.contains("RESET")) {
                SequenceResetMessage seqReset = new SequenceResetMessage();
                seqReset.setGapFillFlag(false);
                seqReset.setNewSeqNo(Integer.parseInt(command.substring(command.indexOf(' ') + 1)));
                session.send(seqReset);
            }

            if (command.contains("LOGOUT")) {
                break;
            }
        }

        session.logout();
        if (session.isAuthenticated()) {
            System.out.println("Logout failed. Exiting...");
            System.exit(1);
        }
        System.exit(0);
    }

    private String readLine() {
        try {
            return stdin.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
