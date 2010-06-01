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
package fixengine.messages;

import java.io.IOException;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import fixengine.io.Converter;
import fixengine.io.IoException;

/**
 * @author Pekka Enberg 
 */
public class MessageConverter implements Converter<Message> {
    private static Charset charset = Charset.forName("US-ASCII");
    private static CharsetDecoder decoder = charset.newDecoder();

    @Override
    public Message convertToObject(ByteBuffer buffer) {
        try {
            CharBuffer cb = decoder.decode(buffer);
            StringReader input = new StringReader(cb.toString());
            TokenStream tokens = new TokenStream(input);
            MessageHeader header = new MessageHeader();
            header.parse(tokens);
            Message message = header.newMessage();
            message.setBeginString(header.getBeginString());
            message.parse(tokens);
            return message;
        } catch (ParseException e) {
            return new GarbledMessage();
        } catch (IOException e) {
            throw new IoException(e);
        }
    }

    @Override
    public void convertToBuffer(ByteBuffer buffer, Message message) {
        buffer.put(message.format().getBytes());
    }
}
