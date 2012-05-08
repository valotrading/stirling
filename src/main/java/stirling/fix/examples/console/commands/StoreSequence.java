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
import stirling.fix.session.Sequence;

/**
 * @author Karim Osman
 */
public class StoreSequence implements Command {
  public void execute(ConsoleClient client, Scanner scanner) throws CommandArgException {
    Arguments arguments = new Arguments(scanner);
    String senderCompId = arguments.requiredValue(ArgumentNames.SENDER_COMP_ID.value());
    String targetCompId = arguments.requiredValue(ArgumentNames.TARGET_COMP_ID.value());
    Sequence incomingSeq = seq(ArgumentNames.INCOMING_SEQ, arguments);
    Sequence outgoingSeq = seq(ArgumentNames.OUTGOING_SEQ, arguments);
    client.getSessionStore().resetOutgoingSeq(client.getSession(), incomingSeq, outgoingSeq);
  }

  private Sequence seq(ArgumentNames name, Arguments arguments) throws CommandArgException {
    Sequence sequence = new Sequence();
    String value = arguments.value(name.value());
    if (value != null)
      sequence.reset(arguments.requiredIntValue(name.value()));
    return sequence;
  }

  public String[] getArgumentNames(ConsoleClient client) {
    List<String> fields = new ArrayList<String>();
    for (ArgumentNames argument : ArgumentNames.values()) {
      fields.add(argument.value() + "=");
    }
    return fields.toArray(new String[0]);
  }

  public String usage() {
    return "SenderCompID=<id> TargetCompID=<id> <Argument=value>* : Resets outgoing and outgoing sequence numbers.";
  }

  private enum ArgumentNames {
    SENDER_COMP_ID("SenderCompID"),
    TARGET_COMP_ID("TargetCompID"),
    INCOMING_SEQ("IncomingSeq"),
    OUTGOING_SEQ("OutgoingSeq");

    private String value;

    private ArgumentNames(String value) {
      this.value = value;
    }

    public String value() {
      return value;
    }
  }
}
