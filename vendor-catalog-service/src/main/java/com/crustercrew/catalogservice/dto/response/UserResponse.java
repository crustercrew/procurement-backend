package com.crustercrew.catalogservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    @Builder.Default
    Long id = 0L;
    @Builder.Default
    String email = "";
    @Builder.Default
    String fullName = "";
    @Builder.Default
    String role = "";
    @Builder.Default
    Long departmentId = null;
    @Builder.Default
    Boolean isActive = false;
    @Builder.Default
    String createdAt = null;
}
