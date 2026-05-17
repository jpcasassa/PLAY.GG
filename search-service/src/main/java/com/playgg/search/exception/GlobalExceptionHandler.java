package com.playgg.search.exception;

import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.*;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiError> notFound(ResourceNotFoundException ex) {
    logger.error("404 {}", ex.getMessage());
    return build(HttpStatus.NOT_FOUND, ex.getMessage(), Map.of());
  }

  @ExceptionHandler({
    MethodArgumentNotValidException.class,
    ConstraintViolationException.class,
    IllegalArgumentException.class
  })
  public ResponseEntity<ApiError> bad(Exception ex) {
    Map<String, String> details = new HashMap<>();
    if (ex instanceof MethodArgumentNotValidException e) {
      e.getBindingResult()
          .getFieldErrors()
          .forEach(er -> details.put(er.getField(), er.getDefaultMessage()));
    }
    logger.error("400 {}", ex.getMessage());
    return build(HttpStatus.BAD_REQUEST, "Datos invalidos", details);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> error(Exception ex) {
    logger.error("500", ex);
    return build(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", Map.of());
  }

  private ResponseEntity<ApiError> build(HttpStatus s, String m, Map<String, String> d) {
    return ResponseEntity.status(s)
        .body(new ApiError(LocalDateTime.now(), s.value(), s.getReasonPhrase(), m, d));
  }
}
