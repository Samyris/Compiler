package lexical;

public class LiteralFloat extends Token{
    public final float value;

    public LiteralFloat(float value) {
        super(Tag.LIT_FLOAT);
        this.value = value;
    }

    @Override
    public String toString() {
        return "< " + value + " , " + Tag.getName(tag) + " >";
    }

}
