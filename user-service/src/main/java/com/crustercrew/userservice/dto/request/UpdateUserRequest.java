package com.crustercrew.userservice.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserRequest {
    @Builder.Default
    String fullName = null;
    @Builder.Default
    String role = null;
    @Builder.Default
    Long departmentId = null;
    @Builder.Default
    Boolean isActive = null;
}
