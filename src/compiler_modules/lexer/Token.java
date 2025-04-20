package compiler_modules.lexer;

public abstract class Token {
    public final int tag;

    public Token(int t) {
        tag = t;
    }

    public abstract String getStringRepresentation();

    public String getTagValueAsString() {
        return "" + tag;
    }

    public String mapTagToString() {
        switch (tag) {
            case Tag.START:
                return "START";
            case Tag.EXIT:
                return "EXIT";
            case Tag.INT:
                return "INT";
            case Tag.FLOAT:
                return "FLOAT";
            case Tag.STRING:
                return "STRING";
            case Tag.IF:
                return "IF";
            case Tag.THEN:
                return "THEN";
            case Tag.ELSE:
                return "ELSE";
            case Tag.DO:
                return "DO";
            case Tag.WHILE:
                return "WHILE";
            case Tag.END:
                return "END";
            case Tag.SCAN:
                return "SCAN";
            case Tag.PRINT:
                return "PRINT";
            case Tag.EQ:
                return "EQ";
            case Tag.GT:
                return "GT";
            case Tag.GE:
                return "GE";
            case Tag.LT:
                return "LT";
            case Tag.LE:
                return "LE";
            case Tag.NE:
                return "NE";
            case Tag.ADD:
                return "ADD";
            case Tag.SUB:
                return "SUB";
            case Tag.OR:
                return "OR";
            case Tag.MUL:
                return "MUL";
            case Tag.DIV:
                return "DIV";
            case Tag.MOD:
                return "MOD";
            case Tag.AND:
                return "AND";
            case Tag.ASSIGN:
                return "ASSIGN";
            case Tag.COMMA:
                return "COMMA";
            case Tag.SEMICOLON:
                return "SEMICOLON";
            case Tag.NOT:
                return "NOT";
            case Tag.OPEN_PAREN:
                return "OPEN_PAREN";
            case Tag.CLOSE_PAREN:
                return "CLOSE_PAREN";
            case Tag.INT_VALUE:
                return "INT_VALUE";
            case Tag.FLOAT_VALUE:
                return "FLOAT_VALUE";
            case Tag.STRING_VALUE:
                return "STRING_VALUE";
            case Tag.ID:
                return "ID";
            default:
                return "UNKOWN";
        }
    }
}