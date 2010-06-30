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

import fixengine.session.Sequence;

import fixengine.examples.console.ConsoleClient;

/**
 * @author Karim Osman
 */
public class Reset implements Command {
  public void execute(ConsoleClient client, Scanner scanner) throws CommandArgException {
    if (client.getSession() != null)
      client.getSession().sequenceReset(client.getConnection(), sequence(scanner));
  }

  private Sequence sequence(Scanner scanner) throws CommandArgException {
    if (!scanner.hasNextInt())
      throw new CommandArgException("sequence number must be specified and it must be an integer");
    Sequence seq = new Sequence();
    seq.reset(scanner.nextInt());
    return seq;
  }
}
