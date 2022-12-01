package web.util;

public class RequestMessageResponse {

    private final String message;

    public RequestMessageResponse(Throwable throwable) {
        this.message = throwable.getMessage();
    }

    public RequestMessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
