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
package stirling.fix.console;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import silvertip.CommandLine;
import silvertip.Connection;
import silvertip.Events;
import stirling.fix.Version;
import stirling.fix.console.commands.*;
import stirling.fix.messages.MessageFactory;
import stirling.fix.messages.fix42.DefaultMessageFactory;
import stirling.fix.session.Session;
import stirling.fix.session.store.InMemorySessionStore;

public class ConsoleClient {
    private static final Version VERSION = Version.FIX_4_2;
    private static final String SENDER_COMP_ID = "initiator";
    private static final String TARGET_COMP_ID = "OPENFIX";
    private static final String SLASH = System.getProperty("file.separator");
    private static final String FIX_DIRECTORY = System.getProperty("user.home") + SLASH + ".fixengine";
    private static final String HISTORY_FILE = FIX_DIRECTORY + SLASH + "console_history";

    private final Map<String, Command> commands = new HashMap<String, Command>();
    private final Map<String, String> orderIDs = new HashMap<String, String>();

    private final Console console;

    private stirling.fix.session.store.SessionStore sessionStore;
    private Connection conn;
    private Session session;
    private Events events;

    private stirling.fix.Config config = new stirling.fix.Config() {
        {
            setVersion(VERSION);
            setSenderCompId(SENDER_COMP_ID);
            setTargetCompId(TARGET_COMP_ID);
        }
    };

    private MessageFactory messageFactory = new DefaultMessageFactory();

    public static void main(String[] args) throws Exception {
        Console console = System.console();
        if (console == null) {
            System.err.println("no console");
        } else {
            console.printf("Stirling console, type 'help' for help text\n");
            new ConsoleClient(console).run(getInitialCommandLines(args));
        }
    }

    private static List<String> getInitialCommandLines(String[] args) throws IOException {
        if (args.length > 0)
            return readInitialCommandLines(args[0]);
        return new ArrayList<String>();
    }

