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

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.jmock.Expectations;
import org.junit.runner.RunWith;

/**
 * @author Pekka Enberg 
 */
@RunWith(JDaveRunner.class)
public class CodedObjectOutputStreamSpec extends Specification<CodedObjectOutputStream<Object>> {
    @SuppressWarnings("unchecked") private final Converter<Object> converter = mock(Converter.class);
    private final Stream connection = mock(Stream.class);

    public class Any {
        private final ByteBuffer buffer = ByteBuffer.allocate(32);
        private final CodedObjectOutputStream<Object> stream = new CodedObjectOutputStream<Object>(connection, buffer, converter);

        public void sendsConvertedObject() {
            final Object object = new Object();
            checking(new Expectations() {{
                one(converter).convertToBuffer(buffer, object);
                one(connection).write(buffer);
            }});
            stream.writeObject(object);
        }

        public void delegatesClose() {
            checking(new Expectations() {{
                one(connection).close();
            }});
            stream.close();
        }

        public void delegatesIsClosed() {
            checking(new Expectations() {{
                one(connection).isClosed(); will(returnValue(true));
            }});
            specify(stream.isClosed(), must.equal(true));
        }
    }
}
