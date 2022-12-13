package web.util;

import org.springframework.validation.Errors;

import java.util.StringJoiner;

public class RequestFormatException extends RuntimeException {

    private final Errors errors;

    public RequestFormatException(Errors errors) {
        this.errors = errors;
    }

    @Override
    public String getMessage() {
        StringJoiner joiner = new StringJoiner("; ");
        errors.getFieldErrors().forEach(fieldError -> joiner.add(fieldError.getDefaultMessage()));
        return joiner.toString();
    }
}
