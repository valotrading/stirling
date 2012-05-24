/*
 * Copyright 2012 the original author or authors.
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
package stirling.fix.examples.console.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import stirling.fix.examples.console.Arguments;
import stirling.fix.examples.console.ConsoleClient;
import stirling.fix.session.store.DiskSessionStore;
import stirling.fix.session.store.InMemorySessionStore;
import stirling.fix.session.store.MongoSessionStore;
import stirling.fix.session.store.NonPersistentInMemorySessionStore;
import stirling.fix.session.store.SessionStoreException;

public class SessionStore implements Command {
  private static final String ARGUMENT_NAME = "Name";

  private Map<String, SessionStoreFactory> factories = new HashMap<String, SessionStoreFactory>();

  public SessionStore() {
    factories.put("disk", new SessionStoreFactory.Disk());
    factories.put("in-memory", new SessionStoreFactory.InMemory());
    factories.put("mongo", new SessionStoreFactory.Mongo());
    factories.put("non-persistent-in-memory", new SessionStoreFactory.NonPersistentInMemory());
  }

  public void execute(ConsoleClient client, Scanner scanner) throws CommandException {
    String sessionStore = new Arguments(scanner).requiredValue(ARGUMENT_NAME);
    SessionStoreFactory factory = factories.get(sessionStore);
    if (factory == null)
      throw new CommandArgException("unknown session store: " + sessionStore);
    try {
      client.setSessionStore(factory.create());
    } catch (SessionStoreException e) {
      throw new CommandException(e.getMessage());
    }
  }

  public String[] getArgumentNames(ConsoleClient client) {
    List<String> sessionStores = new ArrayList<String>(factories.keySet());
    List<String> argumentNames = new ArrayList<String>();
    Collections.sort(sessionStores);
    for (String sessionStore : sessionStores)
      argumentNames.add(ARGUMENT_NAME + "=" + sessionStore);
    return argumentNames.toArray(new String[0]);
  }

  @Override public String usage() {
    return ARGUMENT_NAME + "=<session-store> : Sets the session store.";
  }
}
