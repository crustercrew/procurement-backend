package com.crustercrew.catalogservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorResponse {

    private Long id;

    private Long userId;

    private String companyName;

    private String taxIdNpwp;

    private String contactEmail;

    private Boolean isActive;

    private UserResponse user;
}