    private static List<String> readInitialCommandLines(String filename) throws IOException {
        BufferedReader reader = null;
        String line = null;
        try {
            reader = new BufferedReader(new FileReader(filename));
            List<String> commandLines = new ArrayList<String>();
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#") || line.isEmpty())
                    continue;
                commandLines.add(line);
            }
            return commandLines;
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    public ConsoleClient(Console console) {
        this.console = console;
        this.sessionStore = new InMemorySessionStore();
        registerCommands();
    }

    public stirling.fix.session.store.SessionStore getSessionStore() {
        return sessionStore;
    }

    public void setSessionStore(stirling.fix.session.store.SessionStore sessionStore) {
        try {
            this.sessionStore.close();
        } catch (IOException e) {
        }
        this.sessionStore = sessionStore;
    }

    public MessageFactory getMessageFactory() {
        return messageFactory;
    }

    public void setMessageFactory(MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    public Connection getConnection() {
        return conn;
    }

    public void setConnection(Connection conn) {
        this.conn = conn;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void setConfig(stirling.fix.Config config) {
        this.config = config;
    }

    public stirling.fix.Config getConfig() {
        return config;
    }

    public String findOrderID(String clOrdID) {
        return orderIDs.get(clOrdID);
    }

    public void setOrderID(String clOrdID, String orderID) {
        orderIDs.put(clOrdID, orderID);
    }

    public Events getEvents() {
        return events;
    }

    public void quit() {
        events.stop();
    }

    public void run(List<String> initialCommandLines) throws IOException {
        events = Events.open();
        for (String commandLine : initialCommandLines) {
            console.printf("< %s\n", commandLine);
            Scanner scanner = new Scanner(commandLine);
            String commandName = scanner.next().toLowerCase();
            Command cmd = getCommand(commandName);
            if (cmd == null)
                continue;
            try {
                cmd.execute(ConsoleClient.this, scanner);
            } catch (CommandException e) {
                error(e.getMessage());
            }
        }
        final CommandLine commandLine = CommandLine.open(new CommandLine.Callback() {
            @Override
            public void commandLine(String commandLine) {
                Scanner scanner = new Scanner(commandLine);
                String commandName = scanner.next().toLowerCase();
                Command cmd = getCommand(commandName);
                if (cmd == null) {
                    error("unknown command");
                    printHelp();
                    prompt();
                } else {
                    try {
                        cmd.execute(ConsoleClient.this, scanner);
                    } catch (CommandArgException e) {
                        error(e.getMessage());
                        printUsage(commandName);
                        prompt();
                    } catch (CommandException e) {
                        error(e.getMessage());
                        prompt();
                    }
                }
            }
        });
        initializeFixDirectory();
        registerHistory(commandLine);
        commandLine.addCompletor(new CommandCompletor(this, commands));
        events.register(commandLine);
        events.dispatch(100);
    }

    private Command getCommand(String commandName) {
        return commands.get(commandName);
    }

    public void error(String message) {
        console.printf("ERROR: " + message + "\n");
    }

    private void prompt() {
        console.printf("> ");
    }

    private void registerCommands() {
        commands.put("quit", new Quit());
        commands.put("exit", new Quit());
        commands.put("connect", new Connect());
        commands.put("logon", new Logon());
        commands.put("logout", new Logout());
        commands.put("reset", new Reset());
        commands.put("session-store", new SessionStore());
        commands.put("storeseq", new StoreSequence());
        commands.put("available", new Available());
        commands.put("unavailable", new Unavailable());
        commands.put("profile", new Profile());
        commands.put("new-order-single", new NewOrderSingle());
        commands.put("cancel-order", new CancelOrder());
        commands.put("update-order", new UpdateOrder());
        commands.put("collateral-inquiry", new CollateralInquiry());
        commands.put("request-for-positions", new RequestForPositions());
        commands.put("config", new Config());
        commands.put("help", helpCommand());
        commands.put("?", helpCommand());
    }

    private void initializeFixDirectory() {
        File dir = new File(FIX_DIRECTORY);
        if (dir.isDirectory()) {
            return;
        }
        if (!dir.mkdirs()) {
            System.err.println("Unable to create directory: " + FIX_DIRECTORY);
        }
    }

    private void registerHistory(CommandLine commandLine) {
        try {
            commandLine.setHistory(new File(HISTORY_FILE));
        } catch (IOException e) {
            System.err.println("Unable to load console history");
        }
    }

    private Command helpCommand() {
        return new Command() {
            @Override
            public void execute(ConsoleClient client, Scanner scanner) throws CommandException {
                String commandName = null;
                if (scanner.hasNext())
                    commandName = scanner.next();
                console.printf("\n\n");
                if (commands.containsKey(commandName))
                    printUsage(commandName);
                else
                    printHelp();
                prompt();
            }

            @Override
            public String[] getArgumentNames(ConsoleClient client) {
                return commands.keySet().toArray(new String[0]);
            }

            @Override
            public String description() {
                return "Lists all available commands.";
            }

            @Override
            public String usage() {
                return ": Lists all available commands. Use 'help <command>' to display help for a single command.";
            }
        };
    }

    private void printUsage(String commandName) {
        Command command = getCommand(commandName);
        console.printf("Usage:\n");
        console.printf("  %s %s\n", commandName, command.usage());
        String[] argumentNames = command.getArgumentNames(this);
        if (argumentNames.length > 0)
            printAllAvailableArguments(commandName, argumentNames);
    }

    private void printAllAvailableArguments(String commandName, String[] argumentNames) {
        console.printf("\nAll available arguments for '" + commandName + "': ");
        for (String argument : argumentNames) {
            if (argumentHasValue(argument))
                console.printf(argument);
            else
                console.printf(argument + "<value>");
            console.printf(" ");
        }
        console.printf("\n\n");
    }

    private boolean argumentHasValue(String argument) {
        return argument.indexOf("=") != argument.length() - 1;
    }

    private void printHelp(String commandName) {
        Command command = getCommand(commandName);
        console.printf("  %-22s %s\n", commandName, command.description());
    }

    private void printHelp() {
        console.printf("Available commands:\n");
        List<String> commandNames = new ArrayList<String>(commands.keySet());
        Collections.sort(commandNames);
        for (String commandName : commandNames) {
            printHelp(commandName);
        }
        console.printf("\n");
    }
}
