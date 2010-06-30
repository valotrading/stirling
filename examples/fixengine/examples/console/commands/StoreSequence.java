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
import fixengine.session.store.SessionStore;

import fixengine.examples.console.ConsoleClient;

/**
 * @author Karim Osman
 */
public class StoreSequence implements Command {
  public void execute(ConsoleClient client, Scanner scanner) throws CommandArgException {
    client.getSessionStore().resetOutgoingSeq(senderCompId(scanner), targetCompId(scanner),
        incomingSeq(scanner), outgoingSeq(scanner));
  }

  private String senderCompId(Scanner scanner) throws CommandArgException {
    if (!scanner.hasNext())
      throw new CommandArgException("senderCompId must be specified");
    return scanner.next();
  }

  private String targetCompId(Scanner scanner) throws CommandArgException {
    if (!scanner.hasNext())
      throw new CommandArgException("targetCompId must be specified");
    return scanner.next();
  }

  private Sequence incomingSeq(Scanner scanner) throws CommandArgException {
    Sequence incomingSeq = new Sequence();
    if (scanner.hasNextInt()) incomingSeq.reset(scanner.nextInt());
    return incomingSeq;

  }

  private Sequence outgoingSeq(Scanner scanner) throws CommandArgException {
    Sequence outgoingSeq = new Sequence();
    if (scanner.hasNextInt()) outgoingSeq.reset(scanner.nextInt());
    return outgoingSeq;

  }
}
