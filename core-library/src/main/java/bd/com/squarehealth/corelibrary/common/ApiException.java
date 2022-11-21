package bd.com.squarehealth.corelibrary.common;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ApiException extends Exception {

    private HttpStatus status;
    private String message;
    private Map<String, Object> data;

    public ApiException(Exception exception) {
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.message = exception.getMessage();
        this.setStackTrace(exception.getStackTrace());
    }

    public ApiException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApiException(HttpStatus status, String message, Map<String, Object> data) {
        this(status, message);

        setData(data);
    }

    public void setData(String key, Object value) {
        if (data == null) { data = new HashMap<>(); }

        data.put(key, value);
    }

    public void setData(Map<String, Object> data) {
        if (data == null || data.size() == 0) { return; }
        if (this.data == null) { this.data = new HashMap<>(); }

        this.data.putAll(data);
    }

    public ApiResponse toApiResponse() {
        return new ApiResponse(status, message, data);
    }
}
