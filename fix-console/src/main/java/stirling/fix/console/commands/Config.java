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
package stirling.fix.console.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import stirling.fix.Version;
import stirling.fix.console.Arguments;
import stirling.fix.console.ConsoleClient;

public class Config implements Command {
    public void execute(ConsoleClient client, Scanner scanner) throws CommandException {
        client.setConfig(config(new Arguments(scanner)));
    }

    private stirling.fix.Config config(Arguments arguments) throws CommandArgException {
        stirling.fix.Config config = new stirling.fix.Config();
        config.setVersion(version(arguments.requiredValue(ArgumentNames.VERSION.value())));
        config.setSenderCompId(arguments.requiredValue(ArgumentNames.SENDER_COMP_ID.value()));
        config.setTargetCompId(arguments.requiredValue(ArgumentNames.TARGET_COMP_ID.value()));
        config.setSenderSubId(arguments.value(ArgumentNames.SENDER_SUB_ID.value()));
        config.setTargetSubId(arguments.value(ArgumentNames.TARGET_SUB_ID.value()));
        return config;
    }

    private Version version(String value) throws CommandArgException {
        for (Version version : Version.values()) {
            if (version.value().equals(value.toUpperCase())) {
                return version;
            }
        }
        throw new CommandArgException(String.format("unknown version: '%s'", value));
    }

    public String[] getArgumentNames(ConsoleClient client) {
        List<String> fields = new ArrayList<String>();
        for (ArgumentNames argument : ArgumentNames.values()) {
            fields.add(argument.value() + "=");
        }
        return fields.toArray(new String[0]);
    }

    public String description() {
        return "Configures client for sending FIX messages.";
    }

    public String usage() {
        return "Version=<version> SenderCompID=<id> TargetCompID=<id> <Argument=value>* : " + description();
    }

    private enum ArgumentNames {
        SENDER_COMP_ID("SenderCompID"),
        TARGET_COMP_ID("TargetCompID"),
        SENDER_SUB_ID("SenderSubID"),
        TARGET_SUB_ID("TargetSubID"),
        VERSION("Version");

        private String value;

        private ArgumentNames(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }
}
