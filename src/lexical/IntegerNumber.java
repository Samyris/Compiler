package lexical;

public class IntegerNumber extends Token {
    public final int value;

    public IntegerNumber(int value) {
        super(Tag.INT);
        this.value = value;
    }

    public String toString() {
        return "<" + value + ", " + tag + ">";
    }

}
