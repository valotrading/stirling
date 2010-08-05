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
package fixengine.io;


import lang.Objects;

/**
 * @author Pekka Enberg 
 */
public class Timeout {
    private final int delay;

    public static Timeout seconds(int delay) {
        return new Timeout(delay);
    }

    private Timeout(int delay) {
        this.delay = delay;
    }

    public long delayMsec() {
      return delay * 1000L;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equal(this, obj);
    }

    @Override
    public String toString() {
        return Objects.toString(this);
    }
}
