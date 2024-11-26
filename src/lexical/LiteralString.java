package lexical;

public class LiteralString extends Token{
    public final int value;

    public LiteralString(int value) {
        super(Tag.LIT_INT);
        this.value = value;
    }

    public String toString() {
        return "<" + value + ", " + tag + ">";
    }
}
