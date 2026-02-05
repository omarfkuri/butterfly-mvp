package com.social.api.ex;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.social.api.dto.ApiError;

@RestControllerAdvice
public class ApiExceptionHandler
{
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiError> handleNotFound(
    ResourceNotFoundException ex
  )
  {
    return ResponseEntity
    .status(HttpStatus.NOT_FOUND)
    .body(new ApiError(ex.getMessage()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiError> handleBadRequest(
    IllegalArgumentException ex
  )
  {
    return ResponseEntity
    .badRequest()
    .body(new ApiError(ex.getMessage()));
  }
}
