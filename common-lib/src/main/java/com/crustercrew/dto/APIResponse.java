package com.crustercrew.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record APIResponse<T>(
        String status,
        int code,
        String message,
        T data,
        ErrorResponse error,
        String timestamp
) {
    public static <T> APIResponse<T> success(T data) {
        return new APIResponse<>("SUCCESS", 200, "Success", data, null, Instant.now().toString());
    }

    public static <T> APIResponse<T> success(String message, T data) {
        return new APIResponse<>("SUCCESS", 200, message, data, null, Instant.now().toString());
    }

    public static <T> APIResponse<T> success(int code, String message, T data) {
        return new APIResponse<>("SUCCESS", code, message, data, null, Instant.now().toString());
    }

    public static <T> APIResponse<T> error(int code, String message, String error, String path, String service) {
        return new APIResponse<>(
                "ERROR",
                code,
                message,
                null,
                new ErrorResponse(service, error, path),
                Instant.now().toString()
        );
    }

    public static <T> APIResponse<T> error(int code, String message, String error, String path) {
        return error(code, message, error, path, null);
    }
}