package shared.errors;

import shared.enums.ErrorType;

public class CompilerError extends RuntimeException {
    private final int line;
    private final String details;
    private final ErrorType errorType;

    public CompilerError(ErrorType errorType, int line, String details) {
        super(String.format("%s Error near or at line %d. %s", errorType.name(), line, details));
        this.line = line;
        this.details = details;
        this.errorType = errorType;
    }

    public int getLine() {
        return line;
    }

    public String getDetails() {
        return details;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
