package com.crustercrew.userservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank(message = "Email wajib diisi")
    @Email(message = "Format email tidak valid")
    String email;

    @NotBlank(message = "Password wajib diisi")
    String password;

    @NotBlank(message = "Nama lengkap wajib diisi")
    String fullName;

    @NotNull(message = "Role wajib diisi")
    String role; // akan di-parse ke UserRole enum

    Long departmentId = null; // nullable untuk VENDOR
}
