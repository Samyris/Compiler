package compiler_modules.lexer;

public class Tag {
    public final static int /* RESERVED WORDS: */ START = 256, // start
            EXIT = 257, // exit
            INT = 258, // int
            FLOAT = 259, // float
            STRING = 260, // string
            IF = 261, // if
            THEN = 262, // then
            ELSE = 263, // else
            DO = 264, // do
            WHILE = 265, // while
            END = 266, // end
            SCAN = 267, // scan
            PRINT = 268, // print

            /* OPERATORS AND PUNCTUATION: */
            EQ = 269, // ==
            GT = 270, // >
            GE = 271, // >=
            LT = 272, // <
            LE = 273, // <=
            NE = 274, // !=
            ADD = 275, // +
            SUB = 276, // -
            OR = 277, // ||
            MUL = 278, // *
            DIV = 279, // /
            MOD = 280, // %
            AND = 281, // &&
            ASSIGN = 282, // =
            COMMA = 283, // ,
            SEMICOLON = 284, // ;
            NOT = 285, // !

            /* DELIMITERS: */
            OPEN_PAREN = 286, // (
            CLOSE_PAREN = 287, // )

            /* CONSTANT VALUES: */
            INT_VALUE = 288, // Integer constant, e.g., 42, 0, -7
            FLOAT_VALUE = 289, // Floating-point constant, e.g., 3.14, -0.001, 2.0
            STRING_VALUE = 290, // String constant enclosed in {}, e.g., {Hello, world!}

            /* IDENTIFIERS: */
            ID = 291;

    public static String getTagName(int tag) {
        switch (tag) {
            case START:
                return "START";
            case EXIT:
                return "EXIT";
            case INT:
                return "INT";
            case FLOAT:
                return "FLOAT";
            case STRING:
                return "STRING";
            case IF:
                return "IF";
            case THEN:
                return "THEN";
            case ELSE:
                return "ELSE";
            case DO:
                return "DO";
            case WHILE:
                return "WHILE";
            case END:
                return "END";
            case SCAN:
                return "SCAN";
            case PRINT:
                return "PRINT";
            case EQ:
                return "EQ '=='";
            case GT:
                return "GT '>'";
            case GE:
                return "GE '>='";
            case LT:
                return "LT '<'";
            case LE:
                return "LE '<='";
            case NE:
                return "NE '!='";
            case ADD:
                return "ADD '+'";
            case SUB:
                return "SUB '-'";
            case OR:
                return "OR '||'";
            case MUL:
                return "MUL '*'";
            case DIV:
                return "DIV '/'";
            case MOD:
                return "MOD '%'";
            case AND:
                return "AND '&&'";
            case ASSIGN:
                return "ASSIGN '='";
            case COMMA:
                return "COMMA ','";
            case SEMICOLON:
                return "SEMICOLON ';'";
            case NOT:
                return "NOT '!'";
            case OPEN_PAREN:
                return "OPEN_PAREN '('";
            case CLOSE_PAREN:
                return "CLOSE_PAREN ')'";
            case INT_VALUE:
                return "INT_VALUE (Integer constant)";
            case FLOAT_VALUE:
                return "FLOAT_VALUE (Floating-point constant)";
            case STRING_VALUE:
                return "STRING_VALUE (String constant)";
            case ID:
                return "ID (Identifier)";
            default:
                return "UNKNOWN";
        }
    }
}
