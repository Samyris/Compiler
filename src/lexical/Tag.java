package lexical;

import java.lang.reflect.Field;

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
        LIT_INT = 269,
        LIT_FLOAT = 270,
        LIT_STRING = 271;

        //Operadores Relacionais
        public final static int
        AND = 272, // &&
        OR = 273, // ||
        NOT = 274; // !

        //Operadores de Comparação 
        public final static int
        GT = 275, // Greater Than >
        GE = 276, // Greater Than or Equal To >=
        LT = 277, // Less Than <
        LE = 278, // Less Than or Equal To <=
        DIF = 279, // Different <>
        EQ = 280; // equals ==

        // Operadores Aritnéticos
        public final static int
        ADD = 281,
        SUB = 282,
        MUL = 283,
        DIV = 284,
        MODULE = 285, // %
        ASSIGN = 286; // =
        

        //Pontuação
        public final static int
        COMMA = 287, // ,
        SEMICOLON = 288 , // ;
        COLON = 289, // :
        O_PAR = 290, // (
        C_PAR = 291; // )


        // Identificador
        public final static int
        ID = 297;

        //Outros tokens
        public final static int
        EOF = 65535;

        public static final int INVALID_TOKEN = 0;

        public static String getName(int value) {
            for (Field field : Tag.class.getFields()) {
                try {
                    if (field.getInt(null) == value) {
                        return field.getName();
                    }
                } catch (IllegalAccessException e) {
                    // Ignorar erros de acesso
                }
            }
            return Tag.getName(INVALID_TOKEN);
        }
    }

