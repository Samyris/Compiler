package compiler_modules.lexer;

public class Word extends Token {
    private final String lexeme;
    private final String type;

    public static final Word eq = new Word("==", Tag.EQ);
    public static final Word gt = new Word(">", Tag.GT);
    public static final Word ge = new Word(">=", Tag.GE);
    public static final Word lt = new Word("<", Tag.LT);
    public static final Word le = new Word("<=", Tag.LE);
    public static final Word ne = new Word("!=", Tag.NE);
    public static final Word add = new Word("+", Tag.ADD);
    public static final Word sub = new Word("-", Tag.SUB);
    public static final Word or = new Word("||", Tag.OR);
    public static final Word mul = new Word("*", Tag.MUL);
    public static final Word div = new Word("/", Tag.DIV);
    public static final Word mod = new Word("%", Tag.MOD);
    public static final Word and = new Word("&&", Tag.AND);
    public static final Word assign = new Word("=", Tag.ASSIGN);
    public static final Word comma = new Word(",", Tag.COMMA);
    public static final Word semicolon = new Word(";", Tag.SEMICOLON);
    public static final Word not = new Word("!", Tag.NOT);
    public static final Word open_paren = new Word("(", Tag.OPEN_PAREN);
    public static final Word close_paren = new Word(")", Tag.CLOSE_PAREN);

    public Word(String lexeme, int tag, String type) {
        super(tag);
        this.lexeme = lexeme;
        this.type = type;
    }

    public Word(String lexeme, int tag) {
        this(lexeme, tag, null);
    }

    public String getLexeme() {
        return lexeme;
    }

    public String getType() {
        return type;
    }

    @Override
    public String getStringRepresentation() {
        return lexeme;
    }
}
