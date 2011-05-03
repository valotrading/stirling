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

import java.util.Scanner;

import fixengine.examples.console.Arguments;
import fixengine.examples.console.ConsoleClient;

public class Profile implements Command {
  private static final String DEFAULT_PROFILE = "default";
  private static final String BATS_PROFILE = "bats-europe";
  private static final String MB_TRADING_PROFILE = "mb-trading";
  private static final String ARGUMENT_NAME = "Name";

  public void execute(ConsoleClient client, Scanner scanner) throws CommandArgException {
    String profile = new Arguments(scanner).requiredValue(ARGUMENT_NAME);
    if (profile.equals(DEFAULT_PROFILE))
      client.setMessageFactory(new fixengine.messages.fix42.DefaultMessageFactory());
    else if (profile.equals(BATS_PROFILE))
      client.setMessageFactory(new fixengine.messages.fix42.bats.europe.MessageFactory());
    else if (profile.equals(MB_TRADING_PROFILE))
      client.setMessageFactory(new fixengine.messages.fix44.mbtrading.MessageFactory());
    else
      throw new CommandArgException("unknown profile: " + profile);
  }
}
