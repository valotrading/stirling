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
package fixengine.examples.console;

import java.io.Console;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import fixengine.client.FixClient;
import fixengine.session.store.MongoSessionStore;
import fixengine.session.store.SessionStore;

import fixengine.examples.console.commands.*;

/**
 * @author Karim Osman
 */
public class ConsoleClient {
    private static final String PROMPT = "> ";
    private static Map<String, Command> commands = new HashMap<String, Command>();

    static {
        try {
            SessionStore store = new MongoSessionStore("localhost", 27017);
            registerCommand(new Quit());
            registerCommand(new Logon(store));
            registerCommand(new Logout());
            registerCommand(new Reset());
            registerCommand(new StoreSequence(store));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class State {
        public FixClient client = null;
        public boolean quit = false;
    }

    public static void main(String[] args) throws Exception {
        Console console = System.console();
        if (console == null) {
            System.err.println("no console");
        } else {
            commandLoop(console);
        }
    }

    private static void registerCommand(Command cmd) {
        commands.put(cmd.name(), cmd);
    }

    private static void commandLoop(Console console) {
        State state = new State();
        while (true) {
            String line = console.readLine(PROMPT);
            Scanner scanner = new Scanner(line);
            try {
                if (!scanner.hasNext())
                    continue;
                Command cmd = commands.get(scanner.next().toLowerCase());
                if (cmd == null) {
                    console.printf("unknown command\n");
                    continue;
                }
                cmd.execute(state, console, scanner);
                if (state.quit)
                    break;
            } finally {
                scanner.close();
            }
        }
    }
}
