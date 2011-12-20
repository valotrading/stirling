/*
 * Copyright 2009 the original author or authors.
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
package stirling.fix;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.junit.runner.RunWith;

/**
 * @author Pekka Enberg 
 */
@RunWith(JDaveRunner.class)
public class ConfigSpec extends Specification<Config> {
    public class AnyConfig {
        private final Config config = new Config().setVersion(Version.FIX_4_2);

        public void supportsEarlierVersions() {
            specify(config.supports(Version.FIX_4_1), must.equal(true));
        }

        public void supportsCurrentVersion() {
            specify(config.supports(Version.FIX_4_2), must.equal(true));
        }

        public void doesNotSupportFutureVersions() {
            specify(config.supports(Version.FIX_4_3), must.equal(false));
        }
    }
}
