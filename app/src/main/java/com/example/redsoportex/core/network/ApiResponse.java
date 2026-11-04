package com.example.redsoportex.core.network;

import androidx.annotation.Nullable;
import com.google.gson.annotations.SerializedName;

/**
 * Envoltorio estándar de respuesta del backend Spring Boot:
 * {
 *   "success": true/false,
 *   "message": "...",
 *   "data": { ... }
 * }
 */
public class ApiResponse<T> {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private T data;

    public ApiResponse() {}

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Nullable
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
