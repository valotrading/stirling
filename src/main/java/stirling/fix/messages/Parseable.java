package stirling.fix.messages;

import java.nio.ByteBuffer;

public interface Parseable {
    void parse(ByteBuffer b);
}
