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
import jdave.Block;
import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.junit.runner.RunWith;

/**
 * @author Pekka Enberg 
 */
@RunWith(JDaveRunner.class)
public class MessageHeaderSpec extends Specification<MessageHeader> {
    private MessageHeader header = new MessageHeader();

    public class ValidHeader {
        public MessageHeader create() {
            header.parse(new TokenStream("8=FIX.4.3" + DELIMITER /* BeginString */
                    + "9=1" + DELIMITER /* BodyLength */
                    + "35=0" + DELIMITER /* MsgType */
            ));
            return header;
        }

        public void beginString() {
            specify(header.getBeginString(), must.equal("FIX.4.3"));
        }

        public void bodyLength() {
            specify(header.getBodyLength(), must.equal(1));
        }

        public void msgType() {
            specify(header.getMsgType(), must.equal(MsgType.HEARTBEAT));
        }

        public void instantiatesMessages() {
            specify(header.newMessage() instanceof HeartbeatMessage);
        }

        public void checksums() {
            specify(header.checksum(), must.equal(926));
        }
    }

    public class InvalidBeginString {
        public void raisesException() {
            specify(new Block() {
                @Override
                public void run() throws Throwable {
                    header.parse(new TokenStream("8=Z" + DELIMITER));
                }
            }, must.raise(InvalidBeginStringException.class, "Z"));
        }
    }

    public class InvalidMsgType {
        public MessageHeader create() {
            header.parse(new TokenStream("8=FIX.4.3" + DELIMITER /* BeginString */
                    + "9=1" + DELIMITER /* BodyLength */
                    + "35=ZZ" + DELIMITER /* MsgType */
            ));
            return header;
        }

        public void instantiatesUnknownMessages() {
            specify(header.newMessage() instanceof UnknownMessage);
        }

        public void checksums() {
            specify(header.checksum(), must.equal(1058));
        }
   }
}