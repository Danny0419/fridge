package com.example.fragment_test.ServerAPI;

public class ServerResponse<T> {
    private String status;
    private String error;
    private T data;  // 泛型數據欄位
    private int code; // HTTP狀態碼

    // 構造函數
    public ServerResponse() {}

    public ServerResponse(String status, String error) {
        this.status = status;
        this.error = error;
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    // 輔助方法
    public boolean isSuccessful() {
        return status != null && status.equals("success");
    }
}