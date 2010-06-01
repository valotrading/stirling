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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Pekka Enberg 
 */
class Timer {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private final ReentrantLock lock = new ReentrantLock();
    private ScheduledFuture<?> future;
    private final Runnable runnable;
    private final Timeout timeout;

    public static Timer schedule(Timeout timeout, Runnable runnable) {
        Timer result = new Timer(runnable, timeout);
        result.schedule();
        return result;
    }

    public Timer(Runnable runnable, Timeout timeout) {
        this.runnable = runnable;
        this.timeout = timeout;
    }

    public void schedule() {
        lock.lock();
        try {
            if (future != null) {
                return;
            }
            future = scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    lock.lock();
                    try {
                        cancel();
                        runnable.run();
                        schedule();
                    } finally {
                        lock.unlock();
                    }
                }
            }, timeout.delay(), timeout.unit());
        } finally {
            lock.unlock();
        }
    }

    public void cancel() {
        lock.lock();
        try {
            if (future != null) {
                future.cancel(false);
                future = null;
            }
        } finally {
            lock.unlock();
        }
    }
}