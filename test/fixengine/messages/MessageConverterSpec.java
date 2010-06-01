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
package fixengine.messages;

import static fixengine.messages.Field.DELIMITER;

import java.nio.ByteBuffer;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.junit.runner.RunWith;

/**
 * @author Pekka Enberg 
 */
@RunWith(JDaveRunner.class)
public class MessageConverterSpec extends Specification<MessageConverter> {
    private final MessageConverter converter = new MessageConverter();
    private final ByteBuffer buffer = ByteBuffer.allocate(1024);

    public class ValidMessage {
        public void convertsToMessage() {
            String s = "8=FIX.4.3" + DELIMITER + "9=5" + DELIMITER + "35=0" + DELIMITER + "10=162" + DELIMITER;
            buffer.put(s.getBytes());
            buffer.flip();
            specify(converter.convertToObject(buffer) instanceof HeartbeatMessage);
        }
    }

    public class InvalidMessage {
        public void convertsToGarbledMessage() {
            String s = "8=FIX.4.3" + DELIMITER + "XYZ";
            buffer.put(s.getBytes());
            buffer.flip();
            specify(converter.convertToObject(buffer) instanceof GarbledMessage);
        }
    }
}
