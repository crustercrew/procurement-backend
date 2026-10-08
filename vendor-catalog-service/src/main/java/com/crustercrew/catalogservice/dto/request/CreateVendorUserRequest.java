package com.crustercrew.catalogservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVendorUserRequest {

    @Email(message = "Format email user tidak valid")
    private String email; // Opsional: jika kosong, default ke contactEmail vendor

    @NotBlank(message = "Password wajib diisi jika membuat akun user")
    private String password;

    private String fullName; // Opsional: jika kosong, default ke companyName vendor
}