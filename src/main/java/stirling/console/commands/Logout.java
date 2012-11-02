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
package stirling.console.commands;

import java.util.Scanner;

import stirling.console.ConsoleClient;

/**
 * @author Karim Osman
 */
public class Logout implements Command {
    public void execute(ConsoleClient client, Scanner scanner) throws CommandException {
        if (client.getSession() != null) client.getSession().logout(client.getConnection());
    }

    public String[] getArgumentNames(ConsoleClient client) {
        return new String[0];
    }

    public String description() {
        return "Creates and sends logout message.";
    }

    public String usage() {
        return ": " + description();
    }
}
