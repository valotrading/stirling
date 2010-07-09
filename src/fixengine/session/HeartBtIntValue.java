/*
 * Copyright 2008 the original author or authors.
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
package fixengine.session;

import fixengine.io.Timeout;

/**
 * @author Pekka Enberg
 */
public class HeartBtIntValue {
    /* HeartBtInt + "some reasonable transmission time"  */
    private static final double TX_TIME_FACTOR = 1.2;

    private int seconds;

    public HeartBtIntValue(int seconds) {
        this.seconds = seconds;
    }

    public Timeout heartbeat() {
        return Timeout.seconds(seconds);
    }

    public Timeout testRequest() {
        return Timeout.seconds((int) (seconds * TX_TIME_FACTOR));
    }
}