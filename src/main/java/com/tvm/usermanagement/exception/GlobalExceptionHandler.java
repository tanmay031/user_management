package com.tvm.usermanagement.exception;

import com.tvm.usermanagement.common.ApiResponse;
import com.tvm.usermanagement.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseUtil.createResponse(HttpStatus.CONFLICT, ex.getMessage(), 0, null);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseUtil.createResponse(HttpStatus.NOT_FOUND, ex.getMessage(), 0, null);
    }
}
