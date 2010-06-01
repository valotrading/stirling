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
package fixengine.spec;

import java.util.ArrayList;
import java.util.List;

import fixengine.session.ConnectionManager;

/**
 * @author Pekka Enberg 
 */
public class StubConnectionManager implements ConnectionManager {
    private List<String> senderCompIds = new ArrayList<String>();

    @Override
    public boolean connect(String senderCompId) {
        if (senderCompIds.contains(senderCompId)) {
            return false;
        }
        senderCompIds.add(senderCompId);
        return true;
    }

    @Override
    public void disconnect(String senderCompId) {
        senderCompIds.remove(senderCompId);
    }
    
    public void reset() {
        senderCompIds.clear();
    }
}
