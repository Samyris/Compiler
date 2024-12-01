package lexical;

public class Word extends Token {
    private String lexeme = "";
    public static final Word and = new Word ("&&", Tag.AND);
    public static final Word or = new Word ("||", Tag.OR);
    public static final Word eq = new Word ("==", Tag.EQ);
    public static final Word start = new Word("start",  Tag.START);
    public static final Word exit = new Word("exit",  Tag.EXIT);
    public static final Word end = new Word("end",  Tag.END);
    public static final Word _if = new Word("if",  Tag.IF);
    public static final Word then = new Word("then",  Tag.THEN);
    public static final Word _else = new Word("else",  Tag.ELSE);
    public static final Word _do = new Word("do",  Tag.DO);
    public static final Word _while = new Word("while",  Tag.WHILE);
    public static final Word scan = new Word("scan",  Tag.SCAN);
    public static final Word print = new Word("print",  Tag.PRINT);
    public static final Word type_int = new Word("int", Tag.TYPE_INT);
    public static final Word type_float = new Word("float", Tag.TYPE_FLOAT);
    public static final Word type_string = new Word("string", Tag.TYPE_STRING);
    public static final Word exclamation = new Word("!", Tag.EXCLAMATION);
    public static final Word gt = new Word(">", Tag.GT);
    public static final Word ge = new Word(">=", Tag.GE);
    public static final Word lt = new Word("<", Tag.LT);
    public static final Word le = new Word("<=", Tag.LE);
    public static final Word dif = new Word("!=", Tag.DIF);
    public static final Word add = new Word("+", Tag.ADD);
    public static final Word sub = new Word("-", Tag.SUB);
    public static final Word mul = new Word("*", Tag.MUL);
    public static final Word div = new Word("/", Tag.DIV);
    public static final Word module = new Word("%", Tag.MODULE);
    public static final Word assign = new Word("=", Tag.ASSIGN);
    public static final Word comma = new Word(",", Tag.COMMA);
    public static final Word semicolon = new Word(";", Tag.SEMICOLON);
    public static final Word colon = new Word(";", Tag.COLON);
    public static final Word dot = new Word(".", Tag.DOT);
    public static final Word opar = new Word("(", Tag.O_PAR);
    public static final Word cpar = new Word(")", Tag.C_PAR);
    public static final Word obra = new Word("{", Tag.O_BRACK);
    public static final Word cbra = new Word("}", Tag.C_BRACK);

    public Word(String s, int tag) {
        super(tag);
        lexeme = s;
    }

    public String getLexeme() {
        return lexeme;
    }

    public String toString() {
        return "< " + lexeme + " , " + Tag.getName(tag) + " >";
    }
}
