/*
 * Copyright 2012 the original author or authors.
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

import java.io.File;

import stirling.fix.session.store.DiskSessionStore;
import stirling.fix.session.store.InMemorySessionStore;
import stirling.fix.session.store.MongoSessionStore;
import stirling.fix.session.store.NonPersistentInMemorySessionStore;
import stirling.fix.session.store.SessionStore;
import stirling.fix.session.store.SessionStoreException;

public interface SessionStoreFactory {
    SessionStore create() throws SessionStoreException;

    public static class Disk implements SessionStoreFactory {
        @Override
        public SessionStore create() throws SessionStoreException {
            String directory = System.getProperty("java.io.tmpdir");
            String path = directory + File.separator + "fixengine";
            return new DiskSessionStore(path);
        }
    }

    public static class InMemory implements SessionStoreFactory {
        @Override
        public SessionStore create() throws SessionStoreException {
            return new InMemorySessionStore();
        }
    }

    public static class Mongo implements SessionStoreFactory {
        @Override
        public SessionStore create() throws SessionStoreException {
            return new MongoSessionStore("localhost", 27017);
        }
    }

    public static class NonPersistentInMemory implements SessionStoreFactory {
        @Override
        public SessionStore create() throws SessionStoreException {
            return new NonPersistentInMemorySessionStore();
        }
    }
}
