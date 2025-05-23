package compiler_modules.lexer;

public class StringToken extends Token {
    public final String value;

    public StringToken(String value) {
        super(Tag.STRING_VALUE);
        this.value = value;
    }

    @Override
    public String getStringRepresentation() {
        return value;
    }
}
