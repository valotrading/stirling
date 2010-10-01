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

import fixengine.Version;
import fixengine.examples.console.ConsoleClient;

public class Config implements Command {
  public void execute(ConsoleClient client, Scanner scanner) throws CommandArgException {
    client.setConfig(config(scanner));
  }

  private fixengine.Config config(Scanner scanner) throws CommandArgException {
    fixengine.Config config = new fixengine.Config();
    config.setVersion(Version.FIX_4_2);
    config.setSenderCompId(senderCompID(scanner));
    config.setTargetCompId(targetCompID(scanner));
    config.setSenderSubID(senderSubID(scanner));
    config.setTargetSubID(targetSubID(scanner));
    return config;
  }

  private String senderCompID(Scanner scanner) throws CommandArgException {
    if (!scanner.hasNext())
      throw new CommandArgException("senderCompID must be specified");
    return scanner.next();
  }

  private String targetCompID(Scanner scanner) throws CommandArgException {
    if (!scanner.hasNext())
      throw new CommandArgException("targetCompID must be specified");
    return scanner.next();
  }

  private String senderSubID(Scanner scanner) throws CommandArgException {
    if (!scanner.hasNext())
      return null;
    return scanner.next();
  }

  private String targetSubID(Scanner scanner) throws CommandArgException {
    if (!scanner.hasNext())
      return null;
    return scanner.next();
  }
}
