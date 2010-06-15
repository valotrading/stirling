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

import fixengine.messages.Session;
import fixengine.session.Sequence;

import fixengine.session.store.SessionStore;

import fixengine.examples.console.ConsoleClient.State;

/**
 * @author Karim Osman
 */
public class Reset implements Command {
    public String name() {
        return "reset";
    }

    public void execute(State state, Console console, Scanner scanner) {
        if (!scanner.hasNext()) {
            console.printf("sequence number must be specified and it must be an integer\n");
            return;
        }

        Session session = state.client.getSession();
        if (session == null || !session.isAuthenticated()) {
            console.printf("no session\n");
            return;
        }

        Sequence seq = new Sequence();
        seq.reset(scanner.nextInt());
        session.sequenceReset(seq);
    }
}
