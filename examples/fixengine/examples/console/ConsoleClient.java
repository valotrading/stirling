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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import fixengine.session.Session;
import fixengine.session.store.MongoSessionStore;
import fixengine.session.store.SessionStore;

import fixengine.messages.DefaultMessageFactory;
import fixengine.messages.MessageFactory;

import silvertip.CommandLine;
import silvertip.Connection;
import silvertip.Events;

import fixengine.examples.console.commands.*;

/**
 * @author Karim Osman
 */
public class ConsoleClient {
  private final Map<String, Command> commands = new HashMap<String, Command>();

  private final Console console;
  private final SessionStore sessionStore;

  private Connection conn;
  private Session session;
  private Events events;
  private MessageFactory messageFactory = new DefaultMessageFactory();

  public static void main(String[] args) throws Exception {
    Console console = System.console();
    if (console == null) {
      System.err.println("no console");
    } else {
      MongoSessionStore sessionStore = new MongoSessionStore("localhost", 27017);
      new ConsoleClient(console, sessionStore).run();
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

  public Events getEvents() {
    return events;
  }

  public void quit() {
    events.stop();
  }

  public void run() throws IOException {
    final CommandLine commandLine = CommandLine.open(new CommandLine.Callback() {
      @Override
      public void commandLine(String commandLine) {
        Scanner scanner = new Scanner(commandLine);
        Command cmd = commands.get(scanner.next().toLowerCase());
        if (cmd == null) {
          System.out.println("unknown command");
        } else {
          try {
            cmd.execute(ConsoleClient.this, scanner);
          } catch (CommandArgException e) {
            console.printf(e.getMessage() + "\n");
          }
        }
      }
    });
    events = Events.open(100);
    events.register(commandLine);
    events.dispatch();
  }

  private void registerCommands() {
    commands.put("quit", new Quit());
    commands.put("logon", new Logon());
    commands.put("logout", new Logout());
    commands.put("reset", new Reset());
    commands.put("storeseq", new StoreSequence());
    commands.put("available", new Available());
    commands.put("unavailable", new Unavailable());
    commands.put("profile", new Profile());
  }
}
