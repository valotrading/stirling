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

import stirling.fix.console.commands.CommandArgException;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Arguments {
    private Map<String, String> namesAndValues = new HashMap<String, String>();

    public Arguments(Scanner scanner) {
        parseNamesAndValues(scanner);
    }

    private void parseNamesAndValues(Scanner scanner) {
        while (scanner.hasNext()) {
            String[] nameAndValue = scanner.next().split("=");
            String value = null;
            if (nameAndValue.length > 1)
                value = nameAndValue[1];
            namesAndValues.put(nameAndValue[0], value);
        }
    }

    public String requiredValue(String name) throws CommandArgException {
        String value = value(name);
        if (value != null)
            return value;
        throw new CommandArgException(name + " must be specified");
    }

    public String value(String name) {
        return namesAndValues.get(name);
    }

    public int requiredIntValue(String name) throws CommandArgException {
        String value = requiredValue(name);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new CommandArgException(name + " must be an integer");
        }
    }
}
