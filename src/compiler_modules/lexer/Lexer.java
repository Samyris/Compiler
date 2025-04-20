package compiler_modules.lexer;

import java.io.*;
import java.util.*;

import shared.enums.ErrorType;
import shared.errors.CompilerError;

public class Lexer {
    public static int line = 1;
    private char ch = ' ';
    private FileReader file;
    @SuppressWarnings("rawtypes")
    private Hashtable words = new Hashtable();

    @SuppressWarnings("unchecked")
    private void reserve(Word w) {
        words.put(w.toString(), w);
    }

    public Lexer(String fileName) throws FileNotFoundException {
        try {
            file = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            throw e;
        }

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

    private boolean readch(char c) throws IOException {
        readch();
        if (ch != c)
            return false;
        ch = ' ';
        return true;
    }

    @SuppressWarnings("unchecked")
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
            Word w = (Word) words.get(s);

            if (w != null) {
                return w;
            }

            w = new Word(s, Tag.ID);
            words.put(s, w);

            return w;
        }

        if (ch != (char) -1) {
            throw new CompilerError(ErrorType.LEXICAL, line, "Invalid character: '" + ch + "'.");
        }

        return null;
    }
}
