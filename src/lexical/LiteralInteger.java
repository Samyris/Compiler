package lexical;

public class LiteralInteger extends Token {
    public final int value;

    public LiteralInteger(int value) {
        super(Tag.LIT_INT);
        this.value = value;
    }

    @Override
    public String toString() {
        return "< " + value + " , " + Tag.getName(tag) + " >";
    }

}
