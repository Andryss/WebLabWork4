package web.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RequestErrorResponse {

    private final String timestamp;

    private final String error;

    public RequestErrorResponse(Throwable throwable) {
        timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        error = throwable.getMessage();
    }

    public RequestErrorResponse(String errorMsg) {
        timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        error = errorMsg;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getError() {
        return error;
    }
}
