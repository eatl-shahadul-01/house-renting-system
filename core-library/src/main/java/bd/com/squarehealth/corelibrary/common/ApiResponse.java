package bd.com.squarehealth.corelibrary.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties({ "httpStatus" })
@NoArgsConstructor
public class ApiResponse {

    private Date date = new Date();
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
