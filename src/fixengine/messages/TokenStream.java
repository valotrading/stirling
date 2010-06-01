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

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;

import fixengine.io.IoException;

/**
 * @author Pekka Enberg 
 */
public class TokenStream {
    private static final int PUSHBACK_BUFFER_SIZE = 128;

    private final PushbackReader input;
    private int lookahead;

    public TokenStream(String input) {
        this(new StringReader(input));
    }

    public TokenStream(Reader input) {
        this.input = new PushbackReader(input, PUSHBACK_BUFFER_SIZE);
        this.lookahead = getch();
    }

    public int lookahead() {
        return lookahead;
    }

    public int integer() {
        String s = string(DELIMITER);
        return parseInt(s);
    }

    public Tag tag() {
        String s = string('=');
        if (s.isEmpty()) {
            return null;
        }
        return new Tag(parseInt(s));
    }

    private int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new InvalidNumberFormatException(s);
        }
    }

    public String string(int delimiter) {
        StringBuilder result = new StringBuilder();
        while (true) {
            int ch = lookahead;
            if (ch == delimiter || ch < 0)
                break;
            result.append((char) ch);
            match(ch);
        }
        return result.toString();
    }

    public String data(int length) {
        if (length == 0) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int ch = lookahead;
            result.append((char) ch);
            match(ch);
        }
        return result.toString();
    }

    public int match(int token) {
        if (lookahead != token) {
            throw new UnexpectedTokenException(
                    "Expected '" + (char) token + "', but was: '" + (char) lookahead + "'");
        }
        lookahead = getch();
        return token;
    }

    private int getch() {
        try {
            return input.read();
        } catch (IOException e) {
            throw new ParseException(e);
        }
    }

    public void push(String s) {
        try {
            input.unread(lookahead);
            input.unread(s.toCharArray());
            lookahead = getch();
        } catch (IOException e) {
            throw new IoException(e);
        }
    }
}
