package compiler_modules.lexer;

public class FloatToken extends Token {
    public final float value;

    public FloatToken(float value) {
        super(Tag.FLOAT_VALUE);
        this.value = value;
    }

    @Override
    public String getStringRepresentation() {
        return "" + value;
    }
}
