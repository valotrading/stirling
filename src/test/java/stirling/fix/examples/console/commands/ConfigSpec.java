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
package examples.console.commands;

import stirling.fix.examples.console.commands.CommandArgException;
import stirling.fix.examples.console.commands.Config;
import jdave.Block;
import jdave.Specification;
import jdave.junit4.JDaveRunner;
import org.junit.runner.RunWith;

import java.util.Scanner;

@RunWith(JDaveRunner.class)
public class ConfigSpec extends Specification<Config> {
  public class AnyConfig {
    public void throwsExceptionIfInvalidVersion() {
      specify(new Block() {
        @Override
        public void run() throws Exception {
          new Config().execute(null, new Scanner("Version=invalid"));
        }
      }, must.raise(CommandArgException.class, "unknown version: 'invalid'"));
    }
  }
}
