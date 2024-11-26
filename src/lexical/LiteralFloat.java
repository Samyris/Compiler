package lexical;

public class LiteralFloat extends Token{
    public final int value;

    public LiteralFloat(int value) {
        super(Tag.LIT_FLOAT);
        this.value = value;
    }

    public String toString() {
        return "<" + value + ", " + tag + ">";
    }

}
