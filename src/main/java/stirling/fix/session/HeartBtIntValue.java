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
package stirling.fix.session;

import stirling.fix.io.Timeout;

/**
 * @author Pekka Enberg
 */
public class HeartBtIntValue {
    /* HeartBtInt + "some reasonable transmission time" */
    private static final double TX_TIME_FACTOR = 1.2;

    private long millis;

    public static HeartBtIntValue seconds(int seconds) {
        return new HeartBtIntValue(seconds * 1000);
    }

    public static HeartBtIntValue milliseconds(long millis) {
        return new HeartBtIntValue(millis);
    }

    private HeartBtIntValue(long millis) {
        this.millis = millis;
    }

    public int getSeconds() {
      return (int)(millis / 1000);
    }

    public Timeout heartbeat() {
        return Timeout.milliseconds(millis);
    }

    public Timeout testRequest() {
        return Timeout.milliseconds((long) (millis * TX_TIME_FACTOR));
    }
}
