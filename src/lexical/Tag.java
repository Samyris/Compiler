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
        PRINT = 265,
    
        // Tipos
        INT = 266, 
        FLOAT = 267, 
        STRING = 268,

        //Operadores
        EQ = 269, //equal
        AND = 270, 
        OR = 271, 
        NOT = 272,


        // Identificador
        ID = 278,

        //Outros tokens
        NUM = 279;
    }

