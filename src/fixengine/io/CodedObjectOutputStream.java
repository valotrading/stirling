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

import java.nio.ByteBuffer;

/**
 * @author Pekka Enberg
 */
public class CodedObjectOutputStream<T> implements ObjectOutputStream<T> {
    private final Converter<T> converter;
    private final ByteBuffer buffer;
    private final Stream output;

    public CodedObjectOutputStream(Stream output, ByteBuffer buffer, Converter<T> converter) {
        this.converter = converter;
        this.buffer = buffer;
        this.output = output;
    }

    @Override
    public void writeObject(T object) {
        synchronized (buffer) {
            buffer.clear();
            converter.convertToBuffer(buffer, object);
            buffer.flip();
            output.write(buffer);
        }
    }

    @Override
    public void close() {
        output.close();
    }

    @Override
    public boolean isClosed() {
        return output.isClosed();
    }
}