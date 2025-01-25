package compiler_modules;

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
}