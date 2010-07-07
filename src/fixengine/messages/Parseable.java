package fixengine.messages;

import java.nio.ByteBuffer;

public interface Parseable {
    void parse(ByteBuffer b);
}
