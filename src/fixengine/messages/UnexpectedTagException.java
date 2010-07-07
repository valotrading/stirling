package fixengine.messages;

public class UnexpectedTagException extends Exception {
    private final Tag<?> tag;

    public UnexpectedTagException(Tag<?> tag) {
        this.tag = tag;
    }

    public Tag<?> getTag() {
        return tag;
    }

    private static final long serialVersionUID = 1L;
}
