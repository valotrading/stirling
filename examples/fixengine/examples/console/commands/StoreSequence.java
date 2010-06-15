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

import java.io.Console;
import java.util.Scanner;

import fixengine.session.Sequence;
import fixengine.session.store.SessionStore;

import fixengine.examples.console.ConsoleClient.State;

/**
 * @author Karim Osman
 */
public class StoreSequence implements Command {
    private SessionStore store;

    public StoreSequence(SessionStore store) {
        this.store = store;
    }

    public String name() {
        return "storeseq";
    }

    public void execute(State state, Console console, Scanner scanner) {
        if (!scanner.hasNext()) {
            console.printf("senderCompId must be specified\n");
            return;
        }
        String senderCompId = scanner.next();

        if (!scanner.hasNext()) {
            console.printf("targetCompId must be specified\n");
            return;
        }
        String targetCompId = scanner.next();

        Sequence outgoingSeq = new Sequence();
        if (scanner.hasNextInt()) outgoingSeq.reset(scanner.nextInt());

        store.resetOutgoingSeq(senderCompId, targetCompId, outgoingSeq);
    }
}
