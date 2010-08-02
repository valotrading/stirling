package fixengine.messages;

public class UnexpectedTagException extends Exception {
    private final int tag;

    public UnexpectedTagException(int tag) {
        this.tag = tag;
    }

    public int getTag() {
        return tag;
    }

    private static final long serialVersionUID = 1L;
}
