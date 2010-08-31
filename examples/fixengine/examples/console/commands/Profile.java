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

import fixengine.examples.console.ConsoleClient;

import fixengine.messages.bats.europe.MessageFactory;
import fixengine.messages.DefaultMessageFactory;

public class Profile implements Command {
  private static final String DEFAULT_PROFILE = "default";
  private static final String BATS_PROFILE = "bats";

  public void execute(ConsoleClient client, Scanner scanner) throws CommandArgException {
    String profile = profile(scanner);
    if (profile.equals(DEFAULT_PROFILE))
      client.setMessageFactory(new DefaultMessageFactory());
    else if (profile.equals(BATS_PROFILE))
      client.setMessageFactory(new MessageFactory());
    else
      throw new CommandArgException("unknown profile: " + profile);
  }

  private String profile(Scanner scanner) throws CommandArgException {
    if (!scanner.hasNext())
      throw new CommandArgException("profile must be specified");
    return scanner.next().toLowerCase();
  }
}
