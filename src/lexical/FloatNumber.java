package lexical;

public class FloatNumber extends Token{
    public final int value;

    public FloatNumber(int value) {
        super(Tag.LIT_FLOAT);
        this.value = value;
    }

    public String toString() {
        return "<" + value + ", " + tag + ">";
    }

}
