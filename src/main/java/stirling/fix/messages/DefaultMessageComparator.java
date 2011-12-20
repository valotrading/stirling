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
package stirling.fix.messages;

import stirling.fix.messages.fix42.MsgTypeValue;
import stirling.fix.tags.fix42.ExecID;

public class DefaultMessageComparator implements MessageComparator {
    @Override public boolean equals(Message m1, Message m2) {
        if (!isExecutionReport(m1) || !isExecutionReport(m2))
            return false;
        return execID(m1).equals(execID(m2));
    }

    private boolean isExecutionReport(Message message) {
        return message.getMsgType().equals(MsgTypeValue.EXECUTION_REPORT);
    }

    private String execID(Message message) {
        return message.getString(ExecID.Tag());
    }
}
