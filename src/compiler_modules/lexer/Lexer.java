package compiler_modules.lexer;

import java.io.*;
import java.util.*;

import shared.enums.ErrorType;
import shared.errors.CompilerError;

public class Lexer {
    public static int line = 1; // contador de linhas
    private char ch = ' '; // caractere lido do arquivo
    private FileReader file;
    @SuppressWarnings("rawtypes")
    private Hashtable words = new Hashtable();

    /* Método para inserir palavras reservadas na HashTable */
    @SuppressWarnings("unchecked")
    private void reserve(Word w) {
        words.put(w.getLexeme(), w); // lexema é a chave para entrada na
        // HashTable
    }

    public Lexer(String fileName) throws FileNotFoundException {
        try {
            file = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            throw e;
        }

        // Insere palavras reservadas na HashTable
        reserve(new Word("start", Tag.START));
        reserve(new Word("exit", Tag.EXIT));
        reserve(new Word("int", Tag.INT));
        reserve(new Word("float", Tag.FLOAT));
        reserve(new Word("string", Tag.STRING));
        reserve(new Word("if", Tag.IF));
        reserve(new Word("then", Tag.THEN));
        reserve(new Word("else", Tag.ELSE));
        reserve(new Word("do", Tag.DO));
        reserve(new Word("while", Tag.WHILE));
        reserve(new Word("end", Tag.END));
        reserve(new Word("scan", Tag.SCAN));
        reserve(new Word("print", Tag.PRINT));
    }

    private void readch() throws IOException {
        ch = (char) file.read();
    }

    /* Lê o próximo caractere do arquivo e verifica se é igual a c */
    private boolean readch(char c) throws IOException {
        readch();
        if (ch != c)
            return false;
        ch = ' ';
        return true;
    }

    @SuppressWarnings("unchecked")
    public Token scan() throws IOException {
        // Desconsidera delimitadores na entrada
        for (;; readch()) {
            if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\b')
                continue;
            else if (ch == '\n')
                line++; // conta linhas
            else
                break;
        }

        switch (ch) {
            // Operadores
            case '=':
                if (readch('='))
                    return Word.eq;
                else
                    return Word.assign;
            case '>':
                if (readch('='))
                    return Word.ge;
                else
                    return Word.gt;
            case '<':
                if (readch('='))
                    return Word.le;
                else
                    return Word.lt;
            case '!':
                if (readch('='))
                    return Word.ne;
                else
                    return Word.not;
            case '+':
                readch();
                return Word.add;
            case '-':
                readch();
                return Word.sub;
            case '|':
                if (readch('|'))
                    return Word.or;
                else
                    throw new CompilerError(ErrorType.LEXICAL, line, "Invalid '|' character.");
            case '*':
                readch();
                return Word.mul;
            case '/':
                if (readch('/')) {
                    while (true) {
                        if (ch == '\n') { // Se encontrar uma quebra de linha, o comentário terminou
                            readch();
                            return scan();
                        }
                        if (ch == (char) -1) { // Se chegar ao final do arquivo
                            throw new CompilerError(ErrorType.LEXICAL, line,
                                    "A line break is required after a line comment.");
                        }
                        readch(); // Continua lendo os próximos caracteres
                    }
                } else if (readch('*')) {
                    while (true) {
                        if (ch == '*') { // Se encontrar um '*/', o comentário terminou
                            readch();
                            if (ch == '/') {
                                readch();
                                return null;
                            }
                        }
                        if (ch == (char) -1) { // Se chegar ao final do arquivo
                            throw new CompilerError(ErrorType.LEXICAL, line, "Unexpected end of file after comment.");
                        }
                        readch(); // Continua lendo os próximos caracteres
                    }
                } else
                    return Word.div;
            case '%':
                readch();
                return Word.mod;
            case '&':
                if (readch('&'))
                    return Word.and;
                else
                    throw new CompilerError(ErrorType.LEXICAL, line, "Invalid '&' character.");
            case ',':
                readch();
                return Word.comma;
            case ';':
                readch();
                return Word.semicolon;
            case '(':
                readch();
                return Word.open_paren;
            case ')':
                readch();
                return Word.close_paren;
        }

        // Números
        if (Character.isDigit(ch)) {
            // Tratamento de números inteiros ou flutuantes
            float value = 0;
            boolean isFloat = false;
            int decimalPlace = 1; // Usado para controlar a parte decimal do número

            // Lê a parte inteira
            do {
                value = 10 * value + Character.digit(ch, 10);
                readch();
            } while (Character.isDigit(ch));

            // Verifica se há um ponto para começar a leitura da parte flutuante
            if (ch == '.') {
                isFloat = true;
                readch(); // Lê o ponto '.'

                if (!Character.isDigit(ch)) {
                    throw new CompilerError(ErrorType.LEXICAL, line, "Invalid numerical sequence.");
                }

                // Lê a parte decimal
                while (Character.isDigit(ch)) {
                    value = value + Character.digit(ch, 10) / (float) (decimalPlace * 10);
                    decimalPlace++;
                    readch();
                }
            }

            if (Character.isLetter(ch)) {
                throw new CompilerError(ErrorType.LEXICAL, line, "Invalid numerical sequence.");
            }

            // Retorna um número com base se for inteiro ou flutuante
            if (isFloat) {
                return new FloatToken(value);
            }

            return new IntToken((int) value); // Tratar como inteiro
        }

        // Strings
        if (ch == '{') {
            StringBuilder literalBuilder = new StringBuilder(); // Usando StringBuilder para armazenar a STRING
            readch();

            while (ch != '}') { // Continua até encontrar a chave de fechamento
                if (ch == (char) -1) { // Se chegar ao final do arquivo sem encontrar o fechamento
                    throw new CompilerError(ErrorType.LEXICAL, line, "Unexpected end of file after string literal.");
                }

                literalBuilder.append(ch);

                readch();
            }

            readch();

            return new StringToken(literalBuilder.toString());
        }

        // Identificadores
        if (Character.isLetter(ch) || ch == '_')

        {

            StringBuffer sb = new StringBuffer();
            do {
                sb.append(ch);
                readch();
            } while (Character.isLetterOrDigit(ch) || ch == '_');
            String s = sb.toString();
            Word w = (Word) words.get(s);
            if (w != null)
                return w; // palavra já existe na HashTable
            w = new Word(s, Tag.ID);
            words.put(s, w);
            return w;
        }

        if (ch != (char) -1)
            throw new CompilerError(ErrorType.LEXICAL, line, "Invalid character: '" + ch + "'.");

        return null;
    }
}