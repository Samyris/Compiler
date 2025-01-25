package shared.errors;

import javax.lang.model.type.ErrorType;
import shared.enums.ErrorType;

public class CompilerError extends RuntimeException {

    private final int line;
    private final String details;
    private final ErrorType errorType;

    // Construtor para criar o erro
    public CompilerError(ErrorType errorType, int line, String details) {
        super(String.format("%s Error on line %d. %s", errorType.name(), line, details));
        this.line = line;
        this.details = details;
        this.errorType = errorType;
    }

    // Método para acessar a linha do erro
    public int getLine() {
        return line;
    }

    // Método para acessar os detalhes do erro
    public String getDetails() {
        return details;
    }

    // Método para acessar o tipo do erro
    public ErrorType getErrorType() {
        return errorType;
    }
}