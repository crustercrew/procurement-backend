package com.crustercrew.userservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyCredentialsRequest {
    @NotBlank(message = "Email wajib diisi")
    @Email(message = "Format email tidak valid")
    String email;

    @NotBlank(message = "Password wajib diisi")
    String password;
}
