package com.tvm.usermanagement.common;

public class ApiResponse<T> {
    private int status;
    private String message;
    private int recordCount;
    private T data;

    public ApiResponse(int status, String message, int recordCount, T data) {
        this.status = status;
        this.message = message;
        this.recordCount = recordCount;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}