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
package stirling.fix.examples.console.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import stirling.fix.examples.console.Arguments;
import stirling.fix.examples.console.ConsoleClient;
import stirling.fix.messages.MessageFactory;

public class Profile implements Command {
  private static final String ARGUMENT_NAME = "Name";

  private Map<String, MessageFactory> factories = new HashMap<String, MessageFactory>();

  public Profile() {
    factories.put("default", new stirling.fix.messages.fix42.DefaultMessageFactory());
    factories.put("bats-europe", new stirling.fix.messages.fix42.bats.europe.MessageFactory());
    factories.put("mb-trading", new stirling.fix.messages.fix44.mbtrading.MessageFactory());
    factories.put("burgundy", new stirling.fix.messages.fix44.burgundy.MessageFactory());
    factories.put("hotspot-fx", new stirling.fix.messages.fix42.hotspotfx.MessageFactory());
    factories.put("samrat", new stirling.fix.messages.fix42.samrat.MessageFactory());
  }

  public void execute(ConsoleClient client, Scanner scanner) throws CommandArgException {
    String profile = new Arguments(scanner).requiredValue(ARGUMENT_NAME);
    MessageFactory factory = factories.get(profile);
    if (factory == null)
      throw new CommandArgException("unknown profile: " + profile);
    client.setMessageFactory(factory);
  }

  @Override public String[] getArgumentNames(ConsoleClient client) {
    List<String> profiles = new ArrayList<String>(factories.keySet());
    List<String> argumentNames = new ArrayList<String>();
    Collections.sort(profiles);
    for (String profile : profiles)
      argumentNames.add(ARGUMENT_NAME + "=" + profile);
    return argumentNames.toArray(new String[0]);
  }

  @Override public String usage() {
    return ARGUMENT_NAME + "=<profile> : Sets profile which will be used for creating and sending messages.";
  }
}
