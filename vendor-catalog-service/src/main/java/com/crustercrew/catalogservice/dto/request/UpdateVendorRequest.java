package com.crustercrew.catalogservice.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateVendorRequest {

    private String companyName;

    private String taxIdNpwp;

    @Email(message = "Format email tidak valid")
    private String contactEmail;
}