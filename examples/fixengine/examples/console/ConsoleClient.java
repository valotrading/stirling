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

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

import fixengine.session.Session;
import fixengine.session.store.MongoSessionStore;
import fixengine.session.store.SessionStore;

import fixengine.messages.MessageFactory;
import fixengine.messages.fix42.DefaultMessageFactory;

import fixengine.Version;

import silvertip.CommandLine;
import silvertip.Connection;
import silvertip.Events;

import fixengine.examples.console.commands.*;

/**
 * @author Karim Osman
 */
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
  private final SessionStore sessionStore;

  private Connection conn;
  private Session session;
  private Events events;

  private fixengine.Config config = new fixengine.Config() {
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
      MongoSessionStore sessionStore = new MongoSessionStore("localhost", 27017);
      console.printf("FIX console started. Use \"help\" for a list of available commands. Also, tab completion is available.\n");
      new ConsoleClient(console, sessionStore).run(getInitialCommandLines(args));
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
        if (line.startsWith("#"))
          continue;
        commandLines.add(line);
      }
      return commandLines;
    } finally {
      if (reader != null)
        reader.close();
    }
  }

  public ConsoleClient(Console console, SessionStore sessionStore) {
    this.console = console;
    this.sessionStore = sessionStore;
    registerCommands();
  }

  public Console getConsole() {
    return console;
  }

  public SessionStore getSessionStore() {
    return sessionStore;
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

  public void setConfig(fixengine.Config config) {
    this.config = config;
  }

  public fixengine.Config getConfig() {
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
    events = Events.open(100);
    for (String commandLine : initialCommandLines) {
      Scanner scanner = new Scanner(commandLine);
      String commandName = scanner.next().toLowerCase();
      Command cmd = getCommand(commandName);
      if (cmd == null)
        continue;
      try {
        cmd.execute(ConsoleClient.this, scanner);
      } catch (CommandArgException e) {
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
          }
        }
      }
    });
    initializeFixDirectory();
    registerHistory(commandLine);
    commandLine.addCompletor(new CommandCompletor(this, commands));
    events.register(commandLine);
    events.dispatch();
  }

  private Command getCommand(String commandName) {
    return commands.get(commandName);
  }

  private void error(String message) {
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
    commands.put("storeseq", new StoreSequence());
    commands.put("available", new Available());
    commands.put("unavailable", new Unavailable());
    commands.put("profile", new Profile());
    commands.put("new-order-single", new NewOrderSingle());
    commands.put("cancel-order", new CancelOrder());
    commands.put("update-order", new UpdateOrder());
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
      public void execute(ConsoleClient client, Scanner scanner) throws CommandArgException {
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
      public String usage() {
        return ": Lists all available commands. Use 'help <command>' to display help for a single command.";
      }
    };
  }

  private void printUsage(String commandName) {
    console.printf("Usage:\n");
    printHelp(commandName);
    String[] argumentNames = getCommand(commandName).getArgumentNames(this);
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
    StringBuilder builder = new StringBuilder("  ");
    builder.append(commandName);
    builder.append(" ");
    builder.append(command.usage());
    builder.append("\n");
    console.printf(builder.toString());
  }

  private void printHelp() {
    console.printf("Available commands:\n");
    for (String commandName : commands.keySet()) {
      printHelp(commandName);
    }
    console.printf("\n");
  }
}
