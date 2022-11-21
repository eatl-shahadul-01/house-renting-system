package bd.com.squarehealth.corelibrary.common;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> createResponseEntityFromException(Exception exception) {
        exception.printStackTrace();

        ApiException apiException = exception instanceof ApiException
                ? (ApiException) exception : new ApiException(exception);
        ApiResponse apiResponse = apiException.toApiResponse();

        return new ResponseEntity<>(apiResponse, apiResponse.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        ApiException apiException = new ApiException(HttpStatus.BAD_REQUEST, "Request data validation failed.");
        apiException.setData("errors", errors);

        return createResponseEntityFromException(apiException);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return createResponseEntityFromException(exception);
    }

    @ExceptionHandler()
    protected ResponseEntity<Object> handleGlobalException(Exception exception, WebRequest request) {
        return createResponseEntityFromException(exception);
    }
}
