package compiler_modules.lexer;

import java.io.*;
import java.util.*;

import shared.enums.ErrorType;
import shared.errors.CompilerError;

public class Lexer {
    public static int line = 1;
    private char ch = ' ';
    private FileReader file;
    public static Hashtable<String, Word> symbolTable = new Hashtable<>();

    private void reserve(Word w) {
        symbolTable.put(w.getStringRepresentation(), w);
    }

    @SuppressWarnings("rawtypes")
    public Hashtable getSymbolTable() {
        return symbolTable;
    }

    public Lexer(String fileName) throws FileNotFoundException {
        try {
            file = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            throw e;
        }

        reserve(new Word("start", Tag.START, "KEYWORD"));
        reserve(new Word("exit", Tag.EXIT, "KEYWORD"));
        reserve(new Word("int", Tag.INT, "KEYWORD"));
        reserve(new Word("float", Tag.FLOAT, "KEYWORD"));
        reserve(new Word("string", Tag.STRING, "KEYWORD"));
        reserve(new Word("if", Tag.IF, "KEYWORD"));
        reserve(new Word("then", Tag.THEN, "KEYWORD"));
        reserve(new Word("else", Tag.ELSE, "KEYWORD"));
        reserve(new Word("do", Tag.DO, "KEYWORD"));
        reserve(new Word("while", Tag.WHILE, "KEYWORD"));
        reserve(new Word("end", Tag.END, "KEYWORD"));
        reserve(new Word("scan", Tag.SCAN, "KEYWORD"));
        reserve(new Word("print", Tag.PRINT, "KEYWORD"));
    }

    private void readch() throws IOException {
        ch = (char) file.read();
    }

    private boolean readch(char c) throws IOException {
        readch();
        if (ch != c)
            return false;
        ch = ' ';
        return true;
    }

    public Token scan() throws IOException {
        for (;; readch()) {
            if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\b')
                continue;
            else if (ch == '\n')
                line++;
            else
                break;
        }

        switch (ch) {
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
                        if (ch == '\n') {
                            readch();
                            ++line;
                            return scan();
                        }
                        if (ch == (char) -1) {
                            throw new CompilerError(ErrorType.LEXICAL, line,
                                    "A line break is required after a line comment.");
                        }
                        readch();
                    }
                }
                if (ch == '*') {
                    while (true) {
                        readch();
                        if (ch == '*') {
                            readch();
                            if (ch == '/') {
                                readch();
                                return scan();
                            }
                        }
                        if (ch == (char) -1) {
                            throw new CompilerError(ErrorType.LEXICAL, line, "Unexpected end of file after comment.");
                        }
                        if (ch == '\n') {
                            ++line;
                        }
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

        if (Character.isDigit(ch)) {
            float value = 0;
            boolean isFloat = false;
            int decimalPlace = 1;

            do {
                value = 10 * value + Character.digit(ch, 10);
                readch();
            } while (Character.isDigit(ch));

            if (ch == '.') {
                isFloat = true;
                readch();

                if (!Character.isDigit(ch)) {
                    throw new CompilerError(ErrorType.LEXICAL, line, "Invalid numerical sequence.");
                }

                while (Character.isDigit(ch)) {
                    value = value + Character.digit(ch, 10) / (float) (decimalPlace * 10);
                    decimalPlace++;
                    readch();
                }
            }

            if (Character.isLetter(ch)) {
                throw new CompilerError(ErrorType.LEXICAL, line, "Invalid numerical sequence.");
            }

            if (isFloat) {
                return new FloatToken(value);
            }

            return new IntToken((int) value);
        }

        if (ch == '{') {
            StringBuilder literalBuilder = new StringBuilder();
            readch();

            while (ch != '}') {
                if (ch == (char) -1) {
                    throw new CompilerError(ErrorType.LEXICAL, line, "Unexpected end of file after string literal.");
                }

                if (ch > 127 || ch == 10) {
                    throw new CompilerError(ErrorType.LEXICAL, line,
                            "Invalid character in string literal: '" + (ch == '\n' ? "\\n" : ch) + "'.");

                }

                literalBuilder.append(ch);

                readch();
            }

            readch();

            return new StringToken(literalBuilder.toString());
        }

        if (Character.isLetter(ch) || ch == '_') {
            StringBuffer sb = new StringBuffer();
            do {
                if ((ch < 48 || ch > 57) && (ch < 65 || ch > 90) && (ch < 97 || ch > 122) && ch != 95) {
                    throw new CompilerError(ErrorType.LEXICAL, line, "Invalid character in identifier: '" + ch + "'.");
                }
                sb.append(ch);
                readch();
            } while (Character.isLetterOrDigit(ch) || ch == '_');

            String s = sb.toString();
            Word w = (Word) symbolTable.get(s);

            if (w != null) {
                return w;
            }

            w = new Word(s, Tag.ID);
            // symbolTable.put(s, w);

            return w;
        }

        if (ch != (char) -1) {
            throw new CompilerError(ErrorType.LEXICAL, line, "Invalid character: '" + ch + "'.");
        }

        return null;
    }
}
