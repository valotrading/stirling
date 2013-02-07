/*
 * Copyright 2011 the original author or authors.
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
package stirling.fix.console;

import stirling.fix.console.commands.Command;
import jline.ArgumentCompletor;
import jline.Completor;
import jline.SimpleCompletor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CommandCompletor implements Completor {
    private ConsoleClient consoleClient;
    private Map<String, Command> commands;
    private Completor delegate;
    private SimpleCompletor argumentCompletor;

    public CommandCompletor(ConsoleClient consoleClient, Map<String, Command> commands) {
        this.consoleClient = consoleClient;
        this.commands = commands;
        this.argumentCompletor = argumentCompletor();
        this.delegate = commandsCompletor();
    }

    @Override
    public int complete(final String buffer, final int cursor, final List candidates) {
        setArgumentNames(buffer);
        return delegate.complete(buffer, cursor, candidates);
    }

    private SimpleCompletor argumentCompletor() {
        return new SimpleCompletor("") {
            @Override
            @SuppressWarnings("unchecked")
            public int complete(final String buffer, final int cursor, final List candidates) {
                int index = super.complete(buffer, cursor, candidates);
                for (int i = 0; i < candidates.size(); i++) {
                    String candidate = (String) candidates.get(i);
                    candidates.set(i, candidate.replaceAll("= ", "="));
                }
                return index;
            }
        };
    }

    private Completor commandsCompletor() {
        SimpleCompletor simpleCompletor = new SimpleCompletor(commandNames()) {
            public int complete(final String buffer, final int cursor, final List candidates) {
                setArgumentNames(buffer);
                return super.complete(buffer, cursor, candidates);
            }
        };
        ArgumentCompletor completor = new ArgumentCompletor(new Completor[] {simpleCompletor, argumentCompletor});
        completor.setStrict(false);
        return completor;
    }

    private void setArgumentNames(String buffer) {
        if (buffer == null)
            return;
        String command = buffer.split(" ")[0];
        if (commands.containsKey(command))
            argumentCompletor.setCandidateStrings(argumentNames(command));
    }

    private String[] commandNames() {
        List<String> commandNames = new ArrayList<String>(commands.keySet());
        Collections.sort(commandNames);
        return commandNames.toArray(new String[0]);
    }

    private String[] argumentNames(String command) {
        return commands.get(command).getArgumentNames(consoleClient);
    }
}
