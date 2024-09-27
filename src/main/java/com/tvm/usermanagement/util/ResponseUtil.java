package com.tvm.usermanagement.util;

import com.tvm.usermanagement.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public class ResponseUtil {

    private ResponseUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> ResponseEntity<ApiResponse<T>> createResponse(HttpStatus status, String message,int recordCount, T data) {
        ApiResponse<T> response = new ApiResponse<>(status.value(), message, recordCount, data);
        return new ResponseEntity<>(response, status);
    }

    public static String getErrorMessage(BindingResult bindingResult) {
        return bindingResult.getAllErrors().get(0).getDefaultMessage();
    }
}