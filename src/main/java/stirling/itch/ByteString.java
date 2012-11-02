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
package stirling.itch;

import java.io.UnsupportedEncodingException;

public class ByteString {
    private final byte[] bytes;
    private final int    offset;
    private final int    length;

    public ByteString(byte[] bytes) {
        this(bytes, 0, bytes.length);
    }

    public ByteString(byte[] bytes, int offset, int length) {
        this.bytes  = bytes;
        this.offset = offset;
        this.length = length;
    }

    public byte byteAt(int index) {
        return bytes[offset + index];
    }

    public byte[] getBytesUnsafe() {
        if (offset == 0 && bytes.length == length) {
            return bytes;
        }
        byte[] copy = new byte[length];
        System.arraycopy(bytes, offset, copy, 0, length);
        return copy;
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof ByteString)
            return equals((ByteString)that);
        else
            return false;
    }

    private boolean equals(ByteString that) {
        if (this.length != that.length)
            return false;

        for (int i = 0; i < length; i++) {
            if (this.bytes[this.offset + i] != that.bytes[that.offset + i])
                return false;
        }

        return true;
    }

    public ByteString slice(int offset, int length) {
        return new ByteString(bytes, offset, length);
    }

    public long toLong() {
        long longValue = 0;

        for (int i = offset; i < offset + length; i++) {
            if (bytes[i] != ' ')
                longValue = longValue * 10 + bytes[i] - '0';
        }

        return longValue;
    }

    @Override
    public String toString() {
        try {
            return new String(bytes, offset, length, "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
