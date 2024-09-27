package com.tvm.usermanagement.exception;
import com.tvm.usermanagement.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserNotFoundAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse> handleException(UserNotFoundException exception) {
        ApiResponse errorResponse = new ApiResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage(),0,null);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}