package com.crustercrew.catalogservice.dto.request;

import jakarta.validation.Valid;
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
public class CreateVendorRequest {

    /**
     * Opsi 1: Mengaitkan dengan ID user yang sudah ada (opsional).
     */
    private Long userId;

    @NotBlank(message = "Nama perusahaan diperlukan")
    private String companyName;

    @NotBlank(message = "NPWP diperlukan")
    private String taxIdNpwp;

    @NotBlank(message = "Email kontak diperlukan")
    @Email(message = "Format email tidak valid")
    private String contactEmail;

    /**
     * Opsi 2: Sekaligus membuat akun User baru untuk vendor ini (opsional).
     * Jika diisi, sistem akan otomatis mendaftarkan user dengan role VENDOR ke user-service.
     */
    @Valid
    private CreateVendorUserRequest user;
}