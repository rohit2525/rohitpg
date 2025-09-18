package com.rohit.pg.simulator.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final String KEY_TIMESTAMP = "timestamp";
  private static final String KEY_STATUS = "status";
  private static final String KEY_ERRORS = "errors";
  private static final String KEY_MESSAGE = "message";
  private static final String KEY_ERROR_CODE = "errorCode";

  @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
  public ResponseEntity<Map<String, Object>> handleValidationErrors(Exception ex) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    Map<String, Object> body = createBaseBody(status);
    body.put(KEY_ERRORS, extractValidationErrors(ex));
    return ResponseEntity.status(status).body(body);
  }

  // Handles business errors → 400
  @ExceptionHandler(SimulatorException.class)
  public ResponseEntity<Map<String, Object>> handleSimulatorException(SimulatorException ex) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    Map<String, Object> body = createBaseBody(status);
    body.put(KEY_ERROR_CODE, ex.getCode());
    body.put(KEY_MESSAGE, ex.getMessage());
    return ResponseEntity.status(status).body(body);
  }

  // Handles unexpected errors → 500
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleUnexpected(Exception ex) {
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    Map<String, Object> body = createBaseBody(status);
    body.put(KEY_MESSAGE, "Internal Server Error");
    return ResponseEntity.status(status).body(body);
  }

  private Map<String, Object> createBaseBody(HttpStatus status) {
    Map<String, Object> body = new HashMap<>();
    body.put(KEY_TIMESTAMP, Instant.now().toString());
    body.put(KEY_STATUS, status.value());
    return body;
  }

  private Map<String, String> extractValidationErrors(Exception ex) {
    Map<String, String> errors = new HashMap<>();
    switch (ex) {
      case MethodArgumentNotValidException manve -> {
        addFieldErrors(errors, manve.getBindingResult());
      }
      case BindException be -> {
        addFieldErrors(errors, be.getBindingResult());
      }
      default -> errors.put("error", ex.getMessage());
    }
    return errors;
  }

  private void addFieldErrors(Map<String, String> target, BindingResult bindingResult) {
    bindingResult
        .getFieldErrors()
        .forEach(err -> target.put(err.getField(), err.getDefaultMessage()));
  }
}
