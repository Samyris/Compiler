package compiler_modules.parser;

import compiler_modules.lexer.*;
import shared.enums.ErrorType;
import shared.errors.CompilerError;

public class Parser {
    private Lexer lexer;
    private Token token;
    private String tokenType;
    @SuppressWarnings("unused")
    private String constantOperandType;
    private String currentOperator;
    private String leftOperandType;
    private String rightOperandType;
    private String nodeGeneratedFromRule;
    private boolean exitReached = false;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    private void throwSyntacticError(String message) {
        throw new CompilerError(ErrorType.SYNTACTIC, Lexer.line, message);
    }

    private void throwSemanticError(String message) {
        throw new CompilerError(ErrorType.SEMANTIC, Lexer.line, message);
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
            throwSyntacticError(
                    String.format("Error at token %s. Expected %s.", Tag.getTagName(token.tag), Tag.getTagName(tag)));
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
                throwSyntacticError(String.format("Error at token %s. Expected START.", Tag.getTagName(token.tag)));
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
                throwSyntacticError(
                        String.format("Error at token %s. Expected one of: INT, FLOAT, STRING.",
                                Tag.getTagName(token.tag)));
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
                throwSyntacticError(
                        String.format("Error at token %s. Expected one of: INT, FLOAT, STRING.",
                                Tag.getTagName(token.tag)));
        }
    }

    private void ident_list() {
        switch (token.tag) {
            case Tag.ID:
                Lexer.symbolTable.put(token.getStringRepresentation(),
                        new Word(token.getStringRepresentation(), Tag.ID, tokenType));
                identifier();
                while (token.tag == Tag.COMMA) {
                    eat(Tag.COMMA);
                    Lexer.symbolTable.put(token.getStringRepresentation(),
                            new Word(token.getStringRepresentation(), Tag.ID, tokenType));
                    identifier();
                }
                break;
            default:
                throwSyntacticError(
                        String.format("Error at token %s. Expected ID (Identifier).", Tag.getTagName(token.tag)));
        }
    }

    private void type() {
        switch (token.tag) {
            case Tag.INT:
                tokenType = "INT";
                eat(Tag.INT);
                break;
            case Tag.FLOAT:
                tokenType = "FLOAT";
                eat(Tag.FLOAT);
                break;
            case Tag.STRING:
                tokenType = "STRING";
                eat(Tag.STRING);
                break;
            default:
                throwSyntacticError(String.format("Error at token %s. Expected one of: INT, FLOAT, STRING.",
                        Tag.getTagName(token.tag)));
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
                throwSyntacticError(
                        String.format("Error at token %s. Expected one of: ID (Identifier), IF, DO, SCAN, PRINT.",
                                Tag.getTagName(token.tag)));
        }
    }

    private void stmt() {
        switch (token.tag) {
            case Tag.ID:
                assign_stmt();
                if (leftOperandType != rightOperandType) {
                    throwSemanticError(
                            "Assignment is allowed only when the expression type matches the variable type.");
                }
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
                throwSyntacticError(
                        String.format("Error at token %s. Expected one of: ID (Identifier), IF, DO, SCAN, PRINT.",
                                Tag.getTagName(token.tag)));
        }
    }

    private void assign_stmt() {
        switch (token.tag) {
            case Tag.ID:
                nodeGeneratedFromRule = "identifier";
                identifier();
                eat(Tag.ASSIGN);
                nodeGeneratedFromRule = "simple_expr";
                simple_expr();
                break;
            default:
                throwSyntacticError(
                        String.format("Error at token %s. Expected ID (Identifier).", Tag.getTagName(token.tag)));
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
                throwSyntacticError(String.format("Error at token %s. Expected IF.", Tag.getTagName(token.tag)));

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
            default:
                throwSyntacticError(
                        String.format("Error at token %s. Expected one of: ELSE, END.", Tag.getTagName(token.tag)));
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
                throwSyntacticError(
                        String.format(
                                "Error at token %s. Expected one of: ID (Identifier), INT_VALUE (Integer constant), FLOAT_VALUE (Floating-point constant), STRING_VALUE (String constant), OPEN_PAREN '(', NOT '!', SUB '-'.",
                                Tag.getTagName(token.tag)));
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
                throwSyntacticError(String.format("Error at token %s. Expected DO.", Tag.getTagName(token.tag)));
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
                throwSyntacticError(String.format("Error at token %s. Expected WHILE.", Tag.getTagName(token.tag)));

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
                throwSyntacticError(String.format("Error at token %s. Expected SCAN.", Tag.getTagName(token.tag)));
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
                throwSyntacticError(String.format("Error at token %s. Expected PRINT.", Tag.getTagName(token.tag)));
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
                throwSyntacticError(
                        String.format(
                                "Error at token %s. Expected one of: ID (Identifier), INT_VALUE (Integer constant), FLOAT_VALUE (Floating-point constant), STRING_VALUE (String constant), OPEN_PAREN '(', NOT '!', SUB '-'.",
                                Tag.getTagName(token.tag)));
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
                throwSyntacticError(
                        String.format(
                                "Error at token %s. Expected one of: ID (Identifier), INT_VALUE (Integer constant), FLOAT_VALUE (Floating-point constant), STRING_VALUE (String constant), OPEN_PAREN '(', NOT '!', SUB '-'.",
                                Tag.getTagName(token.tag)));
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
                throwSyntacticError(
                        String.format(
                                "Error at token %s. Expected one of: EQ '==', GT '>', GE '>=', LT '<', LE '<=', NE '!=', THEN, END, CLOSE_PAREN ')'.",
                                Tag.getTagName(token.tag)));
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
                throwSyntacticError(
                        String.format(
                                "Error at token %s. Expected one of: ID (Identifier), INT_VALUE (Integer constant), FLOAT_VALUE (Floating-point constant), STRING_VALUE (String constant), OPEN_PAREN '(', NOT '!', SUB '-'.",
                                Tag.getTagName(token.tag)));
        }
    }

    private void simple_expr_prime() {
        switch (token.tag) {
            case Tag.ADD:
            case Tag.SUB:
            case Tag.OR:
                String leftType = rightOperandType;
                addop();
                term();
                String rightType = rightOperandType;
                switch (currentOperator) {
                    case "ADD":
                        if (leftType.equals("STRING") || rightType.equals("STRING")) {
                            rightOperandType = "STRING";
                        } else if (leftType.equals("FLOAT") || rightType.equals("FLOAT")) {
                            rightOperandType = "FLOAT";
                        } else {
                            rightOperandType = "INT";
                        }
                        break;
                    case "SUB":
                        if (leftType.equals("STRING") || rightType.equals("STRING")) {
                            throwSemanticError("SUB require numeric operands.");
                        } else if (leftType.equals("FLOAT") || rightType.equals("FLOAT")) {
                            rightOperandType = "FLOAT";
                        } else {
                            rightOperandType = "INT";
                        }
                        break;
                }
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
                throwSyntacticError(
                        String.format(
                                "Error at token %s. Expected one of:  ADD '+', SUB '-', OR '||', SEMICOLON ';', CLOSE_PAREN ')', EQ '==', GT '>', GE '>=', LT '<', LE '<=', NE '!=', THEN, END.",
                                Tag.getTagName(token.tag)));
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
                factor_a();
                term_prime();
                break;
            default:
                throwSyntacticError(
                        String.format(
                                "Error at token %s. Expected one of: ID (Identifier), INT_VALUE (Integer constant), FLOAT_VALUE (Floating-point constant), STRING_VALUE (String constant), OPEN_PAREN '(', NOT '!'.",
                                Tag.getTagName(token.tag)));
        }
    }

    private void term_prime() {
        switch (token.tag) {
            case Tag.MUL:
            case Tag.DIV:
            case Tag.MOD:
            case Tag.AND:
                String leftType = rightOperandType;
                mulop();
                factor_a();
                String rightType = rightOperandType;
                switch (currentOperator) {
                    case "DIV":
                    case "MUL":
                        if (leftType.equals("STRING") || rightType.equals("STRING")) {
                            throwSemanticError("DIV and MUL require numeric operands.");
                        } else if (leftType.equals("FLOAT") || rightType.equals("FLOAT")) {
                            rightOperandType = "FLOAT";
                        } else {
                            rightOperandType = "INT";
                        }
                        break;
                    case "MOD":
                        if (leftType.equals("INT") && rightType.equals("INT")) {
                            rightOperandType = "INT";
                        } else {
                            throwSemanticError("MOD requires integer operands.");
                        }
                        break;
                }
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
                throwSyntacticError(
                        String.format(
                                "Error at token %s. Expected one of:  MUL '*', DIV '/', MOD '%%', AND '&&', ADD '+', SUB '-', OR '||', SEMICOLON ';', CLOSE_PAREN ')', EQ '==', GT '>', GE '>=', LT '<', LE '<=', NE '!=', THEN, END.",
                                Tag.getTagName(token.tag)));
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
                throwSyntacticError(
                        String.format(
                                "Error at token %s. Expected one of: ID (Identifier), INT_VALUE (Integer constant), FLOAT_VALUE (Floating-point constant), STRING_VALUE (String constant), OPEN_PAREN '(', NOT '!', SUB '-'.",
                                Tag.getTagName(token.tag)));
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
                throwSyntacticError(String.format(
                        "Error at token %s. Expected one of: ID (Identifier), INT_VALUE (Integer constant), FLOAT_VALUE (Floating-point constant), STRING_VALUE (String constant), OPEN_PAREN '('.",
                        Tag.getTagName(token.tag)));
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
                throwSyntacticError(String.format(
                        "Error at token %s. Expected one of: EQ '==', GT '>', GE '>=', LT '<', LE '<=', NE '!='.",
                        Tag.getTagName(token.tag)));
        }
    }

    private void addop() {
        switch (token.tag) {
            case Tag.ADD:
                currentOperator = "ADD";
                eat(Tag.ADD);
                break;
            case Tag.SUB:
                currentOperator = "SUB";
                eat(Tag.SUB);
                break;
            case Tag.OR:
                currentOperator = "OR";
                eat(Tag.OR);
                break;
            default:
                throwSyntacticError(String.format(
                        "Error at token %s. Expected one of: ADD '+', SUB '-', OR '||'.", Tag.getTagName(token.tag)));
        }
    }

    private void mulop() {
        switch (token.tag) {
            case Tag.MUL:
                currentOperator = "MUL";
                eat(Tag.MUL);
                break;
            case Tag.DIV:
                currentOperator = "DIV";
                eat(Tag.DIV);
                break;
            case Tag.MOD:
                currentOperator = "MOD";
                eat(Tag.MOD);
                break;
            case Tag.AND:
                currentOperator = "AND";
                eat(Tag.AND);
                break;
            default:
                throwSyntacticError(String.format(
                        "Error at token %s. Expected one of: MUL '*', DIV '/', MOD '%%', AND '&&'.",
                        Tag.getTagName(token.tag)));
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
                throwSyntacticError(
                        String.format(
                                "Error at token %s. Expected one of: INT_VALUE (Integer constant), FLOAT_VALUE (Floating-point constant), STRING_VALUE (String constant).",
                                Tag.getTagName(token.tag)));
        }
    }

    private void integer_const() {
        switch (token.tag) {
            case Tag.INT_VALUE:
                constantOperandType = "INT";
                rightOperandType = "INT";
                eat(Tag.INT_VALUE);
                break;
            default:
                throwSyntacticError(String.format(
                        "Error at token %s. Expected INT_VALUE (Integer constant).", Tag.getTagName(token.tag)));
        }
    }

    private void float_const() {
        switch (token.tag) {
            case Tag.FLOAT_VALUE:
                constantOperandType = "FLOAT";
                rightOperandType = "FLOAT";
                eat(Tag.FLOAT_VALUE);
                break;
            default:
                throwSyntacticError(String.format(
                        "Error at token %s. Expected FLOAT_VALUE (Floating-point constant).",
                        Tag.getTagName(token.tag)));
        }
    }

    private void literal() {
        switch (token.tag) {
            case Tag.STRING_VALUE:
                constantOperandType = "STRING";
                rightOperandType = "STRING";
                eat(Tag.STRING_VALUE);
                break;
            default:
                throwSyntacticError(String.format(
                        "Error at token %s. Expected STRING_VALUE (String constant).", Tag.getTagName(token.tag)));
        }
    }

    private void identifier() {
        switch (token.tag) {
            case Tag.ID:
                if (!Lexer.symbolTable.containsKey(token.getStringRepresentation())) {
                    throwSemanticError(String.format("The variable '%s' must be declared before it can be used.",
                            token.getStringRepresentation()));
                }

                Word wordEntry = Lexer.symbolTable.get(token.getStringRepresentation());

                tokenType = wordEntry.getType();

                eat(Tag.ID);

                if (nodeGeneratedFromRule == "identifier") {
                    leftOperandType = tokenType;
                    break;
                }

                rightOperandType = tokenType;

                break;
            default:
                throwSyntacticError(String.format(
                        "Error at token %s. Expected ID (Identifier).", Tag.getTagName(token.tag)));
        }
    }
}
