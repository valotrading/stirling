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
import java.util.List;
import java.util.Scanner;

import stirling.fix.examples.console.Arguments;
import stirling.fix.examples.console.ConsoleClient;

public class Profile implements Command {
  private static final String DEFAULT_PROFILE = "default";
  private static final String BATS_PROFILE = "bats-europe";
  private static final String MB_TRADING_PROFILE = "mb-trading";
  private static final String BURGUNDY_PROFILE = "burgundy";
  private static final String HOTSPOT_FX_PROFILE = "hotspot-fx";
  private static final String ARGUMENT_NAME = "Name";

  public void execute(ConsoleClient client, Scanner scanner) throws CommandArgException {
    String profile = new Arguments(scanner).requiredValue(ARGUMENT_NAME);
    if (profile.equals(DEFAULT_PROFILE))
      client.setMessageFactory(new stirling.fix.messages.fix42.DefaultMessageFactory());
    else if (profile.equals(BATS_PROFILE))
      client.setMessageFactory(new stirling.fix.messages.fix42.bats.europe.MessageFactory());
    else if (profile.equals(MB_TRADING_PROFILE))
      client.setMessageFactory(new stirling.fix.messages.fix44.mbtrading.MessageFactory());
    else if (profile.equals(BURGUNDY_PROFILE))
      client.setMessageFactory(new stirling.fix.messages.fix44.burgundy.MessageFactory());
    else if (profile.equals(HOTSPOT_FX_PROFILE))
      client.setMessageFactory(new stirling.fix.messages.fix42.hotspotfx.MessageFactory());
    else
      throw new CommandArgException("unknown profile: " + profile);
  }

  @Override public String[] getArgumentNames(ConsoleClient client) {
    List<String> profiles = new ArrayList<String>();
    profiles.add(ARGUMENT_NAME + "=" + DEFAULT_PROFILE);
    profiles.add(ARGUMENT_NAME + "=" + BATS_PROFILE);
    profiles.add(ARGUMENT_NAME + "=" + MB_TRADING_PROFILE);
    profiles.add(ARGUMENT_NAME + "=" + BURGUNDY_PROFILE);
    profiles.add(ARGUMENT_NAME + "=" + HOTSPOT_FX_PROFILE);
    return profiles.toArray(new String[0]);
  }

  @Override public String usage() {
    return ARGUMENT_NAME + "=<profile> : Sets profile which will be used for creating and sending messages.";
  }
}
