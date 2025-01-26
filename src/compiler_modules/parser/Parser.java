package compiler_modules.parser;

import java.util.List;
import compiler_modules.lexer.Token;
import compiler_modules.lexer.Tag;
import shared.errors.CompilerError;
import shared.enums.ErrorType;

public class Parser {
    private List<Token> tokens;
    private int currentTokenIndex;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
    }

    private Token currentToken() {
        return tokens.get(currentTokenIndex);
    }

    private void advance() {
        currentTokenIndex++;
    }

    private boolean match(int tag) {
        if (currentToken().tag == tag) {
            advance();
            return true;
        }
        return false;
    }

    public void parse() {
        program();
    }

    private void program() {
        if (match(Tag.START)) {
            if (currentToken().tag == Tag.INT || currentToken().tag == Tag.FLOAT || currentToken().tag == Tag.STRING) {
                declList();
            }
            stmtList();
            if (!match(Tag.EXIT)) {
                throw new CompilerError(ErrorType.SYNTAX, currentToken().tag, "Expected 'exit' at the end of the program");
            }
        } else {
            throw new CompilerError(ErrorType.SYNTAX, currentToken().tag, "Expected 'start' at the beginning of the program");
        }
    }

    private void declList() {
        do {
            decl();
        } while (currentToken().tag == Tag.INT || currentToken().tag == Tag.FLOAT || currentToken().tag == Tag.STRING);
    }

    private void decl() {
        type();
        identList();
        if (!match(Tag.SEMICOLON)) {
            throw new CompilerError(ErrorType.SYNTAX, currentToken().tag, "Expected ';' after declaration");
        }
    }

    private void identList() {
        if (!match(Tag.ID)) {
            throw new CompilerError(ErrorType.SYNTAX, currentToken().tag, "Expected identifier in declaration");
        }
        while (match(Tag.COMMA)) {
            if (!match(Tag.ID)) {
                throw new CompilerError(ErrorType.SYNTAX, currentToken().tag, "Expected identifier after ',' in declaration");
            }
        }
    }

    private void type() {
        if (!(match(Tag.INT) || match(Tag.FLOAT) || match(Tag.STRING))) {
            throw new CompilerError(ErrorType.SYNTAX, currentToken().tag, "Expected type (int, float, string) in declaration");
        }
    }

    private void stmtList() {
        do {
            stmt();
        } while (currentToken().tag != Tag.EXIT && currentToken().tag != Tag.END);
    }

    private void stmt() {
        if (currentToken().tag == Tag.ID) {
            assignStmt();
            if (!match(Tag.SEMICOLON)) {
                throw new CompilerError(ErrorType.SYNTAX, currentToken().tag, "Expected ';' after assignment statement");
            }
        } else if (match(Tag.IF)) {
            ifStmt();
        } else if (match(Tag.DO)) {
            whileStmt();
        } else if (match(Tag.SCAN)) {
            readStmt();
            if (!match(Tag.SEMICOLON)) {
                throw new CompilerError(ErrorType.SYNTAX, currentToken().tag, "Expected ';' after read statement");
            }
        } else if (match(Tag.PRINT)) {
            writeStmt();
            if (!match(Tag.SEMICOLON)) {
                throw new CompilerError(ErrorType.SYNTAX, currentToken().tag, "Expected ';' after write statement");
            }
        } else {
            throw new CompilerError(ErrorType.SYNTAX, currentToken().tag, "Unexpected statement");
        }
    }

    private void assignStmt() {
        if (!match(Tag.ID)) {
            throw new CompilerError(ErrorType.SYNTAX, currentToken().tag, "Expected identifier in assignment statement");
        }
        if (!match(Tag.ASSIGN)) {
            throw new CompilerError(ErrorType.SYNTAX, currentToken().tag, "Expected '=' in assignment statement");
        }
        simpleExpr();
    }

    private void ifStmt() {
        condition();
        if (!match(Tag.THEN)) {
            throw new CompilerError(ErrorType.SYNTAX, currentToken().tag, "Expected 'then' in if statement");
        }
        stmtList();
        if (match(Tag.ELSE)) {
            stmtList();
        }
        if (!match(Tag.END)) {
            throw new CompilerError(ErrorType.SYNTAX, currentToken().tag, "Expected 'end' in if statement");
        }
    }

    private void whileStmt() {
        stmtList();
        stmtSufix();
    }

    private void stmtSufix() {
        if (!match(Tag.WHILE)) {
            throw new CompilerError(ErrorType.SYNTAX, currentToken().tag, "Expected 'while' in while statement suffix");
        }
        condition();
        if (!match(Tag.END)) {
            throw new CompilerError(ErrorType.SYNTAX, currentToken().tag, "Expected 'end' in while statement suffix");
        }
    }

    private void readStmt() {
        if (!match(Tag.SCAN)) {
            throw new CompilerError(ErrorType.SYNTAX, currentToken().tag, "Expected 'scan' in read statement");
        }
        if (!match(Tag.OPEN_PAREN)) {
            throw new CompilerError(ErrorType.SYNTAX, currentToken().tag, "Expected '(' in read statement");
        }
        if (!match(Tag.ID)) {
            throw new CompilerError(ErrorType.SYNTAX, currentToken().tag, "Expected identifier in read statement");
        }
        if (!match(Tag.CLOSE_PAREN)) {
            throw new CompilerError(ErrorType.SYNTAX, currentToken().tag, "Expected ')' in read statement");
        }
    }

    private void writeStmt() {
        if (!match(Tag.PRINT)) {
            throw new CompilerError(ErrorType.SYNTAX, currentToken().tag, "Expected 'print' in write statement");
        }
        if (!match(Tag.OPEN_PAREN)) {
            throw new CompilerError(ErrorType.SYNTAX, currentToken().tag, "Expected '(' in write statement");
        }
        writable();
        if (!match(Tag.CLOSE_PAREN)) {
            throw new CompilerError(ErrorType.SYNTAX, currentToken().tag, "Expected ')' in write statement");
        }
    }

    private void writable() {
        if (currentToken().tag == Tag.STRING_VALUE) {
            match(Tag.STRING_VALUE);
        } else {
            simpleExpr();
        }
    }

    private void condition() {
        expression();
    }

    private void expression() {
        simpleExpr();
        if (currentToken().tag == Tag.EQ || currentToken().tag == Tag.GT || currentToken().tag == Tag.GE || currentToken().tag == Tag.LT || currentToken().tag == Tag.LE || currentToken().tag == Tag.NE) {
            match(currentToken().tag);
            simpleExpr();
        }
    }

    private void simpleExpr() {
        term();
        while (currentToken().tag == Tag.ADD || currentToken().tag == Tag.SUB || currentToken().tag == Tag.OR) {
            match(currentToken().tag);
            term();
        }
    }

    private void term() {
        factorA();
        while (currentToken().tag == Tag.MUL || currentToken().tag == Tag.DIV || currentToken().tag == Tag.MOD || currentToken().tag == Tag.AND) {
            match(currentToken().tag);
            factorA();
        }
    }

    private void factorA() {
        if (match(Tag.NOT) || match(Tag.SUB)) {
            factor();
        } else {
            factor();
        }
    }

    private void factor() {
        if (match(Tag.ID) || match(Tag.INT_VALUE) || match(Tag.FLOAT_VALUE)) {
            // Do nothing, just consume the token
        } else if (match(Tag.OPEN_PAREN)) {
            expression();
            if (!match(Tag.CLOSE_PAREN)) {
                throw new CompilerError(ErrorType.SYNTAX, currentToken().tag, "Expected ')' in factor");
            }
        } else {
            throw new CompilerError(ErrorType.SYNTAX, currentToken().tag, "Unexpected token in factor");
        }
    }
}