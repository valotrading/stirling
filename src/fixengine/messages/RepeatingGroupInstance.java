package fixengine.messages;

import java.nio.ByteBuffer;

public class RepeatingGroupInstance extends AbstractFieldContainer implements Field {
    public RepeatingGroupInstance(Tag<?> delimiter) {
        field(delimiter);
    }

    @Override public String format() {
        return fields.format();
    }

    @Override public boolean hasValue() {
        return false;
    }

    @Override public boolean isEmpty() {
        return false;
    }

    @Override public boolean isFormatValid() {
        return false;
    }

    @Override public boolean isMissing() {
        return false;
    }

    @Override public boolean isParsed() {
        return false;
    }

    @Override public boolean isValueValid() {
        return false;
    }

    @Override public void parse(ByteBuffer b) {
        fields.parse(b);
    }

    @Override public String prettyName() {
        return "";
    }

    @Override public void setRequired(Required required) {
    }
}
