package bd.com.squarehealth.corelibrary.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties({ "httpStatus" })
@NoArgsConstructor
public class ApiResponse {

    private String date = Instant.now().toString();
    private int status;
    private HttpStatus httpStatus;
    private String message;
    private Map<String, Object> data;

    public ApiResponse(HttpStatus status, String message) {
        this.httpStatus = status;
        this.status = status.value();
        this.message = message;
    }

    public ApiResponse(HttpStatus status, String message, Map<String, Object> data) {
        this(status, message);

        setData(data);
    }

    public ApiResponse(Map<String, Object> map) {
        this.status = (int) map.get("status");
        this.httpStatus = HttpStatus.resolve(status);
        this.date = (String) map.get("date");
        this.message = (String) map.get("message");
        this.data = (Map<String, Object>) map.get("data");
    }

    public void setData(String key, Object value) {
        if (this.data == null) { this.data = new HashMap<>(); }

        this.data.put(key, value);
    }

    public void setData(Map<String, Object> data) {
        if (data == null || data.size() == 0) { return; }
        if (this.data == null) { this.data = new HashMap<>(); }

        this.data.putAll(data);
    }
}
