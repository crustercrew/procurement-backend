package com.crustercrew.project.procurementjavabe.dto;

public record GeneralErrorResponse(
        String timestamp,
        int status,
        String error,
        String message,
        String path
) {}
