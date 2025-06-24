package com.blockchain.csr.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    private int code;
    private String message;
    private T data;

    public static <T> BaseResponse<T> success(T data) {
        return BaseResponse.<T>builder()
                .code(200)
                .message("Success")
                .data(data)
                .build();
    }

    public static <T> BaseResponse<T> success(String message, T data) {
        return BaseResponse.<T>builder()
                .code(200)
                .message(message)
                .data(data)
                .build();
    }

    public static BaseResponse<Object> success() {
        return BaseResponse.builder()
                .code(200)
                .message("Success")
                .data(null)
                .build();
    }

    public static BaseResponse<Object> success(String message) {
        return BaseResponse.builder()
                .code(200)
                .message(message)
                .data(null)
                .build();
    }

    public static <T> BaseResponse<T> error(int code, String message) {
        return BaseResponse.<T>builder()
                .code(code)
                .message(message)
                .data(null)
                .build();
    }

    public static <T> BaseResponse<T> error(String message) {
        return BaseResponse.<T>builder()
                .code(400)
                .message(message)
                .data(null)
                .build();
    }

    public static <T> BaseResponse<T> unauthorized(String message) {
        return BaseResponse.<T>builder()
                .code(401)
                .message(message)
                .data(null)
                .build();
    }

    public static <T> BaseResponse<T> forbidden(String message) {
        return BaseResponse.<T>builder()
                .code(403)
                .message(message)
                .data(null)
                .build();
    }

    public static <T> BaseResponse<T> notFound(String message) {
        return BaseResponse.<T>builder()
                .code(404)
                .message(message)
                .data(null)
                .build();
    }

    public static <T> BaseResponse<T> internalError(String message) {
        return BaseResponse.<T>builder()
                .code(500)
                .message(message)
                .data(null)
                .build();
    }
} 