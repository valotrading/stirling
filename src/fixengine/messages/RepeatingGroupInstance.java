package fixengine.messages;

import java.nio.ByteBuffer;

public class RepeatingGroupInstance extends AbstractFieldContainer implements Field {
    private final Tag<?> delimiter;

    public RepeatingGroupInstance(Tag<?> delimiter) {
        field(this.delimiter = delimiter);
    }

    @Override public void parse(ByteBuffer b) {
        int tag = Tag.peekTag(b);
        if (tag != delimiter.value())
            throw new ParseException(fields.lookup(tag).prettyName() + ": Repeating group fields out of order", SessionRejectReasonValue.OUT_OF_ORDER_GROUP_FIELD);
        fields.parse(b);
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

    @Override public boolean isConditional() {
        return false;
    }

    @Override public boolean isParsed() {
        return false;
    }

    @Override public boolean isValueValid() {
        return false;
    }

    @Override public String prettyName() {
        return "";
    }

    @Override public void setRequired(Required required) {
    }
}
