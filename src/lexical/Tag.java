package lexical;

public class Tag {
        public final static int
        //Palavras reservadas
        START = 256,
        EXIT = 257,
        END = 258,
        IF = 259,
        THEN = 260,
        ELSE = 261,
        DO = 262,
        WHILE = 263,
        SCAN = 264,
        PRINT = 265;
    
        // Tipos
        public final static int
        TYPE_INT = 266, 
        TYPE_FLOAT = 267, 
        TYPE_STRING = 268;

        //Literais
        public final static int
        LIT_INT = 999,
        LIT_FLOAT = 34,
        LIT_STRING =76;

        //Operadores Relacionais
        public final static int
        AND = 270, // &&
        OR = 271, // ||
        NOT = 272; // !

        //Operadores de Comparação 
        public final static int
        GT = 34, // Greater Than >
        GE = 54, // Greater Than or Equal To >=
        LT = 44, // Less Than <
        LE = 33, // Less Than or Equal To <=
        DIF = 22, // Different !=
        EQ = 269; // equals ==

        // Operadores Aritnéticos
        public final static int
        ADD = 79,
        SUB = 94,
        MUL = 45,
        DIV = 34,
        MODULE = 848; // %
        

        //Pontuação
        public final static int
        COMMA = 273, // ,
        SEMICOLON = 274 , // ;
        COLON = 275, // :
        DOT = 276, // .
        O_PAR = 277, // (
        C_PAR = 278, // )
        O_BRACK = 279, // [
        C_BRACK = 280; // ]

        // Identificador
        public final static int
        ID = 278;

        //Outros tokens
        public final static int
        EOF = 65535;
    }

