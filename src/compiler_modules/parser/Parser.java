package compiler_modules.parser;

import compiler_modules.lexer.*;
import shared.enums.ErrorType;
import shared.errors.CompilerError;

public class Parser {
    private Lexer lexer;
    private Token token;
    private boolean exitReached = false;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    private void throwInvalidTokenError() {
        throw new CompilerError(ErrorType.SYNTACTIC, Lexer.line,
                String.format("Error near or at token '%s'.", token.mapTagToString()));
    }

    public void parse() {
        advance();
        program();
    }

    private void advance() {
        try {
            if (token != null && "EXIT".equals(token.mapTagToString())) {
                token = lexer.scan();

                if (token != null && exitReached) {
                    throw new CompilerError(ErrorType.SYNTACTIC, Lexer.line,
                            "No tokens should appear after EXIT.");
                }
                return;
            }

            token = lexer.scan();

            if (token == null) {
                throw new CompilerError(ErrorType.SYNTACTIC, Lexer.line,
                        "Unexpected end of file.");
            }

            if ("EXIT".equals(token.mapTagToString())) {
                exitReached = true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private void eat(int tag) {
        if (token.tag == tag) {
            advance();
        } else {
            throwInvalidTokenError();
        }
    }

    private void program() {
        switch (token.tag) {
            case Tag.START:
                eat(Tag.START);
                if (token.tag == Tag.INT || token.tag == Tag.FLOAT || token.tag == Tag.STRING) {
                    decl_list();
                }
                stmt_list();
                eat(Tag.EXIT);
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void decl_list() {
        switch (token.tag) {
            case Tag.INT:
            case Tag.FLOAT:
            case Tag.STRING:
                decl();
                while (token.tag == Tag.INT || token.tag == Tag.FLOAT || token.tag == Tag.STRING) {
                    decl();
                }
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void decl() {
        switch (token.tag) {
            case Tag.INT:
            case Tag.FLOAT:
            case Tag.STRING:
                type();
                ident_list();
                eat(Tag.SEMICOLON);
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void ident_list() {
        switch (token.tag) {
            case Tag.ID:
                identifier();
                while (token.tag == Tag.COMMA) {
                    eat(Tag.COMMA);
                    identifier();
                }
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void type() {
        switch (token.tag) {
            case Tag.INT:
                eat(Tag.INT);
                break;
            case Tag.FLOAT:
                eat(Tag.FLOAT);
                break;
            case Tag.STRING:
                eat(Tag.STRING);
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void stmt_list() {
        switch (token.tag) {
            case Tag.ID:
            case Tag.IF:
            case Tag.DO:
            case Tag.SCAN:
            case Tag.PRINT:
                stmt();
                while (token.tag == Tag.ID || token.tag == Tag.IF || token.tag == Tag.DO || token.tag == Tag.SCAN
                        || token.tag == Tag.PRINT) {
                    stmt();
                }
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void stmt() {
        switch (token.tag) {
            case Tag.ID:
                assign_stmt();
                eat(Tag.SEMICOLON);
                break;
            case Tag.IF:
                if_stmt();
                break;
            case Tag.DO:
                while_stmt();
                break;
            case Tag.SCAN:
                read_stmt();
                eat(Tag.SEMICOLON);
                break;
            case Tag.PRINT:
                write_stmt();
                eat(Tag.SEMICOLON);
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void assign_stmt() {
        switch (token.tag) {
            case Tag.ID:
                identifier();
                eat(Tag.ASSIGN);
                simple_expr();
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void if_stmt() {
        switch (token.tag) {
            case Tag.IF:
                eat(Tag.IF);
                condition();
                eat(Tag.THEN);
                stmt_list();
                if_stmt_prime();
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void if_stmt_prime() {
        switch (token.tag) {
            case Tag.ELSE:
                eat(Tag.ELSE);
                stmt_list();
                eat(Tag.END);
                break;
            case Tag.END:
                eat(Tag.END);
                break;
            case Tag.ID:
            case Tag.IF:
            case Tag.DO:
            case Tag.SCAN:
            case Tag.PRINT:
            case Tag.EXIT:
            case Tag.WHILE:
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void condition() {
        switch (token.tag) {
            case Tag.ID:
            case Tag.INT_VALUE:
            case Tag.FLOAT_VALUE:
            case Tag.STRING_VALUE:
            case Tag.OPEN_PAREN:
            case Tag.NOT:
            case Tag.SUB:
                expression();
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void while_stmt() {
        switch (token.tag) {
            case Tag.DO:
                eat(Tag.DO);
                stmt_list();
                stmt_sufix();
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void stmt_sufix() {
        switch (token.tag) {
            case Tag.WHILE:
                eat(Tag.WHILE);
                condition();
                eat(Tag.END);
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void read_stmt() {
        switch (token.tag) {
            case Tag.SCAN:
                eat(Tag.SCAN);
                eat(Tag.OPEN_PAREN);
                identifier();
                eat(Tag.CLOSE_PAREN);
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void write_stmt() {
        switch (token.tag) {
            case Tag.PRINT:
                eat(Tag.PRINT);
                eat(Tag.OPEN_PAREN);
                writable();
                eat(Tag.CLOSE_PAREN);
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void writable() {
        switch (token.tag) {
            case Tag.ID:
            case Tag.INT_VALUE:
            case Tag.FLOAT_VALUE:
            case Tag.STRING_VALUE:
            case Tag.OPEN_PAREN:
            case Tag.NOT:
            case Tag.SUB:
                simple_expr();
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void expression() {
        switch (token.tag) {
            case Tag.ID:
            case Tag.INT_VALUE:
            case Tag.FLOAT_VALUE:
            case Tag.STRING_VALUE:
            case Tag.OPEN_PAREN:
            case Tag.NOT:
            case Tag.SUB:
                simple_expr();
                expression_prime();
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void expression_prime() {
        switch (token.tag) {
            case Tag.EQ:
            case Tag.GT:
            case Tag.GE:
            case Tag.LT:
            case Tag.LE:
            case Tag.NE:
                relop();
                simple_expr();
                break;
            case Tag.THEN:
            case Tag.END:
            case Tag.CLOSE_PAREN:
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void simple_expr() {
        switch (token.tag) {
            case Tag.ID:
            case Tag.INT_VALUE:
            case Tag.FLOAT_VALUE:
            case Tag.STRING_VALUE:
            case Tag.OPEN_PAREN:
            case Tag.NOT:
            case Tag.SUB:
                term();
                simple_expr_prime();
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void simple_expr_prime() {
        switch (token.tag) {
            case Tag.ADD:
            case Tag.SUB:
            case Tag.OR:
                addop();
                term();
                simple_expr_prime();
                break;
            case Tag.SEMICOLON:
            case Tag.CLOSE_PAREN:
            case Tag.EQ:
            case Tag.GT:
            case Tag.GE:
            case Tag.LT:
            case Tag.LE:
            case Tag.NE:
            case Tag.THEN:
            case Tag.END:
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void term() {
        switch (token.tag) {
            case Tag.ID:
            case Tag.INT_VALUE:
            case Tag.FLOAT_VALUE:
            case Tag.STRING_VALUE:
            case Tag.OPEN_PAREN:
            case Tag.NOT:
            case Tag.SUB:
                factor_a();
                term_prime();
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void term_prime() {
        switch (token.tag) {
            case Tag.MUL:
            case Tag.DIV:
            case Tag.MOD:
            case Tag.AND:
                mulop();
                factor_a();
                term_prime();
                break;
            case Tag.ADD:
            case Tag.SUB:
            case Tag.OR:
            case Tag.SEMICOLON:
            case Tag.CLOSE_PAREN:
            case Tag.EQ:
            case Tag.GT:
            case Tag.GE:
            case Tag.LT:
            case Tag.LE:
            case Tag.NE:
            case Tag.THEN:
            case Tag.END:
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void factor_a() {
        switch (token.tag) {
            case Tag.ID:
            case Tag.INT_VALUE:
            case Tag.FLOAT_VALUE:
            case Tag.STRING_VALUE:
            case Tag.OPEN_PAREN:
                factor();
                break;
            case Tag.NOT:
                eat(Tag.NOT);
                factor();
                break;
            case Tag.SUB:
                eat(Tag.SUB);
                factor();
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void factor() {
        switch (token.tag) {
            case Tag.ID:
                identifier();
                break;
            case Tag.INT_VALUE:
            case Tag.FLOAT_VALUE:
            case Tag.STRING_VALUE:
                constant();
                break;
            case Tag.OPEN_PAREN:
                eat(Tag.OPEN_PAREN);
                expression();
                eat(Tag.CLOSE_PAREN);
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void relop() {
        switch (token.tag) {
            case Tag.EQ:
                eat(Tag.EQ);
                break;
            case Tag.GT:
                eat(Tag.GT);
                break;
            case Tag.GE:
                eat(Tag.GE);
                break;
            case Tag.LT:
                eat(Tag.LT);
                break;
            case Tag.LE:
                eat(Tag.LE);
                break;
            case Tag.NE:
                eat(Tag.NE);
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void addop() {
        switch (token.tag) {
            case Tag.ADD:
                eat(Tag.ADD);
                break;
            case Tag.SUB:
                eat(Tag.SUB);
                break;
            case Tag.OR:
                eat(Tag.OR);
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void mulop() {
        switch (token.tag) {
            case Tag.MUL:
                eat(Tag.MUL);
                break;
            case Tag.DIV:
                eat(Tag.DIV);
                break;
            case Tag.MOD:
                eat(Tag.MOD);
                break;
            case Tag.AND:
                eat(Tag.AND);
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void constant() {
        switch (token.tag) {
            case Tag.INT_VALUE:
                integer_const();
                break;
            case Tag.FLOAT_VALUE:
                float_const();
                break;
            case Tag.STRING_VALUE:
                literal();
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void integer_const() {
        switch (token.tag) {
            case Tag.INT_VALUE:
                eat(Tag.INT_VALUE);
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void float_const() {
        switch (token.tag) {
            case Tag.FLOAT_VALUE:
                eat(Tag.FLOAT_VALUE);
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void literal() {
        switch (token.tag) {
            case Tag.STRING_VALUE:
                eat(Tag.STRING_VALUE);
                break;
            default:
                throwInvalidTokenError();
        }
    }

    private void identifier() {
        switch (token.tag) {
            case Tag.ID:
                eat(Tag.ID);
                break;
            default:
                throwInvalidTokenError();
        }
    }
}
