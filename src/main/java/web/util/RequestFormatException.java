package web.util;

import org.springframework.validation.Errors;

public class RequestFormatException extends RuntimeException {

    private final Errors errors;

    public RequestFormatException(Errors errors) {
        this.errors = errors;
    }

    @Override
    public String getMessage() {
        StringBuilder builder = new StringBuilder();
        errors.getFieldErrors().forEach(fieldError -> builder.append(fieldError.getField()).append(" - ").append(fieldError.getDefaultMessage()).append(";"));
        return builder.toString();
    }
}
