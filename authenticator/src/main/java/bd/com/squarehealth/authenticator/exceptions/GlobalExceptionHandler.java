package bd.com.squarehealth.authenticator.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends bd.com.squarehealth.corelibrary.common.GlobalExceptionHandler {}
