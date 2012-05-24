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

import java.util.Scanner;

import stirling.fix.examples.console.Arguments;
import stirling.fix.session.Sequence;

import stirling.fix.examples.console.ConsoleClient;

/**
 * @author Karim Osman
 */
public class Reset implements Command {
  private static final String SEQUENCE_ARGUMENT_NAME = "SequenceNumber";

  public void execute(ConsoleClient client, Scanner scanner) throws CommandException {
    if (client.getSession() != null)
      client.getSession().sequenceReset(client.getConnection(), sequence(scanner));
  }

  private Sequence sequence(Scanner scanner) throws CommandArgException {
    Sequence seq = new Sequence();
    seq.reset(new Arguments(scanner).requiredIntValue(SEQUENCE_ARGUMENT_NAME));
    return seq;
  }

  public String[] getArgumentNames(ConsoleClient client) {
    return new String[] {SEQUENCE_ARGUMENT_NAME + "="};
  }

  public String usage() {
    return SEQUENCE_ARGUMENT_NAME + "=<number> : Sends sequence number reset message.";
  }
}
